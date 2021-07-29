package xyz.spg.armageddon.core.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public final class PoisonEnchantment extends Enchantment
{
	public PoisonEnchantment()
	{
		super(Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND });
	}

	@Override
	public void doPostAttack(LivingEntity attacker, Entity attacked, int enchantmentLevel)
	{
		super.doPostAttack(attacker, attacked, enchantmentLevel);

		if(attacked instanceof LivingEntity living)
			living.addEffect(new MobEffectInstance(MobEffects.POISON, (enchantmentLevel + 1) * 25, 0), attacker);
	}

	@Override
	public int getMaxLevel()
	{
		return 20;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean isTreasureOnly()
	{
		return true;
	}

	@Override
	public boolean isTradeable()
	{
		return true;
	}

	@Override
	public boolean isDiscoverable()
	{
		return true;
	}

	@Override
	public boolean isAllowedOnBooks()
	{
		return true;
	}
}
