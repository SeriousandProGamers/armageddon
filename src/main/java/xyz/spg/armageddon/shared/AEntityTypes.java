package xyz.spg.armageddon.shared;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraftforge.registries.ObjectHolder;
import xyz.spg.armageddon.core.entity.CowZombie;
import xyz.spg.armageddon.core.entity.PigZombie;
import xyz.spg.armageddon.core.entity.SheepZombie;

@ObjectHolder(Armageddon.ID_MOD)
public final class AEntityTypes
{
	@ObjectHolder(ANames.ROTTEN_EGG) public static final EntityType<? extends ThrownEgg> ROTTEN_EGG = null;

	// @ObjectHolder(ANames.PANDA_ZOMBIE) public static final EntityType<? extends AbstractZombie> PANDA_ZOMBIE = null;
	// @ObjectHolder(ANames.POLAR_BEAR_ZOMBIE) public static final EntityType<? extends AbstractZombie> POLAR_BEAR_ZOMBIE = null;
	// @ObjectHolder(ANames.FOX_ZOMBIE) public static final EntityType<? extends AbstractZombie> FOX_ZOMBIE = null;
	// @ObjectHolder(ANames.WOLF_ZOMBIE) public static final EntityType<? extends AbstractZombie> WOLF_ZOMBIE = null;
	// @ObjectHolder(ANames.RABBIT_ZOMBIE) public static final EntityType<? extends AbstractZombie> RABBIT_ZOMBIE = null;
	// @ObjectHolder(ANames.CHICKEN_ZOMBIE) public static final EntityType<? extends AbstractZombie> CHICKEN_ZOMBIE = null;
	@ObjectHolder(ANames.SHEEP_ZOMBIE) public static final EntityType<? extends SheepZombie> SHEEP_ZOMBIE = null;
	@ObjectHolder(ANames.COW_ZOMBIE) public static final EntityType<? extends CowZombie> COW_ZOMBIE = null;
	@ObjectHolder(ANames.PIG_ZOMBIE) public static final EntityType<? extends PigZombie> PIG_ZOMBIE = null;

	// @ObjectHolder(ANames.BLAZE_ZOMBIE) public static final EntityType<? extends AbstractZombie> BLAZE_ZOMBIE = null;
	// @ObjectHolder(ANames.EXPLOSIVE_ZOMBIE) public static final EntityType<? extends AbstractZombie> EXPLOSIVE_ZOMBIE = null;
	// @ObjectHolder(ANames.TELEPORTING_ZOMBIE) public static final EntityType<? extends AbstractZombie> TELEPORTING_ZOMBIE = null;

	static void register() { }
}
