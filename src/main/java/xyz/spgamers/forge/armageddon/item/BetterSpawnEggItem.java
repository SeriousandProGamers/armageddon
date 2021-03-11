package xyz.spgamers.forge.armageddon.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.util.Constants;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

// Copy of SpawnEggItem modified to use Suppliers
// rather than directly requiring a EntityType instance
// EntityType's are registered after Item's are
// which means our custom EntityType is always null when
// the constructor is run
public class BetterSpawnEggItem extends Item
{
	private final int primaryColor;
	private final int secondaryColor;
	private final Supplier<EntityType<?>> entityTypeObject;

	public BetterSpawnEggItem(Supplier<EntityType<?>> entityTypeObject, int primaryColor, int secondaryColor, Properties properties)
	{
		super(properties);

		this.entityTypeObject = entityTypeObject;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();

		if(world.isRemote())
			return ActionResultType.SUCCESS;

		ItemStack stack = context.getItem();
		BlockPos pos = context.getPos();
		Direction side = context.getFace();
		BlockState blockState = world.getBlockState(pos);

		if(blockState.isIn(Blocks.SPAWNER))
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if(tileEntity instanceof MobSpawnerTileEntity)
			{
				AbstractSpawner spawner = ((MobSpawnerTileEntity) tileEntity).getSpawnerBaseLogic();
				EntityType<?> entityType = getEntityType(stack.getTag());
				spawner.setEntityType(entityType);
				tileEntity.markDirty();
				world.notifyBlockUpdate(pos, blockState, blockState, 3);
				stack.shrink(1);
				return ActionResultType.CONSUME;
			}
		}

		BlockPos pos1;

		if(blockState.getCollisionShape(world, pos).isEmpty())
			pos1 = pos;
		else
			pos1 = pos.offset(side);

		EntityType<?> entityType = getEntityType(stack.getTag());
		boolean flag = !Objects.equals(pos, pos1) && side == Direction.UP;
		Entity entity = entityType.spawn((ServerWorld) world, stack, context.getPlayer(), pos1, SpawnReason.SPAWN_EGG, true, flag);

		if(entity != null)
			stack.shrink(1);

		return ActionResultType.CONSUME;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		RayTraceResult rayTraceResult = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

		if(rayTraceResult.getType() != RayTraceResult.Type.BLOCK)
			return ActionResult.resultPass(stack);
		else if(!world.isRemote())
			return ActionResult.resultSuccess(stack);
		else
		{
			BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
			BlockPos pos = blockRayTraceResult.getPos();

			if(!(world.getBlockState(pos).getBlock() instanceof FlowingFluidBlock))
				return ActionResult.resultPass(stack);
			else if(world.isBlockModifiable(player, pos) && player.canPlayerEdit(pos, blockRayTraceResult.getFace(), stack))
			{
				EntityType<?> entityType = getEntityType(stack.getTag());
				Entity entity = entityType.spawn((ServerWorld) world, stack, player, pos, SpawnReason.SPAWN_EGG, false, false);

				if(entity == null)
					return ActionResult.resultPass(stack);
				else
				{
					if(!player.isCreative())
						stack.shrink(1);

					player.addStat(Stats.ITEM_USED.get(this));
					return ActionResult.resultConsume(stack);
				}
			}
			else
				return ActionResult.resultFail(stack);
		}
	}

	public boolean hasType(CompoundNBT tagCompound, EntityType<?> type)
	{
		return Objects.equals(getEntityType(tagCompound), type);
	}

	@OnlyIn(Dist.CLIENT)
	public int getColor(int tintIndex)
	{
		return tintIndex == 0 ? primaryColor : secondaryColor;
	}

	public EntityType<?> getEntityType(CompoundNBT tagCompound)
	{
		if(tagCompound != null && tagCompound.contains(Constants.NBTKeys.ENTITY_TAG, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT entityTag = tagCompound.getCompound(Constants.NBTKeys.ENTITY_TAG);

			if(entityTag.contains(Constants.NBTKeys.ID, net.minecraftforge.common.util.Constants.NBT.TAG_STRING))
				return EntityType.byKey(entityTag.getString(Constants.NBTKeys.ID)).orElseGet(entityTypeObject);
		}

		return entityTypeObject.get();
	}

	public Optional<MobEntity> getChildToSpawn(PlayerEntity player, MobEntity mob, EntityType<? extends MobEntity> entityType, ServerWorld world, BlockPos pos, ItemStack stack)
	{
		if(!hasType(stack.getTag(), entityType))
			return Optional.empty();
		else
		{
			MobEntity entity;

			if(mob instanceof AgeableEntity)
				entity = ((AgeableEntity) mob).func_241840_a(world, (AgeableEntity) mob);
			else
				entity = entityType.create(world);

			if(entity == null)
				return Optional.empty();
			else
			{
				entity.setChild(true);

				if(entity.isChild())
				{
					entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0F, 0F);
					world.func_242417_l(entity);

					if(stack.hasDisplayName())
						entity.setCustomName(stack.getDisplayName());
					if(!player.isCreative())
						stack.shrink(1);
				}

				return Optional.ofNullable(entity);
			}
		}
	}
}
