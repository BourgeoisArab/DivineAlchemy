package bourgeoisarab.divinealchemy.init;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.entity.EntityPlayerClone;
import bourgeoisarab.divinealchemy.common.entity.EntitySpecialCreeper;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;

public class ModEntities {

	public static void init() {
		EntityRegistry.registerModEntity(EntitySpecialCreeper.class, "ChargeCreeper", 101, DivineAlchemy.instance, 80, 3, false);
		EntityRegistry.registerModEntity(EntitySplashPotion.class, "SplashPotionBottle", 102, DivineAlchemy.instance, 80, 3, true);
		EntityRegistry.registerModEntity(EntityPlayerClone.class, "PlayerClone", 103, DivineAlchemy.instance, 128, 1, true);
	}

}
