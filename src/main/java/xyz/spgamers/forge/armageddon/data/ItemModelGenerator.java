package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import xyz.apex.forge.apexcore.lib.data.StateAndModelGenerator;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ItemModelGenerator extends StateAndModelGenerator
{
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, fileHelper, ModConstants.MOD_ID);
	}

	@Override
	protected void registerStatesAndModels()
	{
		for(RegistryObject<Item> obj : ModItems.ITEMS.getEntries())
		{
			obj.ifPresent(item -> {
				if(item instanceof SpawnEggItem)
					spawnEggItem(item);
				else
					generatedItem(item);
			});
		}
	}
}
