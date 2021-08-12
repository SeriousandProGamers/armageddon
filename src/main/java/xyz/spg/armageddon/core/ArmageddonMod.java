package xyz.spg.armageddon.core;

import com.google.common.base.Functions;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.commons.lang3.tuple.Pair;
import xyz.spg.armageddon.core.client.ClientSetup;
import xyz.spg.armageddon.core.data.*;
import xyz.spg.armageddon.core.enchantment.PoisonEnchantment;
import xyz.spg.armageddon.core.entity.*;
import xyz.spg.armageddon.core.item.DeferredSpawnEggItem;
import xyz.spg.armageddon.core.item.DeferredThrownEggItem;
import xyz.spg.armageddon.core.item.SpoiledMilkBucketItem;
import xyz.spg.armageddon.shared.*;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(Armageddon.ID_MOD)
public final class ArmageddonMod
{
	public static final CreativeTab CREATIVE_TAB = new CreativeTab();

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
		event.enqueueWork(this::fixSpawnEggs);
		event.enqueueWork(Armageddon::postRegister);
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
		event.put(AEntityTypes.COW_ZOMBIE, CowZombie.createAttributes().build());
		event.put(AEntityTypes.SHEEP_ZOMBIE, SheepZombie.createAttributes().build());
		event.put(AEntityTypes.CHICKEN_ZOMBIE, ChickenZombie.createAttributes().build());
	}

	private void fixSpawnEggs()
	{
		Map<EntityType<? extends Mob>, SpawnEggItem> by_id = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "f_43201_");
		by_id.put(AEntityTypes.PIG_ZOMBIE, AItems.PIG_ZOMBIE_SPAWN_EGG);
		by_id.put(AEntityTypes.COW_ZOMBIE, AItems.COW_ZOMBIE_SPAWN_EGG);
		by_id.put(AEntityTypes.SHEEP_ZOMBIE, AItems.SHEEP_ZOMBIE_SPAWN_EGG);
		by_id.put(AEntityTypes.CHICKEN_ZOMBIE, AItems.CHICKEN_ZOMBIE_SPAWN_EGG);
	}

	private void setupEntities()
	{
		SpawnPlacements.register(AEntityTypes.PIG_ZOMBIE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PigZombie::canPigZombieSpawn);
		SpawnPlacements.register(AEntityTypes.COW_ZOMBIE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CowZombie::canCowZombieSpawn);
		SpawnPlacements.register(AEntityTypes.SHEEP_ZOMBIE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SheepZombie::canSheepZombieSpawn);
		SpawnPlacements.register(AEntityTypes.CHICKEN_ZOMBIE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChickenZombie::canChickenZombieSpawn);
	}

	static
	{
		// Items
		item(ANames.ROTTEN_PORKCHOP, Item::new, properties -> properties.food(AFoods.ROTTEN_PORKCHOP));
		item(ANames.ROTTEN_BEEF, Item::new, properties -> properties.food(AFoods.ROTTEN_BEEF));
		item(ANames.ROTTEN_MUTTON, Item::new, properties -> properties.food(AFoods.ROTTEN_MUTTON));
		item(ANames.ROTTEN_CHICKEN, Item::new, properties -> properties.food(AFoods.ROTTEN_CHICKEN));
		item(ANames.ROTTEN_RABBIT, Item::new, properties -> properties.food(AFoods.ROTTEN_RABBIT));
		item(ANames.ROTTEN_RABBIT_FOOT, Item::new);
		item(ANames.ROTTEN_FISH, Item::new, properties -> properties.food(AFoods.ROTTEN_FISH));
		item(ANames.SPOILED_MILK_BUCKET, SpoiledMilkBucketItem::new, properties -> properties.craftRemainder(Items.BUCKET).stacksTo(1)); // TODO: Register spoiled milk fluid

		// Entities
		entityAndThrownEgg(ANames.ROTTEN_EGG, ThrownRottenEgg::new, MobCategory.MISC, Functions.identity(), builder -> builder.sized(.25F, .25F).clientTrackingRange(4).updateInterval(10));

		// entityAndSpawnEgg(ANames.PANDA_ZOMBIE, PandaZombie::new, MobCategory.MONSTER, 44975, 1776418, Functions.identity(), builder -> builder.sized(1.3F, 1.25F).clientTrackingRange(10));
		// entityAndSpawnEgg(ANames.POLAR_BEAR_ZOMBIE, PolarBearZombie::new, MobCategory.MONSTER, 44975, 9803152, Functions.identity(), builder -> builder.immuneTo(Blocks.POWDER_SNOW).sized(1.4F, 1.4F).clientTrackingRange(10));
		// entityAndSpawnEgg(ANames.FOX_ZOMBIE, FoxZombie::new, MobCategory.MONSTER, 44975, 13396256, Functions.identity(), builder -> builder.sized(.6F, .7F).clientTrackingRange(8).immuneTo(Blocks.SWEET_BERRY_BUSH));
		// entityAndSpawnEgg(ANames.WOLF_ZOMBIE, WolfZombie::new, MobCategory.MONSTER, 0, 0, Functions.identity(), builder -> builder.sized(.6F, .85F).clientTrackingRange(10));
		// entityAndSpawnEgg(ANames.RABBIT_ZOMBIE, RabbitZombie::new, MobCategory.MONSTER, 44975, 7555121, Functions.identity(), builder -> builder.sized(.4F, .5F).clientTrackingRange(8));
		entityAndSpawnEgg(ANames.CHICKEN_ZOMBIE, ChickenZombie::new, MobCategory.MONSTER, 44975, 16711680, Functions.identity(), builder -> builder.sized(.4F, .7F).clientTrackingRange(10));
		entityAndSpawnEgg(ANames.SHEEP_ZOMBIE, SheepZombie::new, MobCategory.MONSTER, 44975, 16758197, Functions.identity(), builder -> builder.sized(.9F, 1.3F).clientTrackingRange(10));
		entityAndSpawnEgg(ANames.COW_ZOMBIE, CowZombie::new, MobCategory.MONSTER, 44975, 4470310, Functions.identity(), builder -> builder.sized(.9F, 1.4F).clientTrackingRange(10));
		entityAndSpawnEgg(ANames.PIG_ZOMBIE, PigZombie::new, MobCategory.MONSTER, 44975, 14377823, Functions.identity(), builder -> builder.sized(.9F, .9F).clientTrackingRange(10));
		// entityAndSpawnEgg(ANames.BLAZE_ZOMBIE, BlazeZombie::new, MobCategory.MONSTER, 0, 0, Functions.identity(), builder -> builder);
		// entityAndSpawnEgg(ANames.EXPLOSIVE_ZOMBIE, ExplosiveZombie::new, MobCategory.MONSTER, 0, 0, Functions.identity(), builder -> builder);
		// entityAndSpawnEgg(ANames.TELEPORTING_ZOMBIE, TeleportingZombie::new, MobCategory.MONSTER, 0, 0, Functions.identity(), builder -> builder);

		// Enchantments
		enchantment(ANames.POISON, PoisonEnchantment::new);

		// MobEffects

		// Potions
	}

	// region: Helpers
	// region: Item
	private static <I extends Item> RegistryObject<I> item(String itemName, Function<Item.Properties, I> itemBuilder, Function<Item.Properties, Item.Properties> propertiesModifier)
	{
		return Armageddon.ITEMS.register(
				itemName,
				() -> itemBuilder.apply(propertiesModifier.apply(new Item.Properties().tab(CREATIVE_TAB)))
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
		return spawnEgg(entityTypeObject.getId().getPath() + ANames.SPAWN_EGG_SUFFIX, entityTypeObject::get, primaryColor, secondaryColor, propertiesModifier);
	}

	private static <E extends Mob> RegistryObject<DeferredSpawnEggItem> spawnEgg(RegistryObject<EntityType<E>> entityTypeObject, int primaryColor, int secondaryColor)
	{
		return spawnEgg(entityTypeObject, primaryColor, secondaryColor, Functions.identity());
	}
	// endregion

	// region: Thrown Egg
	private static RegistryObject<DeferredThrownEggItem> thrownEgg(String itemName, Supplier<EntityType<? extends ThrowableItemProjectile>> entityTypeSupplier, Function<Item.Properties, Item.Properties> propertiesModifier)
	{
		return item(
				itemName,
				properties -> new DeferredThrownEggItem(entityTypeSupplier, properties),
				propertiesModifier
		);
	}

	private static RegistryObject<DeferredThrownEggItem> thrownEgg(String itemName, Supplier<EntityType<? extends ThrowableItemProjectile>> entityTypeSupplier)
	{
		return thrownEgg(itemName, entityTypeSupplier, Functions.identity());
	}

	private static <E extends ThrowableItemProjectile> RegistryObject<DeferredThrownEggItem> thrownEgg(RegistryObject<EntityType<E>> entityTypeObject, Function<Item.Properties, Item.Properties> propertiesModifier)
	{
		return thrownEgg(entityTypeObject.getId().getPath(), entityTypeObject::get, propertiesModifier);
	}

	private static <E extends ThrowableItemProjectile> RegistryObject<DeferredThrownEggItem> thrownEgg(RegistryObject<EntityType<E>> entityTypeObject)
	{
		return thrownEgg(entityTypeObject, Functions.identity());
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

	private static <E extends ThrowableItemProjectile> Pair<RegistryObject<EntityType<E>>, RegistryObject<DeferredThrownEggItem>> entityAndThrownEgg(String entityName, EntityType.EntityFactory<E> entityFactory, MobCategory mobCategory, Function<Item.Properties, Item.Properties> itemPropertiesModifier, Function<EntityType.Builder<E>, EntityType.Builder<E>> entityModifier)
	{
		var entity = entity(entityName, entityFactory, mobCategory, entityModifier);
		var thrownEgg = thrownEgg(entity, itemPropertiesModifier);
		return Pair.of(entity, thrownEgg);
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
