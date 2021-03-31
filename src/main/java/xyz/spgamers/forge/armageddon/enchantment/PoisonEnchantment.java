package xyz.spgamers.forge.armageddon.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public final class PoisonEnchantment extends Enchantment
{
	public PoisonEnchantment()
	{
		super(Rarity.COMMON, EnchantmentType.WEAPON, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND });
	}

	@Override
	public void onEntityDamaged(LivingEntity user, Entity target, int level)
	{
		if(target instanceof LivingEntity)
			((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.POISON, (level + 1) * 20, 0));
	}

	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	@Override
	public boolean isTreasureEnchantment()
	{
		return true;
	}

	@Override
	public boolean canGenerateInLoot()
	{
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean canVillagerTrade()
	{
		return false;
	}
}
