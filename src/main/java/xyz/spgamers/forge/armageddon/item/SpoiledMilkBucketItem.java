package xyz.spgamers.forge.armageddon.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import xyz.spgamers.forge.armageddon.util.WorldHelper;

import javax.annotation.Nullable;

public final class SpoiledMilkBucketItem extends Item
{
	public SpoiledMilkBucketItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving)
	{
		if(WorldHelper.isServerWorld(world))
		{
			for(Pair<EffectInstance, Float> pair : Foods.ROTTEN_FLESH.getEffects())
			{
				if(pair.getFirst() != null && world.rand.nextFloat() < pair.getSecond())
					entityLiving.addPotionEffect(new EffectInstance(pair.getFirst()));
			}

			return stack;
		}

		if(entityLiving instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
			CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
			player.addStat(Stats.ITEM_USED.get(this));
		}

		if(entityLiving instanceof PlayerEntity && !((PlayerEntity) entityLiving).isCreative())
			stack.shrink(1);

		return stack.isEmpty() ? getContainerItem(stack) : stack;
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack)
	{
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		return DrinkHelper.startDrinking(world, player, hand);
	}

	// if some other mod implements spoiled milk fluid
	// let them use this item for that fluid
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		return new FluidBucketWrapper(stack);
	}
}
