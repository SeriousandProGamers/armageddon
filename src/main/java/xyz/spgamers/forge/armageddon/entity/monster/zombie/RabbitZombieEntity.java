package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.JumpController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ZombieHelper;

import java.util.Random;

public final class RabbitZombieEntity extends AbstractZombieEntity
{
	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;
	private int currentMoveTypeDuration;
	private int carrotTicks;

	public RabbitZombieEntity(World world)
	{
		super(ModEntities.RABBIT_ZOMBIE.get(), world, Armageddon.SERVER_CONFIG.animals::isRabbitZombieEnabled);

		jumpController = new JumpHelperController(this);
		moveController = new MoveHelperController(this);
		setMovementSpeed(0D);
	}

	@Override
	protected float getJumpUpwardsMotion()
	{
		if(!collidedHorizontally && (!moveController.isUpdating() || !(moveController.getY() > getPosY() + .5D)))
		{
			Path path = navigator.getPath();

			if(path != null && !path.isFinished())
			{
				Vector3d position = path.getPosition(this);

				if(position.y > getPosY() + .5D)
					return .5F;
			}

			return moveController.getSpeed() <= .6D ? .2F : .3F;
		}
		else
			return .5F;
	}

	@Override
	protected void jump()
	{
		super.jump();

		double d0 = moveController.getSpeed();

		if(d0 > 0D)
		{
			double d1 = horizontalMag(getMotion());

			if(d1 < .01D)
				moveRelative(.1F, new Vector3d(0D, 0D, 1D));
		}

		if(!world.isRemote())
			world.setEntityState(this, (byte) 1);
	}

	@OnlyIn(Dist.CLIENT)
	public float getJumpCompletion(float f)
	{
		return jumpDuration == 0 ? 0F : ((float) jumpTicks + f) / (float) jumpDuration;
	}

	public void setMovementSpeed(double newSpeed)
	{
		getNavigator().setSpeed(newSpeed);
		moveController.setMoveTo(moveController.getX(), moveController.getY(), moveController.getZ(), newSpeed);
	}

	@Override
	public void setJumping(boolean jumping)
	{
		super.setJumping(jumping);

		if(jumping)
			playSound(getJumpSound(), getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * .2F + 1F) * .8F);
	}

	public void startJumping()
	{
		setJumping(true);
		jumpDuration = 10;
		jumpTicks = 0;
	}

	@Override
	protected void updateAITasks()
	{
		if(currentMoveTypeDuration > 0)
			currentMoveTypeDuration--;

		if(carrotTicks > 0)
		{
			carrotTicks -= rand.nextInt(3);

			if(carrotTicks < 0)
				carrotTicks = 0;
		}

		if(onGround)
		{
			if(!wasOnGround)
			{
				setJumping(false);
				checkLandingDelay();
			}

			JumpHelperController jumpHelper = (JumpHelperController) jumpController;

			if(!jumpHelper.isJumping())
			{
				if(moveController.isUpdating() && currentMoveTypeDuration == 0)
				{
					Path path = navigator.getPath();
					Vector3d position = new Vector3d(moveController.getX(), moveController.getY(), moveController.getZ());

					if(path != null && !path.isFinished())
						position = path.getPosition(this);

					calculateRotationYaw(position.x, position.z);
					startJumping();
				}
			}
			else if(!jumpHelper.canJump)
				enableJumpControl();
		}

		/*if(isAggressive())
			setMovementSpeed(2.2D);*/

		wasOnGround = onGround;
	}

	@Override
	public boolean shouldSpawnRunningEffects()
	{
		return false;
	}

	private void calculateRotationYaw(double x, double z)
	{
		rotationYaw = (float) (Math.atan2(z - getPosZ(), x - getPosX()) * (double) (180F / (float) Math.PI)) - 90F;
	}

	private void enableJumpControl()
	{
		((JumpHelperController) jumpController).canJump = true;
	}

	private void disableJumpControl()
	{
		((JumpHelperController) jumpController).canJump = false;
	}

	private void updateMoveTypeDuration()
	{
		if(moveController.getSpeed() < 2.2D)
			currentMoveTypeDuration = 10;
		else
			currentMoveTypeDuration = 1;
	}

	private void checkLandingDelay()
	{
		updateMoveTypeDuration();
		disableJumpControl();
	}

	@Override
	public void livingTick()
	{
		super.livingTick();

		if(jumpTicks != jumpDuration)
			jumpTicks++;
		else if(jumpDuration != 0)
		{
			jumpTicks = 0;
			jumpDuration = 0;
			setJumping(false);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id)
	{
		if(id == 1)
		{
			handleRunningEffect();
			jumpDuration = 10;
			jumpTicks = 0;
		}
		else
			super.handleStatusUpdate(id);
	}

	private SoundEvent getJumpSound()
	{
		return SoundEvents.ENTITY_RABBIT_JUMP;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_RABBIT_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ENTITY_RABBIT_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_RABBIT_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
		// NOOP: getStepSound() is not nullable, this stops the sound being played
	}

	public static AttributeModifierMap.MutableAttribute registerRabbitZombieAttributes()
	{
		return ZombieHelper.registerZombieAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 3D).createMutableAttribute(Attributes.MOVEMENT_SPEED, .3F);
	}

	public static boolean canRabbitZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.animals.isRabbitZombieEnabled())
			return false;
		if(!ZombieHelper.canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}

	private static final class JumpHelperController extends JumpController
	{
		private final RabbitZombieEntity rabbit;
		private boolean canJump;

		JumpHelperController(RabbitZombieEntity rabbit)
		{
			super(rabbit);

			this.rabbit = rabbit;
		}

		public boolean isJumping()
		{
			return isJumping;
		}

		@Override
		public void tick()
		{
			if(isJumping)
			{
				rabbit.startJumping();
				isJumping = true;
			}
		}
	}

	private static final class MoveHelperController extends MovementController
	{
		private final RabbitZombieEntity rabbit;
		private double nextJumpSpeed;

		MoveHelperController(RabbitZombieEntity rabbit)
		{
			super(rabbit);

			this.rabbit = rabbit;
		}

		@Override
		public void tick()
		{
			if(rabbit.onGround && !rabbit.isJumping && !((JumpHelperController) rabbit.jumpController).isJumping())
				rabbit.setMovementSpeed(0D);
			else if(isUpdating())
				rabbit.setMovementSpeed(nextJumpSpeed);

			super.tick();
		}

		@Override
		public void setMoveTo(double x, double y, double z, double speedIn)
		{
			if(rabbit.isInWater())
				speedIn = 1.5D;

			super.setMoveTo(x, y, z, speedIn);

			if(speedIn > 0D)
				nextJumpSpeed = speedIn;
		}
	}
}
