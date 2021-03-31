package xyz.spgamers.forge.armageddon.entity.monster.zombie;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TieredItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.spgamers.forge.armageddon.Armageddon;
import xyz.spgamers.forge.armageddon.data.loot.EnchantSpecificWithLevels;
import xyz.spgamers.forge.armageddon.init.ModEnchantments;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ZombieHelper;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public final class FoxZombieEntity extends AbstractZombieEntity
{
	public static final DataParameter<Boolean> IS_INTERESTED = EntityDataManager.createKey(FoxZombieEntity.class, DataSerializers.BOOLEAN);

	private float interestedAngle;
	private float interestedAngle0;
	private int eatTicks;

	public FoxZombieEntity(World world)
	{
		super(ModEntities.FOX_ZOMBIE.get(), world, Armageddon.SERVER_CONFIG.animals::isFoxZombieEnabled);
	}

	@Override
	protected void registerData()
	{
		super.registerData();

		dataManager.register(IS_INTERESTED, false);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();

		goalSelector.addGoal(11, new FindItemsGoal(this));
	}

	public boolean isInterested()
	{
		return dataManager.get(IS_INTERESTED);
	}

	public void setInterested(boolean isInterested)
	{
		dataManager.set(IS_INTERESTED, isInterested);
	}

	@Override
	public void livingTick()
	{
		if(!world.isRemote() && isAlive() && isServerWorld())
		{
			eatTicks++;
			ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);

			if(canEatItem(stack))
			{
				if(eatTicks > 600)
				{
					ItemStack stack1 = stack.onItemUseFinish(world, this);

					if(!stack1.isEmpty())
						setItemStackToSlot(EquipmentSlotType.MAINHAND, stack1);

					eatTicks = 0;
				}
				else if(eatTicks > 560 && rand.nextFloat() < .1F)
				{
					playSound(getEatSound(stack), 1F, 1F);
					world.setEntityState(this, (byte) 45);
				}
			}

			LivingEntity attackTarget = getAttackTarget();

			if(attackTarget == null || !attackTarget.isAlive())
				setInterested(false);
		}

		super.livingTick();
	}

	@Override
	protected void spawnDrops(DamageSource damageSourceIn)
	{
		ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);

		if(!stack.isEmpty())
		{
			entityDropItem(stack);
			setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
		}

		super.spawnDrops(damageSourceIn);
	}

	@Override
	public void handleStatusUpdate(byte id)
	{
		if(id == 45)
		{
			ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);

			if(!stack.isEmpty())
			{
				for(int i = 0; i < 8; i++)
				{
					Vector3d vec = new Vector3d(
								((double)rand.nextFloat() - .5D) * .1D,
								Math.random() * .1D + .1D,
								0D
							).rotatePitch(-rotationPitch * ((float) Math.PI / 180F))
							.rotateYaw(-rotationYaw * ((float) Math.PI / 180F));

					world.addParticle(
							new ItemParticleData(ParticleTypes.ITEM, stack),
							getPosX() + getLookVec().x / 2D,
							getPosY(),
							getPosZ() + getLookVec().z / 2D,
							vec.x,
							vec.y + .05D,
							vec.z
					);
				}
			}
		}
		else
			super.handleStatusUpdate(id);
	}

	private boolean canEatItem(ItemStack stack)
	{
		return stack.isFood() && getAttackTarget() == null && onGround && !isSleeping();
	}

	@Override
	public void tick()
	{
		super.tick();

		setCanPickUpLoot(true);

		interestedAngle0 = interestedAngle;

		if(isInterested())
			interestedAngle += (1F - interestedAngle) * .4F;
		else
			interestedAngle += (0F - interestedAngle) * .4F;
	}

	@OnlyIn(Dist.CLIENT)
	public float getInterestedAngle(float partialTicks)
	{
		return MathHelper.lerp(partialTicks, interestedAngle0, interestedAngle) * .11F * (float) Math.PI;
	}

	@Override
	public SoundEvent getEatSound(ItemStack itemStackIn)
	{
		return SoundEvents.ENTITY_FOX_EAT;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_FOX_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.ENTITY_FOX_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_FOX_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
		// NOOP: getStepSound() is not nullable, this stops the sound being played
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
	{
		return isChild() ? sizeIn.height * .85F : .4F;
	}

	public boolean hasPoisonItem()
	{
		ItemStack stack = getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.POISON_ENCHANTMENT.get(), stack);
		return level > 0;
	}

	@Override
	protected void updateEquipmentIfNeeded(ItemEntity itemEntity)
	{
		ItemStack stack = itemEntity.getItem();

		if(func_233665_g_(stack))
		{
			Enchantment enchantment = ModEnchantments.POISON_ENCHANTMENT.get();

			// should probably find better way to detect tools & weapons
			if(enchantment.canApply(stack) && (stack.getItem() instanceof TieredItem || stack.getItem() == Items.BOOK))
				addEnchantmentOnce(stack, ModEnchantments.POISON_ENCHANTMENT.get(), 2);

			triggerItemPickupTrigger(itemEntity);
			setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
			inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 2F;
			onItemPickup(itemEntity, stack.getCount());
			itemEntity.remove();
			eatTicks = 0;
			return;
		}

		super.updateEquipmentIfNeeded(itemEntity);
	}

	public static AttributeModifierMap.MutableAttribute registerFoxZombieAttributes()
	{
		return ZombieHelper.registerZombieAttributes().createMutableAttribute(Attributes.MOVEMENT_SPEED, .3F).createMutableAttribute(Attributes.MAX_HEALTH, 10D).createMutableAttribute(Attributes.FOLLOW_RANGE, 32D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 2D);
	}

	public static boolean canFoxZombieSpawn(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random)
	{
		if(!Armageddon.SERVER_CONFIG.animals.isFoxZombieEnabled())
			return false;
		if(!ZombieHelper.canZombieSpawn(entityType, world, reason, pos, random))
			return false;
		// MonsterEntity does not extend AnimalEntity
		// return AnimalEntity.canAnimalSpawn(entityType, world, reason, pos, random);
		// code from AnimalEntity#canAnimalSpawn()
		return world.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
	}

	public static ItemStack addEnchantmentOnce(ItemStack stack, Enchantment enchantment, int level)
	{
		if(stack.getItem() == Items.BOOK)
			return EnchantSpecificWithLevels.addEnchantment(stack, enchantment, level);
		else if(stack.getItem() == Items.ENCHANTED_BOOK)
		{
			EnchantedBookItem.addEnchantment(stack, new EnchantmentData(enchantment, level));
			return stack;
		}
		else
		{
			// copy of code from EnchantedBookItem#addEnchantment()
			// modified for ItemStacks
			// stops item getting multiple of same enchantment
			// merges duplicates and keeps only largest level
			ListNBT enchantments = stack.getEnchantmentTagList();
			boolean flag = true;

			for(int i = 0; i < enchantments.size(); i++)
			{
				CompoundNBT enchantmentTag = enchantments.getCompound(i);
				ResourceLocation enchantmentName = ResourceLocation.tryCreate(enchantmentTag.getString("id"));

				if(enchantmentName != null && enchantmentName.equals(enchantment.getRegistryName()))
				{
					if(enchantmentTag.getInt("lvl") < level)
						enchantmentTag.putShort("lvl", (short) level);

					flag = false;
					break;
				}
			}

			if(flag)
			{
				CompoundNBT enchantmentTag = new CompoundNBT();
				enchantmentTag.putString("id", enchantment.getRegistryName().toString());
				enchantmentTag.putShort("lvl", (short) level);
				enchantments.add(enchantmentTag);
			}

			stack.setTagInfo("Enchantments", enchantments);
			return stack;
		}
	}

	public static final class FindItemsGoal extends Goal
	{
		private final FoxZombieEntity fox;

		private FindItemsGoal(FoxZombieEntity fox)
		{
			super();

			this.fox = fox;

			setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean shouldExecute()
		{
			if(!fox.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty())
			{
				fox.setInterested(false);
				return false;
			}
			else if(fox.getAttackTarget() == null || fox.getRevengeTarget() == null)
			{
				if(fox.getRNG().nextInt(10) != 0)
				{
					fox.setInterested(false);
					return false;
				}
				else
				{
					List<ItemEntity> items = fox.world.getEntitiesWithinAABB(ItemEntity.class, fox.getBoundingBox().grow(8D, 8D, 8D), item -> !item.cannotPickup() && item.isAlive());
					return !items.isEmpty() && fox.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty();
				}
			}
			else
			{
				fox.setInterested(false);
				return false;
			}
		}

		@Override
		public void tick()
		{
			List<ItemEntity> items = fox.world.getEntitiesWithinAABB(ItemEntity.class, fox.getBoundingBox().grow(8D, 8D, 8D), item -> !item.cannotPickup() && item.isAlive());
			ItemStack stack = fox.getItemStackFromSlot(EquipmentSlotType.MAINHAND);

			if(stack.isEmpty() && !items.isEmpty())
			{
				// fox.setInterested(true);
				fox.navigator.tryMoveToEntityLiving(items.get(0), 1.2F);
			}
		}

		@Override
		public void startExecuting()
		{
			List<ItemEntity> items = fox.world.getEntitiesWithinAABB(ItemEntity.class, fox.getBoundingBox().grow(8D, 8D, 8D), item -> !item.cannotPickup() && item.isAlive());

			if(!items.isEmpty())
			{
				fox.setInterested(true);
				fox.navigator.tryMoveToEntityLiving(items.get(0), 1.2F);
			}
		}
	}
}
