package xyz.spgamers.forge.armageddon.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.enchantment.PoisonEnchantment;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.function.Supplier;

public final class ModEnchantments
{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ModConstants.MOD_ID);

	public static final RegistryObject<PoisonEnchantment> POISON_ENCHANTMENT = register(
			ModConstants.Enchantments.POISON,
			PoisonEnchantment::new
	);

	private ModEnchantments()
	{
		throw new IllegalStateException();
	}

	public static <E extends Enchantment> RegistryObject<E> register(String enchantmentName, Supplier<E> enchantmentSupplier)
	{
		return ENCHANTMENTS.register(enchantmentName, enchantmentSupplier);
	}
}
