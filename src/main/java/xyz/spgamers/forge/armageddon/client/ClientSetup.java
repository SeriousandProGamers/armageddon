package xyz.spgamers.forge.armageddon.client;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.spgamers.forge.armageddon.config.ClientConfig;
import xyz.spgamers.forge.armageddon.init.ModEntities;

public final class ClientSetup
{
	public static final ClientConfig CLIENT_CONFIG = new ClientConfig();

	public ClientSetup()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG.configSpec);

		bus.addListener(this::onClientSetup);
	}

	private void onClientSetup(FMLClientSetupEvent event)
	{
		ModEntities.clientSetup();
	}
}
