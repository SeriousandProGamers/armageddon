package xyz.spg.armageddon.core.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.IForgeShearable;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.ALootTables;
import xyz.spg.armageddon.shared.NBTTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class SheepZombie extends AbstractZombie implements IForgeShearable
{
	private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(SheepZombie.class, EntityDataSerializers.BYTE);
	private static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), map -> {
		map.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
		map.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
		map.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
		map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
		map.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
		map.put(DyeColor.LIME, Blocks.LIME_WOOL);
		map.put(DyeColor.PINK, Blocks.PINK_WOOL);
		map.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
		map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
		map.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
		map.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
		map.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
		map.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
		map.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
		map.put(DyeColor.RED, Blocks.RED_WOOL);
		map.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
	});

	public SheepZombie(EntityType<? extends AbstractZombie> entityType, Level level)
	{
		super(entityType, level);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return createZombieAttributes();
	}

	public static boolean canSheepZombieSpawn(EntityType<? extends AbstractZombie> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random rng)
	{
		if(entityType != AEntityTypes.SHEEP_ZOMBIE)
			return false;
		if(!canZombieSpawn(entityType, level, spawnType, pos, rng))
			return false;
		return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) && level.getLightEmission(pos) > 8;
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(DATA_WOOL_ID, (byte) 0);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.SHEEP_AMBIENT;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return SoundEvents.SHEEP_HURT;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.SHEEP_DEATH;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag)
	{
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putBoolean(NBTTags.SHEARED, isSheared());
		compoundTag.putByte(NBTTags.COLOR, (byte) getColor().getId());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag)
	{
		super.readAdditionalSaveData(compoundTag);
		setSheared(compoundTag.getBoolean(NBTTags.SHEARED));
		setColor(DyeColor.byId(compoundTag.getByte(NBTTags.COLOR)));
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions)
	{
		return .95F * dimensions.height;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag compoundTag)
	{
		setColor(Sheep.getRandomSheepColor(level.getRandom()));
		setSheared(random.nextBoolean());
		return super.finalizeSpawn(level, difficultyInstance, spawnType, groupData, compoundTag);
	}

	@Override
	protected boolean isHumanoid()
	{
		return false;
	}

	@Override
	public boolean canBreakDoors()
	{
		return false;
	}

	@Override
	public boolean supportsBreakDoorGoal()
	{
		return false;
	}

	@Override
	protected boolean convertsInWater()
	{
		return false;
	}

	@Nullable
	@Override
	protected SoundEvent getStepSound()
	{
		return SoundEvents.SHEEP_STEP;
	}

	public boolean isSheared()
	{
		return (entityData.get(DATA_WOOL_ID) & 16) != 0;
	}

	public DyeColor getColor()
	{
		return DyeColor.byId(entityData.get(DATA_WOOL_ID) & 15);
	}

	public void setColor(DyeColor color)
	{
		byte b0 = entityData.get(DATA_WOOL_ID);
		entityData.set(DATA_WOOL_ID, (byte) (b0 & 240 | color.getId() & 15));
	}

	public void setSheared(boolean isSheared)
	{
		byte b0 = entityData.get(DATA_WOOL_ID);

		if(isSheared)
			entityData.set(DATA_WOOL_ID, (byte) (b0 | 16));
		else
			entityData.set(DATA_WOOL_ID, (byte) (b0 & -17));
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if(isAlive() && !isSheared())
		{
			ItemStack stack = player.getItemInHand(hand);

			if(stack.getItem() instanceof DyeItem dyeItem)
			{
				if(getColor() != dyeItem.getDyeColor())
				{
					level.playSound(player, this, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1F, 1F);

					if(!level.isClientSide)
					{
						setColor(dyeItem.getDyeColor());
						stack.shrink(1);
					}

					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}

			return InteractionResult.PASS;
		}

		return super.mobInteract(player, hand);
	}

	@Override
	protected ResourceLocation getDefaultLootTable()
	{
		if(isSheared())
			return AEntityTypes.SHEEP_ZOMBIE.getDefaultLootTable();
		else
		{
			return switch(getColor())
					{
						default -> ALootTables.ZOMBIE_SHEEP_WHITE;
						case ORANGE -> ALootTables.ZOMBIE_SHEEP_ORANGE;
						case MAGENTA -> ALootTables.ZOMBIE_SHEEP_MAGENTA;
						case LIGHT_BLUE -> ALootTables.ZOMBIE_SHEEP_LIGHT_BLUE;
						case YELLOW -> ALootTables.ZOMBIE_SHEEP_YELLOW;
						case LIME -> ALootTables.ZOMBIE_SHEEP_LIME;
						case PINK -> ALootTables.ZOMBIE_SHEEP_PINK;
						case GRAY -> ALootTables.ZOMBIE_SHEEP_GRAY;
						case LIGHT_GRAY -> ALootTables.ZOMBIE_SHEEP_LIGHT_GRAY;
						case CYAN -> ALootTables.ZOMBIE_SHEEP_CYAN;
						case PURPLE -> ALootTables.ZOMBIE_SHEEP_PURPLE;
						case BLUE -> ALootTables.ZOMBIE_SHEEP_BLUE;
						case BROWN -> ALootTables.ZOMBIE_SHEEP_BROWN;
						case GREEN -> ALootTables.ZOMBIE_SHEEP_GREEN;
						case RED -> ALootTables.ZOMBIE_SHEEP_RED;
						case BLACK -> ALootTables.ZOMBIE_SHEEP_BLACK;
					};
		}
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos)
	{
		return isAlive() && !isSheared() && !isBaby();
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune)
	{
		world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1F, 1F);

		if(!world.isClientSide)
		{
			setSheared(true);

			var i = 1 + random.nextInt(3);
			List<ItemStack> items = Lists.newArrayList();

			for(var j = 0; j < i; j++)
			{
				items.add(new ItemStack(ITEM_BY_DYE.get(getColor())));
			}

			return items;
		}
		return Collections.emptyList();
	}
}
