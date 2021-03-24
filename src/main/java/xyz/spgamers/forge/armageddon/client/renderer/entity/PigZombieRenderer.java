package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.PigZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class PigZombieRenderer extends MobRenderer<PigZombieEntity, PigModel<PigZombieEntity>>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/pig.png");

	public PigZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new PigModel<>(), .7F);
	}

	@Override
	public ResourceLocation getEntityTexture(PigZombieEntity entity)
	{
		return TEXTURES;
	}
}
