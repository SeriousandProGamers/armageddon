package xyz.spgamers.forge.armageddon.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ChickenZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@OnlyIn(Dist.CLIENT)
public final class ChickenZombieRenderer extends MobRenderer<ChickenZombieEntity, ChickenModel<ChickenZombieEntity>>
{
	public static final ResourceLocation TEXTURES = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/chicken.png");

	public ChickenZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new ChickenModel<>(), .3F);
	}

	@Override
	public ResourceLocation getEntityTexture(ChickenZombieEntity entity)
	{
		return TEXTURES;
	}

	@Override
	protected float handleRotationFloat(ChickenZombieEntity chicken, float partialTicks)
	{
		float f = MathHelper.lerp(partialTicks, chicken.oFlap, chicken.wingRotation);
		float f1 = MathHelper.lerp(partialTicks, chicken.oFlapSpeed, chicken.destPos);
		return (MathHelper.sin(f) + 1F) * f1;
	}
}
