package xyz.spgamers.forge.armageddon;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.item.BetterSpawnEggItem;

public final class ClientSetup
{
	private final IItemColor spawnEggItemColor = (stack, tintIndex) -> ((BetterSpawnEggItem) stack.getItem()).getColor(tintIndex);

	ClientSetup()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::onClientSetup);
		bus.addListener(this::onItemColors);
	}

	private void onClientSetup(FMLClientSetupEvent event)
	{
		ModEntities.clientSetupEntities();
	}

	private void onItemColors(ColorHandlerEvent.Item event)
	{
		for(RegistryObject<Item> entry : ModItems.ITEMS.getEntries())
		{
			Item item = entry.get();

			if(item instanceof BetterSpawnEggItem)
				event.getItemColors().register(spawnEggItemColor, item);
		}
	}
}
