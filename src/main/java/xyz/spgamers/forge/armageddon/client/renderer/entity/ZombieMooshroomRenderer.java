package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import xyz.spgamers.forge.armageddon.client.renderer.entity.layers.ZombieMooshroomMushroomLayer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.ZombieCowModel;
import xyz.spgamers.forge.armageddon.entity.ZombieMooshroomEntity;
import xyz.spgamers.forge.armageddon.util.Constants;

public class ZombieMooshroomRenderer extends MobRenderer<ZombieMooshroomEntity, ZombieCowModel<ZombieMooshroomEntity>>
{
	public ZombieMooshroomRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new ZombieCowModel<>(), .7F);

		addLayer(new ZombieMooshroomMushroomLayer<>(this));
	}

	@Override
	public ResourceLocation getEntityTexture(ZombieMooshroomEntity entity)
	{
		return entity.getMooshroomType() == ZombieMooshroomEntity.Type.RED ? Constants.Textures.ZOMBIE_RED_MOOSHROOM : Constants.Textures.ZOMBIE_BROWN_MOOSHROOM;
	}
}
