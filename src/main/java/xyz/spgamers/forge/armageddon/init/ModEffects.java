package xyz.spgamers.forge.armageddon.init;

import net.minecraft.potion.Effect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ModEffects
{
	public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, ModConstants.MOD_ID);

	/*public static final RegistryObject<ModEffect> ZOMBIE_EVASION = EFFECTS.register(
			ModConstants.Effects.ZOMBIE_EVASION,
			() -> new ModEffect(EffectType.BENEFICIAL, 5149489)
	);*/

	private ModEffects()
	{
		throw new IllegalStateException();
	}

	public static void commonSetup()
	{
	}
}
