package xyz.spg.armageddon.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spg.armageddon.shared.AItems;
import xyz.spg.armageddon.shared.ATags;
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
		tag(Tags.Items.EGGS).add(AItems.ROTTEN_EGG);
		tag(ATags.Items.ROTTEN).add(AItems.ROTTEN_PORKCHOP, AItems.ROTTEN_BEEF, AItems.ROTTEN_MUTTON, AItems.ROTTEN_CHICKEN, AItems.ROTTEN_EGG, AItems.ROTTEN_RABBIT, AItems.ROTTEN_RABBIT_FOOT, AItems.ROTTEN_FISH);
	}

	@Override
	public String getName()
	{
		return "Armageddon-ItemTag-Generator";
	}
}
