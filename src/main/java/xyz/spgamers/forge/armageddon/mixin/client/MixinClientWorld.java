package xyz.spgamers.forge.armageddon.mixin.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
@OnlyIn(Dist.CLIENT)
public abstract class MixinClientWorld
{
	@Inject(
			method = "getCloudColor",
			at = @At("RETURN"),
			cancellable = true
	)
	private void getCloudColor(float partialTicks, CallbackInfoReturnable<Vector3d> cir)
	{
		/*if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
			cir.setReturnValue(BloodMoonHelper.getMoonColorVec3D());*/
	}
}
