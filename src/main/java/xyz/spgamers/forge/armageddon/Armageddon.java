package xyz.spgamers.forge.armageddon;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import xyz.apex.forge.apexcore.lib.util.ModHelper;
import xyz.apex.forge.apexcore.lib.util.Registrar;
import xyz.spgamers.forge.armageddon.client.ClientSetup;
import xyz.spgamers.forge.armageddon.config.ServerConfig;
import xyz.spgamers.forge.armageddon.data.*;
import xyz.spgamers.forge.armageddon.init.*;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@Mod(ModConstants.MOD_ID)
public final class Armageddon
{
	public static final ServerConfig SERVER_CONFIG = new ServerConfig();

	public Armageddon()
	{
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG.configSpec);

		// default item properties for all items in this mod
		Registrar.registerModDefaultItemProperties(ModConstants.MOD_ID, () -> new Item.Properties().group(ModItems.ITEM_GROUP));

		ModHelper.registerDeferredRegisters(ModItems.ITEMS, ModEntities.ENTITIES, ModEffects.EFFECTS, ModPotions.POTIONS, ModEnchantments.ENCHANTMENTS);
		ModLootFunctionTypes.register();
		ModHelper.addListener(this::onCommonSetup);
		ModHelper.addListener(this::onGatherData);
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
		/*ModConstants.NETWORK.registerMessage(
				0,
				BloodMoonStateChangePacket.class,
				BloodMoonStateChangePacket::encode,
				BloodMoonStateChangePacket::new,
				BloodMoonStateChangePacket::process
		);*/
	}
}