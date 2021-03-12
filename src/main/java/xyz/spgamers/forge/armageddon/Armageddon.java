package xyz.spgamers.forge.armageddon;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.spgamers.forge.armageddon.client.ClientSetup;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@Mod(ModConstants.MOD_ID)
public final class Armageddon
{
	public Armageddon()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);

		ModItems.ITEMS.register(bus);
		ModEntities.ENTITIES.register(bus);

		bus.addListener(this::onCommonSetup);
		bus.addListener(this::onGatherData);
	}

	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		registerPackets();
	}

	private void onGatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		if(event.includeClient())
		{
			// Client asset generators
			// block / item model json files, blockstate json files, language json files, etc
		}

		if(event.includeServer())
		{
			// Server asset generators
			// recipes, advancements, tags, loot tables, etc
		}
	}

	private void registerPackets()
	{
	}
}