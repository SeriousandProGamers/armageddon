package xyz.spgamers.forge.armageddon.util;

import net.minecraft.world.IWorld;

public final class WorldHelper
{
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
}
