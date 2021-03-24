package xyz.spgamers.forge.armageddon.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.WolfZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.WolfZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class WolfZombieRenderer extends MobRenderer<WolfZombieEntity, WolfZombieModel>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/wolf.png");

	public WolfZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new WolfZombieModel(), .5F);
	}

	@Override
	protected float handleRotationFloat(WolfZombieEntity livingBase, float partialTicks)
	{
		// return livingBase.getTailRotation();
		return super.handleRotationFloat(livingBase, partialTicks);
	}

	@Override
	public void render(WolfZombieEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		/*if(entityIn.isWolfWet())
		{
			float f = entityIn.getShadingWhileWet(partialTicks);
			entityModel.setTint(f, f, f);
		}*/

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

		/*if(entityIn.isWolfWet())
			entityModel.setTint(1F, 1F, 1F);*/
	}

	@Override
	public ResourceLocation getEntityTexture(WolfZombieEntity entity)
	{
		return TEXTURE;
	}
}
