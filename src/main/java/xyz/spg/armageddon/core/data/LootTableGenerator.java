package xyz.spg.armageddon.core.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.fmllegacy.RegistryObject;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.Armageddon;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class LootTableGenerator extends LootTableProvider
{
	public LootTableGenerator(DataGenerator generator)
	{
		super(generator);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
	{
		return ImmutableList.of(Pair.of(EntityTypes::new, LootContextParamSets.ENTITY));
	}

	@Override
	public String getName()
	{
		return "Armageddon-LootTable-Generator";
	}

	@Override protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) { }

	public static final class EntityTypes extends EntityLoot
	{
		@Override
		protected void addTables()
		{
			add(AEntityTypes.PIG_ZOMBIE, LootTable.lootTable());

			// this.add(EntityType.ZOMBIE, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.IRON_INGOT)).add(LootItem.lootTableItem(Items.CARROT)).add(LootItem.lootTableItem(Items.POTATO).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))).when(LootItemKilledByPlayerCondition.killedByPlayer()).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));this.add(EntityType.COW, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BEEF).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
			// this.add(EntityType.CREEPER, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			// this.add(EntityType.ENDERMAN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.ENDER_PEARL).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
			// this.add(EntityType.FOX, LootTable.lootTable());
			// this.add(EntityType.PANDA, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Blocks.BAMBOO).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
			// this.add(EntityType.PIG, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.PORKCHOP).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
			// this.add(EntityType.POLAR_BEAR, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COD).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))).add(LootItem.lootTableItem(Items.SALMON).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
			// this.add(EntityType.RABBIT, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.RABBIT_HIDE).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.RABBIT).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.RABBIT_FOOT)).when(LootItemKilledByPlayerCondition.killedByPlayer()).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1F, 0.03F))));
			// this.add(EntityType.SHEEP, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.MUTTON).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
			// this.add(BuiltInLootTables.SHEEP_BLACK, createSheepTable(Blocks.BLACK_WOOL));
			// this.add(BuiltInLootTables.SHEEP_BLUE, createSheepTable(Blocks.BLUE_WOOL));
			// this.add(BuiltInLootTables.SHEEP_BROWN, createSheepTable(Blocks.BROWN_WOOL));
			// this.add(BuiltInLootTables.SHEEP_CYAN, createSheepTable(Blocks.CYAN_WOOL));
			// this.add(BuiltInLootTables.SHEEP_GRAY, createSheepTable(Blocks.GRAY_WOOL));
			// this.add(BuiltInLootTables.SHEEP_GREEN, createSheepTable(Blocks.GREEN_WOOL));
			// this.add(BuiltInLootTables.SHEEP_LIGHT_BLUE, createSheepTable(Blocks.LIGHT_BLUE_WOOL));
			// this.add(BuiltInLootTables.SHEEP_LIGHT_GRAY, createSheepTable(Blocks.LIGHT_GRAY_WOOL));
			// this.add(BuiltInLootTables.SHEEP_LIME, createSheepTable(Blocks.LIME_WOOL));
			// this.add(BuiltInLootTables.SHEEP_MAGENTA, createSheepTable(Blocks.MAGENTA_WOOL));
			// this.add(BuiltInLootTables.SHEEP_ORANGE, createSheepTable(Blocks.ORANGE_WOOL));
			// this.add(BuiltInLootTables.SHEEP_PINK, createSheepTable(Blocks.PINK_WOOL));
			// this.add(BuiltInLootTables.SHEEP_PURPLE, createSheepTable(Blocks.PURPLE_WOOL));
			// this.add(BuiltInLootTables.SHEEP_RED, createSheepTable(Blocks.RED_WOOL));
			// this.add(BuiltInLootTables.SHEEP_WHITE, createSheepTable(Blocks.WHITE_WOOL));
			// this.add(BuiltInLootTables.SHEEP_YELLOW, createSheepTable(Blocks.YELLOW_WOOL));
			// this.add(EntityType.WOLF, LootTable.lootTable());
			// this.add(EntityType.ZOMBIE, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.IRON_INGOT)).add(LootItem.lootTableItem(Items.CARROT)).add(LootItem.lootTableItem(Items.POTATO).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))).when(LootItemKilledByPlayerCondition.killedByPlayer()).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))));
		}

		@Override
		protected Iterable<EntityType<?>> getKnownEntities()
		{
			return Armageddon.ENTITIES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
		}
	}
}
