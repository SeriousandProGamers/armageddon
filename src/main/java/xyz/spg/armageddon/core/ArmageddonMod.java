package xyz.spg.armageddon.core;

import com.google.common.base.Functions;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.commons.lang3.tuple.Pair;
import xyz.spg.armageddon.core.client.ClientSetup;
import xyz.spg.armageddon.core.data.*;
import xyz.spg.armageddon.core.enchantment.PoisonEnchantment;
import xyz.spg.armageddon.core.entity.PigZombie;
import xyz.spg.armageddon.core.item.DeferredSpawnEggItem;
import xyz.spg.armageddon.shared.AEnchantments;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.AItems;
import xyz.spg.armageddon.shared.Armageddon;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(Armageddon.ID_MOD)
public final class ArmageddonMod
{
	public static final CreativeTab CREATIVE_TAB = new CreativeTab();
	private static Function<Item.Properties, Item.Properties> DEFAULT_ITEM_PROPERTIES = properties -> properties.tab(CREATIVE_TAB);

	public ArmageddonMod()
	{
		var bus = FMLJavaModLoadingContext.get().getModEventBus();

		Armageddon.register(bus);

		bus.addListener(this::onCommonSetup);
		bus.addListener(this::onGatherData);
		bus.addListener(this::onCreateEntityAttribute);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);
	}

	private void onCommonSetup(FMLCommonSetupEvent event)
	{
		event.enqueueWork(this::setupEntities);
	}

	private void onGatherData(GatherDataEvent event)
	{
		var generator = event.getGenerator();
		var fileHelper = event.getExistingFileHelper();

		if(event.includeClient())
		{
			generator.addProvider(new LanguageGenerator(generator));
			generator.addProvider(new ItemModelGenerator(generator, fileHelper));
			generator.addProvider(new RomanNumeralGenerator(generator));
		}

		if(event.includeServer())
		{
			generator.addProvider(new LootTableGenerator(generator));
			generator.addProvider(new EntityTypeTagGenerator(generator, fileHelper));

			var blockTags = new BlockTagGenerator(generator, fileHelper);
			generator.addProvider(blockTags);
			generator.addProvider(new ItemTagGenerator(generator, blockTags, fileHelper));
		}
	}

	private void onCreateEntityAttribute(EntityAttributeCreationEvent event)
	{
		event.put(AEntityTypes.PIG_ZOMBIE, PigZombie.createAttributes().build());
	}

	private void setupEntities()
	{
		SpawnPlacements.register(AEntityTypes.PIG_ZOMBIE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PigZombie::canPigZombieSpawn);
	}

	static
	{
		// Items

		// Entities
		// entityAndSpawnEgg(AEntityTypes.Names.PANDA_ZOMBIE, PandaZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(1.3F, 1.25F).clientTrackingRange(10));
		// entityAndSpawnEgg(AEntityTypes.Names.POLAR_BEAR_ZOMBIE, PolarBearZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.immuneTo(Blocks.POWDER_SNOW).sized(1.4F, 1.4F).clientTrackingRange(10));
		// entityAndSpawnEgg(AEntityTypes.Names.FOX_ZOMBIE, FoxZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(.6F, .7F).clientTrackingRange(8).immuneTo(Blocks.SWEET_BERRY_BUSH));
		// entityAndSpawnEgg(AEntityTypes.Names.WOLF_ZOMBIE, WolfZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(.6F, .85F).clientTrackingRange(10));
		// entityAndSpawnEgg(AEntityTypes.Names.RABBIT_ZOMBIE, RabbitZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(.4F, .5F).clientTrackingRange(8));
		// entityAndSpawnEgg(AEntityTypes.Names.CHICKEN_ZOMBIE, ChickenZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(.4F, .7F).clientTrackingRange(10));
		// entityAndSpawnEgg(AEntityTypes.Names.SHEEP_ZOMBIE, SheepZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(.9F, 1.3F).clientTrackingRange(10));
		// entityAndSpawnEgg(AEntityTypes.Names.COW_ZOMBIE, CowZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(.9F, 1.4F).clientTrackingRange(10));
		entityAndSpawnEgg(AEntityTypes.Names.PIG_ZOMBIE, PigZombie::new, MobCategory.MONSTER, 44975, 14377823, DEFAULT_ITEM_PROPERTIES, builder -> builder.sized(.9F, .9F).clientTrackingRange(10));
		// entityAndSpawnEgg(AEntityTypes.Names.BLAZE_ZOMBIE, BlazeZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder);
		// entityAndSpawnEgg(AEntityTypes.Names.EXPLOSIVE_ZOMBIE, ExplosiveZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder);
		// entityAndSpawnEgg(AEntityTypes.Names.TELEPORTING_ZOMBIE, TeleportingZombie::new, MobCategory.MONSTER, 0, 0, DEFAULT_ITEM_PROPERTIES, builder -> builder);

		// Enchantments
		enchantment(AEnchantments.Names.POISON, PoisonEnchantment::new);

		// MobEffects

		// Potions
	}

	// region: Helpers
	// region: Item
	private static <I extends Item> RegistryObject<I> item(String itemName, Function<Item.Properties, I> itemBuilder, Function<Item.Properties, Item.Properties> propertiesModifier)
	{
		return Armageddon.ITEMS.register(
				itemName,
				() -> itemBuilder.apply(propertiesModifier.apply(new Item.Properties()))
		);
	}

	private static <I extends Item> RegistryObject<I> item(String itemName, Function<Item.Properties, I> itemBuilder)
	{
		return item(itemName, itemBuilder, Function.identity());
	}

	// region: SpawnEgg
	private static RegistryObject<DeferredSpawnEggItem> spawnEgg(String itemName, Supplier<EntityType<? extends Mob>> entityTypeSupplier, int primaryColor, int secondaryColor, Function<Item.Properties, Item.Properties> propertiesModifier)
	{
		return item(
				itemName,
				properties -> new DeferredSpawnEggItem(entityTypeSupplier, primaryColor, secondaryColor, properties),
				propertiesModifier
		);
	}

	private static RegistryObject<DeferredSpawnEggItem> spawnEgg(String itemName, Supplier<EntityType<? extends Mob>> entityTypeSupplier, int primaryColor, int secondaryColor)
	{
		return spawnEgg(itemName, entityTypeSupplier, primaryColor, secondaryColor, Functions.identity());
	}

	private static <E extends Mob> RegistryObject<DeferredSpawnEggItem> spawnEgg(RegistryObject<EntityType<E>> entityTypeObject, int primaryColor, int secondaryColor, Function<Item.Properties, Item.Properties> propertiesModifier)
	{
		return spawnEgg(entityTypeObject.getId().getPath() + "_spawn_egg", entityTypeObject::get, primaryColor, secondaryColor, propertiesModifier);
	}

	private static <E extends Mob> RegistryObject<DeferredSpawnEggItem> spawnEgg(RegistryObject<EntityType<E>> entityTypeObject, int primaryColor, int secondaryColor)
	{
		return spawnEgg(entityTypeObject, primaryColor, secondaryColor, Functions.identity());
	}
	// endregion
	// endregion

	// region: Entity
	private static <E extends Entity> RegistryObject<EntityType<E>> entity(String entityName, EntityType.EntityFactory<E> entityFactory, MobCategory mobCategory, Function<EntityType.Builder<E>, EntityType.Builder<E>> modifier)
	{
		return Armageddon.ENTITIES.register(
				entityName,
				() -> modifier.apply(EntityType.Builder.of(entityFactory, mobCategory)).build(String.format("%s:%s", Armageddon.ID_MOD, entityName))
		);
	}

	private static <E extends Entity> RegistryObject<EntityType<E>> entity(String entityName, EntityType.EntityFactory<E> entityFactory, MobCategory mobCategory)
	{
		return entity(entityName, entityFactory, mobCategory, Functions.identity());
	}
	// endregion

	// region: Enchantment
	private static <E extends Enchantment> RegistryObject<E> enchantment(String enchantmentName, Supplier<E> enchantmentSupplier)
	{
		return Armageddon.ENCHANTMENTS.register(enchantmentName, enchantmentSupplier);
	}
	// endregion

	// region: Combo
	private static <E extends Mob> Pair<RegistryObject<EntityType<E>>, RegistryObject<DeferredSpawnEggItem>> entityAndSpawnEgg(String entityName, EntityType.EntityFactory<E> entityFactory, MobCategory mobCategory, int spawnEggPrimaryColor, int spawnEggSecondaryColor, Function<Item.Properties, Item.Properties> itemPropertiesModifier, Function<EntityType.Builder<E>, EntityType.Builder<E>> entityModifier)
	{
		var entity = entity(entityName, entityFactory, mobCategory, entityModifier);
		var spawnEgg = spawnEgg(entity, spawnEggPrimaryColor, spawnEggSecondaryColor, itemPropertiesModifier);
		return Pair.of(entity, spawnEgg);
	}
	// endregion
	// endregion

	public static final class CreativeTab extends CreativeModeTab
	{
		private CreativeTab()
		{
			super(Armageddon.ID_MOD);
		}

		@Override
		public ItemStack makeIcon()
		{
			return AItems.PIG_ZOMBIE_SPAWN_EGG.getDefaultInstance();
		}
	}
}
