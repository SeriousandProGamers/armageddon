package xyz.spgamers.forge.armageddon.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import xyz.spgamers.forge.armageddon.item.BetterSpawnEggItem;

import java.util.Optional;

public class BaseZombieEntity extends ZombieEntity
{
	public BaseZombieEntity(EntityType<? extends ZombieEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public BaseZombieEntity(World worldIn)
	{
		super(worldIn);
	}

	@Override
	public ActionResultType func_233661_c_(PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if(stack.getItem() == Items.LEAD && canBeLeashedTo(player))
		{
			setLeashHolder(player, true);
			stack.shrink(1);
			return ActionResultType.func_233537_a_(world.isRemote());
		}
		else
		{
			if(stack.getItem() == Items.NAME_TAG)
			{
				ActionResultType actionresulttype = stack.interactWithEntity(player, this, hand);

				if(actionresulttype.isSuccessOrConsume())
					return actionresulttype;
			}

			if(stack.getItem() instanceof SpawnEggItem)
			{
				if(world instanceof ServerWorld)
				{
					SpawnEggItem spawnEggItem = (SpawnEggItem) stack.getItem();
					Optional<MobEntity> childToSpawn = spawnEggItem.getChildToSpawn(player, this, (EntityType) getType(), (ServerWorld) world, getPositionVec(), stack);
					childToSpawn.ifPresent(entity -> onChildSpawnFromEgg(player, entity));
					return childToSpawn.isPresent() ? ActionResultType.SUCCESS : ActionResultType.PASS;
				}
				else
					return ActionResultType.CONSUME;
			}
			else if(stack.getItem() instanceof BetterSpawnEggItem)
			{
				if(world instanceof ServerWorld)
				{
					BetterSpawnEggItem spawnEggItem = (BetterSpawnEggItem) stack.getItem();
					Optional<MobEntity> childToSpawn = spawnEggItem.getChildToSpawn(player, this, (EntityType) getType(), (ServerWorld) world, getPosition(), stack);
					childToSpawn.ifPresent(entity -> onChildSpawnFromEgg(player, entity));
					return childToSpawn.isPresent() ? ActionResultType.SUCCESS : ActionResultType.PASS;
				}
				else
					return ActionResultType.CONSUME;
			}
			else
				return ActionResultType.PASS;
		}
	}
}
