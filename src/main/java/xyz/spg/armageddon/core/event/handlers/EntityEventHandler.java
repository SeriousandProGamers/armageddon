package xyz.spg.armageddon.core.event.handlers;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.Armageddon;

@Mod.EventBusSubscriber(modid = Armageddon.ID_MOD)
public final class EntityEventHandler
{
	@SubscribeEvent
	public static void onPreLivingConvert(LivingConversionEvent.Pre event)
	{
		if(event.getOutcome() == EntityType.ZOMBIFIED_PIGLIN && event.getEntityLiving() instanceof Pig pig)
		{
			if(convertPigToZombiePig(pig))
			{
				event.setConversionTimer(0);
				event.setCanceled(true);
			}
		}
	}

	private static boolean convertPigToZombiePig(Pig pig)
	{
		var level = pig.level;
		var zombiePig = AEntityTypes.PIG_ZOMBIE.create(level);

		if(zombiePig != null)
		{
			if(ForgeEventFactory.canLivingConvert(pig, AEntityTypes.PIG_ZOMBIE, timer -> { }))
			{
				zombiePig.moveTo(pig.getX(), pig.getY(), pig.getZ());
				zombiePig.setNoAi(pig.isNoAi());
				zombiePig.setBaby(pig.isBaby());

				if(pig.hasCustomName())
				{
					zombiePig.setCustomName(pig.getCustomName());
					zombiePig.setCustomNameVisible(pig.isCustomNameVisible());
				}

				zombiePig.setPersistenceRequired();
				ForgeEventFactory.onLivingConvert(pig, zombiePig);
				level.addFreshEntity(zombiePig);
				pig.discard();
				return true;
			}
		}

		return false;
	}
}
