package xyz.spg.armageddon.shared;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public final class ATags
{
	static void register()
	{
		Names.register();
		Blocks.register();
		Items.register();
		EntityTypes.register();
	}

	public static final class Names
	{
		// region: Blocks
		public static final String PUMPKINS = "pumpkins";
		public static final String PUMPKINS_PLAIN = "pumpkins/plain";
		public static final String PUMPKINS_CARVED = "pumpkins/carved";
		public static final String PUMPKINS_JACK_O_LANTERN = "pumpkins/jack_o_lantern";
		// endregion

		// region: EntityTypes
		public static final String ZOMBIES = "zombies";
		public static final String ZOMBIES_MOB = "zombies/mobs";
		public static final String ZOMBIES_SPECIAL = "zombies/special";
		// endregion

		private static void register() { }
	}

	public static final class Blocks
	{
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS = bindForge(Names.PUMPKINS);
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS_PLAIN = bindForge(Names.PUMPKINS_PLAIN);
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS_CARVED = bindForge(Names.PUMPKINS_CARVED);
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS_JACK_O_LANTERN = bindForge(Names.PUMPKINS_JACK_O_LANTERN);

		// region: Helpers
		private static Tags.IOptionalNamedTag<Block> bind(String modId, String tagName)
		{
			return net.minecraft.tags.BlockTags.createOptional(new ResourceLocation(modId, tagName));
		}

		private static Tags.IOptionalNamedTag<Block> bindForge(String tagName)
		{
			return bind(Armageddon.ID_FORGE, tagName);
		}

		private static Tags.IOptionalNamedTag<Block> bindModded(String tagName)
		{
			return bind(Armageddon.ID_MOD, tagName);
		}

		private static Tags.IOptionalNamedTag<Block> bindVanilla(String tagName)
		{
			return bind(Armageddon.ID_MINECRAFT, tagName);
		}
		// endregion

		private static void register() { }
	}

	public static final class Items
	{
		// region: Helpers
		private static Tags.IOptionalNamedTag<Item> bind(String modId, String tagName)
		{
			return net.minecraft.tags.ItemTags.createOptional(new ResourceLocation(modId, tagName));
		}

		private static Tags.IOptionalNamedTag<Item> bindForge(String tagName)
		{
			return bind(Armageddon.ID_FORGE, tagName);
		}

		private static Tags.IOptionalNamedTag<Item> bindModded(String tagName)
		{
			return bind(Armageddon.ID_MOD, tagName);
		}

		private static Tags.IOptionalNamedTag<Item> bindVanilla(String tagName)
		{
			return bind(Armageddon.ID_MINECRAFT, tagName);
		}
		// endregion

		private static void register() { }
	}

	public static final class EntityTypes
	{
		public static final Tags.IOptionalNamedTag<EntityType<?>> ZOMBIES = bindForge(Names.ZOMBIES);
		public static final Tags.IOptionalNamedTag<EntityType<?>> ZOMBIES_MOB = bindForge(Names.ZOMBIES_MOB);
		public static final Tags.IOptionalNamedTag<EntityType<?>> ZOMBIES_SPECIAL = bindForge(Names.ZOMBIES_SPECIAL);

		// region: Helpers
		private static Tags.IOptionalNamedTag<EntityType<?>> bind(String modId, String tagName)
		{
			return net.minecraft.tags.EntityTypeTags.createOptional(new ResourceLocation(modId, tagName));
		}

		private static Tags.IOptionalNamedTag<EntityType<?>> bindForge(String tagName)
		{
			return bind(Armageddon.ID_FORGE, tagName);
		}

		private static Tags.IOptionalNamedTag<EntityType<?>> bindModded(String tagName)
		{
			return bind(Armageddon.ID_MOD, tagName);
		}

		private static Tags.IOptionalNamedTag<EntityType<?>> bindVanilla(String tagName)
		{
			return bind(Armageddon.ID_MINECRAFT, tagName);
		}
		// endregion

		private static void register() { }
	}
}
