package xyz.spg.armageddon.core.event.handlers;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.ATags;
import xyz.spg.armageddon.shared.Armageddon;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Armageddon.ID_MOD)
public final class EntityEventHandler
{
	private static final Map<UUID, Integer> entityRainTicks = Maps.newHashMap();

	@SubscribeEvent
	public static void onStruckByLightning(EntityStruckByLightningEvent event)
	{
		var hit = event.getEntity();

		if(hit instanceof Mob mob && tryConvertToZombie(mob))
			event.setCanceled(true);
		else if(!hit.getType().is(ATags.EntityTypes.ZOMBIES_MOB))
			setEntityRainTicks(hit, 0);
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if(event.getEntityLiving() instanceof Mob mob)
		{
			var zombieType = Armageddon.getZombieType(mob);
			var level = mob.level;
			var rainTicks = getEntityRainTicks(mob);
			var prevRainTicks = rainTicks;

			if(level.isThundering() && level.isRaining())
				rainTicks++;
			else
				rainTicks = 0;

			/*if(!mob.level.isClientSide)
			{
				mob.setCustomName(new TextComponent("Rain Ticks: %d".formatted(rainTicks)));
				mob.setCustomNameVisible(true);
			}*/

			if(zombieType != null && rainTicks % 75 == 0 && level.random.nextInt(1000) == 0)
			{
				if(level.isRainingAt(mob.blockPosition()) && level.isThundering())
				{
					var lightningBolt = EntityType.LIGHTNING_BOLT.create(level);

					if(lightningBolt != null)
					{
						lightningBolt.moveTo(Vec3.atBottomCenterOf(mob.blockPosition()));
						lightningBolt.setVisualOnly(false);
						level.addFreshEntity(lightningBolt);
						rainTicks = 0;
					}
				}
			}

			if(rainTicks != prevRainTicks)
				setEntityRainTicks(mob, rainTicks);
		}
	}

	// region: Helpers
	// region: Rain Ticks
	private static int getEntityRainTicks(Entity entity)
	{
		var uuid = entity.getUUID();
		return entityRainTicks.computeIfAbsent(uuid, $ -> 0);
	}

	private static void setEntityRainTicks(Entity entity, int rainTicks)
	{
		var uuid = entity.getUUID();
		entityRainTicks.put(uuid, rainTicks);
	}
	// endregion

	private static boolean tryConvertToZombie(Mob living)
	{
		var level = living.level;
		var zombieType = Armageddon.getZombieType(living);

		if(zombieType == null)
			return false;

		var zombie = zombieType.create(level);

		if(zombie == null || !ForgeEventFactory.canLivingConvert(living, AEntityTypes.PIG_ZOMBIE, timer -> { }))
			return false;

		zombie.moveTo(living.getX(), living.getY(), living.getZ());
		zombie.setNoAi(living.isNoAi());
		zombie.setBaby(living.isBaby());

		if(living.hasCustomName())
		{
			zombie.setCustomName(living.getCustomName());
			zombie.setCustomNameVisible(living.isCustomNameVisible());
		}

		zombie.setPersistenceRequired();

		// copy across rain ticks
		var rainTicks = getEntityRainTicks(living);
		setEntityRainTicks(zombie, rainTicks);

		ForgeEventFactory.onLivingConvert(living, zombie);
		level.addFreshEntity(zombie);
		living.discard();
		return true;
	}
	// endregion
}
