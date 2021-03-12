package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.util.Random;

// Add any common shared logic between the different zombie types here
// rather than duplicate code across them all
public class AbstractZombieEntity extends ZombieEntity
{
	protected AbstractZombieEntity(EntityType<? extends AbstractZombieEntity> type, World world)
	{
		super(type, world);
	}

	protected static AttributeModifierMap.MutableAttribute registerZombieAttributes()
	{
		return ZombieEntity.func_234342_eQ_();
	}

	protected static boolean canZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return MonsterEntity.canMonsterSpawnInLight(entityType, world, reason, pos, random);
	}
}
