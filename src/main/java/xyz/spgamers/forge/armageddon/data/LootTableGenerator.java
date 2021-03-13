package xyz.spgamers.forge.armageddon.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.fml.RegistryObject;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.init.ModItems;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class LootTableGenerator extends ForgeLootTableProvider
{
	public LootTableGenerator(DataGenerator generator)
	{
		super(generator);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
	{
		return ImmutableList.of(
				Pair.of(Entities::new, LootParameterSets.ENTITY)
		);
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
	{
		map.forEach((location, lootTable) -> LootTableManager.validateLootTable(validationtracker, location, lootTable));
	}

	private static class Entities extends EntityLootTables
	{
		@Override
		protected void addTables()
		{
			registerLootTable(
					ModEntities.ZOMBIE_PIG.get(),
					LootTable.builder()
					         .addLootPool(
					         		LootPool.builder()
						                    .rolls(ConstantRange.of(1))
							                .addEntry(
							                		ItemLootEntry.builder(ModItems.ROTTEN_PORKCHOP::get)
									                             .acceptFunction(SetCount.builder(RandomValueRange.of(1F, 3F)))
									                             /*.acceptFunction(
									                             		Smelt.func_215953_b()
										                                     .acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, ON_FIRE))
									                             )*/
									                             .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0F, 1F)))
							                )
					         )
			);
		}

		@Override
		protected Iterable<EntityType<?>> getKnownEntities()
		{
			return ModEntities.ENTITIES.getEntries()
			                           .stream()
			                           .map(RegistryObject::get)
			                           .collect(ImmutableList.toImmutableList());
		}
	}
}
