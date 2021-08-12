package xyz.spg.armageddon.core.client.renderer;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.entity.ChickenZombie;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public final class ChickenZombieRenderer<T extends ChickenZombie> extends MobRenderer<T, ChickenModel<T>>
{
	private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation(Armageddon.ID_MOD, "textures/entity/zombie/chicken.png");

	public ChickenZombieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)), .3F);
	}

	@Override
	protected float getBob(T zombie, float p_115306_)
	{
		var f = Mth.lerp(p_115306_, zombie.oFlap, zombie.flap);
		var f1 = Mth.lerp(p_115306_, zombie.oFlapSpeed, zombie.flapSpeed);
		return (Mth.sin(f) + 1F) * f1;
	}

	@Override
	public ResourceLocation getTextureLocation(T zombie)
	{
		return CHICKEN_LOCATION;
	}
}
