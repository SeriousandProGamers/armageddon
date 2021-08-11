package xyz.spg.armageddon.shared;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.versions.forge.ForgeVersion;
import xyz.spg.armageddon.core.entity.AbstractZombie;

import javax.annotation.Nullable;

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

	private static final BiMap<EntityType<?>, EntityType<? extends AbstractZombie>> zombieTypeMap = HashBiMap.create();

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
		zombieTypeMap.put(EntityType.PIG, AEntityTypes.PIG_ZOMBIE);
		zombieTypeMap.put(EntityType.COW, AEntityTypes.COW_ZOMBIE);
	}

	// region: Helpers
	@Nullable
	public static EntityType<? extends AbstractZombie> getZombieType(Entity entity)
	{
		return zombieTypeMap.get(entity.getType());
	}

	@Nullable
	public static EntityType<?> getLivingType(AbstractZombie zombie)
	{
		return zombieTypeMap.inverse().get(zombie.getType());
	}
	// endregion
}
