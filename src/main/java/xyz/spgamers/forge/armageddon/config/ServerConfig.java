package xyz.spgamers.forge.armageddon.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ServerConfig
{
	public final ForgeConfigSpec configSpec;

	public ServerConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		configSpec = builder.build();
	}
}
