package xyz.spgamers.forge.armageddon.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
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
import xyz.spgamers.forge.armageddon.util.WorldHelper;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public final class SpawnEggItem<E extends Entity> extends Item implements IItemColor
{
	private final int primaryColor;
	private final int secondaryColor;
	private final Supplier<EntityType<E>> entityTypeSupplier;

	public SpawnEggItem(Supplier<EntityType<E>> entityTypeSupplier, int primaryColor, int secondaryColor, Properties properties)
	{
		super(properties);

		this.entityTypeSupplier = entityTypeSupplier;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}

	@Override
	public Collection<ItemGroup> getCreativeTabs()
	{
		// display on both our custom item group
		// and vanilla misc group with other spawn eggs
		return ImmutableSet.of(ItemGroup.MISC, group);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();

		if(WorldHelper.isClientWorld(world))
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
				spawner.setEntityType(entityTypeSupplier.get());
				tileEntity.markDirty();
				world.notifyBlockUpdate(pos, blockState, blockState, 3);
				stack.shrink(1);
				return ActionResultType.SUCCESS;
			}
		}

		BlockPos pos1;

		if(blockState.getCollisionShape(world, pos).isEmpty())
			pos1 = pos;
		else
			pos1 = pos.offset(side);

		// There is no reason this method should not be our type
		// it calls another spawn() method that returns the generic type
		//noinspection unchecked
		E entity = (E) entityTypeSupplier.get().spawn((ServerWorld) world, stack, context.getPlayer(), pos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(pos, pos1) && side == Direction.UP);

		if(entity != null)
			stack.shrink(1);

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		BlockRayTraceResult rayTraceResult = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

		if(rayTraceResult.getType() != RayTraceResult.Type.BLOCK)
			return ActionResult.resultPass(stack);
		else if(WorldHelper.isClientWorld(world))
			return ActionResult.resultFail(stack);
		else
		{
			BlockPos pos = rayTraceResult.getPos();

			if(!(world.getBlockState(pos).getBlock() instanceof FlowingFluidBlock))
				return ActionResult.resultPass(stack);
			else if(world.isBlockModifiable(player, pos) && player.canPlayerEdit(pos, rayTraceResult.getFace(), stack))
			{
				// There is no reason this method should not be our type
				// it calls another spawn() method that returns the generic type
				//noinspection unchecked
				E entity = (E) entityTypeSupplier.get().spawn((ServerWorld) world, stack, player, pos, SpawnReason.SPAWN_EGG, false, false);

				if(entity == null)
					return ActionResult.resultPass(stack);

				stack.shrink(1);
				player.addStat(Stats.ITEM_USED.get(this));
				return ActionResult.resultSuccess(stack);
			}
			else
				return ActionResult.resultFail(stack);
		}
	}

	@Override
	public int getColor(ItemStack stack, int tintIndex)
	{
		return tintIndex == 0 ? primaryColor : secondaryColor;
	}

	public static final class SpawnEggDispenserBehavior extends DefaultDispenseItemBehavior
	{
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			Direction direction = source.getBlockState().get(DispenserBlock.FACING);
			SpawnEggItem<?> spawnEggItem = (SpawnEggItem<?>) stack.getItem();
			Entity entity = spawnEggItem.entityTypeSupplier.get().spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);

			if(entity != null)
				stack.shrink(1);

			return stack;
		}
	}
}
