package xyz.spgamers.forge.armageddon.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Function;

public final class ClientConfig
{
	public final ForgeConfigSpec configSpec;
	private final ForgeConfigSpec.Builder configBuilder;

	public ClientConfig()
	{
		configBuilder = new ForgeConfigSpec.Builder();
				// .comment("Configs for Client side", "Changes get hot-swapped into running game automagicly."); // kills the game

		configSpec = configBuilder.build();
	}

	private <T> T config(String configName, Function<ForgeConfigSpec.Builder, T> configBuilder)
	{
		this.configBuilder.push(configName);
		T config = configBuilder.apply(this.configBuilder);
		this.configBuilder.pop();
		return config;
	}
}
