package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.CowZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class CowZombieRenderer extends MobRenderer<CowZombieEntity, CowModel<CowZombieEntity>>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/cow_zombie.png");

	public CowZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new CowModel<>(), .7F);
	}

	@Override
	public ResourceLocation getEntityTexture(CowZombieEntity entity)
	{
		return TEXTURES;
	}
}
