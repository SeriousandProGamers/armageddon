package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.EntityEnumDataHelper;
import xyz.spgamers.forge.armageddon.util.ModConstants;
import xyz.spgamers.forge.armageddon.util.ZombieHelper;

import java.util.Random;

public final class PandaZombieEntity extends AbstractZombieEntity
{
	public static final EntityEnumDataHelper<PandaEntity.Gene, PandaZombieEntity> MAIN_GENE = EntityEnumDataHelper.create(ModConstants.NBT.MAIN_GENE, PandaEntity.Gene.class, PandaZombieEntity.class);
	public static final EntityEnumDataHelper<PandaEntity.Gene, PandaZombieEntity> HIDDEN_GENE = EntityEnumDataHelper.create(ModConstants.NBT.HIDDEN_GENE, PandaEntity.Gene.class, PandaZombieEntity.class);

	public PandaZombieEntity(World world)
	{
		super(ModEntities.PANDA_ZOMBIE.get(), world, Armageddon.SERVER_CONFIG.animals::isPandaZombieEnabled);
	}

	public PandaEntity.Gene getMainGene()
	{
		return MAIN_GENE.getValue(this);
	}

	public void setMainGene(PandaEntity.Gene pandaType)
	{
		MAIN_GENE.setValue(this, pandaType);
	}

	public PandaEntity.Gene getHiddenGene()
	{
		return HIDDEN_GENE.getValue(this);
	}

	public void setHiddenGene(PandaEntity.Gene pandaType)
	{
		HIDDEN_GENE.setValue(this, pandaType);
	}

	@Override
	public void setupTurnedZombie(MobEntity originalEntity)
	{
		if(originalEntity instanceof PandaEntity)
		{
			PandaEntity panda = (PandaEntity) originalEntity;
			PandaEntity.Gene main = panda.getMainGene();
			PandaEntity.Gene hidden = panda.getHiddenGene();
			setMainGene(main);
			setHiddenGene(hidden);
		}
	}

	@Override
	protected void registerData()
	{
		super.registerData();

		MAIN_GENE.registerDataKey(this);
		HIDDEN_GENE.registerDataKey(this);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		MAIN_GENE.writeToEntityNBT(this, compound);
		HIDDEN_GENE.writeToEntityNBT(this, compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		MAIN_GENE.readFromEntityNBT(this, compound);
		HIDDEN_GENE.readFromEntityNBT(this, compound);
	}

	public PandaEntity.Gene func_213590_ei()
	{
		// func_221101_b is private
		// return PandaEntity.Gene.func_221101_b()

		PandaEntity.Gene main = getMainGene();

		if(main.func_221107_c())
			return main == getHiddenGene() ? main : PandaEntity.Gene.NORMAL;
		else
			return main;
	}

	public boolean isLazy()
	{
		return func_213590_ei() == PandaEntity.Gene.LAZY;
	}

	public boolean isWorried()
	{
		return func_213590_ei() == PandaEntity.Gene.WORRIED;
	}

	public boolean isPlayful()
	{
		return func_213590_ei() == PandaEntity.Gene.PLAYFUL;
	}

	public boolean isWeak()
	{
		return func_213590_ei() == PandaEntity.Gene.WEAK;
	}

	public boolean isAggressive()
	{
		return func_213590_ei() == PandaEntity.Gene.AGGRESSIVE;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_PANDA_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ENTITY_PANDA_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PANDA_DEATH;
	}

	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.ENTITY_PANDA_STEP;
	}

	public static AttributeModifierMap.MutableAttribute registerPandaZombieAttributes()
	{
		return ZombieHelper.registerZombieAttributes();
	}

	public static boolean canPandaZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.animals.isPandaZombieEnabled())
			return false;
		if(!ZombieHelper.canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}
}
