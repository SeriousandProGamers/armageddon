package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.util.Constants;

public class LanguageGenerator extends LanguageProvider
{
	public LanguageGenerator(DataGenerator gen)
	{
		super(gen, Constants.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		addItem(ModItems.ZOMBIE_PIG_SPAWN_EGG, "Zombie Pig Spawn Egg");
		addItem(ModItems.ZOMBIE_CREEPER_SPAWN_EGG, "Zombie Creeper Spawn Egg");

		addEntityType(ModEntities.ZOMBIE_PIG, "Zombie Pig");
		addEntityType(ModEntities.ZOMBIE_CREEPER, "Zombie Creeper");
	}
}
