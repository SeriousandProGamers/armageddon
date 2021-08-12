package xyz.spg.armageddon.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.entity.RabbitZombie;

@OnlyIn(Dist.CLIENT)
public final class RabbitZombieModel<T extends RabbitZombie> extends EntityModel<T>
{
	private final ModelPart leftRearFoot;
	private final ModelPart rightRearFoot;
	private final ModelPart leftHaunch;
	private final ModelPart rightHaunch;
	private final ModelPart body;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart head;
	private final ModelPart rightEar;
	private final ModelPart leftEar;
	private final ModelPart tail;
	private final ModelPart nose;
	private float jumpRotation;

	public RabbitZombieModel(ModelPart part)
	{
		leftRearFoot = part.getChild("left_hind_foot");
		rightRearFoot = part.getChild("right_hind_foot");
		leftHaunch = part.getChild("left_haunch");
		rightHaunch = part.getChild("right_haunch");
		body = part.getChild("body");
		leftFrontLeg = part.getChild("left_front_leg");
		rightFrontLeg = part.getChild("right_front_leg");
		head = part.getChild("head");
		rightEar = part.getChild("right_ear");
		leftEar = part.getChild("left_ear");
		tail = part.getChild("tail");
		nose = part.getChild("nose");
	}

	@Override
	public void setupAnim(T rabbit, float p2, float p3, float p4, float p5, float p6)
	{
		var f = p4 - (float) rabbit.tickCount;
		nose.xRot = p6 * ((float) Math.PI / 180F);
		head.xRot = p6 * ((float) Math.PI / 180F);
		rightEar.xRot = p6 * ((float) Math.PI / 180F);
		leftEar.xRot = p6 * ((float) Math.PI / 180F);
		nose.yRot = p5 * ((float) Math.PI / 180F);
		head.yRot = p5 * ((float) Math.PI / 180F);
		rightEar.yRot = nose.yRot - .2617994F;
		leftEar.yRot = nose.yRot + .2617994F;
		jumpRotation = Mth.sin(rabbit.getJumpCompletion(f) * (float) Math.PI);
		leftHaunch.xRot = (jumpRotation * 50F - 21F) * ((float) Math.PI / 180F);
		rightHaunch.xRot = (jumpRotation * 50F - 21F) * ((float) Math.PI / 180F);
		leftRearFoot.xRot = jumpRotation * 50F * ((float) Math.PI / 180F);
		rightRearFoot.xRot = jumpRotation * 50F * ((float) Math.PI / 180F);
		leftFrontLeg.xRot = (jumpRotation * -40F - 11F) * ((float) Math.PI / 180F);
		rightFrontLeg.xRot = (jumpRotation * -40F - 11F) * ((float) Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int p3, int p4, float p5, float p6, float p7, float p8)
	{
		if(young)
		{
			poseStack.pushPose();
			poseStack.scale(.56666666F, .56666666F, .56666666F);
			poseStack.translate(0D, 1.375D, .125D);

			head.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			leftEar.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightEar.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			nose.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);

			poseStack.popPose();
			poseStack.pushPose();
			poseStack.scale(.4F, .4F, .4F);
			poseStack.translate(0D, 2.25D, 0D);

			leftRearFoot.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightRearFoot.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			leftHaunch.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightHaunch.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			body.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			leftFrontLeg.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightFrontLeg.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			tail.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);

			poseStack.popPose();
		}
		else
		{
			poseStack.pushPose();
			poseStack.scale(.6F, .6F, .6F);
			poseStack.translate(0D, 1D, 0D);

			leftRearFoot.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightRearFoot.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			leftHaunch.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightHaunch.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			body.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			leftFrontLeg.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightFrontLeg.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			head.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			rightEar.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			leftEar.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			tail.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);
			nose.render(poseStack, vertexConsumer, p3, p4, p5, p6, p7, p8);

			poseStack.popPose();
		}
	}

	@Override
	public void prepareMobModel(T rabbit, float p2, float p3, float p4)
	{
		super.prepareMobModel(rabbit, p2, p3, p4);
		jumpRotation = Mth.sin(rabbit.getJumpCompletion(p4) * (float) Math.PI);
	}
}
