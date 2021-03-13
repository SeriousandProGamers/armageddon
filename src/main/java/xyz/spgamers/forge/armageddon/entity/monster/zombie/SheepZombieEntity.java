package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ModConstants;
import xyz.spgamers.forge.armageddon.util.WorldHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class SheepZombieEntity extends AbstractZombieEntity implements IForgeShearable
{
	public static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(SheepZombieEntity.class, DataSerializers.BYTE);

	private int sheepTimer;
	private EatGrassGoal eatGrassGoal;

	public SheepZombieEntity(World world)
	{
		super(ModEntities.SHEEP_ZOMBIE.get(), world, Armageddon.SERVER_CONFIG.animals::isSheepZombieEnabled);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();

		eatGrassGoal = new EatGrassGoal(this);
		goalSelector.addGoal(5, eatGrassGoal);
	}

	@Override
	protected void updateAITasks()
	{
		sheepTimer = eatGrassGoal.getEatingGrassTimer();
		super.updateAITasks();
	}

	@Override
	public void livingTick()
	{
		if(WorldHelper.isClientWorld(world))
			sheepTimer = Math.max(0, sheepTimer - 1);

		super.livingTick();
	}

	@Override
	protected void registerData()
	{
		super.registerData();

		dataManager.register(DYE_COLOR, (byte) 0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if(id == 10)
			sheepTimer = 40;
		else
			super.handleStatusUpdate(id);
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationPointY(float partialTicks)
	{
		if(sheepTimer <= 0F)
			return 0F;
		else if(sheepTimer >= 4F && sheepTimer <= 36F)
			return 1F;
		else
			return sheepTimer < 4F ? ((float) sheepTimer - partialTicks) / 4F : -((sheepTimer - 40F) - partialTicks) / 4F;
	}

	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationAngleX(float partialTicks)
	{
		if(sheepTimer > 4F && sheepTimer <= 36F)
		{
			float f = ((sheepTimer - 4F) - partialTicks) / 32F;
			return ((float) Math.PI / 5F) + .21991149F * MathHelper.sin(f * 28.7F);
		}
		else
			return sheepTimer > 0F ? ((float) Math.PI / 5F) : rotationPitch * ((float) Math.PI / 180F);
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nullable PlayerEntity player, @Nonnull ItemStack item, World world, BlockPos pos, int fortune)
	{
		world.playMovingSound(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1F, 1F);

		if(WorldHelper.isServerWorld(world))
		{
			setSheared(true);
			int i = 1 + rand.nextInt(3);

			List<ItemStack> drops = Lists.newArrayList();
			Map<DyeColor, IItemProvider> woolMap = ObfuscationReflectionHelper.getPrivateValue(SheepEntity.class, null, "WOOL_BY_COLOR");

			for(int j = 0; j < i; j++)
			{
				DyeColor color = getFleeceColor();
				// Dye color tags are the dye items, not wool
				// Item wool = color.getTag().getRandomElement(rand);

				if(woolMap == null)
					drops.add(Items.WHITE_WOOL.getDefaultInstance());
				else
					drops.add(woolMap.getOrDefault(color, Items.WHITE_WOOL).asItem().getDefaultInstance());
			}

			return drops;
		}

		return Collections.emptyList();
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, World world, BlockPos pos)
	{
		if(!Armageddon.SERVER_CONFIG.animals.isSheepZombieShearble())
			return false;
		if(isChild() || isSheared())
			return false;
		return isAlive();
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putBoolean(ModConstants.NBT.SHEARED, isSheared());
		compound.putByte(ModConstants.NBT.COLOR, (byte) getFleeceColor().getId());
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		setSheared(compound.getBoolean(ModConstants.NBT.SHEARED));
		setFleeceColor(DyeColor.byId(compound.getByte(ModConstants.NBT.COLOR)));
	}

	public DyeColor getFleeceColor()
	{
		return DyeColor.byId(dataManager.get(DYE_COLOR) & 15);
	}

	public void setFleeceColor(DyeColor color)
	{
		byte b = dataManager.get(DYE_COLOR);
		dataManager.set(DYE_COLOR, (byte) (b & 240 | color.getId() & 15));
	}

	public boolean isSheared()
	{
		// if shearing is disabled all sheep have no wool
		if(!Armageddon.SERVER_CONFIG.animals.isSheepZombieShearble())
			return true;
		return (dataManager.get(DYE_COLOR) & 16) != 0;
	}

	public void setSheared(boolean sheared)
	{
		// if shearing is disabled all sheep have no wool
		if(!Armageddon.SERVER_CONFIG.animals.isSheepZombieShearble())
			sheared = true;

		byte b = dataManager.get(DYE_COLOR);

		if(sheared)
			dataManager.set(DYE_COLOR, (byte) (b | 16));
		else
			dataManager.set(DYE_COLOR, (byte) (b & -17));
	}

	public void eatGrassBonus()
	{
		setSheared(false);

		/*if(isChild())
			addGrowth(60);*/
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag)
	{
		setFleeceColor(SheepEntity.getRandomSheepColor(world.getRandom()));
		return super.onInitialSpawn(world, difficulty, reason, spawnData, dataTag);
	}

	@Nullable
	@Override
	// copy data across when being turned
	public <T extends MobEntity> T func_233656_b_(EntityType<T> entityType, boolean copyLoot)
	{
		T entity = super.func_233656_b_(entityType, copyLoot);

		if(entity instanceof SheepZombieEntity)
		{
			SheepZombieEntity sheep = (SheepZombieEntity) entity;
			sheep.setSheared(isSheared());
			sheep.setFleeceColor(getFleeceColor());
		}

		return entity;
	}

	@Override
	public boolean isChickenJockeyAllowed()
	{
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_SHEEP_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.ENTITY_SHEEP_STEP;
	}

	public static AttributeModifierMap.MutableAttribute registerSheepZombieAttributes()
	{
		return registerZombieAttributes();
	}

	public static boolean canSheepZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.animals.isSheepZombieEnabled())
			return false;
		if(!canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}
}
