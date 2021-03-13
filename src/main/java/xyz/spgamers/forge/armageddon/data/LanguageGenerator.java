package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
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
		addItem(ModItems.ROTTEN_BEEF, "Rotten Beef");
		addItem(ModItems.ROTTEN_FISH, "Rotten Fish");
		addItem(ModItems.ROTTEN_RABBIT, "Rotten Rabbit");
		addItem(ModItems.ROTTEN_CHICKEN, "Rotten Chicken");
		addItem(ModItems.ROTTEN_EGG, "Rotten Egg");
		addItem(ModItems.ROTTEN_MUTTON, "Rotten Mutton");
		addEntityType(ModEntities.ROTTEN_EGG, "Rotten Egg");
		// addEffect(ModEffects.ZOMBIE_EVASION, "Zombie Evasion");

		// human readable names are Zombie<> but code names are <>Zombie
		addEntityType(ModEntities.PIG_ZOMBIE, "Zombie Pig");
		addEntityType(ModEntities.COW_ZOMBIE, "Zombie Cow");
		// addEntityType(ModEntities.CHICKEN_ZOMBIE, "Zombie Chicken");
		addEntityType(ModEntities.SHEEP_ZOMBIE, "Zombie Sheep");
		addSpawnEggTranslations();
	}

	@Override
	public void add(Effect key, String value)
	{
		String effectName = key.getName();

		add(effectName, value);
		add(String.format("%s.%s", Items.POTION.getTranslationKey(), effectName), String.format("Potion of %s", value));
		add(String.format("%s.%s", Items.SPLASH_POTION.getTranslationKey(), effectName), String.format("Splash Potion of %s", value));
		add(String.format("%s.%s", Items.LINGERING_POTION.getTranslationKey(), effectName), String.format("Lingering Potion of %s", value));
		add(String.format("%s.%s", Items.TIPPED_ARROW.getTranslationKey(), effectName), String.format("Arrow of %s", value));
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
