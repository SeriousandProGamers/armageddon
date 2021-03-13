package xyz.spgamers.forge.armageddon.init;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ZombieCowEntity;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ZombiePigEntity;
import xyz.spgamers.forge.armageddon.item.SpawnEggItem;
import xyz.spgamers.forge.armageddon.item.group.ModItemGroup;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MOD_ID);

	public static final ModItemGroup ITEM_GROUP = new ModItemGroup();

	public static final RegistryObject<Item> ROTTEN_PORKCHOP = registerItem(
			ModConstants.Items.ROTTEN_PORKCHOP,
			Item::new,
			() -> defaultItemProperties().food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_BEEF = registerItem(
			ModConstants.Items.ROTTEN_BEEF,
			Item::new,
			() -> defaultItemProperties().food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<SpawnEggItem<ZombiePigEntity>> ZOMBIE_PIG_SPAWN_EGG = registerSpawnEgg(
			ModEntities.ZOMBIE_PIG,
			44975, 14377823,
			Armageddon.SERVER_CONFIG.entities::isZombiePigEnabled,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SpawnEggItem<ZombieCowEntity>> ZOMBIE_COW_SPAWN_EGG = registerSpawnEgg(
			ModEntities.ZOMBIE_COW,
			44975, 10592673,
			Armageddon.SERVER_CONFIG.entities::isZombieCowEnabled,
			ModItems::defaultItemProperties
	);

	private ModItems()
	{
		throw new IllegalStateException();
	}

	public static void commonSetup()
	{
		ITEMS.getEntries().forEach(obj -> obj.ifPresent(item -> {
			if(item instanceof SpawnEggItem)
				DispenserBlock.registerDispenseBehavior(item, new SpawnEggItem.SpawnEggDispenserBehavior());
		}));
	}

	private static <E extends Entity> RegistryObject<SpawnEggItem<E>> registerSpawnEgg(RegistryObject<EntityType<E>> entityTypeObj, int primaryColor, int secondaryColor, BooleanSupplier isEntityEnabledSupplier, Supplier<Item.Properties> propertiesSupplier)
	{
		return registerItem(
				// <entity_type>_spawn_egg
				String.format("%s_%s", entityTypeObj.getId().getPath(), ModConstants.Items.SPAWN_EGG_SUFFIX),
				properties -> new SpawnEggItem<>(entityTypeObj, primaryColor, secondaryColor, isEntityEnabledSupplier, properties),
				propertiesSupplier
		);
	}

	private static <I extends Item> RegistryObject<I> registerItem(String itemName, Function<Item.Properties, I> itemBuilder, Supplier<Item.Properties> propertiesSupplier)
	{
		return ITEMS.register(
				itemName,
				() -> itemBuilder.apply(propertiesSupplier.get())
		);
	}

	private static Item.Properties defaultItemProperties()
	{
		return new Item.Properties().group(ITEM_GROUP);
	}
}
