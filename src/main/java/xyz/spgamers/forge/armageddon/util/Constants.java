package xyz.spgamers.forge.armageddon.util;

import net.minecraft.util.ResourceLocation;
import xyz.spgamers.forge.armageddon.Armageddon;

public class Constants
{
	public static final String MOD_ID = "armageddon";

	private Constants()
	{
		throw new IllegalStateException();
	}

	public static final class Items
	{
		public static final String ZOMBIE_PIG_SPAWN_EGG = "zombie_pig_spawn_egg";

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

	public static final class Textures
	{
		public static final ResourceLocation ZOMBIE_PIG = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_pig.png");

		private Textures()
		{
			throw new IllegalStateException();
		}
	}

	public static final class NBTKeys
	{
		public static final String ENTITY_TAG = "EntityTag";
		public static final String ID = "id";

		private NBTKeys()
		{
			throw new IllegalStateException();
		}
	}
}
