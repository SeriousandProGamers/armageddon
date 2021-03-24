package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.util.ModConstants;
import xyz.spgamers.forge.armageddon.util.WorldHelper;
import xyz.spgamers.forge.armageddon.util.ZombieHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public final class ChickenZombieEntity extends AbstractZombieEntity
{
	public float wingRotation;
	public float destPos;
	public float oFlapSpeed;
	public float oFlap;
	public float windRotDelta = 1F;
	public int timeUntilNextEgg = rand.nextInt(6000) + 6000;
	public boolean chickenJockey;

	public ChickenZombieEntity(World world)
	{
		super(ModEntities.CHICKEN_ZOMBIE.get(), world, Armageddon.SERVER_CONFIG.animals::isChickenZombieEnabled);

		setPathPriority(PathNodeType.WATER, 0F);
	}

	@Override
	public void livingTick()
	{
		super.livingTick();

		oFlap = wingRotation;
		oFlapSpeed = destPos;

		destPos = (float) ((double) destPos + (onGround ? -1D : 4D) * .3D);
		destPos = MathHelper.clamp(destPos, 0F, 1F);

		if(!onGround && windRotDelta < 1F)
			windRotDelta = 1F;

		windRotDelta = (float) ((double) windRotDelta * .9D);
		Vector3d motion = getMotion();

		if(!onGround && motion.y < 0D)
			setMotion(motion.mul(1D, .6D, 1D));

		wingRotation += windRotDelta * 2F;

		if(WorldHelper.isServerWorld(world) && isAlive() && !isChild() && !isChickenJockey() && --timeUntilNextEgg <= 0)
		{
			playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1F, (rand.nextFloat() - rand.nextFloat()) * .2F + 1F);
			entityDropItem(ModItems.ROTTEN_EGG.get());
			timeUntilNextEgg = rand.nextInt(6000) + 6000;
		}
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier)
	{
		return false;
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);

		setChickenJockey(compound.getBoolean(ModConstants.NBT.IS_CHICKEN_JOCKEY));

		if(compound.contains(ModConstants.NBT.EGG_LAY_TIME, Constants.NBT.TAG_INT))
			timeUntilNextEgg = compound.getInt(ModConstants.NBT.EGG_LAY_TIME);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putBoolean(ModConstants.NBT.IS_CHICKEN_JOCKEY, isChickenJockey());
		compound.putInt(ModConstants.NBT.EGG_LAY_TIME, timeUntilNextEgg);
	}

	@Override
	public boolean canDespawn(double distanceToClosestPlayer)
	{
		return isChickenJockey();
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		super.updatePassenger(passenger);

		float f = MathHelper.sin(renderYawOffset * ((float) Math.PI / 180F));
		float f1 = MathHelper.sin(renderYawOffset * ((float) Math.PI / 180F));

		passenger.setPosition(getPosX() + (double) (.1F * f), getPosYHeight(.5D) + passenger.getYOffset() + 0D, getPosZ() - (double) (.1F * f1));

		if(passenger instanceof LivingEntity)
			((LivingEntity) passenger).renderYawOffset = renderYawOffset;
	}

	public boolean isChickenJockey()
	{
		return chickenJockey;
	}

	public void setChickenJockey(boolean chickenJockey)
	{
		this.chickenJockey = chickenJockey;
	}

	@Nullable
	@Override
	public Entity getControllingPassenger()
	{
		List<Entity> passengers = getPassengers();

		if(isChickenJockey() && !passengers.isEmpty())
			return passengers.get(0);
		return null;
	}

	@Override
	public boolean canBeSteered()
	{
		return isChickenJockey();
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_CHICKEN_DEATH;
	}

	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.ENTITY_CHICKEN_STEP;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
	{
		return isChild() ? sizeIn.height * .85F : sizeIn.height * .925F;
	}

	public static AttributeModifierMap.MutableAttribute registerChickenZombieAttributes()
	{
		return ZombieHelper.registerZombieAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 4D).createMutableAttribute(Attributes.MOVEMENT_SPEED, .25D);
	}

	public static boolean canChickenZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.animals.isChickenZombieEnabled())
			return false;
		if(!ZombieHelper.canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}
}
