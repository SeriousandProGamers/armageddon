package xyz.spg.armageddon.shared;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Armageddon.ID_MOD)
public final class AEnchantments
{
	@ObjectHolder(Names.POISON) public static final Enchantment POISON = null;

	static void register()
	{
		Names.register();
	}

	public static final class Names
	{
		public static final String POISON = "poison";

		private static void register() { }
	}
}
