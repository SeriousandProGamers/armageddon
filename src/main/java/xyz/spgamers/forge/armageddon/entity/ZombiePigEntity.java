package xyz.spgamers.forge.armageddon.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ZombiePigEntity extends ZombieEntity
{
	public ZombiePigEntity(EntityType<? extends ZombieEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_PIG_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	public static AttributeModifierMap.MutableAttribute bakeAttributes()
	{
		return ZombieEntity.func_234342_eQ_().createMutableAttribute(Attributes.MAX_HEALTH, 20D);
	}
}
