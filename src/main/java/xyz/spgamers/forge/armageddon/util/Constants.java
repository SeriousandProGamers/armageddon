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
		public static final String ZOMBIE_COW_SPAWN_EGG = "zombie_cow_spawn_egg";
		public static final String ZOMBIE_BROWN_MOOSHROOM_SPAWN_EGG = "zombie_brown_mooshroom_spawn_egg";
		public static final String ZOMBIE_RED_MOOSHROOM_SPAWN_EGG = "zombie_red_mooshroom_spawn_egg";

		private Items()
		{
			throw new IllegalStateException();
		}
	}

	public static final class Entities
	{
		public static final String ZOMBIE_PIG = "zombie_pig";
		public static final String ZOMBIE_CREEPER = "zombie_creeper";
		public static final String ZOMBIE_COW = "zombie_cow";
		public static final String ZOMBIE_BROWN_MOOSHROOM = "zombie_brown_mooshroom";
		public static final String ZOMBIE_RED_MOOSHROOM = "zombie_red_mooshroom";

		private Entities()
		{
			throw new IllegalStateException();
		}
	}

	public static final class Textures
	{
		public static final ResourceLocation ZOMBIE_PIG = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_pig.png");
		public static final ResourceLocation ZOMBIE_CREEPER = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_creeper.png");
		public static final ResourceLocation ZOMBIE_COW = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_cow.png");
		public static final ResourceLocation ZOMBIE_BROWN_MOOSHROOM = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_brown_mooshroom.png");
		public static final ResourceLocation ZOMBIE_RED_MOOSHROOM = new ResourceLocation(MOD_ID, "textures/entity/zombie/zombie_red_mooshroom.png");

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
		public static final String TYPE = "Type";
		public static final String EFFECT_ID = "EffectId";
		public static final String EFFECT_DURATION = "EffectDuration";

		private NBTKeys()
		{
			throw new IllegalStateException();
		}
	}
}
