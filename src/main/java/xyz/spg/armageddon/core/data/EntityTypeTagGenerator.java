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
		// region: Zombies
		// region: Humanoid
		tag(ATags.EntityTypes.ZOMBIES_HUMANOID)
				.add(
						/* Vanilla */
						EntityType.ZOMBIE,
						EntityType.ZOMBIE_VILLAGER,
						EntityType.DROWNED,
						EntityType.HUSK
				);
		// endregion

		// region: Passive
		tag(ATags.EntityTypes.ZOMBIES_PASSIVE)
				.add(
						/* Modded */
						/*AEntityTypes.PANDA_ZOMBIE,*/
						/*AEntityTypes.POLAR_BEAR_ZOMBIE,*/
						/*AEntityTypes.FOX_ZOMBIE,*/
						AEntityTypes.WOLF_ZOMBIE,
						AEntityTypes.RABBIT_ZOMBIE,
						AEntityTypes.CHICKEN_ZOMBIE,
						AEntityTypes.SHEEP_ZOMBIE,
						AEntityTypes.COW_ZOMBIE,
						AEntityTypes.PIG_ZOMBIE,

						/* Vanilla */
						EntityType.ZOMBIE_VILLAGER,
						EntityType.ZOMBIE_HORSE,
						EntityType.ZOGLIN
				);
		// endregion

		// region: Hostile
		tag(ATags.EntityTypes.ZOMBIES_HOSTILE)
				.add(
						/* Modded */
						/*AEntityTypes.BLAZE_ZOMBIE,*/
						/*AEntityTypes.EXPLOSIVE_ZOMBIE,*/
						/*AEntityTypes.TELEPORTING_ZOMBIE,*/

						/* Vanilla */
						EntityType.ZOMBIE,
						EntityType.DROWNED,
						EntityType.HUSK
				);
		// endregion

		// region: Generic
		tag(ATags.EntityTypes.ZOMBIES)
				.add(
						/* Modded */
						/*AEntityTypes.PANDA_ZOMBIE,*/
						/*AEntityTypes.POLAR_BEAR_ZOMBIE,*/
						/*AEntityTypes.FOX_ZOMBIE,*/
						AEntityTypes.WOLF_ZOMBIE,
						AEntityTypes.RABBIT_ZOMBIE,
						AEntityTypes.CHICKEN_ZOMBIE,
						AEntityTypes.SHEEP_ZOMBIE,
						AEntityTypes.COW_ZOMBIE,
						AEntityTypes.PIG_ZOMBIE,

						/*AEntityTypes.BLAZE_ZOMBIE,*/
						/*AEntityTypes.EXPLOSIVE_ZOMBIE,*/
						/*AEntityTypes.TELEPORTING_ZOMBIE,*/

						/* Vanilla */
						EntityType.ZOMBIE,
						EntityType.ZOMBIE_VILLAGER,
						EntityType.ZOMBIE_HORSE,
						EntityType.ZOMBIFIED_PIGLIN,
						EntityType.DROWNED,
						EntityType.HUSK,
						EntityType.ZOGLIN
				)
				.addTags(
						ATags.EntityTypes.ZOMBIES_HUMANOID,
						ATags.EntityTypes.ZOMBIES_PASSIVE,
						ATags.EntityTypes.ZOMBIES_HOSTILE
				);
		// endregion
		// endregion

		// region: Eggs
		tag(EntityTypeTags.IMPACT_PROJECTILES).add(AEntityTypes.ROTTEN_EGG);
		tag(ATags.EntityTypes.EGGS).add(EntityType.EGG, AEntityTypes.ROTTEN_EGG);
		// endregion
	}

	@Override
	public String getName()
	{
		return "Armageddon-EntityType-Generator";
	}
}
