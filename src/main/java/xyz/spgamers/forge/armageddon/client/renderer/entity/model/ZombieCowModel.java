package xyz.spgamers.forge.armageddon.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import xyz.spgamers.forge.armageddon.entity.ZombieCowEntity;

public class ZombieCowModel<E extends ZombieCowEntity> extends QuadrupedModel<E>
{
	public ZombieCowModel()
	{
		super(12, 0F, false, 10F, 4F, 2F, 2F, 24);

		headModel = new ModelRenderer(this, 0, 0);
		headModel.addBox(-4F, -4F, -6F, 8F, 8F, 6F, 0F);
		headModel.setRotationPoint(0F, 4F, -8F);
		headModel.setTextureOffset(22, 0).addBox(-5F, -5F, -4F, 1F, 3F, 1F, 0F);
		headModel.setTextureOffset(22, 0).addBox(4F, -5F, -4F, 1F, 3F, 1F, 0F);

		body = new ModelRenderer(this, 18, 4);
		body.addBox(-6F, -10F, -7F, 12F, 18F, 10F, 0F);
		body.setRotationPoint(0F, 5F, 2F);
		body.setTextureOffset(52, 0).addBox(-2F, 2F, -8F, 4F, 6F, 1F);

		--legBackRight.rotationPointX;
		++legBackLeft.rotationPointX;
		legBackRight.rotationPointZ += 0F;
		legBackLeft.rotationPointZ += 0F;
		--legFrontRight.rotationPointX;
		++legFrontLeft.rotationPointX;
		--legFrontRight.rotationPointZ;
		--legFrontLeft.rotationPointZ;
	}

	public ModelRenderer getHead()
	{
		return headModel;
	}
}
