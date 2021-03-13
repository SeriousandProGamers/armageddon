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
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ChickenZombieEntity;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.CowZombieEntity;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.PigZombieEntity;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.SheepZombieEntity;
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

	public static final RegistryObject<Item> ROTTEN_FISH = registerItem(
			ModConstants.Items.ROTTEN_FISH,
			Item::new,
			() -> defaultItemProperties().food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_RABBIT = registerItem(
			ModConstants.Items.ROTTEN_RABBIT,
			Item::new,
			() -> defaultItemProperties().food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_CHICKEN = registerItem(
			ModConstants.Items.ROTTEN_CHICKEN,
			Item::new,
			() -> defaultItemProperties().food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_EGG = registerItem(
			ModConstants.Items.ROTTEN_EGG,
			Item::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<Item> ROTTEN_MUTTON = registerItem(
			ModConstants.Items.ROTTEN_MUTTON,
			Item::new,
			() -> defaultItemProperties().food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<SpawnEggItem<PigZombieEntity>> PIG_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.PIG_ZOMBIE,
			44975, 14377823,
			Armageddon.SERVER_CONFIG.animals::isPigZombieEnabled,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SpawnEggItem<CowZombieEntity>> COW_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.COW_ZOMBIE,
			44975, 10592673,
			Armageddon.SERVER_CONFIG.animals::isCowZombieEnabled,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SpawnEggItem<ChickenZombieEntity>> CHICKEN_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.CHICKEN_ZOMBIE,
			44975, 16711680,
			Armageddon.SERVER_CONFIG.animals::isChickenZombieEnabled,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SpawnEggItem<SheepZombieEntity>> SHEEP_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.SHEEP_ZOMBIE,
			44975, 16758197,
			Armageddon.SERVER_CONFIG.animals::isSheepZombieEnabled,
			ModItems::defaultItemProperties
	);

	private ModItems()
	{
		throw new IllegalStateException();
	}

	public static void commonSetup()
	{
		ITEM_GROUP.setItems(ITEMS);

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
