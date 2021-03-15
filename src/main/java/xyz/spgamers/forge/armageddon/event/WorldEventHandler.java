package xyz.spgamers.forge.armageddon.event;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.util.ModConstants;
import xyz.spgamers.forge.armageddon.util.WorldHelper;
import xyz.spgamers.forge.armageddon.world.BloodMoonSavedData;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public final class WorldEventHandler
{
	private WorldEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.side.isClient())
			return;

		ServerWorld world = (ServerWorld) event.world;
		BloodMoonSavedData bloodMoonData = BloodMoonSavedData.instance(world);
		bloodMoonData.update();
		WorldHelper.updateWorldPreviousDayTime(world);
	}
}
