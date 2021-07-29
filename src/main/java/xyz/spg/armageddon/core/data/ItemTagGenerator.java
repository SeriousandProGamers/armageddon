package xyz.spg.armageddon.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spg.armageddon.shared.Armageddon;

public final class ItemTagGenerator extends ItemTagsProvider
{
	public ItemTagGenerator(DataGenerator generator, BlockTagsProvider blockTagsProvider, ExistingFileHelper fileHelper)
	{
		super(generator, blockTagsProvider, Armageddon.ID_MOD, fileHelper);
	}

	@Override
	protected void addTags()
	{
	}

	@Override
	public String getName()
	{
		return "Armageddon-ItemTag-Generator";
	}
}
