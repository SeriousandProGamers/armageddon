package xyz.spgamers.forge.armageddon.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.client.renderer.entity.ZombieCreeperRenderer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.ZombiePigRenderer;
import xyz.spgamers.forge.armageddon.entity.ZombieCreeperEntity;
import xyz.spgamers.forge.armageddon.entity.ZombiePigEntity;
import xyz.spgamers.forge.armageddon.util.Constants;

public final class ModEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MOD_ID);

	public static final RegistryObject<EntityType<ZombiePigEntity>> ZOMBIE_PIG = ENTITIES.register(
			Constants.Entities.ZOMBIE_PIG,
			() -> EntityType.Builder.create(ZombiePigEntity::new, EntityClassification.MONSTER).build(Constants.Entities.ZOMBIE_PIG)
	);

	public static final RegistryObject<EntityType<ZombieCreeperEntity>> ZOMBIE_CREEPER = ENTITIES.register(
			Constants.Entities.ZOMBIE_CREEPER,
			() -> EntityType.Builder.create(ZombieCreeperEntity::new, EntityClassification.MONSTER).build(Constants.Entities.ZOMBIE_CREEPER)
	);

	private ModEntities()
	{
		throw new IllegalStateException();
	}

	public static void setupEntities()
	{
		EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_PIG.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, ZombiePigEntity::canMonsterSpawn);
		EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_CREEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, ZombieCreeperEntity::canMonsterSpawn);

		GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_PIG.get(), ZombiePigEntity.bakeAttributes().create());
		GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_CREEPER.get(), ZombieCreeperEntity.bakeAttributes().create());
	}

	@OnlyIn(Dist.CLIENT)
	public static void clientSetupEntities()
	{
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_PIG.get(), ZombiePigRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_CREEPER.get(), ZombieCreeperRenderer::new);
	}
}
