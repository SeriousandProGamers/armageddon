package xyz.spgamers.forge.armageddon.util;

import net.minecraft.world.IWorld;

public final class WorldHelper
{
	private WorldHelper()
	{
		throw new IllegalStateException();
	}

	public static boolean isNight(IWorld world)
	{
		long dayTime = world.getWorldInfo().getDayTime();
		return dayTime <= 24000L && dayTime >= 13000L;
	}
}
