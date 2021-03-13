package xyz.spgamers.forge.armageddon.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import java.util.function.Supplier;

public final class ModBrewingRecipe implements IBrewingRecipe
{
	private final Supplier<Potion> inputSupplier;
	private final IItemProvider ingredient;
	private final Supplier<Potion> outputSupplier;

	public ModBrewingRecipe(Supplier<Potion> inputSupplier, IItemProvider ingredient, Supplier<Potion> outputSupplier)
	{
		this.inputSupplier = inputSupplier;
		this.ingredient = ingredient;
		this.outputSupplier = outputSupplier;
	}

	@Override
	public boolean isInput(ItemStack input)
	{
		if(input.isEmpty())
			return false;
		return PotionUtils.getPotionFromItem(input) == this.inputSupplier.get();
	}

	@Override
	public boolean isIngredient(ItemStack ingredient)
	{
		if(ingredient.isEmpty())
			return false;
		return ingredient.getItem() == this.ingredient.asItem();
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	{
		if(!isInput(input) || !isIngredient(ingredient))
			return ItemStack.EMPTY;

		return PotionUtils.addPotionToItemStack(input.copy(), outputSupplier.get());
	}
}
