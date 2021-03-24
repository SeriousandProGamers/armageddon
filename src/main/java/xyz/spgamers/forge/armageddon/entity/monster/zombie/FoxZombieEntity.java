package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ZombieHelper;

import java.util.Random;

public final class FoxZombieEntity extends AbstractZombieEntity
{
	public FoxZombieEntity(World world)
	{
		super(ModEntities.FOX_ZOMBIE.get(), world, Armageddon.SERVER_CONFIG.animals::isFoxZombieEnabled);
	}

	@Override
	public SoundEvent getEatSound(ItemStack itemStackIn)
	{
		return SoundEvents.ENTITY_FOX_EAT;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_FOX_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ENTITY_FOX_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_FOX_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
		// NOOP: getStepSound() is not nullable, this stops the sound being played
	}

	public static AttributeModifierMap.MutableAttribute registerFoxZombieAttributes()
	{
		return ZombieHelper.registerZombieAttributes();
	}

	public static boolean canFoxZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.animals.isFoxZombieEnabled())
			return false;
		if(!ZombieHelper.canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}
}
