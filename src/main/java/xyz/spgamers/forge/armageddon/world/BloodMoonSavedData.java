package xyz.spgamers.forge.armageddon.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import xyz.spgamers.forge.armageddon.event.types.BloodMoonEvent;
import xyz.spgamers.forge.armageddon.packet.BloodMoonStateChangePacket;
import xyz.spgamers.forge.armageddon.util.ModConstants;
import xyz.spgamers.forge.armageddon.util.WorldHelper;

public final class BloodMoonSavedData extends WorldSavedData
{
	public static final String NAME = String.format("%s_BloodMoonSavedData", ModConstants.MOD_ID);

	private final ServerWorld world;
	private final boolean isSupported;
	private boolean isBloodMoon = false;

	private BloodMoonSavedData(ServerWorld world)
	{
		super(NAME);

		this.world = world;
		isSupported = isWorldSupported(world);
	}

	@Override
	public void read(CompoundNBT tagCompound)
	{
		isBloodMoon = false;

		if(isSupported)
		{
			if(tagCompound.contains(ModConstants.NBT.IS_BLOOD_MOON, Constants.NBT.TAG_BYTE))
				setBloodMoon(true);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT tagCompound)
	{
		tagCompound.putBoolean(ModConstants.NBT.IS_BLOOD_MOON, isBloodMoon);
		return tagCompound;
	}

	public void setBloodMoon(boolean enabled)
	{
		boolean wasBloodMoon = isBloodMoon;

		if(isBloodMoon && !enabled)
		{
			onEndBloodMoon();
			isBloodMoon = false;
		}
		else if(enabled && !isBloodMoon)
			isBloodMoon = onStartBloodMoon();

		if(isBloodMoon != wasBloodMoon)
		{
			ModConstants.NETWORK.send(PacketDistributor.ALL.noArg(), new BloodMoonStateChangePacket(world, isBloodMoon));
			markDirty();
		}
	}

	public boolean isBloodMoon()
	{
		return isBloodMoon;
	}

	public void update()
	{
		if(isSupported)
		{
			long previousTime = WorldHelper.getPreviousWorldDayTime(world);
			long worldTime = world.getDayTime();

			boolean wasNight = previousTime <= 24000L && previousTime >= 13000L;
			boolean isNight = worldTime <= 24000L && worldTime >= 13000L;

			// when a new night starts
			if(isNight && !wasNight)
			{
				// full moon && random chance
				// world#getMoonPhase() is marked OnlyIn(CLIENT) will not exist server side
				// if(world.getMoonPhase() == 0 && world.rand.nextBoolean())
				if(world.getDimensionType().getMoonPhase(worldTime) == 0 && world.rand.nextFloat() < .4F)
					setBloodMoon(true);
			}
			// when a new day starts
			else if(wasNight && !isNight)
				setBloodMoon(false);
		}
		else
			isBloodMoon = false;
	}

	private boolean onStartBloodMoon()
	{
		boolean cancelled = MinecraftForge.EVENT_BUS.post(new BloodMoonEvent.Start(world));

		if(cancelled)
			return false;

		return true;
	}

	private void onEndBloodMoon()
	{
		MinecraftForge.EVENT_BUS.post(new BloodMoonEvent.Stop(world));
	}

	public static BloodMoonSavedData instance(ServerWorld world)
	{
		return world.getSavedData().getOrCreate(() -> new BloodMoonSavedData(world), NAME);
	}

	private static boolean isWorldSupported(World world)
	{
		RegistryKey<World> dimension = world.getDimensionKey();
		boolean isSupportedDefault = true;

		// does not work in the nether or end dimensions
		// does not work in dimensions where time does not move
		if(dimension == World.THE_NETHER || dimension == World.THE_END || world.getDimensionType().doesFixedTimeExist())
			isSupportedDefault = false;

		BloodMoonEvent.WorldSupported event = new BloodMoonEvent.WorldSupported(world, isSupportedDefault);
		MinecraftForge.EVENT_BUS.post(event);
		return event.isSupported();
	}
}
