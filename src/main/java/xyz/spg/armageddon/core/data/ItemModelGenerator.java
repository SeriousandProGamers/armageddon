package xyz.spg.armageddon.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spg.armageddon.shared.AItems;
import xyz.spg.armageddon.shared.Armageddon;

import java.util.Objects;

public final class ItemModelGenerator extends ItemModelProvider
{
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, Armageddon.ID_MOD, fileHelper);
	}

	@Override
	protected void registerModels()
	{
		spawnEggItem(AItems.PIG_ZOMBIE_SPAWN_EGG);
	}

	@Override
	public String getName()
	{
		return "Armageddon-ItemModel-Generator";
	}

	// region: Helpers
	private ItemModelBuilder generatedItem(ItemLike item)
	{
		return withExistingParent(itemName(item), mcLoc("item/generated")).texture("layer0", itemTexture(item));
	}

	private ItemModelBuilder handheldItem(ItemLike item)
	{
		return withExistingParent(itemName(item), mcLoc("item/handheld")).texture("layer0", itemTexture(item));
	}

	private ItemModelBuilder handheldRodItem(ItemLike item)
	{
		return withExistingParent(itemName(item), mcLoc("item/handheld_rod")).texture("layer0", itemTexture(item));
	}

	private ItemModelBuilder spawnEggItem(ItemLike item)
	{
		return withExistingParent(itemName(item), mcLoc("item/template_spawn_egg"));
	}

	private ResourceLocation registryName(ItemLike item)
	{
		return Objects.requireNonNull(item.asItem().getRegistryName());
	}

	private String itemName(ItemLike item)
	{
		return registryName(item).toString();
	}

	private ResourceLocation itemTexture(ItemLike item)
	{
		return appendFolder(registryName(item), ModelProvider.ITEM_FOLDER);
	}

	private ResourceLocation appendFolder(ResourceLocation location, String folder)
	{
		var path = location.getPath();

		if(path.startsWith(folder))
			return location;

		return new ResourceLocation(location.getNamespace(), folder + "/" + path);
	}
	// endregion
}
