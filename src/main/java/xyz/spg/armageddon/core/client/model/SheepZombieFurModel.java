package xyz.spg.armageddon.core.client.model;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.entity.SheepZombie;

@OnlyIn(Dist.CLIENT)
public final class SheepZombieFurModel<T extends SheepZombie> extends QuadrupedModel<T>
{
	// private float headXRot;

	public SheepZombieFurModel(ModelPart modelPart)
	{
		super(modelPart, false, 8F, 4F, 2F, 2F, 24);
	}

	@Override
	public void prepareMobModel(T sheep, float p_103662_, float p_103663_, float p_103664_)
	{
		super.prepareMobModel(sheep, p_103662_, p_103663_, p_103664_);

		// head.y = 6F + sheep.getHeadEatPositionScale(p_103664_) * 9F;
		// headXRot = sheep.getHeadEatAngleScale(p_103664_);
	}

	@Override
	public void setupAnim(T sheep, float p_103667_, float p_103668_, float p_103669_, float p_103670_, float p_103671_)
	{
		super.setupAnim(sheep, p_103667_, p_103668_, p_103669_, p_103670_, p_103671_);

		// head.xRot = headXRot;
	}
}
