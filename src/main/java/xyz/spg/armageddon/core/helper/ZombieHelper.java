package xyz.spg.armageddon.core.helper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.ForgeEventFactory;
import xyz.spg.armageddon.core.entity.AbstractZombie;
import xyz.spg.armageddon.core.event.handlers.EntityEventHandler;

import javax.annotation.Nullable;

public final class ZombieHelper
{
	private static final BiMap<EntityType<? extends LivingEntity>, EntityType<? extends AbstractZombie>> zombieTypeMap = HashBiMap.create();

	public static boolean tryConvertToZombie(Mob living)
	{
		var level = living.level;
		var zombieType = getZombieType(living);

		if(zombieType == null)
			return false;

		var zombie = zombieType.create(level);

		if(zombie == null || !ForgeEventFactory.canLivingConvert(living, zombieType, timer -> { }))
			return false;

		zombie.moveTo(living.getX(), living.getY(), living.getZ());
		zombie.setNoAi(living.isNoAi());
		zombie.setBaby(living.isBaby());

		if(living.hasCustomName())
		{
			zombie.setCustomName(living.getCustomName());
			zombie.setCustomNameVisible(living.isCustomNameVisible());
		}

		zombie.finalizeZombieTypeConversion(living);
		zombie.setPersistenceRequired();

		// copy across rain ticks
		var rainTicks = EntityEventHandler.getEntityRainTicks(living);
		EntityEventHandler.setEntityRainTicks(zombie, rainTicks);

		ForgeEventFactory.onLivingConvert(living, zombie);
		level.addFreshEntity(zombie);
		living.discard();
		return true;
	}

	@Nullable
	public static EntityType<? extends AbstractZombie> getZombieType(Entity entity)
	{
		return zombieTypeMap.get(entity.getType());
	}

	@Nullable
	public static EntityType<? extends LivingEntity> getLivingType(AbstractZombie zombie)
	{
		return zombieTypeMap.inverse().get(zombie.getType());
	}

	public static <Z extends AbstractZombie, L extends LivingEntity> void registerZombieVariant(EntityType<L> livingType, EntityType<Z> zombieType)
	{
		zombieTypeMap.put(livingType, zombieType);
	}
}
