package xyz.spg.armageddon.core.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.Validate;
import xyz.spg.armageddon.core.entity.goal.ZombieAttackGoal;
import xyz.spg.armageddon.core.entity.goal.ZombieAttackTurtleEggGoal;
import xyz.spg.armageddon.core.event.ModEventFactory;
import xyz.spg.armageddon.shared.ATags;
import xyz.spg.armageddon.shared.NBTTags;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class AbstractZombie extends Monster
{
	private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
	private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", .5D, AttributeModifier.Operation.MULTIPLY_BASE);

	private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AbstractZombie.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID = SynchedEntityData.defineId(AbstractZombie.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID = SynchedEntityData.defineId(AbstractZombie.class, EntityDataSerializers.BOOLEAN);

	public static final float ZOMBIE_LEADER_CHANCE = .05F;
	public static final int REINFORCEMENT_ATTEMPTS = 50;
	public static final int REINFORCEMENT_RANGE_MAX = 40;
	public static final int REINFORCEMENT_RANGE_MIN = 7;
	private static final float BREAK_DOOR_CHANCE = .1F;

	private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (difficulty) -> difficulty == Difficulty.HARD;

	private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE);
	private boolean canBreakDoors;
	private int inWaterTime;
	private int conversionTime;
	private final EntityType<? extends AbstractZombie> zombieType;

	public AbstractZombie(EntityType<? extends AbstractZombie> entityType, Level level)
	{
		super(entityType, level);

		zombieType = entityType;
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();

		goalSelector.addGoal(4, new ZombieAttackTurtleEggGoal(this, 1D, 3));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));

		addBehaviourGoals();
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();

		entityData.define(DATA_BABY_ID, false);
		entityData.define(DATA_SPECIAL_TYPE_ID, 0);
		entityData.define(DATA_DROWNED_CONVERSION_ID, false);
	}

	@Override
	public boolean isBaby()
	{
		return entityData.get(DATA_BABY_ID);
	}

	@Override
	protected int getExperienceReward(Player player)
	{
		if(isBaby())
			xpReward = (int) ((float) xpReward * 2.5F);

		return super.getExperienceReward(player);
	}

	@Override
	public void setBaby(boolean baby)
	{
		entityData.set(DATA_BABY_ID, baby);

		if(!level.isClientSide())
		{
			AttributeInstance attributeInstance = getAttribute(Attributes.MOVEMENT_SPEED);

			if(attributeInstance != null)
			{
				attributeInstance.removeModifier(SPEED_MODIFIER_BABY);

				if(baby)
					attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
			}
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor)
	{
		if(DATA_BABY_ID.equals(dataAccessor))
			refreshDimensions();

		super.onSyncedDataUpdated(dataAccessor);
	}

	@Override
	public void tick()
	{
		if(!level.isClientSide() && isAlive() && !isNoAi())
		{
			if(isUnderWaterConverting())
			{
				conversionTime--;

				if(conversionTime < 0 && ForgeEventFactory.canLivingConvert(this, zombieType, timer -> conversionTime = timer))
					doUnderWaterConversion();
			}
			else if(convertsInWater())
			{
				if(isEyeInFluid(FluidTags.WATER))
				{
					inWaterTime++;

					if(inWaterTime >= 600)
						startUnderWaterConversion(300);
				}
				else
					inWaterTime = -1;
			}
		}

		super.tick();
	}

	@Override
	public void aiStep()
	{
		if(isAlive())
		{
			var flag = isSunSensitive() && isSunBurnTick();

			if(flag)
			{
				ItemStack helmet = getItemBySlot(EquipmentSlot.HEAD);

				if(!helmet.isEmpty())
				{
					if(helmet.isDamageableItem())
					{
						helmet.setDamageValue(helmet.getDamageValue() + random.nextInt(2));

						if(helmet.getDamageValue() >= helmet.getMaxDamage())
						{
							broadcastBreakEvent(EquipmentSlot.HEAD);
							setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
						}
					}

					flag = false;
				}

				if(flag)
					setSecondsOnFire(8);
			}
		}

		super.aiStep();
	}

	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		if(!super.hurt(source, amount))
			return false;
		else if(level instanceof ServerLevel serverLevel)
		{
			var target = getTarget();

			if(target == null && source.getEntity() instanceof LivingEntity livingSource)
				target = livingSource;

			var i = Mth.floor(getX());
			var j = Mth.floor(getY());
			var k = Mth.floor(getZ());

			var reinforcementsAttribute = getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
			var summonChance = reinforcementsAttribute == null ? 0D : reinforcementsAttribute.getValue();
			var summonAidEvent = ModEventFactory.fireZombieSummonAid(this, level, i, j, k, target, summonChance);
			var eventResult = summonAidEvent.getResult();

			if(summonAidEvent.hasResult() && eventResult == Event.Result.DENY)
				return true;

			if((summonAidEvent.hasResult() && eventResult == Event.Result.ALLOW) || (target != null && level.getDifficulty() == Difficulty.HARD && (double) random.nextFloat() < summonChance && level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)))
			{
				AbstractZombie summonedAid = null;

				if(summonAidEvent.hasResult() && eventResult == Event.Result.ALLOW)
					summonedAid = summonAidEvent.getCustomSummonedAid();
				if(summonedAid == null)
					summonedAid = zombieType.create(level);

				if(summonedAid != null)
				{
					var summonedType = summonedAid.zombieType;
					var spawnPlacementType = SpawnPlacements.getPlacementType(summonedType);
					var pos = new BlockPos.MutableBlockPos();

					for(var l = 0; l < REINFORCEMENT_ATTEMPTS; l++)
					{
						var i1 = i + Mth.nextInt(random, REINFORCEMENT_RANGE_MIN, REINFORCEMENT_RANGE_MAX) * Mth.nextInt(random, -1, 1);
						var j1 = j + Mth.nextInt(random, REINFORCEMENT_RANGE_MIN, REINFORCEMENT_RANGE_MAX) * Mth.nextInt(random, -1, 1);
						var k1 = k + Mth.nextInt(random, REINFORCEMENT_RANGE_MIN, REINFORCEMENT_RANGE_MAX) * Mth.nextInt(random, -1, 1);

						pos.set(i1, j1, k1);

						if(NaturalSpawner.isSpawnPositionOk(spawnPlacementType, level, pos, summonedType) && SpawnPlacements.checkSpawnRules(summonedType, serverLevel, MobSpawnType.REINFORCEMENT, pos, level.random))
						{
							summonedAid.setPos(i1, j1, k1);

							if(!level.hasNearbyAlivePlayer(i1, j1, k1, 7D) && level.isUnobstructed(summonedAid) && level.noCollision(summonedAid) && !level.containsAnyLiquid(summonedAid.getBoundingBox()))
							{
								if(target != null)
									summonedAid.setTarget(target);

								summonedAid.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(summonedAid.blockPosition()), MobSpawnType.REINFORCEMENT, null, null);
								serverLevel.addFreshEntityWithPassengers(summonedAid);

								var summonedReinforcementsAttribute = summonedAid.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);

								if(reinforcementsAttribute != null)
									reinforcementsAttribute.addPermanentModifier(new AttributeModifier("Zombie reinforcement caller charge", -.05F, AttributeModifier.Operation.ADDITION));
								if(summonedReinforcementsAttribute != null)
									summonedReinforcementsAttribute.addPermanentModifier(new AttributeModifier("Zombie reinforcement callee charge", -.05F, AttributeModifier.Operation.ADDITION));

								break;
							}
						}
					}
				}
			}

			return true;
		}
		else
			return false;
	}

	@Override
	public boolean doHurtTarget(Entity entity)
	{
		var flag = super.doHurtTarget(entity);

		if(flag)
		{
			var f = level.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();

			if(getMainHandItem().isEmpty() && isOnFire() && random.nextFloat() < f * .3F)
				entity.setSecondsOnFire(2 * (int) f);
		}

		return flag;
	}

	@Override @Nullable
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ZOMBIE_AMBIENT;
	}

	@Override @Nullable
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ZOMBIE_HURT;
	}

	@Override @Nullable
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ZOMBIE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockState)
	{
		var stepSound = getStepSound();

		if(stepSound != null)
			playSound(stepSound, .15F, 1F);
		else
			super.playStepSound(pos, blockState);
	}

	@Override
	public MobType getMobType()
	{
		return MobType.UNDEAD;
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance)
	{
		super.populateDefaultEquipmentSlots(difficultyInstance);
		var difficultyFactor = level.getDifficulty() == Difficulty.HARD ? .05F : .01F;

		if(isHumanoid() && random.nextFloat() < difficultyFactor)
		{
			var i = random.nextInt(3);
			Item item;

			if(i == 0)
				item = Items.IRON_SWORD;
			else
				item = Items.IRON_SHOVEL;

			if(item != null)
				setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag)
	{
		super.addAdditionalSaveData(compoundTag);

		compoundTag.putBoolean(NBTTags.IS_BABY, isBaby());
		compoundTag.putBoolean(NBTTags.CAN_BREAK_DOORS, canBreakDoors());
		compoundTag.putInt(NBTTags.IN_WATER_TIME, isInWater() ? inWaterTime : -1);
		compoundTag.putInt(NBTTags.DROWNED_CONVERSION_TIMER, isUnderWaterConverting() ? conversionTime : -1);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag)
	{
		super.readAdditionalSaveData(compoundTag);

		setBaby(compoundTag.getBoolean(NBTTags.IS_BABY));
		setCanBreakDoors(compoundTag.getBoolean(NBTTags.CAN_BREAK_DOORS));
		inWaterTime = compoundTag.getInt(NBTTags.IN_WATER_TIME);

		if(compoundTag.contains(NBTTags.DROWNED_CONVERSION_TIMER, Constants.NBT.TAG_ANY_NUMERIC))
		{
			var timer = compoundTag.getInt(NBTTags.DROWNED_CONVERSION_TIMER);

			if(timer > -1)
				startUnderWaterConversion(timer);
		}
	}

	@Override
	public void killed(ServerLevel level, LivingEntity attacker)
	{
		super.killed(level, attacker);

		var difficulty = level.getDifficulty();

		if((difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) && attacker instanceof Villager villager && ForgeEventFactory.canLivingConvert(villager, EntityType.ZOMBIE_VILLAGER, timer -> { }))
		{
			if(difficulty == Difficulty.HARD && random.nextBoolean())
				return;
			if(!isHumanoid())
				return;

			var zombieVillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);

			if(zombieVillager != null)
			{
				zombieVillager.finalizeSpawn(level, level.getCurrentDifficultyAt(zombieVillager.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), null);
				zombieVillager.setVillagerData(villager.getVillagerData());
				zombieVillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE).getValue());
				zombieVillager.setTradeOffers(villager.getOffers().createTag());
				zombieVillager.setVillagerXp(villager.getVillagerXp());

				ForgeEventFactory.onLivingConvert(villager, zombieVillager);

				if(!isSilent())
					level.levelEvent(null, 1026, blockPosition(), 0);
			}
		}
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions)
	{
		return isBaby() ? .93F : 1.74F;
	}

	@Override
	public boolean canHoldItem(ItemStack stack)
	{
		if(!isHumanoid())
			return false;
		if(stack.is(Tags.Items.EGGS))
			return false;
		if(isBaby())
			return false;
		if(isPassenger())
			return false;
		return super.canHoldItem(stack);
	}

	@Override
	public boolean wantsToPickUp(ItemStack stack)
	{
		if(!isHumanoid())
			return false;
		if(stack.is(Items.GLOW_INK_SAC))
			return false;
		return super.wantsToPickUp(stack);
	}

	@Override
	public double getMyRidingOffset()
	{
		return isBaby() ? 0D : -.45D;
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int lootingLevel, boolean hurtByPlayer)
	{
		super.dropCustomDeathLoot(source, lootingLevel, hurtByPlayer);

		var entity = source.getEntity();

		if(entity instanceof Creeper creeper && creeper.canDropMobsSkull())
		{
			var skull = getSkull();

			if(!skull.isEmpty())
			{
				creeper.increaseDroppedSkulls();
				spawnAtLocation(skull);
			}
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compoundTag)
	{
		groupData = super.finalizeSpawn(level, difficultyInstance, spawnType, groupData, compoundTag);
		var f = difficultyInstance.getSpecialMultiplier();
		setCanPickUpLoot(random.nextFloat() < .55F * f);

		if(groupData == null)
			groupData = new Zombie.ZombieGroupData(getSpawnAsBabyOdds(random), true);

		if(groupData instanceof Zombie.ZombieGroupData zombieGroupData)
		{
			if(isHumanoid())
			{
				if(zombieGroupData.isBaby)
				{
					setBaby(true);

					if(zombieGroupData.canSpawnJockey)
					{
						if(level.getRandom().nextFloat() < .05D)
						{
							var list = level.getEntitiesOfClass(Chicken.class, getBoundingBox().inflate(5F, 3F, 5D), EntitySelector.ENTITY_NOT_BEING_RIDDEN);

							if(!list.isEmpty())
							{
								var chicken = list.get(0);
								chicken.setChickenJockey(true);
								startRiding(chicken);
							}
						}
						else if(level.getRandom().nextFloat() < .05D)
						{
							var chicken = EntityType.CHICKEN.create(this.level);

							if(chicken != null)
							{
								chicken.moveTo(getX(), getY(), getZ());
								chicken.finalizeSpawn(level, difficultyInstance, MobSpawnType.JOCKEY, null, null);
								chicken.setChickenJockey(true);
								startRiding(chicken);
								level.addFreshEntityWithPassengers(chicken);
							}
						}
					}
				}

				setCanBreakDoors(supportsBreakDoorGoal() && random.nextFloat() < f * BREAK_DOOR_CHANCE);
				populateDefaultEquipmentSlots(difficultyInstance);
				populateDefaultEquipmentEnchantments(difficultyInstance);
			}
		}

		if(getItemBySlot(EquipmentSlot.HEAD).isEmpty())
		{
			var date = LocalDate.now();
			var day = date.get(ChronoField.DAY_OF_MONTH);
			var month = date.get(ChronoField.MONTH_OF_YEAR);

			if(month == 10 && day == 31 && random.nextFloat() < .25F)
			{
				var pumpkinType = random.nextFloat() < .1F ? PUMPKIN_TYPE_JACK_O_LANTERN : PUMPKIN_TYPE_CARVED;
				var pumpkin = randomPumpkin(random, pumpkinType);
				setItemSlot(EquipmentSlot.HEAD, pumpkin.asItem().getDefaultInstance());
				armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0F;
			}
		}

		handAttributes(f);
		return groupData;
	}

	protected boolean isHumanoid()
	{
		return false;
	}

	protected void addBehaviourGoals()
	{
		goalSelector.addGoal(2, new ZombieAttackGoal(this, 1D, false));
		goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1D, true, 4, this::canBreakDoors));
		goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1D));
		targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(ZombifiedPiglin.class));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
		targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
	}

	public boolean isUnderWaterConverting()
	{
		return entityData.get(DATA_DROWNED_CONVERSION_ID);
	}

	public boolean canBreakDoors()
	{
		return isHumanoid() && canBreakDoors;
	}

	public void setCanBreakDoors(boolean canBreakDoors)
	{
		if(isHumanoid() && supportsBreakDoorGoal())
		{
			if(this.canBreakDoors != canBreakDoors)
			{
				this.canBreakDoors = canBreakDoors;
				((GroundPathNavigation) navigation).setCanOpenDoors(canBreakDoors);

				if(canBreakDoors)
					goalSelector.addGoal(1, breakDoorGoal);
				else
					goalSelector.removeGoal(breakDoorGoal);
			}
		}
		else if(this.canBreakDoors)
		{
			goalSelector.removeGoal(breakDoorGoal);
			this.canBreakDoors = false;
		}
	}

	public boolean supportsBreakDoorGoal()
	{
		return isHumanoid();
	}

	protected boolean convertsInWater()
	{
		return getZombieConversionType() != null;
	}

	private void startUnderWaterConversion(int conversionTime)
	{
		this.conversionTime = conversionTime;
		entityData.set(DATA_DROWNED_CONVERSION_ID, true);
	}

	protected void doUnderWaterConversion()
	{
		var conversionType = getZombieConversionType();
		Validate.notNull(conversionType); // should never be null if we can get here
		convertToZombieType(conversionType);

		if(!isSilent())
			level.levelEvent(null, 1040, blockPosition(), 0);
	}

	@Nullable
	protected EntityType<? extends AbstractZombie> getZombieConversionType()
	{
		return null;
	}

	protected void convertToZombieType(EntityType<? extends AbstractZombie> conversionType)
	{
		var zombie = convertTo(conversionType, true);

		if(zombie != null)
		{
			zombie.handAttributes(zombie.level.getCurrentDifficultyAt(zombie.blockPosition()).getSpecialMultiplier());

			if(zombie.isHumanoid() && zombie.supportsBreakDoorGoal())
				zombie.setCanBreakDoors(canBreakDoors());

			ForgeEventFactory.onLivingConvert(this, zombie);
		}
	}

	protected boolean isSunSensitive()
	{
		return true;
	}

	@Nullable
	protected SoundEvent getStepSound()
	{
		return SoundEvents.ZOMBIE_STEP;
	}

	protected void handAttributes(float f)
	{
		randomizeReinforcementsChance();

		var knockback = getAttribute(Attributes.ATTACK_KNOCKBACK);
		var followRange = getAttribute(Attributes.FOLLOW_RANGE);
		var reinforcements = getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
		var maxHealth = getAttribute(Attributes.MAX_HEALTH);

		var d0 = random.nextDouble() * 1.5D * f;

		if(knockback != null)
			knockback.addPermanentModifier(new AttributeModifier("Random spawn bonus", random.nextDouble() * .05F, AttributeModifier.Operation.ADDITION));
		if(d0 > 1D && followRange != null)
			followRange.addPermanentModifier(new AttributeModifier("Random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));

		if(random.nextFloat() < f * ZOMBIE_LEADER_CHANCE)
		{
			if(reinforcements != null)
				reinforcements.addPermanentModifier(new AttributeModifier("Leader zombie bonus", random.nextDouble() * .25D + .5D, AttributeModifier.Operation.ADDITION));
			if(maxHealth != null)
				maxHealth.addPermanentModifier(new AttributeModifier("Leader zombie bonus", random.nextDouble() * 3D + 1D, AttributeModifier.Operation.MULTIPLY_TOTAL));

			if(isHumanoid())
				setCanBreakDoors(supportsBreakDoorGoal());
		}
	}

	protected void randomizeReinforcementsChance()
	{
		AttributeInstance reinforcements = getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);

		if(reinforcements != null)
			reinforcements.setBaseValue(random.nextDouble() * ForgeConfig.SERVER.zombieBaseSummonChance.get());
	}

	protected ItemStack getSkull()
	{
		return ItemStack.EMPTY;
	}

	public void finalizeZombieTypeConversion(Mob original)
	{
	}

	protected static AttributeSupplier.Builder createZombieAttributes()
	{
		return Monster.createMonsterAttributes()
		              .add(Attributes.FOLLOW_RANGE, 35D)
		              .add(Attributes.MOVEMENT_SPEED, .23F)
		              .add(Attributes.ATTACK_DAMAGE, 3D)
		              .add(Attributes.ARMOR, 2D)
		              .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}

	public static boolean getSpawnAsBabyOdds(Random rng)
	{
		return rng.nextFloat() < ForgeConfig.SERVER.zombieBabyChance.get();
	}

	public static boolean canZombieSpawn(EntityType<? extends AbstractZombie> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random rng)
	{
		return Monster.checkMonsterSpawnRules(entityType, level, spawnType, pos, rng);
	}

	// TODO: Find better place for this
	public static final int PUMPKIN_TYPE_ANY = -1;
	public static final int PUMPKIN_TYPE_PLAIN = 0;
	public static final int PUMPKIN_TYPE_CARVED = 1;
	public static final int PUMPKIN_TYPE_JACK_O_LANTERN = 2;

	private static final List<Block> PUMPKINS = ImmutableList.of(Blocks.PUMPKIN, Blocks.CARVED_PUMPKIN, Blocks.JACK_O_LANTERN);

	// pumpkinType [ -1: any, 0: plain, 1: carved, 2: jack-o-lantern ]
	public static Block randomPumpkin(Random rng, int pumpkinType)
	{
		pumpkinType = Mth.clamp(pumpkinType, -1, 2);
		Tag<Block> tag;

		switch(pumpkinType)
		{
			default -> tag = ATags.Blocks.PUMPKINS;
			case PUMPKIN_TYPE_PLAIN -> tag = ATags.Blocks.PUMPKINS_PLAIN;
			case PUMPKIN_TYPE_CARVED -> tag = ATags.Blocks.PUMPKINS_CARVED;
			case PUMPKIN_TYPE_JACK_O_LANTERN -> tag = ATags.Blocks.PUMPKINS_JACK_O_LANTERN;
		}

		if(tag.getValues().isEmpty())
		{
			return switch(pumpkinType)
					{
						default -> PUMPKINS.get(rng.nextInt(PUMPKINS.size()));
						case PUMPKIN_TYPE_PLAIN -> Blocks.PUMPKIN;
						case PUMPKIN_TYPE_CARVED -> Blocks.CARVED_PUMPKIN;
						case PUMPKIN_TYPE_JACK_O_LANTERN -> Blocks.JACK_O_LANTERN;
					};
		}
		else
			return tag.getRandomElement(rng);
	}
}
