package xyz.spg.armageddon.core.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.client.model.WolfZombieModel;
import xyz.spg.armageddon.core.entity.WolfZombie;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public final class WolfZombieRenderer<T extends WolfZombie> extends MobRenderer<T, WolfZombieModel<T>>
{
	private static final ResourceLocation WOLF_LOCATION = new ResourceLocation(Armageddon.ID_MOD, "textures/entity/zombie/wolf.png");

	public WolfZombieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new WolfZombieModel<>(context.bakeLayer(ModelLayers.WOLF)), .5F);
	}

	@Override
	protected float getBob(T zombie, float p_115306_)
	{
		return zombie.getTailAngle();
	}

	@Override
	public void render(T zombie, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_)
	{
		if(zombie.isWet())
		{
			var wetShade = zombie.getWetShade(p_115457_);
			model.setColor(wetShade, wetShade, wetShade);
		}

		super.render(zombie, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);

		if(zombie.isWet())
			model.setColor(1F, 1F, 1F);
	}

	@Override
	public ResourceLocation getTextureLocation(T zombie)
	{
		return WOLF_LOCATION;
	}
}
