package xyz.spg.armageddon.core.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import xyz.spg.armageddon.shared.AEntityTypes;
import xyz.spg.armageddon.shared.ATags;
import xyz.spg.armageddon.shared.Armageddon;

public final class EntityTypeTagGenerator extends EntityTypeTagsProvider
{
	public EntityTypeTagGenerator(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, Armageddon.ID_MOD, fileHelper);
	}

	@Override
	protected void addTags()
	{
		// Zombies
		tag(ATags.EntityTypes.ZOMBIES_SPECIAL).add(
				/*AEntityTypes.BLAZE_ZOMBIE,*/
				/*AEntityTypes.EXPLOSIVE_ZOMBIE,*/
				/*AEntityTypes.TELEPORTING_ZOMBIE*/

				// Vanilla
				EntityType.ZOMBIFIED_PIGLIN
		);

		tag(ATags.EntityTypes.ZOMBIES_MOB).add(
				/*AEntityTypes.PANDA_ZOMBIE,*/
				/*AEntityTypes.POLAR_BEAR_ZOMBIE,*/
				/*AEntityTypes.FOX_ZOMBIE,*/
				/*AEntityTypes.WOLF_ZOMBIE,*/
				/*AEntityTypes.RABBIT_ZOMBIE,*/
				/*AEntityTypes.CHICKEN_ZOMBIE,*/
				/*AEntityTypes.SHEEP_ZOMBIE,*/
				AEntityTypes.COW_ZOMBIE,
				AEntityTypes.PIG_ZOMBIE,

				// Vanilla
				EntityType.ZOMBIE,
				EntityType.DROWNED,
				EntityType.HUSK,

				EntityType.ZOMBIE_HORSE,
				EntityType.ZOMBIE_VILLAGER
		);

		tag(ATags.EntityTypes.ZOMBIES)
				.addTags(ATags.EntityTypes.ZOMBIES_MOB, ATags.EntityTypes.ZOMBIES_SPECIAL);

		// Eggs
		tag(EntityTypeTags.IMPACT_PROJECTILES).add(AEntityTypes.ROTTEN_EGG);
		tag(ATags.EntityTypes.EGGS).add(EntityType.EGG, AEntityTypes.ROTTEN_EGG);
	}

	@Override
	public String getName()
	{
		return "Armageddon-EntityType-Generator";
	}
}
