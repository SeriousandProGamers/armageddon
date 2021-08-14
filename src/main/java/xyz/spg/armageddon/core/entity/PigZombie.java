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

public final class PigZombie extends AbstractZombie
{
	public PigZombie(EntityType<? extends AbstractZombie> entityType, Level level)
	{
		super(entityType, level);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.PIG_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.PIG_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.PIG_DEATH;
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.PIG_STEP;
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

	public static boolean canPigZombieSpawn(EntityType<? extends AbstractZombie> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random rng)
	{
		if(entityType != AEntityTypes.PIG_ZOMBIE)
			return false;
		if(!canZombieSpawn(entityType, level, spawnType, pos, rng))
			return false;
		return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getLightEmission(pos) > 8;
	}
}
