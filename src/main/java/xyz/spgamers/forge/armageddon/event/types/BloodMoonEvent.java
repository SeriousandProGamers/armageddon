package xyz.spgamers.forge.armageddon.event.types;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * {@link BloodMoonEvent} is fired when an event involving the blood moon occurs.<br>
 * If a method utilizes this {@link Event} as its parameter, the method will
 * receive every child of this class.<br>
 * <br>
 * {@link #world} contains the {@link IWorld World} this event is occurring in.<br>
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 * <br>
 * @see WorldEvent
 */
public class BloodMoonEvent extends WorldEvent
{
	private BloodMoonEvent(IWorld world)
	{
		super(world);
	}

	/**
	 * {@link WorldSupported BloodMoonEvent.WorldSupported} is fired once per world load.<br>
	 * This event is used to modify if given world can support blood moon events or not.<br>
	 * By default only {@link World#OVERWORLD Overworld} and any worlds why time is not frozen are supported.<br>
	 * <br>
	 * To mark your world as supported use {@link #setSupported()}.<br>
	 * <br>
	 * {@link #isSupportedDefault} contains whether or not this world is supported by default.<br>
	 * {@link #isSupportedForcefully} contains whether or not this world has been forcefully supported.<br>
	 * <br>
	 * This event is not {@link Cancelable}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
	 * <br>
	 * @see BloodMoonEvent
	 */
	public static final class WorldSupported extends BloodMoonEvent
	{
		private final boolean isSupportedDefault;
		private boolean isSupportedForcefully = false;

		public WorldSupported(IWorld world, boolean isSupportedDefault)
		{
			super(world);

			this.isSupportedDefault = isSupportedDefault;
		}

		/**
		 * Returns true if this world is supported.<br>
		 * <br>
		 * @return true if this world is supported.<br>
		 */
		public boolean isSupported()
		{
			return isSupportedDefault() || isSupportedForcefully();
		}

		/**
		 * Returns true if this world is supported by default.<br>
		 * <br>
		 * @return true if this world is supported by default.<br>
		 */
		public boolean isSupportedDefault()
		{
			return isSupportedDefault;
		}

		/**
		 * Returns true if this world has been forcefully supported or not.<br>
		 * <br>
		 * @return true if world has been forcefully supported.<br>
		 */
		public boolean isSupportedForcefully()
		{
			return isSupportedForcefully;
		}

		/**
		 * Use this method to forcefully mark your world as supported.<br>
		 */
		public void setSupported()
		{
			isSupportedForcefully = true;
		}
	}

	/**
	 * {@link Start BloodMoonEvent.Start} is fired when a blood moon event starts.<br>
	 * <br>
	 * This event is {@link Cancelable}.<br>
	 * If this event is cancelled the blood moon will not be started.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
	 * <br>
	 * @see BloodMoonEvent
	 */
	@Cancelable
	public static final class Start extends BloodMoonEvent
	{
		public Start(IWorld world)
		{
			super(world);
		}
	}

	/**
	 * {@link Stop BloodMoonEvent.Stop} is fired when a blood moon event stops.<br>
	 * <br>
	 * This event is not {@link Cancelable}.<br>
	 * <br>
	 * This event does not have a result. {@link HasResult}<br>
	 * <br>
	 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
	 * <br>
	 * @see BloodMoonEvent
	 */
	public static final class Stop extends BloodMoonEvent
	{
		public Stop(IWorld world)
		{
			super(world);
		}
	}
}
