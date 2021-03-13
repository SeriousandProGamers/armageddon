package xyz.spgamers.forge.armageddon.item.group;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.List;

public final class ModItemGroup extends ItemGroup
{
	private int cycleTime = 0;
	private int itemIndex = 0;

	private final List<Item> items = Lists.newArrayList();

	public ModItemGroup()
	{
		super(ModConstants.MOD_ID);
	}

	public void setItems(DeferredRegister<Item> register)
	{
		itemIndex = 0;
		cycleTime = 0;
		items.clear();

		register.getEntries()
		        .stream()
		        .filter(RegistryObject::isPresent)
		        .map(RegistryObject::get)
		        .forEach(items::add);

		// set initial icon if its empty
		super.getIcon();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack createIcon()
	{
		return items.get(itemIndex).getDefaultInstance();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack getIcon()
	{
		cycleTime++;

		if(cycleTime >= 75)
		{
			cycleTime = 0;

			itemIndex = (itemIndex + 1) % (items.size() - 1);

			/*if(itemIndex + 1 > items.size() - 1)
				itemIndex = 0;
			else
				itemIndex++;*/

			// reuse the icon field in ItemGroup, use reflection since its private
			ObfuscationReflectionHelper.setPrivateValue(ItemGroup.class, this, createIcon(), "icon");
		}

		return ObfuscationReflectionHelper.getPrivateValue(ItemGroup.class, this, "icon");
	}
}
