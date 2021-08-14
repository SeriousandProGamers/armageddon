package xyz.spg.armageddon.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import xyz.spg.armageddon.core.entity.control.RabbitZombieJumpControl;
import xyz.spg.armageddon.core.entity.control.RabbitZombieMoveControl;
import xyz.spg.armageddon.shared.AEntityTypes;

import javax.annotation.Nullable;
import java.util.Random;

public final class RabbitZombie extends AbstractZombie
{
	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;
	private int jumpDelayTicks;

	public RabbitZombie(EntityType<? extends AbstractZombie> entityType, Level level)
	{
		super(entityType, level);

		jumpControl = new RabbitZombieJumpControl(this);
		moveControl = new RabbitZombieMoveControl(this);

		setSpeedModifier(0D);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.RABBIT_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.RABBIT_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.RABBIT_DEATH;
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.RABBIT_JUMP;
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
	protected float getJumpPower()
	{
		if(!horizontalCollision && (!moveControl.hasWanted() || !(moveControl.getWantedY() > getY() + .5D)))
		{
			var path = navigation.getPath();

			if(path != null && !path.isDone())
			{
				var vec = path.getNextEntityPos(this);

				if(vec.y > getY() + .5D)
					return .5F;
			}

			return moveControl.getSpeedModifier() <= .6D ? .2F : .3F;
		}
		else
			return .5F;
	}

	@Override
	protected void jumpFromGround()
	{
		super.jumpFromGround();
		var d0 = moveControl.getSpeedModifier();

		if(d0 > 0D)
		{
			var d1 = getDeltaMovement().horizontalDistanceSqr();

			if(d1 < .01D)
				moveRelative(.1F, new Vec3(0D, 0D, 1D));
		}

		if(!level.isClientSide)
			level.broadcastEntityEvent(this, (byte) 1);
	}

	public float getJumpCompletion(float ticks)
	{
		return jumpDuration == 0 ? 0F : ((float) jumpTicks + ticks) / (float) jumpDuration;
	}

	public void setSpeedModifier(double speedModifier)
	{
		getNavigation().setSpeedModifier(speedModifier);
		moveControl.setWantedPosition(moveControl.getWantedX(), moveControl.getWantedY(), moveControl.getWantedZ(), speedModifier);
	}

	@Override
	public void setJumping(boolean jumping)
	{
		super.setJumping(jumping);

		if(jumping)
		{
			var jumpSound = getJumpSound();

			if(jumpSound != null)
				playSound(jumpSound, getSoundVolume(), ((random.nextFloat() - random.nextFloat()) * .2F + 1F) * .8F);
		}
	}

	public void startJumping()
	{
		setJumping(true);
		jumpDuration = 10;
		jumpTicks = 0;
	}

	public boolean isJumping_Living()
	{
		return jumping;
	}

	@Override
	protected void customServerAiStep()
	{
		if(jumpDelayTicks > 0)
			jumpDelayTicks--;

		if(onGround)
		{
			if(!wasOnGround)
			{
				setJumping(false);
				checkLandingDelay();
			}

			if(/*getRabbitType() == 99 &&*/ jumpDelayTicks == 0)
			{
				var living = getTarget();

				if(living != null && distanceToSqr(living) < 16D)
				{
					facePoint(living.getX(), living.getZ());
					moveControl.setWantedPosition(living.getX(), living.getY(), living.getZ(), moveControl.getSpeedModifier());
					startJumping();
					wasOnGround = true;
				}
			}

			var jumpControl = (RabbitZombieJumpControl) this.jumpControl;

			if(!jumpControl.wantJump())
			{
				if(moveControl.hasWanted() && jumpDelayTicks == 0)
				{
					var path = navigation.getPath();
					var vec = new Vec3(moveControl.getWantedX(), moveControl.getWantedY(), moveControl.getWantedZ());

					if(path != null && !path.isDone())
						vec = path.getNextEntityPos(this);

					facePoint(vec.x, vec.z);
					startJumping();
				}
			}
			else if(!jumpControl.canJump())
				enableJumpControl();
		}

		wasOnGround = onGround;
	}

	@Override
	public boolean canSpawnSprintParticle()
	{
		return false;
	}

	private void facePoint(double x, double z)
	{
		setYRot((float) (Mth.atan2(z - getZ(), x - getX()) * (double) (180F / (float) Math.PI)));
	}

	private void enableJumpControl()
	{
		((RabbitZombieJumpControl) jumpControl).setCanJump(true);
	}

	private void disableJumpControl()
	{
		((RabbitZombieJumpControl) jumpControl).setCanJump(false);
	}

	private void setLandingDelay()
	{
		if(moveControl.getSpeedModifier() < 2.2D)
			jumpDelayTicks = 10;
		else
			jumpDelayTicks = 1;
	}

	private void checkLandingDelay()
	{
		setLandingDelay();
		disableJumpControl();
	}

	@Override
	public void aiStep()
	{
		super.aiStep();

		if(jumpTicks != jumpDuration)
			jumpTicks++;
		else if(jumpDuration != 0)
		{
			jumpTicks = 0;
			jumpDuration = 0;

			setJumping(false);
		}
	}

	@Nullable
	public SoundEvent getJumpSound()
	{
		return SoundEvents.RABBIT_JUMP;
	}

	@Override
	public boolean doHurtTarget(Entity entity)
	{
		playSound(SoundEvents.RABBIT_ATTACK, 1F, (random.nextFloat() - random.nextFloat()) * .2F + 1F);
		return entity.hurt(DamageSource.mobAttack(this), 8F);
	}

	@Override
	public void handleEntityEvent(byte event)
	{
		if(event == 1)
		{
			spawnSprintParticle();
			jumpDuration = 10;
			jumpTicks = 0;
		}
		else
			super.handleEntityEvent(event);
	}

	@Override
	public Vec3 getLeashOffset()
	{
		return new Vec3(0D, .6F * getEyeHeight(), getBbWidth() * .4F);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return createZombieAttributes();
	}

	public static boolean canRabbitZombieSpawn(EntityType<? extends AbstractZombie> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random rng)
	{
		if(entityType != AEntityTypes.RABBIT_ZOMBIE)
			return false;
		if(!canZombieSpawn(entityType, level, spawnType, pos, rng))
			return false;

		var blockState = level.getBlockState(pos.below());
		return (blockState.is(Blocks.GRASS) || blockState.is(BlockTags.SNOW) || blockState.is(Tags.Blocks.SAND)) && level.getRawBrightness(pos, 0) > 8;
	}
}
