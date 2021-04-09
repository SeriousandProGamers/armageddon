package xyz.spgamers.forge.armageddon.init;

import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.apex.forge.apexcore.lib.util.Registrar;
import xyz.spgamers.forge.armageddon.item.RottenRabbitFootItem;
import xyz.spgamers.forge.armageddon.item.SpoiledMilkBucketItem;
import xyz.spgamers.forge.armageddon.item.group.ModItemGroup;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MOD_ID);

	public static final ModItemGroup ITEM_GROUP = new ModItemGroup();

	public static final RegistryObject<Item> ROTTEN_PORKCHOP = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.ROTTEN_PORKCHOP,
			properties -> properties.food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_BEEF = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.ROTTEN_BEEF,
			properties -> properties.food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_FISH = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.ROTTEN_FISH,
			properties -> properties.food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_RABBIT = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.ROTTEN_RABBIT,
			properties -> properties.food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_RABBIT_FOOT = Registrar.registerItemBuilder(
			ITEMS,
			ModConstants.Items.ROTTEN_RABBIT_FOOT,
			RottenRabbitFootItem::new
	);

	public static final RegistryObject<Item> ROTTEN_CHICKEN = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.ROTTEN_CHICKEN,
			properties -> properties.food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<Item> ROTTEN_EGG = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.ROTTEN_EGG
	);

	public static final RegistryObject<Item> ROTTEN_MUTTON = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.ROTTEN_MUTTON,
			properties -> properties.food(Foods.ROTTEN_FLESH)
	);

	public static final RegistryObject<SpoiledMilkBucketItem> SPOILED_MILK_BUCKET = Registrar.registerItem(
			ITEMS,
			ModConstants.Items.SPOILED_MILK_BUCKET,
			SpoiledMilkBucketItem::new,
			Registrar::bucketItemPropertiesModifier
	);

	public static final RegistryObject<SpawnEggItem> PIG_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.PIG_ZOMBIE,
			44975, 14377823
	);

	public static final RegistryObject<SpawnEggItem> COW_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.COW_ZOMBIE,
			44975, 10592673
	);

	public static final RegistryObject<SpawnEggItem> CHICKEN_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.CHICKEN_ZOMBIE,
			44975, 16711680
	);

	public static final RegistryObject<SpawnEggItem> SHEEP_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.SHEEP_ZOMBIE,
			44975, 16758197
	);

	public static final RegistryObject<SpawnEggItem> FOX_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.FOX_ZOMBIE,
			44975, 13396256
	);

	public static final RegistryObject<SpawnEggItem> PANDA_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.PANDA_ZOMBIE,
			44975, 1776418
	);

	public static final RegistryObject<SpawnEggItem> POLAR_BEAR_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.POLAR_BEAR_ZOMBIE,
			44975, 9803152
	);

	public static final RegistryObject<SpawnEggItem> RABBIT_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.RABBIT_ZOMBIE,
			44975, 7555121
	);

	public static final RegistryObject<SpawnEggItem> WOLF_ZOMBIE_SPAWN_EGG = Registrar.registerSpawnEggItem(
			ITEMS,
			ModEntities.WOLF_ZOMBIE,
			44975, 13545366
	);

	private ModItems()
	{
		throw new IllegalStateException();
	}

	public static void commonSetup()
	{
		ITEM_GROUP.setItems(ITEMS);
	}
}
