package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.PandaZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.PandaZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class PandaZombieRenderer extends MobRenderer<PandaZombieEntity, PandaZombieModel>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/panda.png");

	public PandaZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new PandaZombieModel(9, 0F), .9F);
	}

	@Override
	public ResourceLocation getEntityTexture(PandaZombieEntity entity)
	{
		return TEXTURE;
	}
}
