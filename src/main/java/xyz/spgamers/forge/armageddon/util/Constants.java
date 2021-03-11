package xyz.spgamers.forge.armageddon.util;

import net.minecraft.util.ResourceLocation;

public final class Constants
{
	public static final String MOD_ID = "armageddon";

	private Constants()
	{
		throw new IllegalStateException();
	}

	public static final class Items
	{
		public static final String ZOMBIE_PIG_SPAWN_EGG = "zombie_pig_spawn_egg";
		public static final String ZOMBIE_CREEPER_SPAWN_EGG = "zombie_creeper_spawn_egg";

		private Items()
		{
			throw new IllegalStateException();
		}
	}

	public static final class Entities
	{
		public static final String ZOMBIE_PIG = "zombie_pig";
		public static final String ZOMBIE_CREEPER = "zombie_creeper";

		private Entities()
		{
			throw new IllegalStateException();
		}
	}

	public static final class Textures
	{
		public static final ResourceLocation ZOMBIE_PIG = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_pig.png");
		public static final ResourceLocation ZOMBIE_CREEPER = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_creeper.png");
		public static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("minecraft", "textures/entity/creeper/creeper_armor.png");

		private Textures()
		{
			throw new IllegalStateException();
		}
	}

	public static final class NBTKeys
	{
		public static final String ENTITY_TAG = "EntityTag";
		public static final String ID = "id";
		public static final String POWERED = "powered";
		public static final String FUSE = "Fuse";
		public static final String EXPLOSION_RADIUS = "ExplosionRadius";
		public static final String IGNITED = "ignited";

		private NBTKeys()
		{
			throw new IllegalStateException();
		}
	}
}
