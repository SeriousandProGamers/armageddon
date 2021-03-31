package xyz.spgamers.forge.armageddon.mixin.client;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
@OnlyIn(Dist.CLIENT)
public abstract class MixinBiome
{
	@Inject(
			method = "getSkyColor",
			at = @At("RETURN"),
			cancellable = true
	)
	private void getSkyColor(CallbackInfoReturnable<Integer> cir)
	{
		/*if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
			cir.setReturnValue(BloodMoonHelper.MOON_COLOR.getColor());*/
	}

	@Inject(
			method = "getFogColor",
			at = @At("RETURN"),
			cancellable = true
	)
	private void getFogColor(CallbackInfoReturnable<Integer> cir)
	{
		/*if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
			cir.setReturnValue(BloodMoonHelper.MOON_COLOR.getColor());*/
	}

	@Inject(
			method = "getWaterColor",
			at = @At("RETURN"),
			cancellable = true
	)
	private void getWaterColor(CallbackInfoReturnable<Integer> cir)
	{
		/*if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
			cir.setReturnValue(BloodMoonHelper.MOON_COLOR.getColor());*/
	}

	@Inject(
			method = "getWaterFogColor",
			at = @At("RETURN"),
			cancellable = true
	)
	private void getWaterFogColor(CallbackInfoReturnable<Integer> cir)
	{
		/*if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
			cir.setReturnValue(BloodMoonHelper.MOON_COLOR.getColor());*/
	}
}
