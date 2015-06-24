package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public abstract class PotionPerformEffect extends ModPotion {

	public PotionPerformEffect(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	public abstract void performEffect(EntityLivingBase entity, PotionEffect potionEffect);

}
