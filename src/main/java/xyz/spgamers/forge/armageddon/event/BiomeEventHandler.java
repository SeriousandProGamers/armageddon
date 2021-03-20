package xyz.spgamers.forge.armageddon.event;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID)
public final class BiomeEventHandler
{
	private BiomeEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event)
	{
		MobSpawnInfoBuilder spawns = event.getSpawns();

		if(!spawns.getSpawner(EntityClassification.MONSTER).isEmpty())
		{
			// standard zombie spawning
			withZombieSpawns(spawns, 95);
		}
	}


	private static void withZombieSpawns(MobSpawnInfoBuilder spawns, int zombieWeight)
	{
		spawns
				.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.PIG_ZOMBIE.get(), zombieWeight, 4, 4))
				.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.COW_ZOMBIE.get(), zombieWeight, 4, 4))
				.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.CHICKEN_ZOMBIE.get(), zombieWeight, 4, 4))
				.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.SHEEP_ZOMBIE.get(), zombieWeight, 4, 4))
				.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.FOX_ZOMBIE.get(), zombieWeight, 4, 4))
				.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.PANDA_ZOMBIE.get(), zombieWeight, 4, 4))
				/*.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.POLAR_BEAR_ZOMBIE.get(), zombieWeight, 4, 4))*/
				/*.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.RABBIT_ZOMBIE.get(), zombieWeight, 4, 4))*/
				/*.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.WOLF_ZOMBIE.get(), zombieWeight, 4, 4))*/
		;
	}
}
