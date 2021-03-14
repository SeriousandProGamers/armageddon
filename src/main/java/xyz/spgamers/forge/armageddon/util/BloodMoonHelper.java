package xyz.spgamers.forge.armageddon.util;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public final class BloodMoonHelper
{
	@Nonnull
	public static final Color MOON_COLOR = Color.fromTextFormatting(TextFormatting.RED);

	private BloodMoonHelper()
	{
		throw new IllegalStateException();
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
