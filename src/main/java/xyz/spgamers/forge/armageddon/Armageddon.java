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
import xyz.spgamers.forge.armageddon.data.ItemModelGenerator;
import xyz.spgamers.forge.armageddon.data.LanguageGenerator;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.util.Constants;

@Mod(Constants.MOD_ID)
public class Armageddon
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

	private void onGatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		if(event.includeClient())
		{
			generator.addProvider(new LanguageGenerator(generator));
			// generator.addProvider(new BlockStateGenerator(generator, fileHelper));
			generator.addProvider(new ItemModelGenerator(generator, fileHelper));
		}

		if(event.includeServer())
		{
			// generator.addProvider(new LootTableGenerator(generator));
			// generator.addProvider(new RecipeGenerator(generator));
		}
	}

	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		ModEntities.setupEntities();
	}
}