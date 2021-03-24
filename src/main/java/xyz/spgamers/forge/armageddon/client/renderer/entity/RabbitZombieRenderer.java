package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.RabbitZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.RabbitZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class RabbitZombieRenderer extends MobRenderer<RabbitZombieEntity, RabbitZombieModel>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/rabbit.png");

	public RabbitZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new RabbitZombieModel(), .3F);
	}

	@Override
	public ResourceLocation getEntityTexture(RabbitZombieEntity entity)
	{
		return TEXTURE;
	}
}
