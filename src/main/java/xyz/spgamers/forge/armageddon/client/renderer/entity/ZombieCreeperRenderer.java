package xyz.spgamers.forge.armageddon.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import xyz.spgamers.forge.armageddon.client.renderer.entity.layers.ZombieCreeperChargeLayer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.ZombieCreeperModel;
import xyz.spgamers.forge.armageddon.entity.ZombieCreeperEntity;
import xyz.spgamers.forge.armageddon.util.Constants;

public class ZombieCreeperRenderer extends MobRenderer<ZombieCreeperEntity, ZombieCreeperModel>
{
	public ZombieCreeperRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new ZombieCreeperModel(), .5F);

		addLayer(new ZombieCreeperChargeLayer(this));
	}

	@Override
	protected void preRenderCallback(ZombieCreeperEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
	{
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		float f1 = 1F + MathHelper.sin(f * 100F) * f * .01F;
		f = MathHelper.clamp(f, 0F, 1F);
		f *= f;
		f *= f;
		float f2 = (1F + f * .4F) * f1;
		float f3 = (1F + f * .1F) / f1;
		matrixStackIn.scale(f2, f3, f2);
	}

	@Override
	protected float getOverlayProgress(ZombieCreeperEntity livingEntityIn, float partialTicks)
	{
		float f = livingEntityIn.getCreeperFlashIntensity(partialTicks);
		return (int) (f * 10F) % 2 == 0 ? 0F : MathHelper.clamp(f, .5F, 1F);
	}

	@Override
	public ResourceLocation getEntityTexture(ZombieCreeperEntity entity)
	{
		return Constants.Textures.ZOMBIE_CREEPER;
	}
}
