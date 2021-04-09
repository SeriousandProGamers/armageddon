package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import xyz.spgamers.forge.armageddon.init.ModEnchantments;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.item.RottenRabbitFootItem;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.stream.Collectors;

public final class LanguageGenerator extends xyz.apex.forge.apexcore.lib.data.LanguageGenerator
{
	private static final String ENGLISH_LOCALE = "en_us";
	private static final String SPAWN_EGG_SUFFIX = "Spawn Egg";

	public LanguageGenerator(DataGenerator generator)
	{
		super(generator, ModConstants.MOD_ID, ENGLISH_LOCALE);
	}

	@Override
	protected void addTranslations()
	{
		add(RottenRabbitFootItem.STATUS_TRANSLATION_KEY, "%s has given you %s");

		add(ModItems.ITEM_GROUP, "Armageddon");
		addItem(ModItems.ROTTEN_PORKCHOP, "Rotten Porkchop");
		addItem(ModItems.ROTTEN_BEEF, "Rotten Beef");
		addItem(ModItems.ROTTEN_FISH, "Rotten Fish");
		addItem(ModItems.ROTTEN_RABBIT, "Rotten Rabbit");
		addItem(ModItems.ROTTEN_RABBIT_FOOT, "Rotten Rabbit Foot");
		addItem(ModItems.ROTTEN_CHICKEN, "Rotten Chicken");
		addItem(ModItems.ROTTEN_EGG, "Rotten Egg");
		addItem(ModItems.ROTTEN_MUTTON, "Rotten Mutton");
		addItem(ModItems.SPOILED_MILK_BUCKET, "Spoiled Milk Bucket");
		// addEffect(ModEffects.ZOMBIE_EVASION, "Zombie Evasion");

		addEnchantment(ModEnchantments.POISON_ENCHANTMENT, "Poison");

		// human readable names are Zombie<> but code names are <>Zombie
		addEntityType(ModEntities.PIG_ZOMBIE, "Zombie Pig");
		addEntityType(ModEntities.COW_ZOMBIE, "Zombie Cow");
		addEntityType(ModEntities.CHICKEN_ZOMBIE, "Zombie Chicken");
		addEntityType(ModEntities.SHEEP_ZOMBIE, "Zombie Sheep");
		addEntityType(ModEntities.FOX_ZOMBIE, "Zombie Fox");
		addEntityType(ModEntities.PANDA_ZOMBIE, "Zombie Panda");
		addEntityType(ModEntities.POLAR_BEAR_ZOMBIE, "Zombie Polar Bear");
		addEntityType(ModEntities.RABBIT_ZOMBIE, "Zombie Rabbit");
		addEntityType(ModEntities.WOLF_ZOMBIE, "Zombie Wolf");
		addSpawnEggTranslations(ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toSet()), SPAWN_EGG_SUFFIX);
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
}
