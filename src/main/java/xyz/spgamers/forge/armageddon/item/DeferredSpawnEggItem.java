package xyz.spgamers.forge.armageddon.item;

import com.google.common.collect.Lists;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public final class DeferredSpawnEggItem extends SpawnEggItem implements IItemColor
{
	private static final List<DeferredSpawnEggItem> UNADDED_EGGS = Lists.newArrayList();
	private final Lazy<? extends EntityType<?>> entityTypeSupplier;

	public DeferredSpawnEggItem(RegistryObject<? extends EntityType<?>> entityTypeSupplier, int primaryColor, int secondaryColor, Properties properties)
	{
		super(null, primaryColor, secondaryColor, properties);

		this.entityTypeSupplier = Lazy.of(entityTypeSupplier);
		UNADDED_EGGS.add(this);
	}

	public static void initUnaddedEggs()
	{
		Map<EntityType<?>, SpawnEggItem> eggs = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "EGGS");
		Validate.notNull(eggs);

		UNADDED_EGGS.forEach(egg -> {
			eggs.put(egg.getType(null), egg);
			DispenserBlock.registerDispenseBehavior(egg, SpawnEggDispenserBehavior.INSTANCE);
		});

		UNADDED_EGGS.clear();
	}

	@Override
	public EntityType<?> getType(@Nullable CompoundNBT nbt)
	{
		if(nbt != null && nbt.contains("EntityTag", 10))
		{
			CompoundNBT compoundnbt = nbt.getCompound("EntityTag");

			if(compoundnbt.contains("id", 8))
				return EntityType.byKey(compoundnbt.getString("id")).orElseGet(entityTypeSupplier);
		}

		return entityTypeSupplier.get();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack, int tintIndex)
	{
		return getColor(tintIndex);
	}

	public static final class SpawnEggDispenserBehavior extends DefaultDispenseItemBehavior
	{
		public static final SpawnEggDispenserBehavior INSTANCE = new SpawnEggDispenserBehavior();

		private SpawnEggDispenserBehavior() { }

		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			Direction direction = source.getBlockState().get(DispenserBlock.FACING);
			EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
			Entity entity = entityType.spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
			stack.shrink(1);
			return stack;
		}
	}
}
