package xyz.spg.armageddon.core.entity.goal;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import xyz.spg.armageddon.core.entity.AbstractZombie;

public final class ZombieAttackGoal extends MeleeAttackGoal
{
	private final AbstractZombie zombie;
	private int raiseArmsTicks;

	public ZombieAttackGoal(AbstractZombie zombie, double speedModifier, boolean followingTargetEvenIfNotSeen)
	{
		super(zombie, speedModifier, followingTargetEvenIfNotSeen);

		this.zombie = zombie;
	}

	@Override
	public void start()
	{
		super.start();
		raiseArmsTicks = 0;
	}

	@Override
	public void stop()
	{
		super.stop();
		zombie.setAggressive(false);
	}

	@Override
	public void tick()
	{
		super.tick();

		raiseArmsTicks++;

		if(raiseArmsTicks >= 5 && getTicksUntilNextAttack() < getAttackInterval() / 2)
			zombie.setAggressive(true);
		else
			zombie.setAggressive(false);
	}
}
