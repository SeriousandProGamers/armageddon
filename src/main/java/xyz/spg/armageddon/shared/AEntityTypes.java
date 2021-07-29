package xyz.spg.armageddon.shared;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;
import xyz.spg.armageddon.core.entity.AbstractZombie;

@ObjectHolder(Armageddon.ID_MOD)
public final class AEntityTypes
{
	// @ObjectHolder(Names.PANDA_ZOMBIE) public static final EntityType<? extends AbstractZombie> PANDA_ZOMBIE = null;
	// @ObjectHolder(Names.POLAR_BEAR_ZOMBIE) public static final EntityType<? extends AbstractZombie> POLAR_BEAR_ZOMBIE = null;
	// @ObjectHolder(Names.FOX_ZOMBIE) public static final EntityType<? extends AbstractZombie> FOX_ZOMBIE = null;
	// @ObjectHolder(Names.WOLF_ZOMBIE) public static final EntityType<? extends AbstractZombie> WOLF_ZOMBIE = null;
	// @ObjectHolder(Names.RABBIT_ZOMBIE) public static final EntityType<? extends AbstractZombie> RABBIT_ZOMBIE = null;
	// @ObjectHolder(Names.CHICKEN_ZOMBIE) public static final EntityType<? extends AbstractZombie> CHICKEN_ZOMBIE = null;
	// @ObjectHolder(Names.SHEEP_ZOMBIE) public static final EntityType<? extends AbstractZombie> SHEEP_ZOMBIE = null;
	// @ObjectHolder(Names.COW_ZOMBIE) public static final EntityType<? extends AbstractZombie> COW_ZOMBIE = null;
	@ObjectHolder(Names.PIG_ZOMBIE) public static final EntityType<? extends AbstractZombie> PIG_ZOMBIE = null;

	// @ObjectHolder(Names.BLAZE_ZOMBIE) public static final EntityType<? extends AbstractZombie> BLAZE_ZOMBIE = null;
	// @ObjectHolder(Names.EXPLOSIVE_ZOMBIE) public static final EntityType<? extends AbstractZombie> EXPLOSIVE_ZOMBIE = null;
	// @ObjectHolder(Names.TELEPORTING_ZOMBIE) public static final EntityType<? extends AbstractZombie> TELEPORTING_ZOMBIE = null;

	static void register()
	{
		Names.register();
	}

	public static final class Names
	{
		// public static final String PANDA_ZOMBIE = "panda_zombie";
		// public static final String POLAR_BEAR_ZOMBIE = "polar_bear_zombie";
		// public static final String FOX_ZOMBIE = "fox_zombie";
		// public static final String WOLF_ZOMBIE = "wolf_zombie";
		// public static final String RABBIT_ZOMBIE = "rabbit_zombie";
		// public static final String CHICKEN_ZOMBIE = "chicken_zombie";
		// public static final String SHEEP_ZOMBIE = "sheep_zombie";
		// public static final String COW_ZOMBIE = "cow_zombie";
		public static final String PIG_ZOMBIE = "pig_zombie";

		// public static final String BLAZE_ZOMBIE = "blaze_zombie";
		// public static final String EXPLOSIVE_ZOMBIE = "explosive_zombie";
		// public static final String TELEPORTING_ZOMBIE = "teleporting_zombie";

		private static void register() { }
	}
}
