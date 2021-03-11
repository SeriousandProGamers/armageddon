package xyz.spgamers.forge.armageddon.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import xyz.spgamers.forge.armageddon.entity.ZombieCreeperEntity;

public class ZombieCreeperModel<E extends ZombieCreeperEntity> extends SegmentedModel<E>
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
// What monstrosity is this model?!?
/*
package xyz.spgamers.forge.armageddon.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.ZombieCreeperEntity;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ZombieCreeperModel<T extends ZombieCreeperEntity> extends AgeableModel<T>
{
	public ModelRenderer body;
	public ModelRenderer legLeftBack;
	public ModelRenderer legLeftFront;
	public ModelRenderer legRightFront;
	public ModelRenderer head;
	public ModelRenderer headOverlay;
	public ModelRenderer legRightBack;
	public ModelRenderer armRight;
	public ModelRenderer armLeft;

	public ZombieCreeperModel()
	{
		super(false, 9F, 0F);

		textureWidth = 64;
		textureHeight = 32;

		legRightBack = new ModelRenderer(this, 0, 16);
		legRightBack.setRotationPoint(-2F, 18F, 4F);
		legRightBack.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0F, 0F, 0F);

		legRightFront = new ModelRenderer(this, 0, 16);
		legRightFront.setRotationPoint(-2F, 18F, -4F);
		legRightFront.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0F, 0F, 0F);

		armLeft = new ModelRenderer(this, 16, 16);
		armLeft.mirror = true;
		armLeft.setRotationPoint(5F, 9.5F, 0F);
		armLeft.addBox(-1F, -2F, -2F, 4F, 6F, 4F, 0F, 0F, 0F);
		setRotateAngle(armLeft, -1.3962634015954636F, .10000736647217022F, -.10000736647217022F);

		head = new ModelRenderer(this, 0, 0);
		head.setRotationPoint(0F, 6F, 0F);
		head.addBox(-4F, -8F, -4F, 8F, 8F, 8F, 0F, 0F, 0F);

		legLeftBack = new ModelRenderer(this, 0, 16);
		legLeftBack.setRotationPoint(2F, 18F, 4F);
		legLeftBack.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0F, 0F, 0F);

		legLeftFront = new ModelRenderer(this, 0, 16);
		legLeftFront.setRotationPoint(2F, 18F, -4F);
		legLeftFront.addBox(-2F, 0F, -2F, 4F, 6F, 4F, 0F, 0F, 0F);

		headOverlay = new ModelRenderer(this, 32, 0);
		headOverlay.setRotationPoint(0F, 6F, 0F);
		headOverlay.addBox(-4F, -8F, -4F, 8F, 8F, 8F, .5F, .5F, .5F);

		armRight = new ModelRenderer(this, 16, 16);
		armRight.setRotationPoint(-5F, 9.5F, 0F);
		armRight.addBox(-3F, -2F, -2F, 4F, 6F, 4F, 0F, 0F, 0F);
		setRotateAngle(armRight, -1.3962634015954636F, -.10000736647217022F, .10000736647217022F);

		body = new ModelRenderer(this, 16, 16);
		body.setRotationPoint(0F, 6F, 0F);
		body.addBox(-4F, 0F, -2F, 8F, 12F, 4F, 0F, 0F, 0F);
	}

	@Override
	protected Iterable<ModelRenderer> getHeadParts()
	{
		return ImmutableList.of(head, headOverlay);
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts()
	{
		return ImmutableList.of(legRightBack, legRightFront, armLeft, legLeftBack, legLeftFront, armRight, body);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		legLeftBack.rotateAngleX = MathHelper.cos(limbSwing * .6662F) * 1.4F * limbSwingAmount;
		legLeftFront.rotateAngleX = MathHelper.cos(limbSwing * .6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		legRightBack.rotateAngleX = MathHelper.cos(limbSwing * .6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		legRightFront.rotateAngleX = MathHelper.cos(limbSwing * .6662F) * 1.4F * limbSwingAmount;
		// armLeft.rotateAngleX = MathHelper.cos(limbSwing * .6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		// armRight.rotateAngleX = MathHelper.cos(limbSwing * .6662F) * 1.4F * limbSwingAmount;
		ModelHelper.func_239105_a_(armLeft, armRight, entityIn.isAggressive(), swingProgress, ageInTicks);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
*/