package xyz.spgamers.forge.armageddon.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
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
		if(Armageddon.SERVER_CONFIG.animals.isChickenZombieEnabled())
		{
			// child zombies have higher priority to go for chickens
			// players have priority of 2, child zombies prefer chickens and have priority of 1
			if(isChild())
				targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ChickenEntity.class, true));
			else
				targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ChickenEntity.class, true));
		}

		if(Armageddon.SERVER_CONFIG.animals.isPigZombieEnabled())
			targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PigEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isCowZombieEnabled())
			targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, CowEntity.class, true));
		if(Armageddon.SERVER_CONFIG.animals.isSheepZombieEnabled())
			targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SheepEntity.class, true));
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

				turned.onInitialSpawn(
						world,
						world.getDifficultyForLocation(getPosition()),
						SpawnReason.CONVERSION,
						new ZombieEntity.GroupData(original.isChild(), false),
						null
				);

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
