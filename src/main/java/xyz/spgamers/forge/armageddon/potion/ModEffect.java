package xyz.spgamers.forge.armageddon.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class ModEffect extends Effect
{
	// why is the constructor protected
	public ModEffect(EffectType effectType, int liquidColor)
	{
		super(effectType, liquidColor);
	}
}
