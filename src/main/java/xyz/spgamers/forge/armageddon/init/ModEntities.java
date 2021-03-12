package xyz.spgamers.forge.armageddon.init;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.spgamers.forge.armageddon.util.ModConstants;

public final class ModEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModConstants.MOD_ID);

	private ModEntities()
	{
		throw new IllegalStateException();
	}
}
