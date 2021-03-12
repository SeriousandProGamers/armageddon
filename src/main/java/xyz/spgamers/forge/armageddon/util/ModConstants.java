package xyz.spgamers.forge.armageddon.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModConstants
{
	public static final String MOD_ID = "armageddon";
	public static final Logger LOGGER = LogManager.getLogger("Armageddon");

	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(MOD_ID, "main"),
			() -> "1.0",
			s -> true,
			s -> true
	);

	private ModConstants()
	{
		throw new IllegalStateException();
	}

	public static final class Items
	{
		private Items()
		{
			throw new IllegalStateException();
		}
	}

	public static final class Entities
	{
		public static final String ZOMBIE_PIG = "zombie_pig";

		private Entities()
		{
			throw new IllegalStateException();
		}
	}

	public static final class NBT
	{
		private NBT()
		{
			throw new IllegalStateException();
		}
	}
}
