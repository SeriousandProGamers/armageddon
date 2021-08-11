package xyz.spg.armageddon.core.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.OnlyIns;
import net.minecraftforge.common.util.Constants;
import xyz.spg.armageddon.shared.NBTTags;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@OnlyIns(
		value = { @OnlyIn(value = Dist.CLIENT, _interface = ItemColor.class) }
)
public final class DeferredSpawnEggItem extends SpawnEggItem implements ItemColor
{
	private final LazyLoadedValue<EntityType<? extends Mob>> lazyEntityType;

	public DeferredSpawnEggItem(Supplier<EntityType<? extends Mob>> entityType, int primaryColor, int secondaryColor, Properties properties)
	{
		super(null, primaryColor, secondaryColor, properties);

		lazyEntityType = new LazyLoadedValue<>(entityType);
	}

	@Override
	public EntityType<?> getType(@Nullable CompoundTag tag)
	{
		if(tag != null && tag.contains(NBTTags.ENTITY_TAG, Constants.NBT.TAG_COMPOUND))
		{
			var compoundTag = tag.getCompound(NBTTags.ENTITY_TAG);

			if(compoundTag.contains(NBTTags.ID, Constants.NBT.TAG_STRING))
				return EntityType.byString(compoundTag.getString(NBTTags.ID)).orElseGet(lazyEntityType::get);
		}

		return lazyEntityType.get();
	}

	@Override @OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack, int tintIndex)
	{
		return getColor(tintIndex);
	}
}
