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
		Blocks.register();
		Items.register();
		EntityTypes.register();
	}

	public static final class Blocks
	{
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS = bindForge(ANames.PUMPKINS);
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS_PLAIN = bindForge(ANames.PUMPKINS_PLAIN);
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS_CARVED = bindForge(ANames.PUMPKINS_CARVED);
		public static final Tags.IOptionalNamedTag<Block> PUMPKINS_JACK_O_LANTERN = bindForge(ANames.PUMPKINS_JACK_O_LANTERN);

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
		public static final Tags.IOptionalNamedTag<Item> ROTTEN = bindForge(ANames.ROTTEN);

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
		public static final Tags.IOptionalNamedTag<EntityType<?>> ZOMBIES = bindForge(ANames.ZOMBIES);
		public static final Tags.IOptionalNamedTag<EntityType<?>> ZOMBIES_HUMANOID = bindForge(ANames.ZOMBIES_HUMANOID);
		public static final Tags.IOptionalNamedTag<EntityType<?>> ZOMBIES_PASSIVE = bindForge(ANames.ZOMBIES_PASSIVE);
		public static final Tags.IOptionalNamedTag<EntityType<?>> ZOMBIES_HOSTILE = bindForge(ANames.ZOMBIES_HOSTILE);

		public static final Tags.IOptionalNamedTag<EntityType<?>> EGGS = bindForge(ANames.EGGS);

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
