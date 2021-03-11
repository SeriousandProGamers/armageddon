package xyz.spgamers.forge.armageddon.entity;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import xyz.spgamers.forge.armageddon.entity.ai.goal.ZombieCreeperSwellGoal;
import xyz.spgamers.forge.armageddon.util.Constants;

import javax.annotation.Nullable;
import java.util.Collection;

public class ZombieCreeperEntity extends BaseZombieEntity implements IChargeableMob
{
	public static final DataParameter<Integer> STATE = EntityDataManager.createKey(ZombieCreeperEntity.class, DataSerializers.VARINT);
	public static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(ZombieCreeperEntity.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(ZombieCreeperEntity.class, DataSerializers.BOOLEAN);

	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 30;
	private int explosionRadius = 3;
	private int droppedSkulls;

	public ZombieCreeperEntity(EntityType<? extends ZombieEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();

		goalSelector.addGoal(2, new ZombieCreeperSwellGoal(this));
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6F, 1D, 1.2D));
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, CatEntity.class, 6F, 1D, 1.2D));
	}

	@Override
	public int getMaxFallHeight()
	{
		return getAttackTarget() == null ? 3 : 3 + (int) (getHealth() - 1F);
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier)
	{
		boolean flag = super.onLivingFall(distance, damageMultiplier);
		timeSinceIgnited = (int) ((float) timeSinceIgnited + distance * 1.5F);

		if(timeSinceIgnited > fuseTime - 5)
			timeSinceIgnited = fuseTime - 5;

		return flag;
	}

	@Override
	protected void registerData()
	{
		super.registerData();

		dataManager.register(STATE, -1);
		dataManager.register(POWERED, false);
		dataManager.register(IGNITED, false);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);

		if(dataManager.get(POWERED))
			compound.putBoolean(Constants.NBTKeys.POWERED, true);

		compound.putShort(Constants.NBTKeys.FUSE, (short) fuseTime);
		compound.putByte(Constants.NBTKeys.EXPLOSION_RADIUS, (byte) explosionRadius);
		compound.putBoolean(Constants.NBTKeys.IGNITED, hasIgnited());
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);

		dataManager.set(POWERED, compound.getBoolean(Constants.NBTKeys.POWERED));

		if(compound.contains(Constants.NBTKeys.FUSE, net.minecraftforge.common.util.Constants.NBT.TAG_ANY_NUMERIC))
			fuseTime = compound.getShort(Constants.NBTKeys.FUSE);
		if(compound.contains(Constants.NBTKeys.EXPLOSION_RADIUS, net.minecraftforge.common.util.Constants.NBT.TAG_ANY_NUMERIC))
			explosionRadius = compound.getShort(Constants.NBTKeys.EXPLOSION_RADIUS);
		if(compound.getBoolean(Constants.NBTKeys.IGNITED))
			ignite();
	}

	@Override
	public void tick()
	{
		if(isAlive())
		{
			lastActiveTime = timeSinceIgnited;

			if(hasIgnited())
				setCreeperState(1);

			int i = getCreeperState();

			if(i > 0 && timeSinceIgnited == 0)
				playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1F, .5F);

			timeSinceIgnited += i;

			if(timeSinceIgnited < 0)
				timeSinceIgnited = 0;

			if(timeSinceIgnited >= fuseTime)
			{
				timeSinceIgnited = fuseTime;
				explode();
			}
		}

		super.tick();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_CREEPER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_CREEPER_DEATH;
	}

	@Override
	protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn)
	{
		super.dropSpecialItems(source, looting, recentlyHitIn);

		Entity entity = source.getTrueSource();

		if(entity instanceof ZombieCreeperEntity)
		{
			ZombieCreeperEntity creeper = (ZombieCreeperEntity) entity;

			if(creeper.ableToCauseSkullDrop())
			{
				creeper.incrementDroppedSkulls();
				entityDropItem(Items.CREEPER_HEAD); // TODO: Zombie creeper head?
			}
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		return true;
	}

	@Override
	public boolean isCharged()
	{
		return dataManager.get(POWERED);
	}

	@OnlyIn(Dist.CLIENT)
	public float getCreeperFlashIntensity(float partialTicks)
	{
		return MathHelper.lerp(partialTicks, (float) lastActiveTime, (float) timeSinceIgnited) / (float) (fuseTime - 2);
	}

	public int getCreeperState()
	{
		return dataManager.get(STATE);
	}

	public void setCreeperState(int state)
	{
		dataManager.set(STATE, state);
	}

	@Override
	public void func_241841_a(ServerWorld world, LightningBoltEntity lightningBolt)
	{
		super.func_241841_a(world, lightningBolt);
		dataManager.set(POWERED, true);
	}

	@Override
	protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		// TODO: Support other mod Flint And Steel items?
		// Would use tags for this or ToolType
		if(stack.getItem() == Items.FLINT_AND_STEEL)
		{
			world.playSound(player, getPosX(), getPosY(), getPosZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, getSoundCategory(), 1F, rand.nextFloat() * .4F + .8F);

			if(!world.isRemote())
			{
				ignite();
				stack.damageItem(1, player, $ -> player.sendBreakAnimation(hand));
			}

			return ActionResultType.func_233537_a_(world.isRemote());
		}
		else
			return super.func_230254_b_(player, hand);
	}

	private void explode()
	{
		if(!world.isRemote())
		{
			Explosion.Mode mode = ForgeEventFactory.getMobGriefingEvent(world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
			float f = isCharged() ? 2F : 1F;
			dead = true;
			world.createExplosion(this, getPosX(), getPosY(), getPosZ(), (float) explosionRadius * f, mode);
			remove();
			spawnLingeringCloud();
		}
	}

	private void spawnLingeringCloud()
	{
		Collection<EffectInstance> collection = getActivePotionEffects();

		if(!collection.isEmpty())
		{
			AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(world, getPosX(), getPosY(), getPosZ());
			areaEffectCloudEntity.setRadius(2.5F);
			areaEffectCloudEntity.setRadiusOnUse(-.5F);
			areaEffectCloudEntity.setWaitTime(10);
			areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
			areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
			collection.stream().map(EffectInstance::new).forEach(areaEffectCloudEntity::addEffect);
			world.addEntity(areaEffectCloudEntity);
		}
	}

	public boolean hasIgnited()
	{
		return dataManager.get(IGNITED);
	}

	public void ignite()
	{
		dataManager.set(IGNITED, true);
	}

	public boolean ableToCauseSkullDrop()
	{
		return isCharged() && droppedSkulls < 1;
	}

	public void incrementDroppedSkulls()
	{
		droppedSkulls++;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return null;
	}

	public static AttributeModifierMap.MutableAttribute bakeAttributes()
	{
		return ZombieEntity.func_234342_eQ_().createMutableAttribute(Attributes.MOVEMENT_SPEED, .25D);
	}
}
