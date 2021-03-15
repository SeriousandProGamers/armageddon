package xyz.spgamers.forge.armageddon.mixin.client;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.spgamers.forge.armageddon.util.BloodMoonHelper;

@Mixin(LightTexture.class)
@OnlyIn(Dist.CLIENT)
public abstract class MixinLightTexture
{
	@Inject(
			method = "updateLightmap",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/vector/Vector3f;lerp(Lnet/minecraft/util/math/vector/Vector3f;F)V",
					ordinal = 0
			),
			cancellable = true,
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void updateLightmap(float partialTicks, CallbackInfo ci, ClientWorld world, float f, float f1, float f3, float f2, Vector3f skyVector)
	{
		if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
		{
			if(skyVector != null)
				skyVector.lerp(BloodMoonHelper.getMoonColorVec3F(), 1F);
		}
	}
}
