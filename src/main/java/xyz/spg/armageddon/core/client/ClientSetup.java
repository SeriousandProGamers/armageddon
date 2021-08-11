package xyz.spg.armageddon.core.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import xyz.spg.armageddon.core.client.renderer.CowZombieRenderer;
import xyz.spg.armageddon.core.client.renderer.PigZombieRenderer;
import xyz.spg.armageddon.core.client.renderer.SheepZombieRenderer;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public final class ClientSetup
{
	public ClientSetup()
	{
		var bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::onClientSetup);
		bus.addListener(this::onItemColors);
	}

	private void onClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(this::setupEntities);
	}

	private void onItemColors(ColorHandlerEvent.Item event)
	{
		var colors = event.getItemColors();

		for(RegistryObject<Item> object : Armageddon.ITEMS.getEntries())
		{
			Item item = object.get();

			if(item instanceof ItemColor itemColor)
				colors.register(itemColor, item);
		}
	}

	private void setupEntities()
	{
		EntityRenderers.register(AEntityTypes.PIG_ZOMBIE, PigZombieRenderer::new);
		EntityRenderers.register(AEntityTypes.COW_ZOMBIE, CowZombieRenderer::new);
		EntityRenderers.register(AEntityTypes.SHEEP_ZOMBIE, SheepZombieRenderer::new);
	}
}
