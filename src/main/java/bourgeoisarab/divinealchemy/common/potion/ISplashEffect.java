package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;

public interface ISplashEffect {

	public void applySplashEffect(World world, double x, double y, double z, EntitySplashPotion entity, PotionEffect effect);

}
