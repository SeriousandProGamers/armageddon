package xyz.spg.armageddon.core.client.renderer;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.client.layer.SheepZombieFurLayer;
import xyz.spg.armageddon.core.client.model.SheepZombieModel;
import xyz.spg.armageddon.core.entity.SheepZombie;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public final class SheepZombieRenderer<T extends SheepZombie> extends MobRenderer<T, SheepZombieModel<T>>
{
	private static final ResourceLocation SHEEP_LOCATION = new ResourceLocation(Armageddon.ID_MOD, "textures/entity/zombie/sheep.png");

	public SheepZombieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new SheepZombieModel<>(context.bakeLayer(ModelLayers.SHEEP)), .7F);

		addLayer(new SheepZombieFurLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(T zombie)
	{
		return SHEEP_LOCATION;
	}
}
