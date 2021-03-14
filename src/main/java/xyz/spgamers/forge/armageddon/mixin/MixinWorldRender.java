package xyz.spgamers.forge.armageddon.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
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
			int rgbColor = BloodMoonHelper.MOON_COLOR.getColor();

			float r = (float) (rgbColor >> 16 & 255) / 255F;
			float g = (float) (rgbColor >> 8 & 255) / 255F;
			float b = (float) (rgbColor & 255) / 255F;

			RenderSystem.color4f(r, g, b, 1F / world.getRainStrength(partialTicks));
		}
	}
}
