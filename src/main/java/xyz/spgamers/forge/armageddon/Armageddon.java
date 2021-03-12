package xyz.spgamers.forge.armageddon;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("armageddon")
public class Armageddon
{
	public Armageddon()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
	}
}