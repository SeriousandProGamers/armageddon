package xyz.spg.armageddon.core.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public final class DeferredThrownEggItem extends Item
{
	private final LazyLoadedValue<EntityType<? extends ThrowableItemProjectile>> lazyThrownEggEntityType;

	public DeferredThrownEggItem(Supplier<EntityType<? extends ThrowableItemProjectile>> thrownEggEntityTypeSupplier, Properties properties)
	{
		super(properties);

		lazyThrownEggEntityType = new LazyLoadedValue<>(thrownEggEntityTypeSupplier);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		var stack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, .5F, .4F / (level.random.nextFloat() * .4F + .8F));

		if(!level.isClientSide)
		{
			var thrownEgg = lazyThrownEggEntityType.get().create(level);

			if(thrownEgg != null)
			{
				thrownEgg.setItem(stack);
				thrownEgg.setPos(player.getX(), player.getEyeY() - (double) .1F, player.getZ());
				thrownEgg.setOwner(player);
				thrownEgg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 1.5F, 1F);
				level.addFreshEntity(thrownEgg);
			}
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		if(!player.getAbilities().instabuild)
			stack.shrink(1);

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}
}
