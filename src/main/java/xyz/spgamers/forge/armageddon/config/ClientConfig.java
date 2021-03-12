package xyz.spgamers.forge.armageddon.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig
{
	public final ForgeConfigSpec configSpec;

	public ClientConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		configSpec = builder.build();
	}
}
