package xyz.spgamers.forge.armageddon.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.SheepZombieEntity;

// SheepWoolModel requires a SheepEntity which our custom sheep is not
@OnlyIn(Dist.CLIENT)
public final class SheepZombieWoolModel extends QuadrupedModel<SheepZombieEntity>
{
	private float headRotationAngleX;

	public SheepZombieWoolModel()
	{
		super(12, 0F, false, 8F, 4F, 2F, 2F, 24);
		headModel = new ModelRenderer(this, 0, 0);
		headModel.addBox(-3F, -4F, -4F, 6F, 6F, 6F, 0.6F);
		headModel.setRotationPoint(0F, 6F, -8F);

		body = new ModelRenderer(this, 28, 8);
		body.addBox(-4F, -10F, -7F, 8F, 16F, 6F, 1.75F);
		body.setRotationPoint(0F, 5F, 2F);

		legBackRight = new ModelRenderer(this, 0, 16);
		legBackRight.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0.5F);
		legBackRight.setRotationPoint(-3F, 12F, 7F);

		legBackLeft = new ModelRenderer(this, 0, 16);
		legBackLeft.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0.5F);
		legBackLeft.setRotationPoint(3F, 12F, 7F);

		legFrontRight = new ModelRenderer(this, 0, 16);
		legFrontRight.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0.5F);
		legFrontRight.setRotationPoint(-3F, 12F, -5F);

		legFrontLeft = new ModelRenderer(this, 0, 16);
		legFrontLeft.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0.5F);
		legFrontLeft.setRotationPoint(3F, 12F, -5F);
	}

	@Override
	public void setLivingAnimations(SheepZombieEntity entity, float limbSwing, float limbSwingAmount, float partialTick)
	{
		super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTick);

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
