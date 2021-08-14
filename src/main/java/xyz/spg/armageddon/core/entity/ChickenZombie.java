package xyz.spg.armageddon.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.util.Constants;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.AItems;
import xyz.spg.armageddon.shared.NBTTags;

import javax.annotation.Nullable;
import java.util.Random;

public final class ChickenZombie extends AbstractZombie
{
	public float flap;
	public float flapSpeed;
	public float oFlapSpeed;
	public float oFlap;
	public float flapping = 1F;
	public int eggTime = random.nextInt(6000) + 6000;

	private boolean isChickenJockey;
	private float nextFlap = 1F;

	public ChickenZombie(EntityType<? extends AbstractZombie> entityType, Level level)
	{
		super(entityType, level);

		setPathfindingMalus(BlockPathTypes.WATER, 0F);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.CHICKEN_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.CHICKEN_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.CHICKEN_DEATH;
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.CHICKEN_STEP;
	}

	@Override
	public boolean canBreakDoors()
	{
		return false;
	}

	@Override
	public boolean supportsBreakDoorGoal()
	{
		return false;
	}

	@Override
	protected boolean convertsInWater()
	{
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions)
	{
		return isBaby() ? dimensions.height * .85F : dimensions.height * .92F;
	}

	@Override
	public void aiStep()
	{
		super.aiStep();

		oFlap = flap;
		oFlapSpeed = flapSpeed;
		flapSpeed = (float) ((double) flapSpeed + (onGround ? -1D : 4D) * .3D);
		flapSpeed = Mth.clamp(flapSpeed, 0F, 1F);

		if(!onGround && flapping < 1F)
			flapping = 1F;

		flapping = (float) ((double) flapping * .9D);
		var delta = getDeltaMovement();

		if(!onGround && delta.y < 0D)
			setDeltaMovement(delta.multiply(1D, .6D, 1D));

		flap += flapping * 2F;

		if(!level.isClientSide && isAlive() && !isBaby() && !isChickenJockey && --eggTime <= 0)
		{
			playSound(SoundEvents.CHICKEN_EGG, 1F, (random.nextFloat() - random.nextFloat()) * .2F + 1F);
			spawnAtLocation(AItems.ROTTEN_EGG);
			eggTime = random.nextInt(6000) + 6000;
		}
	}

	@Override
	protected boolean isFlapping()
	{
		return flyDist > nextFlap;
	}

	@Override
	protected void onFlap()
	{
		nextFlap = flyDist + flapSpeed / 2F;
	}

	@Override
	protected int getExperienceReward(Player player)
	{
		return isChickenJockey ? 10 : super.getExperienceReward(player);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag)
	{
		super.readAdditionalSaveData(compoundTag);
		isChickenJockey = compoundTag.getBoolean(NBTTags.IS_CHICKEN_JOCKEY);

		if(compoundTag.contains(NBTTags.EGG_LAY_TIME, Constants.NBT.TAG_ANY_NUMERIC))
			eggTime = compoundTag.getInt(NBTTags.EGG_LAY_TIME);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag)
	{
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putBoolean(NBTTags.IS_CHICKEN_JOCKEY, isChickenJockey);
		compoundTag.putInt(NBTTags.EGG_LAY_TIME, eggTime);
	}

	@Override
	public boolean removeWhenFarAway(double distance)
	{
		return isChickenJockey;
	}

	public boolean isChickenJockey()
	{
		return isChickenJockey;
	}

	public void setChickenJockey(boolean chickenJockey)
	{
		isChickenJockey = chickenJockey;
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return createZombieAttributes();
	}

	public static boolean canChickenZombieSpawn(EntityType<? extends AbstractZombie> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random rng)
	{
		if(entityType != AEntityTypes.CHICKEN_ZOMBIE)
			return false;
		if(!canZombieSpawn(entityType, level, spawnType, pos, rng))
			return false;
		return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getLightEmission(pos) > 8;
	}
}
