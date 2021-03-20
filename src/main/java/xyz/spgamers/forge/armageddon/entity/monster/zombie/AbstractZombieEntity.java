package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.World;

import java.util.function.BooleanSupplier;

// Add any common shared logic between the different zombie types here
// rather than duplicate code across them all
public class AbstractZombieEntity extends ZombieEntity
{
	private final BooleanSupplier entityEnabledSupplier;

	protected AbstractZombieEntity(EntityType<? extends AbstractZombieEntity> type, World world, BooleanSupplier entityEnabledSupplier)
	{
		super(type, world);

		this.entityEnabledSupplier = entityEnabledSupplier;
	}

	public boolean isEntityEnabled()
	{
		return entityEnabledSupplier.getAsBoolean();
	}

	public void setupTurnedZombie(MobEntity originalEntity)
	{
	}

	@Override
	public void tick()
	{
		super.tick();

		if(!isEntityEnabled() && isAlive())
			onKillCommand();
	}

	@Override
	protected boolean canDropLoot()
	{
		return isEntityEnabled() && super.canDropLoot();
	}
}
