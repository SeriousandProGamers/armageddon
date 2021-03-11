package xyz.spgamers.forge.armageddon.event;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import xyz.spgamers.forge.armageddon.event.handler.EntityEventHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * LivingZombieTurnEvent is fired when an Entity is set to be turned into a Zombified variation. <br>
 * This event is fired whenever an Entity is hurt and the damage dealt would have killed the entity in
 * {@link EntityEventHandler#tryAndTurnEntity(MobEntity, DamageSource, float)}. <br>
 * <br>
 * {@link #mob} The entity that will be turned into a Zombified variant. <br>
 * {@link #source} contains the DamageSource that caused this Entity to be turned. <br>
 * {@link #damage} contains the amount of damage dealt to the Entity. <br>
 * {@link #zombifiedVariant} contains the zombified {@link EntityType} that will be spawned in place of this Entity. <br>
 * <br>
 *  Use {@link #setZombifiedVariant(EntityType)} to modify the zombie variant to be spawned.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, the Entity is killed instead of being turned.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 * @see LivingHurtEvent
 */
@Cancelable
// NPE: Extending LivingHurtEvent caused other handlers to fire
// that shouldnt due to entity being dead at this point
public class LivingZombieTurnEvent extends LivingEvent // LivingHurtEvent
{
	@Nullable
	private EntityType<? extends MobEntity> zombifiedVariant = null;
	private final MobEntity mob;
	private final DamageSource source;
	private final float damage;

	public LivingZombieTurnEvent(MobEntity entity, DamageSource source, float damage)
	{
		super(entity);

		this.source = source;
		this.damage = damage;

		mob = entity;
	}

	public DamageSource getSource()
	{
		return source;
	}

	public float getDamage()
	{
		return damage;
	}

	public MobEntity getMob()
	{
		return mob;
	}

	@Nullable
	public EntityType<? extends MobEntity> getZombifiedVariant()
	{
		return zombifiedVariant;
	}

	public void setZombifiedVariant(@Nonnull EntityType<? extends MobEntity> zombifiedVariant)
	{
		this.zombifiedVariant = zombifiedVariant;
	}
}
