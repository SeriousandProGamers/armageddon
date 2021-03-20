package xyz.spgamers.forge.armageddon.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.item.DeferredSpawnEggItem;
import xyz.spgamers.forge.armageddon.item.SpoiledMilkBucketItem;
import xyz.spgamers.forge.armageddon.item.group.ModItemGroup;
import xyz.spgamers.forge.armageddon.util.ModConstants;

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

	public static final RegistryObject<SpoiledMilkBucketItem> SPOILED_MILK_BUCKET = registerItem(
			ModConstants.Items.SPOILED_MILK_BUCKET,
			SpoiledMilkBucketItem::new,
			() -> defaultItemProperties().containerItem(Items.BUCKET).maxStackSize(1)
	);

	public static final RegistryObject<DeferredSpawnEggItem> PIG_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.PIG_ZOMBIE,
			44975, 14377823,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<DeferredSpawnEggItem> COW_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.COW_ZOMBIE,
			44975, 10592673,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<DeferredSpawnEggItem> CHICKEN_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.CHICKEN_ZOMBIE,
			44975, 16711680,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<DeferredSpawnEggItem> SHEEP_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.SHEEP_ZOMBIE,
			44975, 16758197,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<DeferredSpawnEggItem> FOX_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.FOX_ZOMBIE,
			44975, 13396256,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<DeferredSpawnEggItem> PANDA_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.PANDA_ZOMBIE,
			44975, 1776418,
			ModItems::defaultItemProperties
	);

	/*public static final RegistryObject<DeferredSpawnEggItem> POLAR_BEAR_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.POLAR_BEAR_ZOMBIE,
			44975, 9803152,
			ModItems::defaultItemProperties
	);*/

	/*public static final RegistryObject<DeferredSpawnEggItem> RABBIT_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.RABBIT_ZOMBIE,
			44975, 7555121,
			ModItems::defaultItemProperties
	);*/

	/*public static final RegistryObject<DeferredSpawnEggItem> WOLF_ZOMBIE_SPAWN_EGG = registerSpawnEgg(
			ModEntities.WOLF_ZOMBIE,
			44975, 13545366,
			ModItems::defaultItemProperties
	);*/

	private ModItems()
	{
		throw new IllegalStateException();
	}

	public static void commonSetup()
	{
		ITEM_GROUP.setItems(ITEMS);
		DeferredSpawnEggItem.initUnaddedEggs();
	}

	private static <E extends Entity> RegistryObject<DeferredSpawnEggItem> registerSpawnEgg(RegistryObject<EntityType<E>> entityTypeObj, int primaryColor, int secondaryColor, Supplier<Item.Properties> propertiesSupplier)
	{
		return registerItem(
				// <entity_type>_spawn_egg
				String.format("%s_%s", entityTypeObj.getId().getPath(), ModConstants.Items.SPAWN_EGG_SUFFIX),
				properties -> new DeferredSpawnEggItem(entityTypeObj, primaryColor, secondaryColor, properties),
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
