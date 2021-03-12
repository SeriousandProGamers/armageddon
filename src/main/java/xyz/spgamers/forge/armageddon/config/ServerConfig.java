package xyz.spgamers.forge.armageddon.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

public final class ServerConfig
{
	public final ForgeConfigSpec configSpec;
	public final Entities entities;

	private final ForgeConfigSpec.Builder configBuilder;

	public ServerConfig()
	{
		configBuilder = new ForgeConfigSpec.Builder();
				// .comment("Configs for Server side.", "Changes Require Server Restart!"); // kills the game

		entities = config("entities", Entities::new);
		configSpec = configBuilder.build();
	}

	private <T> T config(String configName, Function<ForgeConfigSpec.Builder, T> configBuilder)
	{
		this.configBuilder.push(configName);
		T config = configBuilder.apply(this.configBuilder);
		this.configBuilder.pop();
		return config;
	}

	public static final class Entities
	{
		private final ForgeConfigSpec.BooleanValue enableZombiePig;

		Entities(ForgeConfigSpec.Builder builder)
		{
			enableZombiePig = builder.comment("Enable or Disable Zombie Pig EntityType.").define("enableZombiePig", true);
		}

		public boolean isZombiePigEnabled()
		{
			return enableZombiePig.get();
		}
	}
}
