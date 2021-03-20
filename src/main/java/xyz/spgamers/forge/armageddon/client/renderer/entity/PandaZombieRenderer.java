package xyz.spgamers.forge.armageddon.client.renderer.entity;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.client.renderer.entity.model.PandaZombieModel;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.PandaZombieEntity;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class PandaZombieRenderer extends MobRenderer<PandaZombieEntity, PandaZombieModel>
{
	private static final Map<PandaEntity.Gene, ResourceLocation> pandaTextures = Maps.newHashMap();

	public PandaZombieRenderer(EntityRendererManager rendererManager)
	{
		super(rendererManager, new PandaZombieModel(9, 0F), .9F);
	}

	@Override
	public ResourceLocation getEntityTexture(PandaZombieEntity entity)
	{
		PandaEntity.Gene gene = entity.func_213590_ei();

		if(pandaTextures.containsKey(gene))
			return pandaTextures.get(gene);

		ResourceLocation texture;

		if(gene == PandaEntity.Gene.NORMAL)
			texture = new ResourceLocation(ModConstants.MOD_ID, "textures/entity/zombie/panda/panda.png");
		else
		{
			String texturePath = String.format("textures/entity/zombie/panda/%s_panda.png", gene.getName());
			texture = new ResourceLocation(ModConstants.MOD_ID, texturePath);
		}

		pandaTextures.put(gene, texture);
		return texture;
	}
}
