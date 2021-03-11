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
import xyz.spgamers.forge.armageddon.client.renderer.entity.ZombieCowRenderer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.ZombieCreeperRenderer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.ZombieMooshroomRenderer;
import xyz.spgamers.forge.armageddon.entity.ZombieMooshroomEntity;
import xyz.spgamers.forge.armageddon.client.renderer.entity.ZombiePigRenderer;
import xyz.spgamers.forge.armageddon.entity.ZombieCowEntity;
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

	public static final RegistryObject<EntityType<ZombieCowEntity>> ZOMBIE_COW = ENTITIES.register(
			Constants.Entities.ZOMBIE_COW,
			() -> EntityType.Builder.create(ZombieCowEntity::new, EntityClassification.MONSTER).build(Constants.Entities.ZOMBIE_COW)
	);

	public static final RegistryObject<EntityType<ZombieMooshroomEntity>> ZOMBIE_BROWN_MOOSHROOM = ENTITIES.register(
			Constants.Entities.ZOMBIE_BROWN_MOOSHROOM,
			() -> EntityType.Builder.create(ZombieMooshroomEntity::new, EntityClassification.MONSTER).build(Constants.Entities.ZOMBIE_BROWN_MOOSHROOM)
	);

	public static final RegistryObject<EntityType<ZombieMooshroomEntity>> ZOMBIE_RED_MOOSHROOM = ENTITIES.register(
			Constants.Entities.ZOMBIE_RED_MOOSHROOM,
			() -> EntityType.Builder.create(ZombieMooshroomEntity::new, EntityClassification.MONSTER).build(Constants.Entities.ZOMBIE_RED_MOOSHROOM)
	);

	private ModEntities()
	{
		throw new IllegalStateException();
	}

	public static void setupEntities()
	{
		EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_PIG.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, ZombiePigEntity::canMonsterSpawn);
		EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_CREEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, ZombieCreeperEntity::canMonsterSpawn);
		EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_COW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, ZombieCowEntity::canMonsterSpawn);
		EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_BROWN_MOOSHROOM.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, ZombieMooshroomEntity::canMooshroomSpawn);
		EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_RED_MOOSHROOM.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.WORLD_SURFACE, ZombieMooshroomEntity::canMooshroomSpawn);

		GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_PIG.get(), ZombiePigEntity.bakeAttributes().create());
		GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_CREEPER.get(), ZombieCreeperEntity.bakeAttributes().create());
		GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_COW.get(), ZombieCowEntity.bakeAttributes().create());
		GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_BROWN_MOOSHROOM.get(), ZombieMooshroomEntity.bakeAttributes().create());
		GlobalEntityTypeAttributes.put(ModEntities.ZOMBIE_RED_MOOSHROOM.get(), ZombieMooshroomEntity.bakeAttributes().create());
	}

	@OnlyIn(Dist.CLIENT)
	public static void clientSetupEntities()
	{
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_PIG.get(), ZombiePigRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_CREEPER.get(), ZombieCreeperRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_COW.get(), ZombieCowRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_BROWN_MOOSHROOM.get(), ZombieMooshroomRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.ZOMBIE_RED_MOOSHROOM.get(), ZombieMooshroomRenderer::new);
	}
}
