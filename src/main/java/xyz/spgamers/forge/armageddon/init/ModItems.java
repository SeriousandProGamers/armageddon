package xyz.spgamers.forge.armageddon.init;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ZombiePigEntity;
import xyz.spgamers.forge.armageddon.item.SpawnEggItem;
import xyz.spgamers.forge.armageddon.item.group.ModItemGroup;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MOD_ID);

	public static final ModItemGroup ITEM_GROUP = new ModItemGroup();

	public static final RegistryObject<SpawnEggItem<ZombiePigEntity>> ZOMBIE_PIG_SPAWN_EGG = registerSpawnEgg(
			ModEntities.ZOMBIE_PIG,
			44975, 14377823,
			Armageddon.SERVER_CONFIG.entities::isZombiePigEnabled,
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
		return ITEMS.register(
				// <entity_type>_spawn_egg
				String.format("%s_%s", entityTypeObj.getId().getPath(), ModConstants.Items.SPAWN_EGG_SUFFIX),
				() -> new SpawnEggItem<>(entityTypeObj, primaryColor, secondaryColor, isEntityEnabledSupplier, propertiesSupplier.get())
		);
	}

	private static Item.Properties defaultItemProperties()
	{
		return new Item.Properties().group(ITEM_GROUP);
	}
}
