package xyz.spgamers.forge.armageddon.event;

import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.util.ModConstants;

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
