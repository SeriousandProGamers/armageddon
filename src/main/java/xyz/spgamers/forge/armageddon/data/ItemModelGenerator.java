package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spgamers.forge.armageddon.util.Constants;

public class ItemModelGenerator extends ItemModelProvider
{
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Constants.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		getSpawnEggBuilder(modLoc(Constants.Items.ZOMBIE_PIG_SPAWN_EGG).toString());
		getSpawnEggBuilder(modLoc(Constants.Items.ZOMBIE_CREEPER_SPAWN_EGG).toString());
		getSpawnEggBuilder(modLoc(Constants.Items.ZOMBIE_COW_SPAWN_EGG).toString());
		getSpawnEggBuilder(modLoc(Constants.Items.ZOMBIE_RED_MOOSHROOM_SPAWN_EGG).toString());
	}

	private ItemModelBuilder getSpawnEggBuilder(String path)
	{
		return getBuilder(path).parent(getExistingFile(mcLoc("item/template_spawn_egg")));
	}
}
