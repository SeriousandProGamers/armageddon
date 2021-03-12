package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;

import java.util.Random;

public final class ZombiePigEntity extends AbstractZombieEntity
{
	public ZombiePigEntity(World world)
	{
		super(ModEntities.ZOMBIE_PIG.get(), world, Armageddon.SERVER_CONFIG.entities::isZombiePigEnabled);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ENTITY_PIG_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.ENTITY_PIG_STEP;
	}

	public static AttributeModifierMap.MutableAttribute registerZombiePigAttributes()
	{
		return registerZombieAttributes();
	}

	public static boolean canZombiePigSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.entities.isZombiePigEnabled())
			return false;
		if(!canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}
}
