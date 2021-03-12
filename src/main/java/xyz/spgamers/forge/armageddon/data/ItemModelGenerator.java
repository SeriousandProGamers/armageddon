package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.item.SpawnEggItem;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ItemModelGenerator extends ItemModelProvider
{
	private static final String SPAWN_EGG_ITEM_MODEL = "item/template_spawn_egg";

	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, ModConstants.MOD_ID, fileHelper);
	}

	@Override
	protected void registerModels()
	{
		generateSpawnEggItemModels();
	}

	// .getRegistryName() is marked nullable
	// but should never return null
	// only null on items with no registry (items not registered correctly)
	@SuppressWarnings("ConstantConditions")
	private void generateSpawnEggItemModels()
	{
		for(RegistryObject<Item> obj : ModItems.ITEMS.getEntries())
		{
			obj.ifPresent(item -> {
				if(item instanceof SpawnEggItem)
					getBuilder(item.getRegistryName().toString()).parent(getExistingFile(mcLoc(SPAWN_EGG_ITEM_MODEL)));
			});
		}
	}
}
