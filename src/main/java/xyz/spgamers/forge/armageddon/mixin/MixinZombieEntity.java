package xyz.spgamers.forge.armageddon.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.AbstractZombieEntity;
import xyz.spgamers.forge.armageddon.entity.monster.zombie.ChickenZombieEntity;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ZombieHelper;

import java.util.List;

@Mixin(ZombieEntity.class)
public abstract class MixinZombieEntity extends MonsterEntity
{
	protected MixinZombieEntity(EntityType<? extends MonsterEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Inject(
			method = "onInitialSpawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/monster/ZombieEntity;setBreakDoorsAItask(Z)V"
			)
	)
	private void onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData spawnData, CompoundNBT dataTag, CallbackInfoReturnable<ILivingEntityData> cir)
	{
		Entity riding = getRidingEntity();

		if(riding instanceof ChickenEntity)
		{
			ChickenEntity chicken = (ChickenEntity) riding;
			ChickenZombieEntity zombie = ModEntities.CHICKEN_ZOMBIE.get().create(world.getWorld());

			if(zombie != null)
			{
				stopRiding();
				zombie.setLocationAndAngles(chicken.getPosX(), chicken.getPosY(), chicken.getPosZ(), chicken.rotationYaw, 0F);
				chicken.onInitialSpawn(world, difficulty, SpawnReason.JOCKEY, null, null);
				zombie.setChickenJockey(true);
				startRiding(zombie);
				world.addEntity(zombie);
				chicken.setChickenJockey(false);
				chicken.remove();
			}
		}

		if(riding == null && isChild())
		{
			if(spawnData instanceof ZombieEntity.GroupData && ((ZombieEntity.GroupData) spawnData).field_241400_b_)
			{
				if(world.getRandom().nextFloat() < .05D)
				{
					List<ChickenZombieEntity> list = world.getEntitiesWithinAABB(ChickenZombieEntity.class, getBoundingBox().grow(5D, 3D, 5D), EntityPredicates.IS_STANDALONE);

					if(list.isEmpty())
					{
						ChickenZombieEntity chicken = ModEntities.CHICKEN_ZOMBIE.get().create(world.getWorld());

						if(chicken != null)
						{
							chicken.setLocationAndAngles(getPosX(), getPosY(), getPosZ(), rotationYaw, 0F);
							chicken.onInitialSpawn(world, difficulty, SpawnReason.JOCKEY, null, null);
							chicken.setChickenJockey(true);
							startRiding(chicken);
							world.addEntity(chicken);
						}
					}
					else
					{
						ChickenZombieEntity chicken = list.get(0);
						chicken.setChickenJockey(true);
						startRiding(chicken);
					}
				}
			}
		}
	}

	@Inject(
			method = "registerGoals",
			at = @At("TAIL")
	)
	private void registerGoals(CallbackInfo ci)
	{
		EntityType<?> type = getType();

		boolean isChicken = type == ModEntities.CHICKEN_ZOMBIE.get();
		boolean isPig = type == ModEntities.PIG_ZOMBIE.get();
		boolean isCow = type == ModEntities.COW_ZOMBIE.get();
		boolean isSheep = type == ModEntities.SHEEP_ZOMBIE.get();
		boolean isFox = type == ModEntities.FOX_ZOMBIE.get();
		boolean isPanda = type == ModEntities.PANDA_ZOMBIE.get();
		boolean isPolarBear = type == ModEntities.POLAR_BEAR_ZOMBIE.get();
		boolean isRabbit = type == ModEntities.RABBIT_ZOMBIE.get();
		boolean isWolf = type == ModEntities.WOLF_ZOMBIE.get();
		boolean isHorse = type == EntityType.ZOMBIE_HORSE;

		boolean isZombie = type == EntityType.ZOMBIE;
		boolean isDrowned = type == EntityType.DROWNED;
		boolean isHusk = type == EntityType.HUSK;
		boolean isVillager = type == EntityType.ZOMBIE_VILLAGER;
		boolean isPiglin = type == EntityType.ZOMBIFIED_PIGLIN;

		boolean isHostile = isZombie || isDrowned || isHusk || isVillager || isPiglin;
		boolean isPassive = isChicken || isPig || isCow || isSheep || isFox || isPanda || isPolarBear || isRabbit || isWolf || isHorse;

		int chickenPriority = 2;
		int pigPriority = 2;
		int cowPriority = 2;
		int sheepPriority = 2;
		int foxPriority = 2;
		int pandaPriority = 2;
		int polarBearPriority = 2;
		int rabbitPriority = 2;
		int wolfPriority = 2;
		int horsePriority = 2;

		if(isHostile)
		{
			chickenPriority++;
			pigPriority++;
			cowPriority++;
			sheepPriority++;
			foxPriority++;
			pandaPriority++;
			polarBearPriority++;
			rabbitPriority++;
			wolfPriority++;
		}

		if(isChicken)
			chickenPriority--;
		if(isPig)
			pigPriority--;
		if(isCow)
			cowPriority--;
		if(isSheep)
			sheepPriority--;
		if(isFox)
			foxPriority--;
		if(isPanda)
			pandaPriority--;
		if(isPolarBear)
			polarBearPriority--;
		if(isRabbit)
			rabbitPriority--;
		if(isWolf)
			wolfPriority--;
		if(isHorse)
			horsePriority--;

		// babies go for chickens more as they like to ride around on them
		if(isChild() && ZombieHelper.isChickenJockeySupported(type))
			chickenPriority -= 2;

		// ensure priorities dont go below 1
		chickenPriority = Math.max(chickenPriority, 1);
		pigPriority = Math.max(pigPriority, 1);
		cowPriority = Math.max(cowPriority, 1);
		sheepPriority = Math.max(sheepPriority, 1);
		foxPriority = Math.max(foxPriority, 1);
		pandaPriority = Math.max(pandaPriority, 1);
		polarBearPriority = Math.max(polarBearPriority, 1);
		rabbitPriority = Math.max(rabbitPriority, 1);
		wolfPriority = Math.max(wolfPriority, 1);
		horsePriority = Math.max(horsePriority, 1);

		if(Armageddon.SERVER_CONFIG.animals.isChickenZombieEnabled())
			targetSelector.addGoal(chickenPriority, new NearestAttackableTargetGoal<>(this, ChickenEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isPigZombieEnabled())
			targetSelector.addGoal(pigPriority, new NearestAttackableTargetGoal<>(this, PigEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isCowZombieEnabled())
			targetSelector.addGoal(cowPriority, new NearestAttackableTargetGoal<>(this, CowEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isSheepZombieEnabled())
			targetSelector.addGoal(sheepPriority, new NearestAttackableTargetGoal<>(this, SheepEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isFoxZombieEnabled())
			targetSelector.addGoal(foxPriority, new NearestAttackableTargetGoal<>(this, FoxEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isPandaZombieEnabled())
			targetSelector.addGoal(pandaPriority, new NearestAttackableTargetGoal<>(this, PandaEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isPolarBearZombieEnabled())
			targetSelector.addGoal(polarBearPriority, new NearestAttackableTargetGoal<>(this, PolarBearEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isRabbitZombieEnabled())
			targetSelector.addGoal(rabbitPriority, new NearestAttackableTargetGoal<>(this, RabbitEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isWolfZombieEnabled())
			targetSelector.addGoal(wolfPriority, new NearestAttackableTargetGoal<>(this, WolfEntity.class, true));

		targetSelector.addGoal(horsePriority, new NearestAttackableTargetGoal<>(this, HorseEntity.class, true));
	}

	@Inject(
			method = "func_241847_a",
			at = @At("TAIL")
	)
	private void func_241847_a(ServerWorld world, LivingEntity originalEntity, CallbackInfo ci)
	{
		Difficulty difficulty = world.getDifficulty();

		if(!(originalEntity instanceof MobEntity))
			return;

		if(difficulty.getId() >= Difficulty.NORMAL.getId())
		{
			if(difficulty != Difficulty.HARD && rand.nextBoolean())
				return;

			MobEntity original = (MobEntity) originalEntity;
			MobEntity turned = ZombieHelper.createTurnedEntity(original);

			if(turned != null)
			{
				// this line only exists for the living drop events
				// this stops the original entity from dropping their loot
				original.setLastAttackedEntity(this);

				ILivingEntityData data = null;

				if(turned instanceof ZombieEntity)
					data = new ZombieEntity.GroupData(original.isChild(), false);
				else if(turned instanceof AgeableEntity)
					data = new AgeableEntity.AgeableData(original.isChild());

				turned.onInitialSpawn(
						world,
						world.getDifficultyForLocation(getPosition()),
						SpawnReason.CONVERSION,
						data,
						null
				);

				if(turned instanceof AbstractZombieEntity)
					((AbstractZombieEntity) turned).setupTurnedZombie(original);

				// turned chicken into zombie chicken
				if(turned.getType() == ModEntities.CHICKEN_ZOMBIE.get() && ZombieHelper.isChickenJockeySupported(getType()))
				{
					// zombie is not riding an entity
					// stops zombie from hoping from
					// chicken to chicken
					if(!isPassenger())
					{
						// if zombie is child and chicken is adult
						if(isChild() && !turned.isChild())
						{
							ChickenZombieEntity chicken = (ChickenZombieEntity) turned;
							chicken.setChickenJockey(true);
							startRiding(chicken);
						}
					}
				}

				if(!isSilent())
					world.playEvent(null, 1026, getPosition(), 0);
			}
		}
	}
}
