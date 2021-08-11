package xyz.spg.armageddon.core.client.model;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.entity.SheepZombie;

@OnlyIn(Dist.CLIENT)
public final class SheepZombieModel<T extends SheepZombie> extends QuadrupedModel<T>
{
	// private float headXRot;

	public SheepZombieModel(ModelPart part)
	{
		super(part, false, 8F, 4F, 2F, 2F, 24);
	}

	@Override
	public void prepareMobModel(T sheep, float p_102615_, float p_102616_, float p_102617_)
	{
		super.prepareMobModel(sheep, p_102615_, p_102616_, p_102617_);

		// head.y = 6F + sheep.getHeadEatPositionScale(p_102617_);
		// headXRot = sheep.getHeadEatAngleScale(p_102617_);
	}

	@Override
	public void setupAnim(T sheep, float p_103510_, float p_103511_, float p_103512_, float p_103513_, float p_103514_)
	{
		super.setupAnim(sheep, p_103510_, p_103511_, p_103512_, p_103513_, p_103514_);

		// head.xRot = headXRot;
	}
}
