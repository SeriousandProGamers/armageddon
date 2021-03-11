package xyz.spgamers.forge.armageddon.init;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

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

	public static <T extends MobEntity, E extends T> E spawnDuplicateEntity(T entity, EntityType<E> entityType, boolean copyLoot)
	{
		// try and copy using vanilla method first
		E copy = entity.func_233656_b_(entityType, copyLoot);

		// if this fails, try do it ourselves
		if(copy == null)
		{
			copy = entityType.create(entity.world);
			// Mojang why is this nullable? it directly calls the constructor for given entity
			Objects.requireNonNull(copy);

			copy.copyLocationAndAnglesFrom(entity);
			copy.setChild(entity.isChild());
			copy.setNoAI(entity.isAIDisabled());

			if(entity.hasCustomName())
			{
				copy.setCustomName(entity.getCustomName());
				copy.setCustomNameVisible(entity.isCustomNameVisible());
			}

			if(entity.isNoDespawnRequired())
				copy.enablePersistence();

			copy.setInvulnerable(entity.isInvulnerable());

			if(copyLoot)
			{
				copy.setCanPickUpLoot(entity.canPickUpLoot());

				for(EquipmentSlotType slotType : EquipmentSlotType.values())
				{
					ItemStack stack = entity.getItemStackFromSlot(slotType);

					if(!stack.isEmpty())
					{
						// method is protected - use reflection to get around this
						// float dropChance = entity.getDropChance(slotType);
						float dropChance = MobEntity_getDropChance(entity, slotType);
						copy.setItemStackToSlot(slotType, stack.copy());
						copy.setDropChance(slotType, dropChance);
						stack.setCount(0); // remove from original entities inventory
					}
				}
			}

			copy.world.addEntity(copy);

			if(entity.isPassenger())
			{
				Entity riding = entity.getRidingEntity();
				Objects.requireNonNull(riding); // should not be null due to isPassenger() being true
				entity.stopRiding();
				copy.startRiding(riding, true);
			}

			entity.remove();
		}

		return copy;
	}

	private static float MobEntity_getDropChance(MobEntity mob, EquipmentSlotType slotType)
	{
		try
		{
			Method method = ObfuscationReflectionHelper.findMethod(MobEntity.class, "getDropChance", EquipmentSlotType.class);
			return (float) method.invoke(mob, slotType);
		}
		catch(IllegalAccessException | InvocationTargetException e)
		{
			return 0F;
		}
	}
}
