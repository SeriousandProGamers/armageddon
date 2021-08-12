package xyz.spg.armageddon.core.entity.control;

import net.minecraft.world.entity.ai.control.MoveControl;
import xyz.spg.armageddon.core.entity.RabbitZombie;

public final class RabbitZombieMoveControl extends MoveControl
{
	private final RabbitZombie rabbit;
	private double nextJumpSpeed;

	public RabbitZombieMoveControl(RabbitZombie rabbit)
	{
		super(rabbit);

		this.rabbit = rabbit;
	}

	@Override
	public void tick()
	{
		if(rabbit.isOnGround() && !rabbit.isJumping_Living() && !((RabbitZombieJumpControl) rabbit.getJumpControl()).wantJump())
			rabbit.setSpeedModifier(0D);
		else if(hasWanted())
			rabbit.setSpeedModifier(nextJumpSpeed);

		super.tick();
	}

	@Override
	public void setWantedPosition(double x, double y, double z, double speed)
	{
		if(rabbit.isInWater())
			speed = 1.5D;

		super.setWantedPosition(x, y, z, speed);

		if(speed > 0D)
			nextJumpSpeed = speed;
	}
}
