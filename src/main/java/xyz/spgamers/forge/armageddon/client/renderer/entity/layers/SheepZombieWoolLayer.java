package xyz.spgamers.forge.armageddon.client.renderer.entity.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.SheepZombieModel;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.SheepZombieWoolModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.SheepZombieEntity;

// vanilla SheepWoolLayer requires a SheepEntity which our custom sheep is not
@OnlyIn(Dist.CLIENT)
public final class SheepZombieWoolLayer extends LayerRenderer<SheepZombieEntity, SheepZombieModel>
{
	// vanilla wool texture
	public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/sheep/sheep_fur.png");
	private final SheepZombieWoolModel sheepModel = new SheepZombieWoolModel();

	public SheepZombieWoolLayer(IEntityRenderer<SheepZombieEntity, SheepZombieModel> entityRenderer)
	{
		super(entityRenderer);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, SheepZombieEntity sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(!sheep.isSheared() && !sheep.isInvisible())
		{
			float red;
			float green;
			float blue;

			if(sheep.hasCustomName() && "jeb_".equals(sheep.getName().getUnformattedComponentText()))
			{
				int i = sheep.ticksExisted / 25 + sheep.getEntityId();
				int colorAmount = DyeColor.values().length;
				int colorIndex1 = i % colorAmount;
				int colorIndex2 = (i + 1) % colorAmount;
				float blendAmount = ((float) (sheep.ticksExisted % 25) + partialTicks) / 25F;

				float[] afloat1 = SheepEntity.getDyeRgb(DyeColor.byId(colorIndex1));
				float[] afloat2 = SheepEntity.getDyeRgb(DyeColor.byId(colorIndex2));

				red = afloat1[0] * (1F - blendAmount) + afloat2[0] * blendAmount;
				green = afloat1[1] * (1F - blendAmount) + afloat2[1] * blendAmount;
				blue = afloat1[2] * (1F - blendAmount) + afloat2[2] * blendAmount;
			}
			else
			{
				float[] afloat = SheepEntity.getDyeRgb(sheep.getFleeceColor());

				red = afloat[0];
				green = afloat[1];
				blue = afloat[2];
			}

			renderCopyCutoutModel(getEntityModel(), sheepModel, TEXTURE, matrixStack, buffer, packedLight, sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, red, green, blue);
		}
	}
}
