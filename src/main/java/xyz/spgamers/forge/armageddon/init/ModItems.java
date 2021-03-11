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

	public static final RegistryObject<BetterSpawnEggItem> ZOMBIE_PIG_SPAWN_EGG = ITEMS.register(
			Constants.Items.ZOMBIE_PIG_SPAWN_EGG,
			() -> new BetterSpawnEggItem(ModEntities.ZOMBIE_PIG::get, 44975, 14377823, itemProperties().group(ItemGroup.MISC))
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
