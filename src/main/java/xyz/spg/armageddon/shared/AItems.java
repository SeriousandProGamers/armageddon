package xyz.spg.armageddon.shared;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Armageddon.ID_MOD)
public final class AItems
{
	// @ObjectHolder(Names.PANDA_ZOMBIE_SPAWN_EGG) public static final Item PANDA_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.POLAR_BEAR_ZOMBIE_SPAWN_EGG) public static final Item POLAR_BEAR_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.FOX_ZOMBIE_SPAWN_EGG) public static final Item FOX_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.WOLF_ZOMBIE_SPAWN_EGG) public static final Item WOLF_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.RABBIT_ZOMBIE_SPAWN_EGG) public static final Item RABBIT_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.CHICKEN_ZOMBIE_SPAWN_EGG) public static final Item CHICKEN_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.SHEEP_ZOMBIE_SPAWN_EGG) public static final Item SHEEP_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.COW_ZOMBIE_SPAWN_EGG) public static final Item COW_ZOMBIE_SPAWN_EGG = null;
	@ObjectHolder(Names.PIG_ZOMBIE_SPAWN_EGG) public static final Item PIG_ZOMBIE_SPAWN_EGG = null;

	// @ObjectHolder(Names.BLAZE_ZOMBIE_SPAWN_EGG) public static final Item BLAZE_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.EXPLOSIVE_ZOMBIE_SPAWN_EGG) public static final Item EXPLOSIVE_ZOMBIE_SPAWN_EGG = null;
	// @ObjectHolder(Names.TELEPORTING_ZOMBIE_SPAWN_EGG) public static final Item TELEPORTING_ZOMBIE_SPAWN_EGG = null;

	static void register()
	{
		Names.register();
	}

	public static final class Names
	{
		// public static final String PANDA_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.PANDA_ZOMBIE + "_spawn_egg";
		// public static final String POLAR_BEAR_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.POLAR_BEAR_ZOMBIE + "_spawn_egg";
		// public static final String FOX_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.FOX_ZOMBIE + "_spawn_egg";
		// public static final String WOLF_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.WOLF_ZOMBIE + "_spawn_egg";
		// public static final String RABBIT_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.RABBIT_ZOMBIE + "_spawn_egg";
		// public static final String CHICKEN_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.CHICKEN_ZOMBIE + "_spawn_egg";
		// public static final String SHEEP_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.SHEEP_ZOMBIE + "_spawn_egg";
		// public static final String COW_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.COW_ZOMBIE + "_spawn_egg";
		public static final String PIG_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.PIG_ZOMBIE + "_spawn_egg";

		// public static final String BLAZE_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.BLAZE_ZOMBIE + "_spawn_egg";
		// public static final String EXPLOSIVE_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.EXPLOSIVE_ZOMBIE + "_spawn_egg";
		// public static final String TELEPORTING_ZOMBIE_SPAWN_EGG = AEntityTypes.Names.TELEPORTING_ZOMBIE + "_spawn_egg";

		private static void register() { }
	}
}
