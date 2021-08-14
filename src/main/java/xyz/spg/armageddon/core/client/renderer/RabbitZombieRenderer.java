package xyz.spg.armageddon.core.client.renderer;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.client.model.RabbitZombieModel;
import xyz.spg.armageddon.core.entity.RabbitZombie;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public class RabbitZombieRenderer<T extends RabbitZombie> extends MobRenderer<T, RabbitZombieModel<T>>
{
	private static final ResourceLocation RABBIT_LOCATION = new ResourceLocation(Armageddon.ID_MOD, "textures/entity/zombie/rabbit.png");

	public RabbitZombieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new RabbitZombieModel<>(context.bakeLayer(ModelLayers.RABBIT)), .3F);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return RABBIT_LOCATION;
	}
}
