package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.ZombieCowModel;
import xyz.spgamers.forge.armageddon.entity.ZombieCowEntity;
import xyz.spgamers.forge.armageddon.util.Constants;

public class ZombieCowRenderer extends MobRenderer<ZombieCowEntity, ZombieCowModel<ZombieCowEntity>>
{
	public ZombieCowRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new ZombieCowModel<>(), .7F);
	}

	@Override
	public ResourceLocation getEntityTexture(ZombieCowEntity entity)
	{
		return Constants.Textures.ZOMBIE_COW;
	}
}
