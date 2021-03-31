package xyz.spgamers.forge.armageddon;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.spgamers.forge.armageddon.client.ClientSetup;
import xyz.spgamers.forge.armageddon.config.ServerConfig;
import xyz.spgamers.forge.armageddon.data.*;
import xyz.spgamers.forge.armageddon.init.*;
import xyz.spgamers.forge.armageddon.packet.BloodMoonStateChangePacket;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@Mod(ModConstants.MOD_ID)
public final class Armageddon
{
	public static final ServerConfig SERVER_CONFIG = new ServerConfig();

	public Armageddon()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG.configSpec);

		ModItems.ITEMS.register(bus);
		ModEntities.ENTITIES.register(bus);
		ModEffects.EFFECTS.register(bus);
		ModPotions.POTIONS.register(bus);
		ModEnchantments.ENCHANTMENTS.register(bus);

		bus.addListener(this::onCommonSetup);
		bus.addListener(this::onGatherData);
	}

	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(ModItems::commonSetup);
		event.enqueueWork(ModEntities::commonSetup);
		event.enqueueWork(ModEffects::commonSetup);
		event.enqueueWork(ModPotions::commonSetup);
		event.enqueueWork(this::registerPackets);
	}

	private void onGatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		if(event.includeClient())
		{
			// Client asset generators
			// block / item model json files, blockstate json files, language json files, etc
			generator.addProvider(new LanguageGenerator(generator));
			generator.addProvider(new ItemModelGenerator(generator, fileHelper));
		}

		if(event.includeServer())
		{
			// Server asset generators
			// recipes, advancements, tags, loot tables, etc
			generator.addProvider(new LootTableGenerator(generator));
			generator.addProvider(new EntityTypeTagGenerator(generator, fileHelper));
			generator.addProvider(new ItemTagGenerator(generator, fileHelper));
		}
	}

	private void registerPackets()
	{
		ModConstants.NETWORK.registerMessage(
				0,
				BloodMoonStateChangePacket.class,
				BloodMoonStateChangePacket::encode,
				BloodMoonStateChangePacket::new,
				BloodMoonStateChangePacket::process
		);
	}
}