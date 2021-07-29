package xyz.spg.armageddon.core.client.renderer;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.entity.AbstractZombie;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public class PigZombieRenderer<T extends AbstractZombie> extends MobRenderer<T, PigModel<T>>
{
	private static final ResourceLocation PIG_LOCATION = new ResourceLocation(Armageddon.ID_MOD, "textures/entity/zombie/pig.png");

	public PigZombieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new PigModel<>(context.bakeLayer(ModelLayers.PIG)), .7F);

		// addLayer(new SaddleLayer<>(this, new PigModel<>(context.bakeLayer(ModelLayers.PIG)), new ResourceLocation(Armageddon.ID_MINECRAFT, "textures/entity/pig/pig_saddle.png")));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return PIG_LOCATION;
	}
}
