package xyz.spg.armageddon.core.event.handlers;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spg.armageddon.core.helper.ZombieHelper;
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

		if(hit instanceof Mob mob && ZombieHelper.tryConvertToZombie(mob))
			event.setCanceled(true);

		setEntityRainTicks(hit, 0);
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if(event.getEntityLiving() instanceof Mob mob)
		{
			var zombieType = ZombieHelper.getZombieType(mob);
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
	public static int getEntityRainTicks(Entity entity)
	{
		var uuid = entity.getUUID();
		return entityRainTicks.computeIfAbsent(uuid, $ -> 0);
	}

	public static void setEntityRainTicks(Entity entity, int rainTicks)
	{
		var uuid = entity.getUUID();
		entityRainTicks.put(uuid, rainTicks);
	}
	// endregion
	// endregion
}
