package xyz.spg.armageddon.core.entity.control;

import net.minecraft.world.entity.ai.control.JumpControl;
import xyz.spg.armageddon.core.entity.RabbitZombie;

public final class RabbitZombieJumpControl extends JumpControl
{
	private final RabbitZombie rabbit;
	private boolean canJump;

	public RabbitZombieJumpControl(RabbitZombie rabbit)
	{
		super(rabbit);

		this.rabbit = rabbit;
	}

	public boolean wantJump()
	{
		return jump;
	}

	public boolean canJump()
	{
		return canJump;
	}

	public void setCanJump(boolean canJump)
	{
		this.canJump = canJump;
	}

	@Override
	public void tick()
	{
		if(jump)
		{
			rabbit.startJumping();
			jump = false;
		}
	}
}
