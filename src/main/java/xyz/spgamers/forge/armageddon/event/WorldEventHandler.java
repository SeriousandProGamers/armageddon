package xyz.spgamers.forge.armageddon.event;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.event.types.BloodMoonEvent;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public final class WorldEventHandler
{
	private static final Map<RegistryKey<World>, Long> worldDayTimeMap = Maps.newHashMap();
	private static final Map<RegistryKey<World>, Boolean> isWorldBloodMoonEnabled = Maps.newHashMap();
	private static final Map<RegistryKey<World>, Boolean> isWorldSupportedMap = Maps.newHashMap();

	private WorldEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		World world = event.world;

		// does world support blood moons
		if(isBloodMoonSupported(world))
		{
			long previousTime = getPreviousWorldDayTime(world);
			long worldTime = world.getDayTime();

			boolean wasNight = previousTime <= 24000L && previousTime >= 13000L;
			boolean isNight = worldTime <= 24000L && worldTime >= 13000L;

			// when a new night starts
			if(isNight && !wasNight)
			{
				// full moon && random chance
				// world#getMoonPhase() is marked OnlyIn(CLIENT) will not exist server side
				// if(world.getMoonPhase() == 0 && world.rand.nextBoolean())
				if(world.getDimensionType().getMoonPhase(worldTime) == 0 && world.rand.nextBoolean())
					toggleBloodMoon(world, true);
			}
			// when a new day starts
			else if(wasNight && !isNight)
				toggleBloodMoon(world, false);
		}

		updateWorldPreviousDayTime(world);
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event)
	{
		IWorld world = event.getWorld();
		RegistryKey<World> dimension = ((World) world).getDimensionKey();

		isWorldSupportedMap.remove(dimension);
		isWorldBloodMoonEnabled.remove(dimension);
		worldDayTimeMap.remove(dimension);
	}

	public static boolean isBloodMoonSupported(World world)
	{
		RegistryKey<World> dimension = world.getDimensionKey();

		if(isWorldSupportedMap.containsKey(dimension))
			return isWorldSupportedMap.get(dimension);

		boolean isSupportedDefault = isBloodMoonSupportedDefault(world);
		BloodMoonEvent.WorldSupported event = new BloodMoonEvent.WorldSupported(world, isSupportedDefault);
		boolean isSupported = event.isSupported();
		isWorldSupportedMap.put(dimension, isSupported);
		return isSupported;
	}

	private static boolean isBloodMoonSupportedDefault(World world)
	{
		RegistryKey<World> dimension = world.getDimensionKey();

		// does not work in the nether or end dimensions
		if(dimension == World.THE_NETHER)
			return false;
		if(dimension == World.THE_END)
			return false;

		// time does not move in this dimension
		if(world.getDimensionType().doesFixedTimeExist())
			return false;
		return true;
	}

	public static boolean isBloodMoon(World world)
	{
		if(!isBloodMoonSupported(world))
			return false;
		return isWorldBloodMoonEnabled.getOrDefault(world.getDimensionKey(), false);
	}

	private static void toggleBloodMoon(World world, boolean enabled)
	{
		boolean wasBloodMoon = isBloodMoon(world);

		// disable the blood moon
		if(wasBloodMoon && !enabled)
			onBloodMoonEnded(world);
		// enable the blood moon
		else if(enabled && !wasBloodMoon)
		{
			boolean cancelled = MinecraftForge.EVENT_BUS.post(new BloodMoonEvent.Start(world));

			if(!cancelled)
				onBloodMoonStarted(world);
		}
	}

	private static void onBloodMoonStarted(World world)
	{
		RegistryKey<World> dimension = world.getDimensionKey();
		isWorldBloodMoonEnabled.put(dimension, true);

		for(PlayerEntity player : world.getPlayers())
		{
			player.sendStatusMessage(
					new StringTextComponent("Blood Moon Started!")
							.modifyStyle(style -> style.setItalic(true)
							                           .setColor(Color.fromTextFormatting(TextFormatting.RED))),
			true
			);

			player.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, 1F, 1F);
		}
	}

	private static void onBloodMoonEnded(World world)
	{
		RegistryKey<World> dimension = world.getDimensionKey();
		isWorldBloodMoonEnabled.put(dimension, false);

		for(PlayerEntity player : world.getPlayers())
		{
			player.sendStatusMessage(
					new StringTextComponent("Blood Moon Ended!")
							.modifyStyle(style -> style.setItalic(true)
							                           .setColor(Color.fromTextFormatting(TextFormatting.RED))),
					true
			);

			player.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, 1F, 1F);
		}

		MinecraftForge.EVENT_BUS.post(new BloodMoonEvent.Stop(world));
	}

	private static long getPreviousWorldDayTime(World world)
	{
		return worldDayTimeMap.getOrDefault(world.getDimensionKey(), 0L);
	}

	private static void updateWorldPreviousDayTime(World world)
	{
		worldDayTimeMap.put(world.getDimensionKey(), world.getDayTime());
	}
}
