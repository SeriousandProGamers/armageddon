package xyz.spg.armageddon.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spg.armageddon.shared.ATags;
import xyz.spg.armageddon.shared.Armageddon;

public final class BlockTagGenerator extends BlockTagsProvider
{
	public BlockTagGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, Armageddon.ID_MOD, fileHelper);
	}

	@Override
	protected void addTags()
	{
		tag(ATags.Blocks.PUMPKINS_CARVED).add(Blocks.CARVED_PUMPKIN);
		tag(ATags.Blocks.PUMPKINS_JACK_O_LANTERN).add(Blocks.JACK_O_LANTERN);
		tag(ATags.Blocks.PUMPKINS_PLAIN).add(Blocks.PUMPKIN);
		tag(ATags.Blocks.PUMPKINS).addTags(ATags.Blocks.PUMPKINS_CARVED, ATags.Blocks.PUMPKINS_JACK_O_LANTERN, ATags.Blocks.PUMPKINS_PLAIN);
	}

	@Override
	public String getName()
	{
		return "Armageddon-BlockTag-Generator";
	}
}
