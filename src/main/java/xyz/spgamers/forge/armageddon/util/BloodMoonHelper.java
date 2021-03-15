package xyz.spgamers.forge.armageddon.util;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.Color;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.world.BloodMoonSavedData;

import java.util.Map;

public final class BloodMoonHelper
{
	// value is hex
	// 0x<hex value>
	// use some color picker
	public static final Color MOON_COLOR = Color.fromInt(0x400707);

	private BloodMoonHelper()
	{
		throw new IllegalStateException();
	}

	public static boolean isBloodMoonActive(ServerWorld world)
	{
		return BloodMoonSavedData.instance(world).isBloodMoon();
	}

	public static Vector3f getMoonColorVec3F()
	{
		int rgbColor = BloodMoonHelper.MOON_COLOR.getColor();

		float r = (float) (rgbColor >> 16 & 255) / 255F;
		float g = (float) (rgbColor >> 8 & 255) / 255F;
		float b = (float) (rgbColor & 255) / 255F;

		return new Vector3f(r, g, b);
	}

	public static Vector3d getMoonColorVec3D()
	{
		int rgbColor = BloodMoonHelper.MOON_COLOR.getColor();

		float r = (float) (rgbColor >> 16 & 255) / 255F;
		float g = (float) (rgbColor >> 8 & 255) / 255F;
		float b = (float) (rgbColor & 255) / 255F;

		return new Vector3d(r, g, b);
	}

	@OnlyIn(Dist.CLIENT)
	public static final class ClientHelper
	{
		private static final Map<RegistryKey<World>, Boolean> isWorldBloodMoonEnabled = Maps.newHashMap();

		public static void setBloodMoonState(RegistryKey<World> dimension, boolean state)
		{
			isWorldBloodMoonEnabled.put(dimension, state);
		}

		public static boolean isBloodMoonEnabled()
		{
			ClientWorld world = Minecraft.getInstance().world;

			if(world == null)
				return false;

			return isWorldBloodMoonEnabled.getOrDefault(world.getDimensionKey(), false);
		}
	}
}
