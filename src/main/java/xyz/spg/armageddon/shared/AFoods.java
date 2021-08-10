package xyz.spg.armageddon.shared;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public final class AFoods
{
	public static final FoodProperties ROTTEN_PORKCHOP = rotten()
			.nutrition(4)
			.saturationMod(.15F)
			.meat()
			.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .8F) // Rotten Flesh > Hunger | Effect
			.build();

	public static final FoodProperties ROTTEN_BEEF = rotten()
			.nutrition(4)
			.saturationMod(.15F)
			.meat()
			.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .8F) // Rotten Flesh > Hunger | Effect
			.build();

	public static final FoodProperties ROTTEN_MUTTON = rotten()
			.nutrition(3)
			.saturationMod(.15F)
			.meat()
			.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .8F) // Rotten Flesh > Hunger | Effect
			.build();

	public static final FoodProperties ROTTEN_CHICKEN = rotten()
			.nutrition(3)
			.saturationMod(.15F)
			.meat()
			.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .8F) // Rotten Flesh > Hunger | Effect
			.build();

	public static final FoodProperties ROTTEN_RABBIT = rotten()
			.nutrition(4)
			.saturationMod(.15F)
			.meat()
			.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .8F) // Rotten Flesh > Hunger | Effect
			.build();

	public static final FoodProperties ROTTEN_FISH = rotten()
			.nutrition(3)
			.saturationMod(.1F)
			.build();

	static void register() { }

	// region: Helpers
	private static FoodProperties.Builder food()
	{
		return new FoodProperties.Builder();
	}

	private static FoodProperties.Builder rotten()
	{
		// Rotten Flesh > Hunger | Effect
		return food().effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), .8F);
		/*
		var food = food();
		// add all effects from Rotten Flesh to this new food
		Foods.ROTTEN_FLESH.getEffects().forEach(effect -> food.effect(effect::getFirst, effect.getSecond()));
		return food;
		*/
	}
	// endregion
}
