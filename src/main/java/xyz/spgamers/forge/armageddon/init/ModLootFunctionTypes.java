package xyz.spgamers.forge.armageddon.init;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import xyz.spgamers.forge.armageddon.data.loot.EnchantSpecificWithLevels;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ModLootFunctionTypes
{
	public static final LootFunctionType ENCHANT_SPECIFIC_WITH_LEVELS = register(
			"enchant_specific_with_levels",
			new EnchantSpecificWithLevels.Serializer()
	);

	private ModLootFunctionTypes()
	{
		throw new IllegalStateException();
	}

	private static LootFunctionType register(String lootFunctionTypeName, ILootSerializer<? extends LootFunction> lootSerializer)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(ModConstants.MOD_ID, lootFunctionTypeName), new LootFunctionType(lootSerializer));
	}

	// does nothing solely here so that the static fields
	// get initialized at correct time
	public static void register()
	{
	}
}
