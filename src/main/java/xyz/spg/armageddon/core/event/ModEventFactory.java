package xyz.spg.armageddon.core.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import xyz.spg.armageddon.core.entity.AbstractZombie;

import javax.annotation.Nullable;

public final class ModEventFactory
{
	public static AbstractZombieEvent.SummonAidEvent fireZombieSummonAid(AbstractZombie zombie, Level level, int x, int y, int z, @Nullable LivingEntity attacker, double summonChance)
	{
		var summonAidEvent = new AbstractZombieEvent.SummonAidEvent(zombie, level, x, y, z, attacker, summonChance);
		MinecraftForge.EVENT_BUS.post(summonAidEvent);
		return summonAidEvent;
	}
}
