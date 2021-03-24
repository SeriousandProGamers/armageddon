package xyz.spgamers.forge.armageddon.init;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.client.renderer.entity.*;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.*;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.function.Function;

public final class ModEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModConstants.MOD_ID);

	public static final RegistryObject<EntityType<PigZombieEntity>> PIG_ZOMBIE = register(
			ModConstants.Entities.PIG_ZOMBIE,
			PigZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(.9F, .9F).trackingRange(10) // same values as pig
	);

	public static final RegistryObject<EntityType<CowZombieEntity>> COW_ZOMBIE = register(
			ModConstants.Entities.COW_ZOMBIE,
			CowZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(.9F, 1.4F).trackingRange(10) // same values as cow
	);

	public static final RegistryObject<EntityType<ChickenZombieEntity>> CHICKEN_ZOMBIE = register(
			ModConstants.Entities.CHICKEN_ZOMBIE,
			ChickenZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(.4F, .7F).trackingRange(10) // same values as chicken
	);

	public static final RegistryObject<EntityType<SheepZombieEntity>> SHEEP_ZOMBIE = register(
			ModConstants.Entities.SHEEP_ZOMBIE,
			SheepZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(.9F, 1.3F).trackingRange(10) // same values as sheep
	);

	public static final RegistryObject<EntityType<FoxZombieEntity>> FOX_ZOMBIE = register(
			ModConstants.Entities.FOX_ZOMBIE,
			FoxZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(.6F, .7F).trackingRange(8).func_233607_a_(Blocks.SWEET_BERRY_BUSH) // same values as fox
	);

	public static final RegistryObject<EntityType<PandaZombieEntity>> PANDA_ZOMBIE = register(
			ModConstants.Entities.PANDA_ZOMBIE,
			PandaZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(1.3F, 1.25F).trackingRange(10) // same values as panda
	);

	public static final RegistryObject<EntityType<PolarBearZombieEntity>> POLAR_BEAR_ZOMBIE = register(
			ModConstants.Entities.POLAR_BEAR_ZOMBIE,
			PolarBearZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(1.4F, 1.4F).trackingRange(10) // same values as polar bear
	);

	public static final RegistryObject<EntityType<RabbitZombieEntity>> RABBIT_ZOMBIE = register(
			ModConstants.Entities.RABBIT_ZOMBIE,
			RabbitZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(.4F, 0.5F).trackingRange(8) // same values as rabbit
	);

	public static final RegistryObject<EntityType<WolfZombieEntity>> WOLF_ZOMBIE = register(
			ModConstants.Entities.WOLF_ZOMBIE,
			WolfZombieEntity::new,
			EntityClassification.MONSTER,
			builder -> builder.size(.6F, .85F).trackingRange(10) // same values as wolf
	);

	private ModEntities()
	{
		throw new IllegalStateException();
	}

	public static void commonSetup()
	{
		PIG_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, PigZombieEntity.registerPigZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PigZombieEntity::canPigZombieSpawn);
		});

		COW_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, CowZombieEntity.registerCowZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CowZombieEntity::canCowZombieSpawn);
		});

		CHICKEN_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, ChickenZombieEntity.registerChickenZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ChickenZombieEntity::canChickenZombieSpawn);
		});

		SHEEP_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, SheepZombieEntity.registerSheepZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SheepZombieEntity::canSheepZombieSpawn);
		});

		FOX_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, FoxZombieEntity.registerFoxZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FoxZombieEntity::canFoxZombieSpawn);
		});

		PANDA_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, PandaZombieEntity.registerPandaZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PandaZombieEntity::canPandaZombieSpawn);
		});

		POLAR_BEAR_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, PolarBearZombieEntity.registerPolarBearZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PolarBearZombieEntity::canPolarBearZombieSpawn);
		});

		RABBIT_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, RabbitZombieEntity.registerRabbitZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RabbitZombieEntity::canRabbitZombieSpawn);
		});

		WOLF_ZOMBIE.ifPresent(entityType -> {
			GlobalEntityTypeAttributes.put(entityType, WolfZombieEntity.registerWolfZombieAttributes().create());
			EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WolfZombieEntity::canWolfZombieSpawn);
		});
	}

	@OnlyIn(Dist.CLIENT)
	public static void clientSetup()
	{
		registerEntityRenderer(PIG_ZOMBIE, PigZombieRenderer::new);
		registerEntityRenderer(COW_ZOMBIE, CowZombieRenderer::new);
		registerEntityRenderer(CHICKEN_ZOMBIE, ChickenZombieRenderer::new);
		registerEntityRenderer(SHEEP_ZOMBIE, SheepZombieRenderer::new);
		registerEntityRenderer(FOX_ZOMBIE, FoxZombieRenderer::new);
		registerEntityRenderer(PANDA_ZOMBIE, PandaZombieRenderer::new);
		registerEntityRenderer(POLAR_BEAR_ZOMBIE, PolarBearZombieRenderer::new);
		registerEntityRenderer(RABBIT_ZOMBIE, RabbitZombieRenderer::new);
		registerEntityRenderer(WOLF_ZOMBIE, WolfZombieRenderer::new);
	}

	// utility methods to make registering entities easier
	@OnlyIn(Dist.CLIENT)
	private static <E extends Entity> void registerEntityRenderer(RegistryObject<EntityType<E>> entityTypeObj, IRenderFactory<E> entityRenderer)
	{
		entityTypeObj.ifPresent(entityType -> RenderingRegistry.registerEntityRenderingHandler(entityType, entityRenderer));
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> register(String entityName, Function<World, E> entityBuilder, EntityClassification entityClassification, Function<EntityType.Builder<E>, EntityType.Builder<E>> entityModifier)
	{
		return ENTITIES.register(
				entityName, // <- does not require the mod domain, deferred register does that for us
				() ->
					// the name in "build" is for data fixers, Mojangs internal system to fix broken data between mc versions
					// it is mostly unused for mods but we still have to pass our entities registry name across
					entityModifier.apply(
							EntityType.Builder.create(
									(/* EntityType<E> */ $, /* World */ world) -> entityBuilder.apply(world), entityClassification
							)
					).build(String.format("%s:%s", ModConstants.MOD_ID, entityName))
		);
	}

	// wrapper around the above method to remove the consumer method
	// its an optional function to modify how the entity type works
	private static <E extends Entity> RegistryObject<EntityType<E>> register(String entityName, Function<World, E> entityBuilder, EntityClassification entityClassification)
	{
		return register(entityName, entityBuilder, entityClassification, $ -> $);
	}
}
