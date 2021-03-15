package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.FoxZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.FoxZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class FoxZombieRenderer extends MobRenderer<FoxZombieEntity, FoxZombieModel>
{
	public static final ResourceLocation FOX = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/fox/fox.png");
	public static final ResourceLocation SLEEPING_FOX = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/fox/fox_sleep.png");
	public static final ResourceLocation SNOW_FOX = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/fox/snow_fox.png");
	public static final ResourceLocation SLEEPING_SNOW_FOX = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/fox/snow_fox_sleep.png");

	public FoxZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new FoxZombieModel(), .4F);

		// addLayer(new FoxZombieHeldItemLayer(this));
	}

	@Override
	public ResourceLocation getEntityTexture(FoxZombieEntity entity)
	{
		if(entity.getVariantType() == FoxEntity.Type.RED)
			return entity.isSleeping() ? SLEEPING_FOX : FOX;
		else
			return entity.isSleeping() ? SNOW_FOX : SLEEPING_SNOW_FOX;
	}
}
