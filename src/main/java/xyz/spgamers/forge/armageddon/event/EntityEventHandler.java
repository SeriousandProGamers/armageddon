package xyz.spgamers.forge.armageddon.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.AbstractZombieEntity;
import xyz.spgamers.forge.armageddon.packet.SpawnTurnedZombiePacket;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public final class EntityEventHandler
{
	private EntityEventHandler()
	{
		throw new IllegalStateException();
	}

	// have to use join world event here
	// or when loading an existing world
	// zombies will not get the new goal types
	// only newly spawned zombies will
	@SubscribeEvent
	// public static void onLivingSpecialSpawn(LivingSpawnEvent.SpecialSpawn event)
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		Entity entity = event.getEntity();

		if(entity instanceof AbstractZombieEntity)
			return;
		if(entity instanceof ZombieEntity)
			AbstractZombieEntity.addCustomZombieGoals((ZombieEntity) entity);
	}

	@SubscribeEvent
	public static void onLivingEntitySpawn(LivingSpawnEvent.SpecialSpawn event)
	{
		/*LivingEntity entity = event.getEntityLiving();

		if(entity instanceof ZombieEntity)
		{
			ZombieEntity zombie = (ZombieEntity) entity;
			Entity riding = zombie.getRidingEntity();

			if(riding instanceof ChickenEntity)
			{
				ChickenEntity chicken = (ChickenEntity) riding;
				ChickenZombieEntity zombieChicken = chicken.func_233656_b_(ModEntities.CHICKEN_ZOMBIE.get(), false);

				if(zombieChicken != null)
				{
					zombie.stopRiding();
					chicken.setChickenJockey(false);
					chicken.onKillCommand();
					zombieChicken.setChickenJockey(true);
					zombie.startRiding(zombieChicken);
				}
			}
		}*/
	}

	// Not working as intended
	/*@SubscribeEvent
	public static void onLivingSetTarget(LivingSetAttackTargetEvent event)
	{
		LivingEntity attacker = event.getEntityLiving();
		LivingEntity target = event.getTarget();

		if(attacker instanceof ZombieEntity && target != null && target.isPotionActive(ModEffects.ZOMBIE_EVASION.get()))
			((ZombieEntity) target).setAttackTarget(null);
	}*/

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		DamageSource damageSource = event.getSource();
		float damage = event.getAmount();
		Entity source = damageSource.getTrueSource();

		// try and turn regular entity into zombified entity
		if(source instanceof ZombieEntity)
		{
			if(!(entity instanceof MobEntity))
				return;
			// vanilla already does this
			if(entity instanceof VillagerEntity)
				return;
			if(damage <= 0 || entity.getHealth() - damage > 0)
				return;

			ZombieEntity zombie = (ZombieEntity) source;
			MobEntity mob = (MobEntity) entity;
			Difficulty difficulty = mob.world.getDifficulty();

			// same logic as what zombie villagers use
			// must be normal or higher
			// if its not hard theres a random chance
			if(difficulty.getId() >= Difficulty.NORMAL.getId())
			{
				if(difficulty != Difficulty.HARD && mob.world.rand.nextBoolean())
					return;

				ModConstants.NETWORK.sendToServer(new SpawnTurnedZombiePacket(mob, zombie));

				if(!zombie.isSilent())
					mob.world.playEvent(null, 1026, mob.getPosition(), 0);
			}
		}
	}
}
