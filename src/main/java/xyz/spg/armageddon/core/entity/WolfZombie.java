package xyz.spg.armageddon.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import xyz.spg.armageddon.shared.AEntityTypes;

import javax.annotation.Nullable;
import java.util.Random;

public final class WolfZombie extends AbstractZombie
{
	private boolean isWet;
	private boolean isShaking;
	private float shakeAnim;
	private float shakeAnimO;

	public WolfZombie(EntityType<? extends AbstractZombie> entityType, Level level)
	{
		super(entityType, level);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		if(getTarget() != null)
			return SoundEvents.WOLF_GROWL;
		return SoundEvents.WOLF_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.WOLF_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.WOLF_DEATH;
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.WOLF_STEP;
	}

	@Override
	protected float getSoundVolume()
	{
		return .4F;
	}

	@Override
	public void aiStep()
	{
		super.aiStep();

		if(!level.isClientSide && isWet && !isShaking && !isPathFinding() && onGround)
		{
			isShaking = true;
			shakeAnim = 0F;
			shakeAnimO = 0F;
			level.broadcastEntityEvent(this, (byte) 8);
		}
	}

	@Override
	public void tick()
	{
		super.tick();

		if(isAlive())
		{
			if(isInWaterRainOrBubble())
			{
				isWet = true;

				if(isShaking && !level.isClientSide)
				{
					level.broadcastEntityEvent(this, (byte) 56);
					cancelShake();
				}
			}
			else if((isWet || isShaking) && isShaking)
			{
				if(shakeAnim == 0F)
				{
					playSound(SoundEvents.WOLF_SHAKE, getSoundVolume(), (random.nextFloat() - random.nextFloat()) * .2F + 1F);
					gameEvent(GameEvent.WOLF_SHAKING);
				}

				shakeAnimO = shakeAnim;
				shakeAnim += .05F;

				if(shakeAnimO >= 2F)
				{
					isWet = false;
					isShaking = false;
					shakeAnimO = 0F;
					shakeAnim = 0F;
				}

				if(shakeAnim > .4F)
				{
					var f = (float) getY();
					var i = (int) (Mth.sin((shakeAnim - .4F) * (float) Math.PI) * 7F);
					var vec = getDeltaMovement();

					for(var j = 0; j < i; j++)
					{
						var f1 = (random.nextFloat() * 2F - 1F) * getBbWidth() * .5F;
						var f2 = (random.nextFloat() * 2F - 1F) * getBbWidth() * .5F;

						level.addParticle(ParticleTypes.SPLASH, getX() + (double) f1, (double) (f + .8F), getX() + (double) f2, vec.x, vec.y, vec.z);
					}
				}
			}
		}
	}

	private void cancelShake()
	{
		isShaking = false;
		shakeAnim = 0F;
		shakeAnimO = 0F;
	}

	@Override
	public void die(DamageSource source)
	{
		isWet = false;
		isShaking = false;
		shakeAnimO = 0F;
		shakeAnim = 0F;
		super.die(source);
	}

	public boolean isWet()
	{
		return isWet;
	}

	public float getWetShade(float f)
	{
		return Math.min(.5F + Mth.lerp(f, shakeAnimO, shakeAnim) / 2F * .5F, 1F);
	}

	public float getBodyRollAngle(float f1, float f2)
	{
		var f = (Mth.lerp(f1, shakeAnimO, shakeAnim) + f2) / 1.8F;

		if(f < 0F)
			f = 0F;
		else if(f > 1F)
			f = 1F;

		return Mth.sin(f * (float) Math.PI) * Mth.sin(f * (float) Math.PI * 11F) * .15F * (float) Math.PI;
	}

	public float getTailAngle()
	{
		var healthOffset = (getMaxHealth() - getHealth()) * .02F * (float) Math.PI;
		return 1.5393804F - healthOffset;
	}

	@Override
	public int getMaxSpawnClusterSize()
	{
		return 8;
	}

	@Override
	public Vec3 getLeashOffset()
	{
		return new Vec3(0D, .6F * getEyeHeight(), getBbWidth() * .4F);
	}

	@Override
	public void handleEntityEvent(byte event)
	{
		if(event == 8)
		{
			isShaking = true;
			shakeAnim = 0F;
			shakeAnimO = 0F;
		}
		else if(event == 56)
			cancelShake();
		else
			super.handleEntityEvent(event);
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions)
	{
		return dimensions.height * .8F;
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

	public static AttributeSupplier.Builder createAttributes()
	{
		return createZombieAttributes();
	}

	public static boolean canWolfZombieSpawn(EntityType<? extends AbstractZombie> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random rng)
	{
		if(entityType != AEntityTypes.WOLF_ZOMBIE)
			return false;
		if(!canZombieSpawn(entityType, level, spawnType, pos, rng))
			return false;
		return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getLightEmission(pos) > 8;
	}
}
