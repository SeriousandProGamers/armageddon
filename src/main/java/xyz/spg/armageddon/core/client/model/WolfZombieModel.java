package xyz.spg.armageddon.core.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ColorableAgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.entity.WolfZombie;

@OnlyIn(Dist.CLIENT)
public final class WolfZombieModel<T extends WolfZombie> extends ColorableAgeableListModel<T>
{
	private final ModelPart head;
	private final ModelPart realHead;
	private final ModelPart body;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart tail;
	private final ModelPart realTail;
	private final ModelPart upperBody;

	public WolfZombieModel(ModelPart part)
	{
		head = part.getChild("head");
		realHead = head.getChild("real_head");
		body = part.getChild("body");
		upperBody = part.getChild("upper_body");
		rightHindLeg = part.getChild("right_hind_leg");
		leftHindLeg = part.getChild("left_hind_leg");
		rightFrontLeg = part.getChild("right_front_leg");
		leftFrontLeg = part.getChild("left_front_leg");
		tail = part.getChild("tail");
		realTail = tail.getChild("real_tail");
	}

	@Override
	public void setupAnim(T p_104137_, float p_104138_, float p_104139_, float p_104140_, float p_104141_, float p_104142_)
	{
		head.xRot = p_104142_ * ((float) Math.PI / 180F);
		head.yRot = p_104141_ * ((float) Math.PI / 180F);
		tail.xRot = p_104140_;
		// tail.xRot = 1F;
	}

	@Override
	public void prepareMobModel(T p_104132_, float p_104133_, float p_104134_, float p_104135_)
	{
		tail.yRot = 0F;

		body.setPos(0F, 14F, 2F);
		body.xRot = ((float) Math.PI / 2F);
		upperBody.setPos(-1F, 14F, -3F);
		upperBody.xRot = body.xRot;
		tail.setPos(-1F, 12F, 8F);
		rightHindLeg.setPos(-2.5F, 16F, 7F);
		leftHindLeg.setPos(.5F, 16F, 7F);
		rightFrontLeg.setPos(-2.5F, 16F, -4F);
		leftFrontLeg.setPos(.5F, 16F, -4F);
		rightHindLeg.xRot = Mth.cos(p_104133_ * .6662F) * 1.4F * p_104134_;
		leftHindLeg.xRot = Mth.cos(p_104133_ * .6662F + (float) Math.PI) * 1.4F * p_104134_;
		rightFrontLeg.xRot = Mth.cos(p_104133_ * .6662F + (float) Math.PI) * 1.4F * p_104134_;
		leftFrontLeg.xRot = Mth.cos(p_104133_ * .6662F) * 1.4F * p_104134_;

		// realHead.zRot = p_104132_.getHeadRollAngle(p_104135_) + p_104132_.getBodyRollAngle(p_104135_, 0F);
		realHead.zRot = p_104132_.getBodyRollAngle(p_104135_, 0F);
		upperBody.zRot = p_104132_.getBodyRollAngle(p_104135_, -8F);
		body.zRot = p_104132_.getBodyRollAngle(p_104135_, -.16F);
		realTail.zRot = p_104132_.getBodyRollAngle(p_104135_, -.2F);
	}

	@Override
	protected Iterable<ModelPart> headParts()
	{
		return ImmutableList.of(head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts()
	{
		return ImmutableList.of(body, rightHindLeg, leftHindLeg, rightFrontLeg, leftFrontLeg, tail, upperBody);
	}
}
