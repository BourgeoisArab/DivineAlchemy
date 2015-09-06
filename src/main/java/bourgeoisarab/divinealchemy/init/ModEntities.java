package bourgeoisarab.divinealchemy.init;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.entity.EntityPlayerClone;
import bourgeoisarab.divinealchemy.common.entity.EntitySpecialCreeper;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

	public static void init() {
		EntityRegistry.registerGlobalEntityID(EntitySpecialCreeper.class, "ChargeCreeper", 101, 0x3300FF, 0x00FF33);
		EntityRegistry.registerModEntity(EntitySpecialCreeper.class, "ChargeCreeper", 101, DivineAlchemy.instance, 80, 3, false);
		EntityRegistry.registerGlobalEntityID(EntitySplashPotion.class, "SplashPotionBottle", 102);
		EntityRegistry.registerModEntity(EntitySplashPotion.class, "SplashPotionBottle", 102, DivineAlchemy.instance, 80, 3, true);
		EntityRegistry.registerGlobalEntityID(EntityPlayerClone.class, "PlayerClone", 103);
		EntityRegistry.registerModEntity(EntityPlayerClone.class, "PlayerClone", 103, DivineAlchemy.instance, 128, 1, true);
	}

}
