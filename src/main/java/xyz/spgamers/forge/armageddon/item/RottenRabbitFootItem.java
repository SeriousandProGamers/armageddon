package xyz.spgamers.forge.armageddon.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class RottenRabbitFootItem extends Item
{
	public static final String NBT_CURRENT_EFFECT = "CurrentEffect";
	public static final String NBT_PREVIOUS_EFFECT = "PreviousEffect";
	public static final String NBT_HOLD_TIME = "HoldTime";
	public static final String NBT_FOOT_DATA = "RabbitFootData";

	public static final String STATUS_TRANSLATION_KEY = "armageddon.rabbit_foot.effected";

	public RottenRabbitFootItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if(world.isRemote())
			return;

		if(entity instanceof LivingEntity)
		{
			LivingEntity living = (LivingEntity) entity;

			// load data off of item nbt
			Effect currentEffect = getEffect(stack, NBT_CURRENT_EFFECT);
			int holdTime = getHoldItem(stack);

			// no effect selected
			if(currentEffect == null)
			{
				// pick new random effect
				pickRandomEffect(world, living, stack);
				return;
			}

			// if currently selected effect is active
			// do nothing & pick new effect
			if(isEffectActive(living, currentEffect))
			{
				clearCurrentEffect(stack);
				return;
			}

			// spectators are not effected
			if(living.isSpectator())
				return;

			holdTime++;
			/*// increment timer
			if(isSelected)
				holdTime++;
			else
				holdTime = 0; // reset timer if not holding item*/

			// every 64 ticks
			if(holdTime >= 64)
			{
				holdTime = 0;

				// random chance to actually happen (25% chance)
				if(world.rand.nextInt(100) > 75)
				{
					// can effect be given
					if(living.canBeHitWithPotion())
					{
						// attempt to give effect
						if(living.addPotionEffect(new EffectInstance(currentEffect, 500, 0)))
						{
							if(living instanceof PlayerEntity)
								((PlayerEntity) living).sendStatusMessage(new TranslationTextComponent(STATUS_TRANSLATION_KEY, stack.getDisplayName(), currentEffect.getDisplayName()), true);

							// clear current effect, to get a new random effect
							clearCurrentEffect(stack);
						}
					}
				}
			}

			// update hold time nbt
			setHoldTime(stack, holdTime);
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		// only true for dev environments
		if(FMLLoader.getNaming().equals("mcp"))
		{
			Effect currentEffect = getEffect(stack, NBT_CURRENT_EFFECT);
			Effect previousEffect = getEffect(stack, NBT_PREVIOUS_EFFECT);
			int holdTime = getHoldItem(stack);

			ITextComponent currentEffectName = currentEffect == null ? new StringTextComponent("None") : currentEffect.getDisplayName();
			ITextComponent previousEffectName = previousEffect == null ? new StringTextComponent("None") : previousEffect.getDisplayName();

			tooltip.add(new StringTextComponent("Current Effect: ").append(currentEffectName));
			tooltip.add(new StringTextComponent("Previous Effect: ").append(previousEffectName));
			tooltip.add(new StringTextComponent("Hold Time: ").appendString("" + holdTime));
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}

	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		return false;
	}

	private static void pickRandomEffect(IWorld world, LivingEntity entity, ItemStack stack)
	{
		List<Effect> badEffects = getBadEffects(entity, stack);

		if(badEffects.isEmpty())
			clearCurrentEffect(stack);
		else
		{
			Effect newEffect = badEffects.get(world.getRandom().nextInt(badEffects.size()));
			setCurrentEffect(stack, newEffect);
		}
	}

	private static boolean isEffectActive(LivingEntity entity, Effect effect)
	{
		// seems to always return false, why is this?
		if(entity.isPotionActive(effect))
			return true;

		for(EffectInstance active : entity.getActivePotionEffects())
		{
			if(active.getDuration() > 0 && active.getPotion() == effect)
				return true;
		}

		return false;
	}

	private static List<Effect> getBadEffects(LivingEntity entity, ItemStack stack)
	{
		ImmutableList.Builder<Effect> builder = ImmutableList.builder();
		Effect previousEffect = getEffect(stack, NBT_PREVIOUS_EFFECT);

		for(Effect effect : ForgeRegistries.POTIONS.getValues())
		{
			if(effect.getEffectType() != EffectType.HARMFUL)
				continue;
			if(previousEffect != null && effect == previousEffect)
				continue;
			if(isEffectActive(entity, effect))
				continue;

			builder.add(effect);
		}

		return builder.build();
	}

	private static int getHoldItem(ItemStack stack)
	{
		CompoundNBT tagCompound = getDataTag(stack);

		if(tagCompound.contains("HoldTime", Constants.NBT.TAG_INT))
			return tagCompound.getInt("HoldTime");
		else
			return 0;
	}

	@Nullable
	private static Effect getEffect(ItemStack stack, String tagName)
	{
		CompoundNBT tagCompound = getDataTag(stack);

		if(tagCompound.contains(tagName, Constants.NBT.TAG_STRING))
		{
			ResourceLocation registryName = new ResourceLocation(tagCompound.getString(tagName));
			return ForgeRegistries.POTIONS.getValue(registryName);
		}
		else
			return null;
	}

	private static void setHoldTime(ItemStack stack, int holdTime)
	{
		CompoundNBT tagCompound = getDataTag(stack);
		tagCompound.putInt(NBT_HOLD_TIME, holdTime);
	}

	@SuppressWarnings("ConstantConditions") // ignore warnings about registry name being null, this is only the case during development (if people dont register their shit correctly)
	private static void setCurrentEffect(ItemStack stack, @Nullable Effect effect)
	{
		clearCurrentEffect(stack);
		CompoundNBT tagCompound = getDataTag(stack);

		if(effect != null)
			tagCompound.putString(NBT_CURRENT_EFFECT, effect.getRegistryName().toString());
	}

	private static void clearCurrentEffect(ItemStack stack)
	{
		CompoundNBT tagCompound = getDataTag(stack);

		if(tagCompound.contains(NBT_CURRENT_EFFECT, Constants.NBT.TAG_STRING))
		{
			tagCompound.putString(NBT_PREVIOUS_EFFECT, tagCompound.getString(NBT_CURRENT_EFFECT));
			tagCompound.remove(NBT_CURRENT_EFFECT);
		}
	}

	private static CompoundNBT getDataTag(ItemStack stack)
	{
		return stack.getOrCreateChildTag(NBT_FOOT_DATA);
	}
}
