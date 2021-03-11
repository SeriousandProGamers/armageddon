package xyz.spgamers.forge.armageddon.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import xyz.spgamers.forge.armageddon.entity.ZombiePigEntity;

public final class ZombiePigModel<E extends ZombiePigEntity> extends QuadrupedModel<E>
{
	public ZombiePigModel()
	{
		this(0F);
	}

	public ZombiePigModel(float scale)
	{
		super(6, scale, false, 4F, 4F, 2F, 2F, 24);

		headModel.setTextureOffset(16, 16).addBox(-2F, 0F, -9F, 4F, 3F, 1F, scale);
	}
}
