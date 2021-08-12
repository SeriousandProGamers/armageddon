package xyz.spg.armageddon.core.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.AItems;

public final class ThrownRottenEgg extends ThrowableItemProjectile
{
	public ThrownRottenEgg(EntityType<? extends ThrowableItemProjectile> entityType, Level level)
	{
		super(entityType, level);
	}

	public ThrownRottenEgg(Level level, LivingEntity owner)
	{
		super(AEntityTypes.ROTTEN_EGG, owner, level);
	}

	public ThrownRottenEgg(Level level, double x, double y, double z)
	{
		super(AEntityTypes.ROTTEN_EGG, x, y, z, level);
	}

	@Override
	public void handleEntityEvent(byte event)
	{
		if(event == 3)
		{
			for(var i = 0; i < 8; i++)
			{
				level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, getItem()), getX(), getY(), getZ(), ((double)random.nextFloat() - .5D) * .08D, ((double)random.nextFloat() - .5D) * .08D, ((double)random.nextFloat() - .5D) * .08D);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result)
	{
		super.onHitEntity(result);
		result.getEntity().hurt(DamageSource.thrown(this, getOwner()), 0F);
	}

	@Override
	protected void onHit(HitResult result)
	{
		super.onHit(result);

		if(!level.isClientSide)
		{
			if(random.nextInt(8) == 0)
			{
				var i = 1;

				if(random.nextInt(32) == 0)
					i = 4;

				for(var j = 0; j < i; j++)
				{
					var chicken = AEntityTypes.CHICKEN_ZOMBIE.create(level);

					if(chicken != null)
					{
						chicken.moveTo(getX(), getY(), getZ(), getYRot(), 0F);
						level.addFreshEntity(chicken);
					}
				}
			}

			level.broadcastEntityEvent(this, (byte) 3);
			discard();
		}
	}

	@Override
	protected Item getDefaultItem()
	{
		return AItems.ROTTEN_EGG;
	}
}
