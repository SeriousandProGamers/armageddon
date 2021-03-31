package xyz.spgamers.forge.armageddon.client.renderer.entity.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.FoxZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.FoxZombieEntity;

@OnlyIn(Dist.CLIENT)
public final class FoxZombieHeldItemLayer extends LayerRenderer<FoxZombieEntity, FoxZombieModel>
{
	public FoxZombieHeldItemLayer(IEntityRenderer<FoxZombieEntity, FoxZombieModel> entityRenderer)
	{
		super(entityRenderer);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, FoxZombieEntity fox, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		boolean flag = fox.isSleeping();
		boolean flag1 = fox.isChild();

		matrixStack.push();

		if(flag1)
		{
			matrixStack.scale(.75F, .75F, .75F);
			matrixStack.translate(0D, .5D, .209375F);
		}

		matrixStack.translate(getEntityModel().head.rotationPointX / 16F, getEntityModel().head.rotationPointY / 16F, getEntityModel().head.rotationPointZ / 16F);
		float f1 = fox.getInterestedAngle(partialTicks);
		matrixStack.rotate(Vector3f.ZP.rotation(f1));
		matrixStack.rotate(Vector3f.YP.rotationDegrees(netHeadYaw));
		matrixStack.rotate(Vector3f.XP.rotationDegrees(headPitch));

		if(flag1)
		{
			if(flag)
				matrixStack.translate(.4F, .26F, .15F);
			else
				matrixStack.translate(.08F, .26F, -.5D);
		}
		else if(flag)
			matrixStack.translate(.46F, .26F, .22F);
		else
			matrixStack.translate(.06F, .27F, -.5D);

		matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));

		if(flag)
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(90F));

		if(fox.hasPoisonItem())
		{
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(-135F));
			matrixStack.translate(.09D, 0D, .045D);
		}

		ItemStack stack = fox.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(fox, stack, ItemCameraTransforms.TransformType.GROUND, false, matrixStack, buffer, packedLightIn);
		matrixStack.pop();
	}
}
