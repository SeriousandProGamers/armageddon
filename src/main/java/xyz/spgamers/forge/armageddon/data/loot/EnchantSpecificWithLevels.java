package xyz.spgamers.forge.armageddon.data.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.init.ModLootFunctionTypes;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public final class EnchantSpecificWithLevels extends LootFunction
{
	private final LazyValue<Enchantment> enchantment;
	@Nullable private final IRandomRange randomRange;

	protected EnchantSpecificWithLevels(ILootCondition[] conditions, Builder builder)
	{
		super(conditions);

		enchantment = new LazyValue<>(builder.enchantmentSupplier);
		this.randomRange = builder.randomRange;
	}

	@Override
	protected ItemStack doApply(ItemStack stack, LootContext context)
	{
		Random rand = context.getRandom();
		Enchantment enchantment = this.enchantment.getValue();

		int level;

		if(randomRange == null)
			level = MathHelper.nextInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
		else
			level = randomRange.generateInt(rand);

		return addEnchantment(stack, enchantment, level);
	}

	@Override
	public LootFunctionType getFunctionType()
	{
		return ModLootFunctionTypes.ENCHANT_SPECIFIC_WITH_LEVELS;
	}

	public static Builder builder(Supplier<Enchantment> enchantmentSupplier)
	{
		return new Builder(enchantmentSupplier);
	}

	public static ItemStack addEnchantment(ItemStack stack, Enchantment enchantment, int level)
	{
		boolean flag = stack.getItem() == Items.BOOK;

		if(flag)
		{
			stack = new ItemStack(Items.ENCHANTED_BOOK);
			EnchantedBookItem.addEnchantment(stack, new EnchantmentData(enchantment, level));
		}
		else
			stack.addEnchantment(enchantment, level);

		return stack;
	}

	public static final class Builder extends LootFunction.Builder<EnchantSpecificWithLevels.Builder>
	{
		private final Supplier<Enchantment> enchantmentSupplier;

		private IRandomRange randomRange;

		public Builder(Supplier<Enchantment> enchantmentSupplier)
		{
			this.enchantmentSupplier = enchantmentSupplier;
		}

		public Builder setRandomRange(IRandomRange randomRange)
		{
			this.randomRange = randomRange;
			return this;
		}

		@Override
		protected Builder doCast()
		{
			return this;
		}

		@Override
		public EnchantSpecificWithLevels build()
		{
			return new EnchantSpecificWithLevels(getConditions(), this);
		}
	}

	public static final class Serializer extends LootFunction.Serializer<EnchantSpecificWithLevels>
	{
		@Override
		public void serialize(JsonObject json, EnchantSpecificWithLevels loot, JsonSerializationContext ctx)
		{
			super.serialize(json, loot, ctx);

			json.addProperty("enchantment", loot.enchantment.getValue().getRegistryName().toString());

			if(loot.randomRange != null)
				json.add("levels", RandomRanges.serialize(loot.randomRange, ctx));
		}

		@Override
		public EnchantSpecificWithLevels deserialize(JsonObject json, JsonDeserializationContext ctx, ILootCondition[] conditions)
		{
			String enchantmentName = JSONUtils.getString(json, "enchantment");
			Builder builder = builder(() -> ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantmentName)));

			if(json.has("levels"))
				builder.setRandomRange(RandomRanges.deserialize(json, ctx));

			return builder.build();
		}
	}
}
