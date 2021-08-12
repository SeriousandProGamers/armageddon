package xyz.spg.armageddon.core.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fmllegacy.RegistryObject;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.AItems;
import xyz.spg.armageddon.shared.ALootTables;
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
			// region: Pig Zombie
			add(AEntityTypes.PIG_ZOMBIE, zombieLootTable(AItems.ROTTEN_PORKCHOP, 3F));
			// endregion

			// region: Cow Zombie
			add(
					AEntityTypes.COW_ZOMBIE,
					zombieLootTable(AItems.ROTTEN_BEEF, 3F)
							.withPool(
									LootPool
											.lootPool()
											.setRolls(
													ConstantValue
															.exactly(
																	1F
															)
											)
											.add(
													LootItem
															.lootTableItem(
																	Items.LEATHER
															)
															.apply(
																	SetItemCountFunction
																			.setCount(
																					UniformGenerator
																							.between(
																									0F,
																									2F
																							)
																			)
															)
															.apply(
																	LootingEnchantFunction
																			.lootingMultiplier(
																					UniformGenerator
																							.between(
																									0F,
																									1F
																							)
																			)
															)
											)
							)
			);
			// endregion

			// region: Sheep Zombie
			add(AEntityTypes.SHEEP_ZOMBIE, zombieLootTable(AItems.ROTTEN_MUTTON, 2F));

			add(ALootTables.ZOMBIE_SHEEP_BLACK, createSheepTable(Blocks.BLACK_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_BLUE, createSheepTable(Blocks.BLUE_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_BROWN, createSheepTable(Blocks.BROWN_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_CYAN, createSheepTable(Blocks.CYAN_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_GRAY, createSheepTable(Blocks.GRAY_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_GREEN, createSheepTable(Blocks.GREEN_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_LIGHT_BLUE, createSheepTable(Blocks.LIGHT_BLUE_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_LIGHT_GRAY, createSheepTable(Blocks.LIGHT_GRAY_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_LIME, createSheepTable(Blocks.LIME_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_MAGENTA, createSheepTable(Blocks.MAGENTA_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_ORANGE, createSheepTable(Blocks.ORANGE_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_PINK, createSheepTable(Blocks.PINK_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_PURPLE, createSheepTable(Blocks.PURPLE_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_RED, createSheepTable(Blocks.RED_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_WHITE, createSheepTable(Blocks.WHITE_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			add(ALootTables.ZOMBIE_SHEEP_YELLOW, createSheepTable(Blocks.YELLOW_WOOL, AEntityTypes.SHEEP_ZOMBIE));
			// endregion

			// region: ChickenZombie
			add(
					AEntityTypes.CHICKEN_ZOMBIE,
					zombieLootTable(AItems.ROTTEN_CHICKEN, 1F)
							.withPool(
									LootPool
											.lootPool()
											.setRolls(
													ConstantValue
															.exactly(
																	1F
															)
											)
											.add(
													LootItem
															.lootTableItem(
																	Items.FEATHER
															)
															.apply(
																	SetItemCountFunction
																			.setCount(
																					UniformGenerator
																							.between(
																									0F,
																									2F
																							)
																			)
															)
															.apply(
																	LootingEnchantFunction
																			.lootingMultiplier(
																					UniformGenerator
																							.between(
																									0F,
																									1F
																							)
																			)
															)
											)
							)
			);
			// endregion

			// region: Rabbit Zombie
			add(
					AEntityTypes.RABBIT_ZOMBIE,
					zombieLootTable(AItems.ROTTEN_RABBIT, 1F)
							.withPool(
									LootPool
											.lootPool()
											.setRolls(
													ConstantValue
															.exactly(
																	1F
															)
											)
											.add(
													LootItem
															.lootTableItem(
																	Items.RABBIT_HIDE
															)
															.apply(
																	SetItemCountFunction
																			.setCount(
																					UniformGenerator
																							.between(
																									0F,
																									1F
																							)
																			)
															)
															.apply(
																	LootingEnchantFunction
																			.lootingMultiplier(
																					UniformGenerator
																							.between(
																									0F,
																									1F
																							)
																			)
															)
											)
							)
							.withPool(
									LootPool
											.lootPool()
											.setRolls(
													ConstantValue
															.exactly(
																	1F
															)
											)
											.add(
													LootItem
															.lootTableItem(
																	AItems.ROTTEN_RABBIT_FOOT
															)
											)
											.when(
													LootItemKilledByPlayerCondition
															.killedByPlayer()
											)
											.when(
													LootItemRandomChanceWithLootingCondition
															.randomChanceAndLootingBoost(
																	.1F,
																	.03F
															)
											)
							)
			);
			// endregion

			// this.add(EntityType.CREEPER, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.GUNPOWDER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))));
			// this.add(EntityType.ENDERMAN, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.ENDER_PEARL).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
			// this.add(EntityType.FOX, LootTable.lootTable());
			// this.add(EntityType.PANDA, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Blocks.BAMBOO).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
			// this.add(EntityType.POLAR_BEAR, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.COD).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).setWeight(3).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))).add(LootItem.lootTableItem(Items.SALMON).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
			// this.add(EntityType.WOLF, LootTable.lootTable());
		}

		@Override
		protected Iterable<EntityType<?>> getKnownEntities()
		{
			return Armageddon.ENTITIES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
		}

		// region: Helpers
		private LootTable.Builder zombieLootTable(ItemLike rottenItem, float maxRottenItem)
		{
			return LootTable
					.lootTable()
					.withPool(
							LootPool
									.lootPool()
									.setRolls(
											ConstantValue
													.exactly(
															1F
													)
									)
									.add(
											LootItem
													.lootTableItem(
															rottenItem
													)
													.apply(
															SetItemCountFunction
																	.setCount(
																			UniformGenerator
																					.between(
																							0F,
																							maxRottenItem
																					)
																	)
													)
													.apply(
															LootingEnchantFunction
																	.lootingMultiplier(
																			UniformGenerator
																					.between(
																							0F,
																							1F
																					)
																	)
													)
									)
					)
					.withPool(
							LootPool
									.lootPool()
									.setRolls(
											ConstantValue
													.exactly(
															1F
													)
									)
									.add(
											LootItem
													.lootTableItem(
															Items.IRON_INGOT
													)
									)
									.add(
											LootItem
													.lootTableItem(
															Items.CARROT
													)
									)
									.add(
											LootItem
													.lootTableItem(
															Items.POTATO
													)
													.apply(
															SmeltItemFunction
																	.smelted()
																	.when(
																			LootItemEntityPropertyCondition
																					.hasProperties(
																							LootContext.EntityTarget.THIS,
																							ENTITY_ON_FIRE
																					)
																	)
													)
									)
									.when(
											LootItemKilledByPlayerCondition
													.killedByPlayer()
									)
									.when(
											LootItemRandomChanceWithLootingCondition
													.randomChanceAndLootingBoost(
															.025F,
															.01F
													)
									)
					);
		}

		private LootTable.Builder createSheepTable(ItemLike wool, EntityType<?> sheepEntity)
		{
			return LootTable
					.lootTable()
					.withPool(
							LootPool
									.lootPool()
									.setRolls(
											ConstantValue
													.exactly(
															1F
													)
									)
									.add(
											LootItem
													.lootTableItem(
															wool
													)
									)
					)
					.withPool(
							LootPool
									.lootPool()
									.setRolls(
											ConstantValue
													.exactly(
															1F
													)
									)
									.add(
											LootTableReference
													.lootTableReference(
															sheepEntity.getDefaultLootTable()
													)
									)
					);
		}
		// endregion
	}
}
