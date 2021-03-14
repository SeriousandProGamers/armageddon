package xyz.spgamers.forge.armageddon.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import xyz.spgamers.forge.armageddon.init.ModEntities;

import javax.annotation.Nullable;
import java.util.Random;

public final class ZombieHelper
{
	private ZombieHelper()
	{
		throw new IllegalStateException();
	}

	public static boolean isChickenJockeySupported(EntityType<?> entityType)
	{
		if(entityType == ModEntities.PIG_ZOMBIE.get())
			return false;
		else if(entityType == ModEntities.COW_ZOMBIE.get())
			return false;
		else if(entityType == ModEntities.CHICKEN_ZOMBIE.get())
			return false;
		else if(entityType == ModEntities.SHEEP_ZOMBIE.get())
			return false;
		else
			return true;
	}

	@Nullable
	public static MobEntity createTurnedEntity(MobEntity original)
	{
		EntityType<?> originalType = original.getType();
		EntityType<? extends MobEntity> turnedType = null;

		if(originalType == EntityType.PIG)
			turnedType = ModEntities.PIG_ZOMBIE.get();
		else if(originalType == EntityType.COW)
			turnedType = ModEntities.COW_ZOMBIE.get();
		else if(originalType == EntityType.CHICKEN)
			turnedType = ModEntities.CHICKEN_ZOMBIE.get();
		else if(originalType == EntityType.SHEEP)
			turnedType = ModEntities.SHEEP_ZOMBIE.get();

		if(turnedType != null)
			return original.func_233656_b_(turnedType, false);

		return null;
	}

	public static AttributeModifierMap.MutableAttribute registerZombieAttributes()
	{
		return ZombieEntity.func_234342_eQ_();
	}

	public static boolean canZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return MonsterEntity.canMonsterSpawnInLight(entityType, world, reason, pos, random);
	}
}
