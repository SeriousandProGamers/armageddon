package xyz.spgamers.forge.armageddon.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.PolarBearZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.PolarBearZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class PolarBearZombieRenderer extends MobRenderer<PolarBearZombieEntity, PolarBearZombieModel>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/polar_bear.png");

	public PolarBearZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new PolarBearZombieModel(), .9F);
	}

	@Override
	public ResourceLocation getEntityTexture(PolarBearZombieEntity entity)
	{
		return TEXTURE;
	}

	@Override
	protected void preRenderCallback(PolarBearZombieEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
	{
		matrixStackIn.scale(1.2F, 1.2F, 1.2F);
		super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}
}
