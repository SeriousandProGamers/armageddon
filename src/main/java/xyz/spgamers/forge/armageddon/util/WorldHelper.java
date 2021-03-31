package xyz.spgamers.forge.armageddon.util;

import com.google.common.collect.Maps;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Map;

public final class WorldHelper
{
	private static final Map<RegistryKey<World>, Long> worldDayTimeMap = Maps.newHashMap();

	private WorldHelper()
	{
		throw new IllegalStateException();
	}

	public static boolean isServerWorld(IWorld world)
	{
		return !world.isRemote();
	}

	public static boolean isClientWorld(IWorld world)
	{
		return world.isRemote();
	}

	public static long getPreviousWorldDayTime(World world)
	{
		return worldDayTimeMap.getOrDefault(world.getDimensionKey(), 0L);
	}

	public static void updateWorldPreviousDayTime(World world)
	{
		worldDayTimeMap.put(world.getDimensionKey(), world.getDayTime());
	}

	public static boolean isNight(IWorld world)
	{
		long dayTime = world.getWorldInfo().getDayTime();
		return dayTime <= 24000L && dayTime >= 13000L;
	}
}
