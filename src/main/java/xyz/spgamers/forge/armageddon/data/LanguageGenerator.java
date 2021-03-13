package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.item.SpawnEggItem;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.Map;

public final class LanguageGenerator extends LanguageProvider
{
	private static final String ENGLISH_LOCALE = "en_us";
	private static final String SPAWN_EGG_SUFFIX = "Spawn Egg";
	private static final String ITEM_GROUP_PREFIX = "itemGroup";

	public LanguageGenerator(DataGenerator generator)
	{
		super(generator, ModConstants.MOD_ID, ENGLISH_LOCALE);
	}

	@Override
	protected void addTranslations()
	{
		add(ModItems.ITEM_GROUP, "Armageddon");
		addItem(ModItems.ROTTEN_PORKCHOP, "Rotten Porkchop");
		addEntityType(ModEntities.ZOMBIE_PIG, "Zombie Pig");
		addSpawnEggTranslations();
	}

	// this assumes entity types have already been translated
	// call this after entity type translation
	private void addSpawnEggTranslations()
	{
		for(RegistryObject<Item> obj : ModItems.ITEMS.getEntries())
		{
			obj.ifPresent(item -> {
				if(item instanceof SpawnEggItem)
				{
					EntityType<?> entityType = ((SpawnEggItem<?>) item).getEntityType();
					String translatedEntityName = getTranslationValue(entityType.getTranslationKey());
					add(item, String.format("%s %s", translatedEntityName, SPAWN_EGG_SUFFIX));
				}
			});
		}
	}

	// adds translation for item groups
	// pulls the correct translation key
	// off of the given item group
	// uses reflection cause mojang made it private
	private void add(ItemGroup group, String name)
	{
		String tabLabel = ObfuscationReflectionHelper.getPrivateValue(ItemGroup.class, group, "tabLabel");
		add(String.format("%s.%s", ITEM_GROUP_PREFIX, tabLabel), name);
	}

	// obtains translation value from the data map
	// if any errors occur translation key is returned
	private String getTranslationValue(String translationKey)
	{
		try
		{
			Map<String, String> data = ObfuscationReflectionHelper.getPrivateValue(LanguageProvider.class, this, "data");

			if(data == null)
				return translationKey;

			return data.getOrDefault(translationKey, translationKey);
		}
		catch(ObfuscationReflectionHelper.UnableToFindFieldException | ObfuscationReflectionHelper.UnableToAccessFieldException e)
		{
			return translationKey;
		}
	}
}
