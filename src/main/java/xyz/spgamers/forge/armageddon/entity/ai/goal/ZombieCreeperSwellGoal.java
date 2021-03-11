package xyz.spgamers.forge.armageddon.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import xyz.spgamers.forge.armageddon.entity.ZombieCreeperEntity;

import java.util.EnumSet;

// Copy of CreeperSwellGoal
// Original looks for CreeperEntity
// and our ZombieCreeperEntity does not extend CreeperEntity
public class ZombieCreeperSwellGoal extends Goal
{
	private final ZombieCreeperEntity swellingCreeper;
	private LivingEntity creeperAttackTarget;

	public ZombieCreeperSwellGoal(ZombieCreeperEntity entity)
	{
		swellingCreeper = entity;

		setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute()
	{
		LivingEntity livingEntity = swellingCreeper.getAttackTarget();
		return swellingCreeper.getCreeperState() > 0 || livingEntity != null && swellingCreeper.getDistanceSq(livingEntity) < 9D;
	}

	@Override
	public void startExecuting()
	{
		super.startExecuting();

		swellingCreeper.getNavigator().clearPath();
		creeperAttackTarget = swellingCreeper.getAttackTarget();
	}

	@Override
	public void resetTask()
	{
		super.resetTask();

		creeperAttackTarget = null;
	}

	@Override
	public void tick()
	{
		super.tick();

		if(creeperAttackTarget == null)
			swellingCreeper.setCreeperState(-1);
		else if(swellingCreeper.getDistanceSq(creeperAttackTarget) > 49D)
			swellingCreeper.setCreeperState(-1);
		else if(!swellingCreeper.getEntitySenses().canSee(creeperAttackTarget))
			swellingCreeper.setCreeperState(-1);
		else
			swellingCreeper.setCreeperState(1);
	}
}
