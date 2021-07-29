package xyz.spg.armageddon.core.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class ZombieAttackTurtleEggGoal extends RemoveBlockGoal
{
	public ZombieAttackTurtleEggGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange)
	{
		super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
	}

	@Override
	public void playDestroyProgressSound(LevelAccessor levelAccessor, BlockPos blockPos)
	{
		levelAccessor.playSound(null, blockPos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + levelAccessor.getRandom().nextFloat() * 0.2F);
	}

	@Override
	public void playBreakSound(Level level, BlockPos pos)
	{
		level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, .7F, .9F + level.random.nextFloat() * .2F);
	}

	@Override
	public double acceptedDistance()
	{
		return 1.14D;
	}
}
