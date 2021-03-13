package xyz.spgamers.forge.armageddon.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.client.renderer.entity.CowZombieRenderer;
import xyz.spgamers.forge.armageddon.client.renderer.entity.PigZombieRenderer;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.CowZombieEntity;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.PigZombieEntity;
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

	public static final RegistryObject<EntityType<EggEntity>> ROTTEN_EGG = register(
			ModConstants.Entities.ROTTEN_EGG,
			ModEntities::rottenEggFactory,
			EntityClassification.MISC,
			builder -> builder.size(.25F, .25F).trackingRange(4).func_233608_b_(10) // same values as egg
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
	}

	@OnlyIn(Dist.CLIENT)
	public static void clientSetup()
	{
		registerEntityRenderer(PIG_ZOMBIE, PigZombieRenderer::new);
		registerEntityRenderer(COW_ZOMBIE, CowZombieRenderer::new);
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

	// cant refrence ROTTEN_EGG while its being constructed
	// little hack to get around that
	private static EggEntity rottenEggFactory(World world)
	{
		return new EggEntity(ROTTEN_EGG.get(), world);
	}
}
