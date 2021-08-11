package xyz.spg.armageddon.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import xyz.spg.armageddon.shared.AEntityTypes;

import javax.annotation.Nullable;
import java.util.Random;

public class CowZombie extends AbstractZombie
{
	public CowZombie(EntityType<? extends AbstractZombie> entityType, Level level)
	{
		super(entityType, level);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.COW_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.COW_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.COW_DEATH;
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.COW_STEP;
	}

	@Override
	protected boolean isHumanoid()
	{
		return false;
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

	public static boolean canCowZombieSpawn(EntityType<? extends AbstractZombie> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random rng)
	{
		if(entityType != AEntityTypes.COW_ZOMBIE)
			return false;
		if(!canZombieSpawn(entityType, level, spawnType, pos, rng))
			return false;
		return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getLightEmission(pos) > 8;
	}
}
