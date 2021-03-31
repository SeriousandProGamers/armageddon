package xyz.spgamers.forge.armageddon.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.layers.FoxZombieHeldItemLayer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.FoxZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.FoxZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class FoxZombieRenderer extends MobRenderer<FoxZombieEntity, FoxZombieModel>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/fox.png");

	public FoxZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new FoxZombieModel(), .4F);

		addLayer(new FoxZombieHeldItemLayer(this));
	}

	@Override
	protected void applyRotations(FoxZombieEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);

		/*if(entityLiving.func_213480_dY() || entityLiving.isStuck())
		{
			float f = -MathHelper.lerp(partialTicks, entityLiving.prevRotationPitch, entityLiving.rotationPitch);
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f));
		}*/
	}

	@Override
	public ResourceLocation getEntityTexture(FoxZombieEntity entity)
	{
		return TEXTURE;
	}
}
