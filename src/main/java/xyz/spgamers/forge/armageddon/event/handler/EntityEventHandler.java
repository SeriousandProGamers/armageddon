package xyz.spgamers.forge.armageddon.event.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.entity.ZombieMooshroomEntity;
import xyz.spgamers.forge.armageddon.event.LivingZombieTurnEvent;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class EntityEventHandler
{
	private EntityEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		World world = event.getWorld();
		Entity entity = event.getEntity();

		if(!(world instanceof ServerWorld))
			return;
		if(!(entity instanceof ZombieEntity))
			return;

		ZombieEntity zombie = (ZombieEntity) entity;

		zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, PigEntity.class, true));
		zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, CowEntity.class, true));
		zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, MooshroomEntity.class, true));
	}

	// priority = EventPriority.HIGHEST
	// ensure our event handler is first to run
	// other mods should run after ours
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onEntityTurn(LivingZombieTurnEvent event)
	{
		MobEntity mob = event.getMob();

		if(mob instanceof PigEntity)
			event.setZombifiedVariant(ModEntities.ZOMBIE_PIG.get());
		else if(mob instanceof MooshroomEntity)
		{
			MooshroomEntity.Type type = ((MooshroomEntity) mob).getMooshroomType();

			if(type == MooshroomEntity.Type.RED)
				event.setZombifiedVariant(ModEntities.ZOMBIE_RED_MOOSHROOM.get());
			else
				event.setZombifiedVariant(ModEntities.ZOMBIE_BROWN_MOOSHROOM.get());
		}
		else if(mob instanceof CowEntity)
			event.setZombifiedVariant(ModEntities.ZOMBIE_COW.get());
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingHurtEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		DamageSource source = event.getSource();
		float damage = event.getAmount();

		if(entity instanceof MobEntity)
			tryAndTurnEntity((MobEntity) entity, source, damage);
	}

	private static void tryAndTurnEntity(MobEntity damagedEntity, DamageSource damageSource, float damage)
	{
		// Vanilla handles turning Villagers into Zombie Villagers
		if(damagedEntity instanceof VillagerEntity)
			return;
		// ensure entity will have died
		if(damage <= 0)
			return;
		if(damagedEntity.getHealth() - damage > 0)
			return;
		// ensure entity was damaged by some form of zombie
		if(!(damageSource.getTrueSource() instanceof ZombieEntity))
			return;

		Difficulty difficulty = damagedEntity.world.getDifficulty();

		// zombie variants only spawn on normal or higher
		if(difficulty.getId() >= Difficulty.NORMAL.getId())
		{
			// if its not hard difficulty give it some random chance
			if(difficulty != Difficulty.HARD && damagedEntity.world.rand.nextBoolean())
				return;

			// post event for mods to listen for and modify as they wish
			LivingZombieTurnEvent event = new LivingZombieTurnEvent(damagedEntity, damageSource, damage);

			// if event was not canceled
			if(MinecraftForge.EVENT_BUS.post(event))
			{
				EntityType<? extends ZombieEntity> zombieType = event.getZombifiedVariant();

				if(zombieType != null)
				{
					ZombieEntity zombie = damagedEntity.func_233656_b_(zombieType, false);

					// zombie.onInitialSpawn(entity.world, entity.world.getDifficultyForLocation(zombie.getPosition()), SpawnReason.CONVERSION, new ZombieEntity.GroupData(false, true), null);

					if(!damagedEntity.isSilent())
						damagedEntity.world.playEvent(null, 1026, damagedEntity.getPosition(), 0);

					damagedEntity.world.addEntity(zombie);
				}
			}
		}
	}
}
