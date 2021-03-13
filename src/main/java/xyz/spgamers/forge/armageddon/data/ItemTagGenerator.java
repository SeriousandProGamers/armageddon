package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ItemTagGenerator extends ItemTagsProvider
{
	public ItemTagGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, new BlockTagsProvider(generator, ModConstants.MOD_ID, fileHelper), ModConstants.MOD_ID, fileHelper);
	}

	@Override
	protected void registerTags()
	{
		// allow our egg to be used in recipes using the vanilla egg?
		// removed for now, rotten eggs are not eggs
		/*getOrCreateBuilder(Tags.Items.EGGS)
				.add(ModItems.ROTTEN_EGG.get());*/
	}
}
