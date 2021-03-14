package xyz.spgamers.forge.armageddon.util;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModConstants
{
	public static final String MOD_ID = "armageddon";
	public static final Logger LOGGER = LogManager.getLogger("Armageddon");

	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(MOD_ID, "main"),
			() -> "1.0",
			s -> true,
			s -> true
	);

	private ModConstants()
	{
		throw new IllegalStateException();
	}

	public static final class Items
	{
		public static final String ROTTEN_PORKCHOP = "rotten_porkchop";
		public static final String ROTTEN_BEEF = "rotten_beef";
		public static final String ROTTEN_FISH = "rotten_fish";
		public static final String ROTTEN_RABBIT = "rotten_rabbit";
		public static final String ROTTEN_CHICKEN = "rotten_chicken";
		public static final String ROTTEN_EGG = "rotten_egg";
		public static final String ROTTEN_MUTTON = "rotten_mutton";
		public static final String SPOILED_MILK_BUCKET = "spoiled_milk_bucket";
		public static final String SPAWN_EGG_SUFFIX = "spawn_egg";

		private Items()
		{
			throw new IllegalStateException();
		}

		public static final class ItemTags
		{
			private ItemTags()
			{
				throw new IllegalStateException();
			}

			private static Tags.IOptionalNamedTag<Item> forge(String path)
			{
				return tag("forge", path);
			}

			private static Tags.IOptionalNamedTag<Item> mod(String path)
			{
				return tag(MOD_ID, path);
			}

			private static Tags.IOptionalNamedTag<Item> tag(String domain, String path)
			{
				return net.minecraft.tags.ItemTags.createOptional(new ResourceLocation(domain, path));
			}
		}
	}

	public static final class Potions
	{
		// public static final String ZOMBIE_EVASION = "zombie_evasion";

		private Potions()
		{
			throw new IllegalStateException();
		}
	}

	public static final class Effects
	{
		// public static final String ZOMBIE_EVASION = Potions.ZOMBIE_EVASION";

		private Effects()
		{
			throw new IllegalStateException();
		}
	}

	public static final class Entities
	{
		public static final String PIG_ZOMBIE = "pig_zombie";
		public static final String COW_ZOMBIE = "cow_zombie";
		public static final String PANDA_ZOMBIE = "panda_zombie";
		public static final String POLAR_BEAR_ZOMBIE = "polar_bear_zombie";
		public static final String FOX_ZOMBIE = "fox_zombie";
		public static final String WOLF_ZOMBIE = "wolf_zombie";
		public static final String RABBIT_ZOMBIE = "rabbit_zombie";
		public static final String CHICKEN_ZOMBIE = "chicken_zombie";
		public static final String SHEEP_ZOMBIE = "sheep_zombie";
		public static final String FIRE_ZOMBIE = "fire_zombie";
		public static final String EXPLOSIVE_ZOMBIE = "explosive_zombie";
		public static final String TELEPORTING_ZOMBIE = "teleporting_zombie";

		private Entities()
		{
			throw new IllegalStateException();
		}

		public static final class EntityTypeTags
		{
			public static final Tags.IOptionalNamedTag<EntityType<?>> FORGE_ZOMBIES = forge("zombies");
			public static final Tags.IOptionalNamedTag<EntityType<?>> FORGE_ZOMBIE = forge("zombies/zombie");

			public static final Tags.IOptionalNamedTag<EntityType<?>> MOD_ZOMBIES = mod("zombies");
			// public static final Tags.IOptionalNamedTag<EntityType<?>> MOD_SPECIAL_ZOMBIES = mod("zombies/special");
			public static final Tags.IOptionalNamedTag<EntityType<?>> MOD_PASSIVE_ZOMBIES = mod("zombies/passive");

			private EntityTypeTags()
			{
				throw new IllegalStateException();
			}

			private static Tags.IOptionalNamedTag<EntityType<?>> forge(String path)
			{
				return tag("forge", path);
			}

			private static Tags.IOptionalNamedTag<EntityType<?>> mod(String path)
			{
				return tag(MOD_ID, path);
			}

			private static Tags.IOptionalNamedTag<EntityType<?>> tag(String domain, String path)
			{
				return net.minecraft.tags.EntityTypeTags.createOptional(new ResourceLocation(domain, path));
			}
		}
	}

	public static final class NBT
	{
		public static final String SHEARED = "Sheared";
		public static final String COLOR = "Color";
		public static final String IS_CHICKEN_JOCKEY = "IsChickenJockey";
		public static final String EGG_LAY_TIME = "EggLayTime";

		private NBT()
		{
			throw new IllegalStateException();
		}
	}
}
