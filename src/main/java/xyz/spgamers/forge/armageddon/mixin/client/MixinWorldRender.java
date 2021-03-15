package xyz.spgamers.forge.armageddon.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.spgamers.forge.armageddon.util.BloodMoonHelper;

@Mixin(WorldRenderer.class)
@OnlyIn(Dist.CLIENT)
public abstract class MixinWorldRender
{
	@Inject(
			method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"
			)
	)
	private void renderSky(MatrixStack matrixStackIn, float partialTicks, CallbackInfo ci)
	{
		if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
		{
			ClientWorld world = Minecraft.getInstance().world;
			Vector3f color = BloodMoonHelper.getMoonColorVec3F();
			RenderSystem.color4f(color.getX(), color.getY(), color.getZ(), 1F / world.getRainStrength(partialTicks));
		}
	}
}
