package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.ZombiePigModel;
import xyz.spgamers.forge.armageddon.entity.ZombiePigEntity;
import xyz.spgamers.forge.armageddon.util.Constants;

public final class ZombiePigRenderer extends MobRenderer<ZombiePigEntity, ZombiePigModel>
{
	public ZombiePigRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new ZombiePigModel(), .7F);
	}

	@Override
	public ResourceLocation getEntityTexture(ZombiePigEntity entity)
	{
		return Constants.Textures.ZOMBIE_PIG;
	}
}
