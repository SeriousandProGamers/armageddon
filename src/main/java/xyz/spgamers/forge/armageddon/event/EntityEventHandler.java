package xyz.spgamers.forge.armageddon.event;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.util.ModConstants;
import xyz.spgamers.forge.armageddon.util.WorldHelper;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public final class EntityEventHandler
{
	private EntityEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event)
	{
		if(event.getEntityLiving().getLastAttackedEntity() instanceof ZombieEntity)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLivingExperienceDrop(LivingExperienceDropEvent event)
	{
		if(event.getEntityLiving().getLastAttackedEntity() instanceof ZombieEntity)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		LivingEntity entity = event.getEntityLiving();
		SpawnReason reason = event.getSpawnReason();
		IWorld world = event.getWorld();

		/*if(entity.getType() != EntityType.ZOMBIE)
		{
			event.setResult(Event.Result.DENY);
			return;
		}*/

		// is night
		if(WorldHelper.isNight(world))
		{
			// normal spawning
			if(reason == SpawnReason.NATURAL)
			{
				if(entity.getType() == EntityType.ZOMBIE)
				{
					// for(int i = 0; i < 4 + world.getRandom().nextInt(2); i++)
					for(int i = 0; i < Armageddon.SERVER_CONFIG.common.getHordeSpawnCount(); i++)
					{
						ZombieEntity zombie = EntityType.ZOMBIE.create((World) world);

						if(zombie != null)
						{
							// zombie.setPosition(entity.getPosX() + (i % 2 == 0 ? i : 0), entity.getPosY(), entity.getPosZ() + (i % 2 != 0 ? i : 0));
							zombie.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
							world.addEntity(zombie);
						}
					}

					/*DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
						Minecraft.getInstance().player.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
					});*/
				}
			}
		}
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
}
