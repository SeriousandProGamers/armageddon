package xyz.spgamers.forge.armageddon.entity;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IShearable;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;
import org.apache.commons.lang3.tuple.Pair;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ZombieMooshroomEntity extends ZombieCowEntity implements IShearable, IForgeShearable
{
	public static final DataParameter<String> MOOSHROOM_TYPE = EntityDataManager.createKey(ZombieMooshroomEntity.class, DataSerializers.STRING);

	private Effect hasStewEffect;
	private int effectDuration;
	private UUID lightningUUID;

	public ZombieMooshroomEntity(EntityType<? extends ZombieEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn)
	{
		return worldIn.getBlockState(pos.down()).isIn(Blocks.MYCELIUM) ? 10F : worldIn.getBrightness(pos) - .5F;
	}

	@Override
	public void func_241841_a(ServerWorld world, LightningBoltEntity lightningBolt)
	{
		UUID uuid = lightningBolt.getUniqueID();

		if(!uuid.equals(lightningUUID))
		{
			setMooshroomType(getMooshroomType() == Type.RED ? Type.BROWN : Type.RED);
			lightningUUID = uuid;
			playSound(SoundEvents.ENTITY_MOOSHROOM_CONVERT, 2F, 1F);
		}
	}

	@Override
	protected void registerData()
	{
		super.registerData();

		dataManager.register(MOOSHROOM_TYPE, Type.RED.name);
	}

	@Override
	protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		// TODO: Support mod bowl items
		if(stack.getItem() == Items.BOWL && !isChild())
		{
			boolean flag = false;
			ItemStack stack1;

			if(hasStewEffect != null)
			{
				flag = true;
				stack1 = new ItemStack(Items.SUSPICIOUS_STEW);
				SuspiciousStewItem.addEffect(stack1, hasStewEffect, effectDuration);
				hasStewEffect = null;
				effectDuration = 0;
			}
			else
				stack1 = new ItemStack(Items.MUSHROOM_STEW);

			ItemStack stack2 = DrinkHelper.fill(stack, player, stack1, false);
			player.setHeldItem(hand, stack2);
			SoundEvent soundEvent;

			if(flag)
				soundEvent = SoundEvents.ENTITY_MOOSHROOM_SUSPICIOUS_MILK;
			else
				soundEvent = SoundEvents.ENTITY_MOOSHROOM_MILK;

			playSound(soundEvent, 1F, 1F);
			return ActionResultType.func_233537_a_(world.isRemote());
		}
		// Forge: Moved to onSheared
		/*else if(false && stack.getItem() == Items.SHEARS && isShearable())
		{
			shear(SoundCategory.PLAYERS);

			if(!world.isRemote())
				stack.damageItem(1, player, $ -> player.sendBreakAnimation(hand));
		}*/
		else if(getMooshroomType() == Type.BROWN && stack.getItem().isIn(ItemTags.SMALL_FLOWERS))
		{
			if(hasStewEffect != null)
			{
				for(int i = 0; i < 2; i++)
				{
					world.addParticle(ParticleTypes.SMOKE, getPosX() + rand.nextDouble() / 2D, getPosYHeight(.5D), getPosZ() + rand.nextDouble() / 2D, 0D, rand.nextDouble() / 5D, 0D);
				}
			}
			else
			{
				Optional<Pair<Effect, Integer>> optional = getStewEffect(stack);

				if(!optional.isPresent())
					return ActionResultType.PASS;

				Pair<Effect, Integer> pair = optional.get();

				if(!player.isCreative())
					stack.shrink(1);

				for(int i = 0; i < 4; i++)
				{
					world.addParticle(ParticleTypes.EFFECT, getPosX() + rand.nextDouble() / 2D, getPosYHeight(.5D), getPosZ() + rand.nextDouble() / 2D, 0D, rand.nextDouble() / 5D, 0D);
				}

				hasStewEffect = pair.getLeft();
				effectDuration = pair.getRight();
				playSound(SoundEvents.ENTITY_MOOSHROOM_EAT, 2F, 1F);
			}

			return ActionResultType.func_233537_a_(world.isRemote());
		}
		else
			return super.func_230254_b_(player, hand);
	}

	@Override
	public void shear(SoundCategory category)
	{
		world.playMovingSound(null, this, SoundEvents.ENTITY_MOOSHROOM_SHEAR, category, 1F, 1F);

		if(!world.isRemote())
		{
			((ServerWorld) world).spawnParticle(ParticleTypes.EXPLOSION, getPosX(), getPosYHeight(.5D), getPosZ(), 1, 0D, 0D, 0D, 0D);

			ZombieCowEntity cow = ModEntities.ZOMBIE_COW.get().create(world);
			cow.setLocationAndAngles(getPosX(), getPosY(), getPosZ(), rotationYaw, rotationPitch);
			cow.setHealth(getHealth());
			cow.renderYawOffset = renderYawOffset;

			if(hasCustomName())
			{
				cow.setCustomName(getCustomName());
				cow.setCustomNameVisible(isCustomNameVisible());
			}

			if(!isNoDespawnRequired())
				cow.enablePersistence();

			cow.setInvisible(isInvulnerable());
			world.addEntity(cow);

			for(int i = 0; i < 5; i++)
			{
				world.addEntity(new ItemEntity(world, getPosX(), getPosYHeight(1D), getPosZ(), new ItemStack(getMooshroomType().renderState.getBlock())));
			}
		}
	}

	@Override
	public boolean isShearable()
	{
		return isAlive() && !isChild();
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);

		compound.putString(Constants.NBTKeys.TYPE, getMooshroomType().name);

		if(hasStewEffect != null)
		{
			compound.putByte(Constants.NBTKeys.EFFECT_ID, (byte) Effect.getId(hasStewEffect));
			compound.putInt(Constants.NBTKeys.EFFECT_DURATION, effectDuration);
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);

		setMooshroomType(Type.getTypeByName(compound.getString(Constants.NBTKeys.TYPE)));

		if(compound.contains(Constants.NBTKeys.EFFECT_ID, net.minecraftforge.common.util.Constants.NBT.TAG_BYTE))
			hasStewEffect = Effect.get(compound.getByte(Constants.NBTKeys.EFFECT_ID));
		if(compound.contains(Constants.NBTKeys.EFFECT_DURATION, net.minecraftforge.common.util.Constants.NBT.TAG_INT))
			effectDuration = compound.getInt(Constants.NBTKeys.EFFECT_DURATION);
	}

	private Optional<Pair<Effect, Integer>> getStewEffect(ItemStack stack)
	{
		Item item = stack.getItem();

		if(item instanceof BlockItem)
		{
			Block block = ((BlockItem) item).getBlock();

			if(block instanceof FlowerBlock)
			{
				FlowerBlock flower = (FlowerBlock) block;
				return Optional.of(Pair.of(flower.getStewEffect(), flower.getStewEffectDuration()));
			}
		}

		return Optional.empty();
	}

	private void setMooshroomType(Type mooshroomType)
	{
		dataManager.set(MOOSHROOM_TYPE, mooshroomType.name);
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, World world, BlockPos pos)
	{
		return isShearable();
	}

	public Type getMooshroomType()
	{
		return Type.getTypeByName(dataManager.get(MOOSHROOM_TYPE));
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nullable PlayerEntity player, @Nonnull ItemStack item, World world, BlockPos pos, int fortune)
	{
		world.playMovingSound(null, this, SoundEvents.ENTITY_MOOSHROOM_SHEAR, player == null ? SoundCategory.BLOCKS : SoundCategory.PLAYERS, 1F, 1F);

		if(!world.isRemote())
		{
			((ServerWorld) world).spawnParticle(ParticleTypes.EXPLOSION, getPosX(), getPosYHeight(.5D), getPosZ(), 1, 0D, 0D, 0D, 0D);
			remove();

			ZombieCowEntity cow = ModEntities.ZOMBIE_COW.get().create(world);
			cow.setLocationAndAngles(getPosX(), getPosY(), getPosZ(), rotationYaw, rotationPitch);
			cow.setHealth(getHealth());
			cow.renderYawOffset = renderYawOffset;

			if(hasCustomName())
			{
				cow.setCustomName(getCustomName());
				cow.setCustomNameVisible(isCustomNameVisible());
			}

			if(!isNoDespawnRequired())
				cow.enablePersistence();

			cow.setInvisible(isInvulnerable());
			world.addEntity(cow);

			List<ItemStack> items = Lists.newArrayList();

			for(int i = 0; i < 5; i++)
			{
				items.add(new ItemStack(getMooshroomType().renderState.getBlock()));
			}

			return items;
		}

		return Collections.emptyList();
	}

	public static boolean canMooshroomSpawn(EntityType<ZombieMooshroomEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return world.getBlockState(pos.down()).isIn(Blocks.MYCELIUM) && world.getLightSubtracted(pos, 0)  > 8;
	}

	public static enum Type
	{
		RED("red", Blocks.RED_MUSHROOM.getDefaultState()),
		BROWN("brown", Blocks.BROWN_MUSHROOM.getDefaultState()),
		;

		public static final Type[] VALUES = values();

		private final String name;
		private final BlockState renderState;

		Type(String name, BlockState renderState)
		{
			this.name = name;
			this.renderState = renderState;
		}

		public String getName()
		{
			return name;
		}

		@OnlyIn(Dist.CLIENT)
		public BlockState getRenderState()
		{
			return renderState;
		}

		public static Type getTypeByName(String name)
		{
			for(Type type : VALUES)
			{
				if(type.name.equals(name))
					return type;
			}

			return RED;
		}
	}
}
