package xyz.spgamers.forge.armageddon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spgamers.forge.armageddon.init.ModEntities;
import xyz.spgamers.forge.armageddon.util.ModConstants;

import java.util.Arrays;

public final class EntityTypeTagGenerator extends EntityTypeTagsProvider
{
	public EntityTypeTagGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, ModConstants.MOD_ID, fileHelper);
	}

	@Override
	protected void registerTags()
	{
		EntityType<?>[] passive = Arrays.asList(
				ModEntities.PIG_ZOMBIE.get(),
				ModEntities.COW_ZOMBIE.get(),
				ModEntities.PANDA_ZOMBIE.get(),
				/*ModEntities.POLAR_BEAR_ZOMBIE.get(),*/
				ModEntities.FOX_ZOMBIE.get(),
				/*ModEntities.WOLF_ZOMBIE.get(),*/
				/*ModEntities.RABBIT_ZOMBIE.get(),*/
				ModEntities.CHICKEN_ZOMBIE.get(),
				ModEntities.SHEEP_ZOMBIE.get()
				/*ModEntities.FIRE_ZOMBIE.get(),*/
				/*ModEntities.EXPLOSIVE_ZOMBIE.get(),*/
				/*ModEntities.TELEPORTING_ZOMBIE.get(),*/
		).toArray(new EntityType[0]);

		/*EntityType<?>[] special = Arrays.asList(
				ModEntities.FIRE_ZOMBIE.get(),
				ModEntities.EXPLOSIVE_ZOMBIE.get(),
				ModEntities.TELEPORTING_ZOMBIE.get()
		).toArray(new EntityType[0]);*/

		getOrCreateBuilder(ModConstants.Entities.EntityTypeTags.MOD_PASSIVE_ZOMBIES)
				.add(passive);

		/*getOrCreateBuilder(ModConstants.Entities.EntityTypeTags.MOD_SPECIAL_ZOMBIES)
				.add(hostile);*/

		getOrCreateBuilder(ModConstants.Entities.EntityTypeTags.MOD_ZOMBIES)
				// .addTag(ModConstants.Entities.EntityTypeTags.MOD_SPECIAL_ZOMBIES)
				.addTag(ModConstants.Entities.EntityTypeTags.MOD_PASSIVE_ZOMBIES);

		getOrCreateBuilder(ModConstants.Entities.EntityTypeTags.FORGE_ZOMBIE)
				.add(EntityType.ZOMBIE);

		getOrCreateBuilder(ModConstants.Entities.EntityTypeTags.FORGE_ZOMBIES)
				.addTag(ModConstants.Entities.EntityTypeTags.FORGE_ZOMBIE)
				.addTag(ModConstants.Entities.EntityTypeTags.MOD_ZOMBIES);
	}
}
