package xyz.spgamers.forge.armageddon.client;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ClientSetup
{
	public ClientSetup()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::onClientSetup);
	}

	private void onClientSetup(FMLClientSetupEvent event)
	{
	}
}
