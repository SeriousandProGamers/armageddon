package xyz.spg.armageddon.core.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spg.armageddon.core.client.model.SheepZombieFurModel;
import xyz.spg.armageddon.core.client.model.SheepZombieModel;
import xyz.spg.armageddon.core.entity.SheepZombie;
import xyz.spg.armageddon.shared.Armageddon;

@OnlyIn(Dist.CLIENT)
public final class SheepZombieFurLayer<T extends SheepZombie> extends RenderLayer<T, SheepZombieModel<T>>
{
	private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation(Armageddon.ID_MINECRAFT, "textures/entity/sheep/sheep_fur.png");

	private final SheepZombieFurModel<T> model;

	public SheepZombieFurLayer(RenderLayerParent<T, SheepZombieModel<T>> layerParent, EntityModelSet modelSet)
	{
		super(layerParent);

		model = new SheepZombieFurModel<>(modelSet.bakeLayer(ModelLayers.SHEEP_FUR));
	}

	public void render(PoseStack p_117421_, MultiBufferSource p_117422_, int p_117423_, T sheep, float p_117425_, float p_117426_, float p_117427_, float p_117428_, float p_117429_, float p_117430_)
	{
		if(!sheep.isSheared())
		{
			if(sheep.isInvisible())
			{
				var var22 = Minecraft.getInstance();
				var var23 = var22.shouldEntityAppearGlowing(sheep);

				if(var23)
				{
					getParentModel().copyPropertiesTo(model);
					model.prepareMobModel(sheep, p_117425_, p_117426_, p_117427_);
					model.setupAnim(sheep, p_117425_, p_117426_, p_117428_, p_117429_, p_117430_);
					VertexConsumer var24 = p_117422_.getBuffer(RenderType.outline(SHEEP_FUR_LOCATION));
					model.renderToBuffer(p_117421_, var24, p_117423_, LivingEntityRenderer.getOverlayCoords(sheep, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
				}

			}
			else
			{
				float var11;
				float var12;
				float var13;

				if(sheep.hasCustomName() && "jeb_".equals(sheep.getName().getContents()))
				{
					var var15 = sheep.tickCount / 25 + sheep.getId();
					var var16 = DyeColor.values().length;
					var var17 = var15 % var16;
					var var18 = (var15 + 1) % var16;
					var var19 = ((float) (sheep.tickCount % 25) + p_117427_) / 25.0F;
					var var20 = Sheep.getColorArray(DyeColor.byId(var17));
					var var21 = Sheep.getColorArray(DyeColor.byId(var18));

					var11 = var20[0] * (1.0F - var19) + var21[0] * var19;
					var12 = var20[1] * (1.0F - var19) + var21[1] * var19;
					var13 = var20[2] * (1.0F - var19) + var21[2] * var19;
				}
				else
				{
					var var14 = Sheep.getColorArray(sheep.getColor());

					var11 = var14[0];
					var12 = var14[1];
					var13 = var14[2];
				}

				coloredCutoutModelCopyLayerRender(getParentModel(), model, SHEEP_FUR_LOCATION, p_117421_, p_117422_, p_117423_, sheep, p_117425_, p_117426_, p_117428_, p_117429_, p_117430_, p_117427_, var11, var12, var13);
			}
		}
	}
}
