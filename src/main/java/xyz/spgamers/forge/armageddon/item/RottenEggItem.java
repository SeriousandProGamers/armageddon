package xyz.spgamers.forge.armageddon.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.WorldHelper;

public final class RottenEggItem extends Item
{
	public RottenEggItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, .5F, .4F / (random.nextFloat() * .4F + .8F));

		if(WorldHelper.isServerWorld(world))
		{
			EggEntity egg = ModEntities.ROTTEN_EGG.get().create(world);

			if(egg != null)
			{
				egg.setShooter(player);
				egg.setItem(stack);
				egg.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0F, 1.5F, 1F);
				world.addEntity(egg);
			}
		}

		player.addStat(Stats.ITEM_USED.get(this));

		if(!player.isCreative())
			stack.shrink(1);

		return ActionResult.func_233538_a_(stack, world.isRemote());
	}
}
