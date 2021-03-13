package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.layers.SheepZombieWoolLayer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.SheepZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.SheepZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class SheepZombieRenderer extends MobRenderer<SheepZombieEntity, SheepZombieModel>
{
	private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation(ModConstants.MOD_ID,"textures/entity/zombie/sheep_zombie.png");

	public SheepZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new SheepZombieModel(), .7F);

		addLayer(new SheepZombieWoolLayer(this));
	}

	@Override
	public ResourceLocation getEntityTexture(SheepZombieEntity entity)
	{
		return SHEARED_SHEEP_TEXTURES;
	}
}
