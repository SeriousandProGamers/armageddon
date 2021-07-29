package xyz.spg.armageddon.core.evemt;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityEvent;
import xyz.spg.armageddon.core.entity.AbstractZombie;

import javax.annotation.Nullable;

public class AbstractZombieEvent extends EntityEvent
{
	protected final AbstractZombie zombie;

	public AbstractZombieEvent(AbstractZombie zombie)
	{
		super(zombie);

		this.zombie = zombie;
	}

	public final AbstractZombie getZombie()
	{
		return zombie;
	}

	@HasResult
	public static class SummonAidEvent extends AbstractZombieEvent
	{
		@Nullable protected AbstractZombie customSummonedAid;

		protected final Level level;
		protected final int x;
		protected final int y;
		protected final int z;
		@Nullable protected final LivingEntity attacker;
		protected final double summonChance;

		public SummonAidEvent(AbstractZombie zombie, Level level, int x, int y, int z, @Nullable LivingEntity attacker, double summonChance)
		{
			super(zombie);

			this.level = level;
			this.x = x;
			this.y = y;
			this.z = z;
			this.attacker = attacker;
			this.summonChance = summonChance;
		}

		@Nullable
		public final AbstractZombie getCustomSummonedAid()
		{
			return customSummonedAid;
		}

		public final void setCustomSummonedAid(AbstractZombie customSummonedAid)
		{
			this.customSummonedAid = customSummonedAid;
		}

		public Level getLevel()
		{
			return level;
		}

		public int getX()
		{
			return x;
		}

		public int getY()
		{
			return y;
		}

		public int getZ()
		{
			return z;
		}

		@Nullable public LivingEntity getAttacker()
		{
			return attacker;
		}

		public double getSummonChance()
		{
			return summonChance;
		}
	}
}
