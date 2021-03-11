package xyz.spgamers.forge.armageddon.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.item.BetterSpawnEggItem;
import xyz.spgamers.forge.armageddon.util.Constants;

public final class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

	// spawn egg colors | <zombie primary> <other entity secondary>
	// 44975 - zombie primary color
	// 14377823 - pig secondary color
	// 0 - creeper secondary color
	// 10592673 - cow secondary color
	// 12040119 - mooshroom secondary color

	public static final RegistryObject<BetterSpawnEggItem> ZOMBIE_PIG_SPAWN_EGG = ITEMS.register(
			Constants.Items.ZOMBIE_PIG_SPAWN_EGG,
			() -> new BetterSpawnEggItem(ModEntities.ZOMBIE_PIG::get, 44975, 14377823, itemProperties().group(ItemGroup.MISC))
	);

	public static final RegistryObject<BetterSpawnEggItem> ZOMBIE_CREEPER_SPAWN_EGG = ITEMS.register(
			Constants.Items.ZOMBIE_CREEPER_SPAWN_EGG,
			() -> new BetterSpawnEggItem(ModEntities.ZOMBIE_CREEPER::get, 44975, 0, itemProperties().group(ItemGroup.MISC))
	);

	public static final RegistryObject<BetterSpawnEggItem> ZOMBIE_COW_SPAWN_EGG = ITEMS.register(
			Constants.Items.ZOMBIE_COW_SPAWN_EGG,
			() -> new BetterSpawnEggItem(ModEntities.ZOMBIE_COW::get, 44975, 10592673, itemProperties().group(ItemGroup.MISC))
	);

	public static final RegistryObject<BetterSpawnEggItem> ZOMBIE_RED_MOOSHROOM_SPAWN_EGG = ITEMS.register(
			Constants.Items.ZOMBIE_RED_MOOSHROOM_SPAWN_EGG,
			() -> new BetterSpawnEggItem(ModEntities.ZOMBIE_RED_MOOSHROOM::get, 44975, 12040119, itemProperties().group(ItemGroup.MISC))
	);

	private ModItems()
	{
		throw new IllegalStateException();
	}

	public static Item.Properties itemProperties()
	{
		return new Item.Properties();
	}
}
