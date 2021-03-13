package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.init.ModEntities;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
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

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag)
	{
		// need to ignore super to remove the chicken jockey spawning
		// Copy of MonsterEntity#onInitialSpawn
		getAttribute(Attributes.FOLLOW_RANGE).applyPersistentModifier(new AttributeModifier("Random spawn bonus", rand.nextGaussian() * .05D, AttributeModifier.Operation.MULTIPLY_BASE));

		if(rand.nextFloat() < .05F)
			setLeftHanded(true);
		else
			setLeftHanded(false);

		// Modified ZombieEntity#onInitialSpawn
		float f = difficulty.getClampedAdditionalDifficulty();
		setCanPickUpLoot(rand.nextFloat() < .55F * f);

		if(spawnData == null)
			spawnData = new GroupData(func_241399_a_(world.getRandom()), true);

		if(spawnData instanceof GroupData)
		{
			GroupData groupData = (GroupData) spawnData;

			if(groupData.isChild)
			{
				setChild(true);

				if(isChickenJockeyAllowed())
				{
					// TODO: Allow spawning on zombie chickens, when we add them
					if(world.getRandom().nextFloat() < .05D)
					{
						// List<ChickenEntity> list = world.getEntitiesWithinAABB(ChickenEntity.class, getBoundingBox().grow(5D, 3D, 5D), EntityPredicates.IS_STANDALONE);
						List<ChickenZombieEntity> list = world.getEntitiesWithinAABB(ChickenZombieEntity.class, getBoundingBox().grow(5D, 3D, 5D), EntityPredicates.IS_STANDALONE);

						if(!list.isEmpty())
						{
							// ChickenEntity chicken = list.get(0);
							ChickenZombieEntity chicken = list.get(0);
							chicken.setChickenJockey(true);
							startRiding(chicken);
						}
					}
					else if(world.getRandom().nextFloat() < .05D)
					{
						// ChickenEntity chicken = EntityType.CHICKEN.create(this.world);
						ChickenZombieEntity chicken = ModEntities.CHICKEN_ZOMBIE.get().create(this.world);

						if(chicken != null)
						{
							chicken.setLocationAndAngles(getPosX(), getPosY(), getPosZ(), rotationYaw, 0F);
							chicken.onInitialSpawn(world, difficulty, SpawnReason.JOCKEY, null, null);
							chicken.setChickenJockey(true);
							startRiding(chicken);
							world.addEntity(chicken);
						}
					}
				}
			}

			setBreakDoorsAItask(canBreakDoors() && rand.nextFloat() < f * .1F);
			setEquipmentBasedOnDifficulty(difficulty);
			setEnchantmentBasedOnDifficulty(difficulty);
		}

		if(getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty())
		{
			LocalDate data = LocalDate.now();
			int i = data.get(ChronoField.DAY_OF_MONTH);
			int j = data.get(ChronoField.MONTH_OF_YEAR);

			if(j == 10 && i == 31 && rand.nextFloat() < .25F)
			{
				setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(rand.nextFloat() < .1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
				inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0F;
			}
		}

		applyAttributeBonuses(f);
		return spawnData;
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();

		addCustomZombieGoals(this);
	}

	public boolean isChickenJockeyAllowed()
	{
		return true;
	}

	public boolean isEntityEnabled()
	{
		return entityEnabledSupplier.getAsBoolean();
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

	protected static AttributeModifierMap.MutableAttribute registerZombieAttributes()
	{
		return ZombieEntity.func_234342_eQ_();
	}

	protected static boolean canZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		return MonsterEntity.canMonsterSpawnInLight(entityType, world, reason, pos, random);
	}

	public static void addCustomZombieGoals(ZombieEntity zombie)
	{
		if(Armageddon.SERVER_CONFIG.animals.isChickenZombieEnabled())
		{
			// child zombies have higher priority to go for chickens
			// players have priority of 2, child zombies prefer chickens and have priority of 1
			if(zombie.isChild())
				zombie.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(zombie, ChickenEntity.class, true));
			else
				zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, ChickenEntity.class, true));
		}

		if(Armageddon.SERVER_CONFIG.animals.isPigZombieEnabled())
			zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, PigEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isCowZombieEnabled())
			zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, CowEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isSheepZombieEnabled())
			zombie.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, SheepEntity.class, true));
	}
}
