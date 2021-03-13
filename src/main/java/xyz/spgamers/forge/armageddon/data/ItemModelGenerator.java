package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.item.SpawnEggItem;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.Objects;

public final class ItemModelGenerator extends ItemModelProvider
{
	private static final String SPAWN_EGG_ITEM_MODEL = "template_spawn_egg";
	private static final String GENERATED_ITEM_MODEL = "generated";
	private static final String LAYER0 = "layer0";

	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, ModConstants.MOD_ID, fileHelper);
	}

	@Override
	protected void registerModels()
	{
		for(RegistryObject<Item> obj : ModItems.ITEMS.getEntries())
		{
			obj.ifPresent(item -> {
				if(item instanceof SpawnEggItem)
					generateSpawnEggItemModel(item);
				else
					generateDefaultItemModel(item);
			});
		}
	}

	// .getRegistryName() is marked nullable
	// but should never return null
	// only null on items with no registry (items not registered correctly)
	@SuppressWarnings("ConstantConditions")
	private void generateSpawnEggItemModel(IItemProvider itemProvider)
	{
		getBuilder(itemProvider.asItem().getRegistryName().toString())
				.parent(getExistingFile(appendItemFolder(mcLoc(SPAWN_EGG_ITEM_MODEL))));
	}

	private void generateDefaultItemModel(IItemProvider itemProvider)
	{
		ResourceLocation  itemName = Objects.requireNonNull(itemProvider.asItem().getRegistryName());

		getBuilder(itemName.toString())
				.parent(getExistingFile(appendItemFolder(mcLoc(GENERATED_ITEM_MODEL))))
				.texture(LAYER0, appendItemFolder(itemName));
	}

	private ResourceLocation appendItemFolder(ResourceLocation loc)
	{
		String path = loc.getPath();

		if(path.startsWith(ITEM_FOLDER))
			return loc;
		else
			return new ResourceLocation(loc.getNamespace(), String.format("%s/%s", ITEM_FOLDER, loc.getPath()));
	}
}
