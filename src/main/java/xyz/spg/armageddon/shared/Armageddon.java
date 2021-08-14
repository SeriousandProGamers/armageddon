package xyz.spg.armageddon.shared;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.versions.forge.ForgeVersion;
import xyz.spg.armageddon.core.helper.ZombieHelper;

public final class Armageddon
{
	public static final String ID_MOD = "armageddon";
	public static final String ID_FORGE = ForgeVersion.MOD_ID;
	public static final String ID_MINECRAFT = "minecraft";

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID_MOD);
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ID_MOD);
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ID_MOD);
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ID_MOD);
	public static final DeferredRegister<Potion> POTION_TYPES = DeferredRegister.create(ForgeRegistries.POTIONS, ID_MOD);

	/**
	 * Internal use only!! <br>
	 * <b><i>DO NOT CALL MANUALLY!!!</i></b>
	 */
	public static void register(IEventBus bus)
	{
		ITEMS.register(bus);
		ENTITIES.register(bus);
		ENCHANTMENTS.register(bus);
		MOB_EFFECTS.register(bus);
		POTION_TYPES.register(bus);

		ANames.register();
		ALootTables.register();
		AFoods.register();
		AItems.register();
		AEntityTypes.register();
		AEnchantments.register();
		AMobEffects.register();
		APotions.register();

		ATags.register();
	}

	/**
	 * Internal use only!! <br>
	 * <b><i>DO NOT CALL MANUALLY!!!</i></b>
	 */
	public static void postRegister()
	{
		ZombieHelper.registerZombieVariant(EntityType.PIG, AEntityTypes.PIG_ZOMBIE);
		ZombieHelper.registerZombieVariant(EntityType.COW, AEntityTypes.COW_ZOMBIE);
		ZombieHelper.registerZombieVariant(EntityType.SHEEP, AEntityTypes.SHEEP_ZOMBIE);
	}
}
