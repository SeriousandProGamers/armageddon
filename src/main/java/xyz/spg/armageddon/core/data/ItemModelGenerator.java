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
		generatedItem(AItems.ROTTEN_PORKCHOP);
		generatedItem(AItems.ROTTEN_BEEF);
		generatedItem(AItems.ROTTEN_MUTTON);
		generatedItem(AItems.ROTTEN_CHICKEN);
		generatedItem(AItems.ROTTEN_EGG);
		generatedItem(AItems.ROTTEN_RABBIT);
		generatedItem(AItems.ROTTEN_RABBIT_FOOT);
		generatedItem(AItems.ROTTEN_FISH);

		// spawnEggItem(AItems.PANDA_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.POLAR_BEAR_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.FOX_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.WOLF_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.RABBIT_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.CHICKEN_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.SHEEP_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.COW_ZOMBIE_SPAWN_EGG);
		spawnEggItem(AItems.PIG_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.BLAZE_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.EXPLOSIVE_ZOMBIE_SPAWN_EGG);
		// spawnEggItem(AItems.TELEPORTING_ZOMBIE_SPAWN_EGG);
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
