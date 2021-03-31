package xyz.spgamers.forge.armageddon.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

public final class ServerConfig
{
	public final ForgeConfigSpec configSpec;
	public final Animals animals;
	public final Mobs mobs;
	public final Common common;

	private final ForgeConfigSpec.Builder configBuilder;

	public ServerConfig()
	{
		configBuilder = new ForgeConfigSpec.Builder();
				// .comment("Configs for Server side.", "Changes Require Server Restart!"); // kills the game

		common = config("common", Common::new);
		animals = config("animals", Animals::new);
		mobs = config("mobs", Mobs::new);
		configSpec = configBuilder.build();
	}

	private <T> T config(String configName, Function<ForgeConfigSpec.Builder, T> configBuilder)
	{
		this.configBuilder.push(configName);
		T config = configBuilder.apply(this.configBuilder);
		this.configBuilder.pop();
		return config;
	}

	public static final class Common
	{
		private final ForgeConfigSpec.IntValue hordeSpawnCount;

		Common(ForgeConfigSpec.Builder builder)
		{
			hordeSpawnCount = builder.comment("Change amount of additional Zombies to spawn in Hordes").defineInRange("hordeSpawnCount", 4, 0, 10);
		}

		public int getHordeSpawnCount()
		{
			return hordeSpawnCount.get();
		}
	}

	public static final class Animals
	{
		private final ForgeConfigSpec.BooleanValue enablePigZombie;
		private final ForgeConfigSpec.BooleanValue enableCowZombie;
		private final ForgeConfigSpec.BooleanValue enableSheepZombie;
		private final ForgeConfigSpec.BooleanValue enableChickenZombie;
		private final ForgeConfigSpec.BooleanValue enableRabbitZombie;
		private final ForgeConfigSpec.BooleanValue enableWolfZombie;
		private final ForgeConfigSpec.BooleanValue enableFoxZombie;
		private final ForgeConfigSpec.BooleanValue enablePolarBearZombie;
		private final ForgeConfigSpec.BooleanValue enablePandaZombie;

		private final ForgeConfigSpec.BooleanValue sheepZombieShearable;

		Animals(ForgeConfigSpec.Builder builder)
		{
			enablePigZombie = builder.comment("Enable or Disable Zombie Pig EntityType.").define("enablePigZombie", true);
			enableCowZombie = builder.comment("Enable or Disable Zombie Cow EntityType.").define("enableCowZombie", true);
			enableChickenZombie = builder.comment("Enable or Disable Zombie Chicken EntityType.").define("enableChickenZombie", true);
			enableRabbitZombie = builder.comment("Enable or Disable Zombie Rabbit EntityType.").define("enableRabbitZombie", true);
			enableWolfZombie = builder.comment("Enable or Disable Zombie Wolf EntityType.").define("enableWolfZombie", true);
			enableFoxZombie = builder.comment("Enable or Disable Zombie Fox EntityType.").define("enableFoxZombie", true);
			enablePolarBearZombie = builder.comment("Enable or Disable Zombie PolarBear EntityType.").define("enablePolarBearZombie", true);
			enablePandaZombie = builder.comment("Enable or Disable Zombie Panda EntityType.").define("enablePandaZombie", true);

			builder.push("sheep");

			enableSheepZombie = builder.comment("Enable or Disable Zombie Sheep EntityType.").define("enable", true);
			sheepZombieShearable = builder.comment(
					"Makes the Sheep Zombie Shearable with Shears.",
					"This is off by default due to Wool hiding most of the Zombie texture",
					"and making sort of hard to determine what sheep are Zombies.")
			                              .define("shearable", false);

			builder.pop();
		}

		public boolean isPigZombieEnabled()
		{
			return enablePigZombie.get();
		}

		public boolean isCowZombieEnabled()
		{
			return enableCowZombie.get();
		}

		public boolean isSheepZombieEnabled()
		{
			return enableSheepZombie.get();
		}

		public boolean isSheepZombieShearble()
		{
			return sheepZombieShearable.get();
		}

		public boolean isChickenZombieEnabled()
		{
			return enableChickenZombie.get();
		}

		public boolean isRabbitZombieEnabled()
		{
			return enableRabbitZombie.get();
		}

		public boolean isWolfZombieEnabled()
		{
			return enableWolfZombie.get();
		}

		public boolean isFoxZombieEnabled()
		{
			return enableFoxZombie.get();
		}

		public boolean isPolarBearZombieEnabled()
		{
			return enablePolarBearZombie.get();
		}

		public boolean isPandaZombieEnabled()
		{
			return enablePandaZombie.get();
		}
	}

	public static final class Mobs
	{
		private final ForgeConfigSpec.BooleanValue enableFireZombie;
		private final ForgeConfigSpec.BooleanValue enableExplosiveZombie;
		private final ForgeConfigSpec.BooleanValue enableTeleportingZombie;

		Mobs(ForgeConfigSpec.Builder builder)
		{
			enableFireZombie = builder.comment("Enable or Disable Fire Zombie type.").define("enableFireZombie", true);
			enableExplosiveZombie = builder.comment("Enable or Disable Explosive Zombie type.").define("enableExplosiveZombie", true);
			enableTeleportingZombie = builder.comment("Enable or Disable Teleporting Zombie type.").define("enableTeleportingZombie", true);
		}

		public boolean isFireZombieEnabled()
		{
			return enableFireZombie.get();
		}

		public boolean isExplosiveZombieEnabled()
		{
			return enableExplosiveZombie.get();
		}

		public boolean isTeleportingZombieEnabled()
		{
			return enableTeleportingZombie.get();
		}
	}
}
