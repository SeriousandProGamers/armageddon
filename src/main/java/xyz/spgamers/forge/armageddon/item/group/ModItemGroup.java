package xyz.spgamers.forge.armageddon.item.group;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import xyz.spgamers.forge.armageddon.init.ModItems;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ModItemGroup extends ItemGroup
{
	public ModItemGroup()
	{
		super(ModConstants.MOD_ID);
	}

	@Override
	public ItemStack createIcon()
	{
		// TODO: Make item group icon, cycle through all mod items
		return ModItems.PIG_ZOMBIE_SPAWN_EGG.get().getDefaultInstance();
	}
}
