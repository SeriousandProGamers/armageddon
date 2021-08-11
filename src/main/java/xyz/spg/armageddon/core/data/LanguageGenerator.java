package xyz.spg.armageddon.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.spg.armageddon.shared.AEnchantments;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.AItems;
import xyz.spg.armageddon.shared.Armageddon;

public final class LanguageGenerator extends LanguageProvider
{
	public LanguageGenerator(DataGenerator generator)
	{
		super(generator, Armageddon.ID_MOD, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		// Creative Tab
		add("itemGroup.%s".formatted(Armageddon.ID_MOD), "Armageddon");

		// Items
		add(AItems.ROTTEN_PORKCHOP, "Rotten Porkchop");
		add(AItems.ROTTEN_BEEF, "Rotten Beef");
		add(AItems.ROTTEN_MUTTON, "Rotten Mutton");
		add(AItems.ROTTEN_CHICKEN, "Rotten Chicken");
		add(AItems.ROTTEN_EGG, "Rotten Egg");
		add(AItems.ROTTEN_RABBIT, "Rotten Rabbit");
		add(AItems.ROTTEN_RABBIT_FOOT, "Rotten Rabbit's Foot");
		add(AItems.ROTTEN_FISH, "Rotten Fish");

		// add(AItems.PANDA_ZOMBIE_SPAWN_EGG, "Panda Zombie Spawn Egg");
		// add(AItems.POLAR_BEAR_ZOMBIE_SPAWN_EGG, "Polar Bear Zombie Spawn Egg");
		// add(AItems.FOX_ZOMBIE_SPAWN_EGG, "Fox Zombie Spawn Egg");
		// add(AItems.WOLF_ZOMBIE_SPAWN_EGG, "Wolf Zombie Spawn Egg");
		// add(AItems.RABBIT_ZOMBIE_SPAWN_EGG, "Rabbit Zombie Spawn Egg");
		// add(AItems.CHICKEN_ZOMBIE_SPAWN_EGG, "Chicken Zombie Spawn Egg");
		// add(AItems.SHEEP_ZOMBIE_SPAWN_EGG, "Sheep Zombie Spawn Egg");
		add(AItems.COW_ZOMBIE_SPAWN_EGG, "Cow Zombie Spawn Egg");
		add(AItems.PIG_ZOMBIE_SPAWN_EGG, "Pig Zombie Spawn Egg");

		// add(AItems.BLAZE_ZOMBIE_SPAWN_EGG, "Blaze Zombie Spawn Egg");
		// add(AItems.EXPLOSIVE_ZOMBIE_SPAWN_EGG, "Explosive Zombie Spawn Egg");
		// add(AItems.TELEPORTING_ZOMBIE_SPAWN_EGG, "Teleporting Zombie Spawn Egg");

		// Entities
		// add(AEntityTypes.PANDA_ZOMBIE, "Panda Zombie");
		// add(AEntityTypes.POLAR_BEAR_ZOMBIE, "Polar Bear Zombie");
		// add(AEntityTypes.FOX_ZOMBIE, "Fox Zombie");
		// add(AEntityTypes.WOLF_ZOMBIE, "Wolf Zombie");
		// add(AEntityTypes.RABBIT_ZOMBIE, "Rabbit Zombie");
		// add(AEntityTypes.CHICKEN_ZOMBIE, "Chicken Zombie");
		// add(AEntityTypes.SHEEP_ZOMBIE, "Sheep Zombie");
		add(AEntityTypes.COW_ZOMBIE, "Cow Zombie");
		add(AEntityTypes.PIG_ZOMBIE, "Pig Zombie");

		// add(AEntityTypes.BLAZE_ZOMBIE, "Blaze Zombie");
		// add(AEntityTypes.EXPLOSIVE_ZOMBIE, "Explosive Zombie");
		// add(AEntityTypes.TELEPORTING_ZOMBIE, "Teleporting Zombie");

		// Enchantments
		add(AEnchantments.POISON, "Poison");

		// MobEffects
		// Potions
	}

	@Override
	public String getName()
	{
		return "Armageddon-Language[en_us]-Generator";
	}
}
