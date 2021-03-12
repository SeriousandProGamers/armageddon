package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ZombiePigEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class ZombiePigRenderer extends MobRenderer<ZombiePigEntity, PigModel<ZombiePigEntity>>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/zombie_pig.png");

	public ZombiePigRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new PigModel<>(), .7F);
	}

	@Override
	public ResourceLocation getEntityTexture(ZombiePigEntity entity)
	{
		return TEXTURES;
	}
}
