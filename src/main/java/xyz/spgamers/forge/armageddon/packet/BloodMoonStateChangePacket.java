package xyz.spgamers.forge.armageddon.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.spgamers.forge.armageddon.util.BloodMoonHelper;

import java.util.function.Supplier;

public final class BloodMoonStateChangePacket
{
	private final RegistryKey<World> dimension;
	private final boolean state;

	public BloodMoonStateChangePacket(World world, boolean state)
	{
		dimension = world.getDimensionKey();
		this.state = state;
	}

	public BloodMoonStateChangePacket(PacketBuffer buffer)
	{
		dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buffer.readResourceLocation());
		state = buffer.readBoolean();
	}

	public static void encode(BloodMoonStateChangePacket packet, PacketBuffer buffer)
	{
		buffer.writeResourceLocation(packet.dimension.getLocation());
		buffer.writeBoolean(packet.state);
	}

	public static void process(BloodMoonStateChangePacket packet, Supplier<NetworkEvent.Context> ctx)
	{
		BloodMoonHelper.ClientHelper.setBloodMoonState(packet.dimension, packet.state);
		ctx.get().setPacketHandled(true);
	}
}
