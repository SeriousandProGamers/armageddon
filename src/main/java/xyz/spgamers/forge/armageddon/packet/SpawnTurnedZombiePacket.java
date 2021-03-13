package xyz.spgamers.forge.armageddon.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

public class SpawnTurnedZombiePacket
{
	private final EntityType<?> entityType;
	private final RegistryKey<World> dimension;
	private final int originalEntityId;
	private final UUID originalEntityUUID;
	private final BlockPos position;
	private final float rotationYaw;
	private final float rotationPitch;

	public SpawnTurnedZombiePacket(MobEntity entity)
	{
		entityType = entity.getType();
		dimension = entity.world.getDimensionKey();
		originalEntityId = entity.getEntityId();
		originalEntityUUID = entity.getUniqueID();
		position = entity.getPosition();
		rotationYaw = entity.rotationYaw;
		rotationPitch = entity.rotationPitch;
	}

	public SpawnTurnedZombiePacket(PacketBuffer buffer)
	{
		entityType = buffer.readRegistryIdUnsafe(ForgeRegistries.ENTITIES);
		dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buffer.readResourceLocation());
		originalEntityId = buffer.readInt();
		originalEntityUUID = buffer.readUniqueId();
		position = buffer.readBlockPos();
		rotationYaw = buffer.readFloat();
		rotationPitch = buffer.readFloat();
	}

	@Nullable
	public MobEntity tryGetOriginalEntity(ServerWorld world)
	{
		Entity entity = world.getEntityByID(originalEntityId);

		if(entity == null)
			entity = world.getEntityByUuid(originalEntityUUID);
		if(entity == null)
			return null;

		if(!(entity instanceof MobEntity))
		{
			ModConstants.LOGGER.error("Original entity is not instance of MobEntity, It SHOULD be!");
			return null;
		}

		return (MobEntity) entity;
	}

	public static void encode(SpawnTurnedZombiePacket packet, PacketBuffer buffer)
	{
		buffer.writeRegistryIdUnsafe(ForgeRegistries.ENTITIES, packet.entityType);
		buffer.writeResourceLocation(packet.dimension.getLocation());
		buffer.writeInt(packet.originalEntityId);
		buffer.writeUniqueId(packet.originalEntityUUID);
		buffer.writeBlockPos(packet.position);
		buffer.writeFloat(packet.rotationYaw);
		buffer.writeFloat(packet.rotationPitch);
	}

	public static void process(SpawnTurnedZombiePacket packet, Supplier<NetworkEvent.Context> ctx)
	{
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		ServerWorld world = server.getWorld(packet.dimension);

		if(world == null)
		{
			ModConstants.LOGGER.error("Failed to obtain ServerWorld instance. This should NEVER happen!");
			return;
		}

		EntityType<? extends MobEntity> turnedEntityType = getTurnedEntityType(packet.entityType);

		if(turnedEntityType == null)
		{
			ModConstants.LOGGER.error("Failed to obtain turned EntityType.");
			return;
		}

		MobEntity originalEntity = packet.tryGetOriginalEntity(world);
		MobEntity turnedEntity = null;

		// create copy of original
		if(originalEntity != null)
			turnedEntity = originalEntity.func_233656_b_(turnedEntityType, false);
		// create default instance
		if(turnedEntity == null)
			turnedEntity = turnedEntityType.create(world);

		if(turnedEntity != null)
		{
			// TODO: Figure out why turning entities are dropping loot
			turnedEntity.onInitialSpawn(world, world.getDifficultyForLocation(packet.position), SpawnReason.CONVERSION, null, null);
			world.func_242417_l(turnedEntity);
		}

		ctx.get().setPacketHandled(true);
	}

	// TODO: Find better method of doing this
	@Nullable
	private static EntityType<? extends MobEntity> getTurnedEntityType(EntityType<?> entityType)
	{
		if(entityType == EntityType.PIG)
			return ModEntities.PIG_ZOMBIE.get();
		else if(entityType == EntityType.COW)
			return ModEntities.COW_ZOMBIE.get();
		/*else if(entityType == EntityType.CHICKEN)
			return ModEntities.CHICKEN_ZOMBIE.get();*/
		else if(entityType == EntityType.SHEEP)
			return ModEntities.SHEEP_ZOMBIE.get();
		else
			return null;
	}
}
