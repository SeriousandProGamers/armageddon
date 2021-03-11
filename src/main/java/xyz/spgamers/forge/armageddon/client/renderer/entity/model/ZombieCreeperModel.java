package xyz.spgamers.forge.armageddon.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import xyz.spgamers.forge.armageddon.entity.ZombieCreeperEntity;

public class ZombieCreeperModel extends SegmentedModel<ZombieCreeperEntity>
{
	private final ModelRenderer head;
	private final ModelRenderer creeperArmor;
	private final ModelRenderer body;
	private final ModelRenderer leg1;
	private final ModelRenderer leg2;
	private final ModelRenderer leg3;
	private final ModelRenderer leg4;

	public ZombieCreeperModel()
	{
		this(0F);
	}

	public ZombieCreeperModel(float scale)
	{
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8F, 8F, 8F, scale);
		head.setRotationPoint(0F, 6F, 0F);

		creeperArmor = new ModelRenderer(this, 32, 0);
		creeperArmor.addBox(-4F, -8F, -4F, 8F, 8F, 8F, scale + .5F);
		creeperArmor.setRotationPoint(0F, 6F, 0F);

		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4F, 0F, -2F, 8F, 12F, 4F, scale);
		body.setRotationPoint(0F, 6F, 0F);

		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2F, 0F, -2F, 4F, 6F, 4F, scale);
		leg1.setRotationPoint(-2F, 18F, 4F);

		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2F, 0F, -2F, 4F, 6F, 4F, scale);
		leg2.setRotationPoint(2F, 18F, 4F);

		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2F, 0F, -2F, 4F, 6F, 4F, scale);
		leg3.setRotationPoint(-2F, 18F, -4F);

		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2F, 0F, -2F, 4F, 6F, 4F, scale);
		leg4.setRotationPoint(2F, 18F, -4F);
	}

	@Override
	public Iterable<ModelRenderer> getParts()
	{
		return ImmutableList.of(head, body, leg1, leg2, leg3, leg4);
	}

	@Override
	public void setRotationAngles(ZombieCreeperEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
		head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
		leg1.rotateAngleX = MathHelper.cos(limbSwing * .6662F) * 1.4F * limbSwingAmount;
		leg2.rotateAngleX = MathHelper.cos(limbSwing * .6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		leg3.rotateAngleX = MathHelper.cos(limbSwing * .6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		leg4.rotateAngleX = MathHelper.cos(limbSwing * .6662F) * 1.4F * limbSwingAmount;
	}
}
