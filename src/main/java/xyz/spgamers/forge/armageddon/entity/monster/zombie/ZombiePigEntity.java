package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;

import java.util.Random;

public class ZombiePigEntity extends AbstractZombieEntity
{
	public ZombiePigEntity(World world)
	{
		super(ModEntities.ZOMBIE_PIG.get(), world);
	}

	public static AttributeModifierMap.MutableAttribute registerZombiePigAttributes()
	{
		return registerZombieAttributes();
	}

	public static boolean canZombiePigSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.entities.isZombiePigEnabled())
			return false;
		if(!canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}
}
