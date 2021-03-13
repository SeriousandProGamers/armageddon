package xyz.spgamers.forge.armageddon.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.AbstractZombieEntity;
import xyz.spgamers.forge.armageddon.item.SpawnEggItem;
import xyz.spgamers.forge.armageddon.util.WorldHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Mod.EventBusSubscriber
public final class PlayerEventHandler
{
	private PlayerEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event)
	{
		Entity entity = event.getTarget();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		World world = event.getWorld();
		PlayerEntity player = event.getPlayer();

		if(item instanceof SpawnEggItem && entity instanceof AbstractZombieEntity)
		{
			SpawnEggItem<?> spawnEgg = (SpawnEggItem<?>) item;

			if(!spawnEgg.isEntityEnabled())
				return;

			AbstractZombieEntity zombie = (AbstractZombieEntity) entity;
			EntityType<?> spawnEggType = spawnEgg.getEntityType();

			if(spawnEggType != zombie.getType())
				return;

			if(WorldHelper.isServerWorld(world))
			{
				Optional<AbstractZombieEntity> optional = SpawnEggItem.getChildToSpawn(player, zombie, (EntityType<AbstractZombieEntity>) zombie.getType(), (ServerWorld) world, zombie.getPosition(), stack);
				optional.ifPresent(child -> onChildSpawnFromEgg(player, zombie, child));

				// stupid little hack
				// item gets taken even if in creative
				// just grow the stack by one if in creative
				if(player.isCreative())
					stack.grow(1);

				event.setCancellationResult(optional.isPresent() ? ActionResultType.SUCCESS : ActionResultType.PASS);
			}
			else
				event.setCancellationResult(ActionResultType.CONSUME);

			event.setCanceled(true);
		}
	}

	// method is protected in MobEntity
	// use reflection to get around this
	private static <E extends MobEntity> void onChildSpawnFromEgg(PlayerEntity player, E parent, E child)
	{
		try
		{
			Method method = ObfuscationReflectionHelper.findMethod(MobEntity.class, "onChildSpawnFromEgg", PlayerEntity.class, MobEntity.class);
			method.invoke(parent, player, child);
		}
		catch(IllegalAccessException | InvocationTargetException | ObfuscationReflectionHelper.UnableToFindMethodException ignored)
		{
		}
	}
}
