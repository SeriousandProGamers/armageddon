package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CowModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ZombieCowEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class ZombieCowRenderer extends MobRenderer<ZombieCowEntity, CowModel<ZombieCowEntity>>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/zombie_cow.png");

	public ZombieCowRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new CowModel<>(), .7F);
	}

	@Override
	public ResourceLocation getEntityTexture(ZombieCowEntity entity)
	{
		return TEXTURES;
	}
}
