package xyz.spgamers.forge.armageddon.event;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.util.ModConstants;
import xyz.spgamers.forge.armageddon.util.ZombieHelper;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public final class BiomeEventHandler
{
	private BiomeEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		ZombieHelper.addAdditionalZombieSpawns(event);
	}
}
