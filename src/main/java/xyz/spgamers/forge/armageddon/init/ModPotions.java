package xyz.spgamers.forge.armageddon.init;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class ModPotions
{
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, ModConstants.MOD_ID);

	/*public static final RegistryObject<Potion> ZOMBIE_EVASION = register(
			ModConstants.Effects.ZOMBIE_EVASION,
			Potion::new,
			() -> new EffectInstance(ModEffects.ZOMBIE_EVASION.get(), 3600)
	);*/

	private ModPotions()
	{
		throw new IllegalStateException();
	}

	public static void commonSetup()
	{
		/*BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(
				() -> Potions.AWKWARD,
				ModItems.ROTTEN_EGG::get,
				ModPotions.ZOMBIE_EVASION
		));*/
	}

	private static <P extends Potion> RegistryObject<P> register(String potionName, BiFunction<String, EffectInstance, P> potionBuilder, Supplier<EffectInstance> effectInstanceSupplier)
	{
		return POTIONS.register(
				potionName,
				() -> potionBuilder.apply(String.format("%s.%s", ModConstants.MOD_ID, potionName), effectInstanceSupplier.get())
		);
	}
}
