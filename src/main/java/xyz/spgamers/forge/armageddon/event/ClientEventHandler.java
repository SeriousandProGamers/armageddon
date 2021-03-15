package xyz.spgamers.forge.armageddon.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.spgamers.forge.armageddon.util.BloodMoonHelper;
import xyz.spgamers.forge.armageddon.util.ModConstants;

@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public final class ClientEventHandler
{
	private ClientEventHandler()
	{
		throw new IllegalStateException();
	}

	@SubscribeEvent
	public static void onFogDensity(EntityViewRenderEvent.FogDensity event)
	{
		if(BloodMoonHelper.ClientHelper.isBloodMoonEnabled())
		{
			event.setDensity(.025F);
			event.setCanceled(true);
		}
	}
}
