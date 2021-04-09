package xyz.spgamers.forge.armageddon.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.apex.forge.apexcore.lib.util.ModHelper;
import xyz.spgamers.forge.armageddon.config.ClientConfig;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;

public final class ClientSetup
{
	public static final ClientConfig CLIENT_CONFIG = new ClientConfig();

	public ClientSetup()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG.configSpec);

		ModHelper.addListener(this::onClientSetup);
		ModHelper.addListener(this::onItemColors);
	}

	private void onClientSetup(FMLClientSetupEvent event)
	{
		ModEntities.clientSetup();
	}

	private void onItemColors(ColorHandlerEvent.Item event)
	{
		ItemColors itemColors = event.getItemColors();

		ModItems.ITEMS.getEntries().forEach(obj -> obj.ifPresent(item -> {
			if(item instanceof IItemColor)
				itemColors.register((IItemColor) item, item);
		}));
	}
}
