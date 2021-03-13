package xyz.spgamers.forge.armageddon.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.SheepZombieEntity;

// SheepModel has generic type that looks for SheepEntity
// which our zombie sheep does not extend
@OnlyIn(Dist.CLIENT)
public final class SheepZombieModel extends QuadrupedModel<SheepZombieEntity>
{
	private float headRotationAngleX;

	public SheepZombieModel()
	{
		super(12, 0F, false, 8F, 4F, 2F, 2F, 24);

		headModel = new ModelRenderer(this, 0, 0);
		headModel.addBox(-3F, -4F, -6F, 6F, 6F, 8F, 0F);
		headModel.setRotationPoint(0F, 6F, -8F);

		body = new ModelRenderer(this, 28, 8);
		body.addBox(-4F, -10F, -7F, 8F, 16F, 6F, 0F);
		body.setRotationPoint(0F, 5F, 2F);
	}

	@Override
	public void setLivingAnimations(SheepZombieEntity entity, float limbSwing, float limbSwingAmount, float partialTick)
	{
		super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);

		// this is the reason this model requires a SheepEntity in vanilla
		// these methods exist there, for sheep eating grass
		headModel.rotationPointY = 6F + entity.getHeadRotationPointY(partialTick) * 9F;
		headRotationAngleX = entity.getHeadRotationAngleX(partialTick);
	}

	@Override
	public void setRotationAngles(SheepZombieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		headModel.rotateAngleX = headRotationAngleX;
	}
}
