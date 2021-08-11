package xyz.spg.armageddon.core.client.renderer;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.entity.AbstractZombie;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public final class CowZombieRenderer<T extends AbstractZombie> extends MobRenderer<T, CowModel<T>>
{
	private static final ResourceLocation COW_LOCATION = new ResourceLocation(Armageddon.ID_MOD, "textures/entity/zombie/cow.png");

	public CowZombieRenderer(EntityRendererProvider.Context context)
	{
		super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), .7F);
	}

	@Override
	public ResourceLocation getTextureLocation(T zombie)
	{
		return COW_LOCATION;
	}
}
