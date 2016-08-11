package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionEffectResist extends ModPotion {

	public PotionEffectResist(String name, boolean isBadEffect, int colour) {
		super(name, isBadEffect, colour);
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		if (entity.getActivePotionEffects().size() > 1) {
			entity.clearActivePotions();
			entity.addPotionEffect(effect);
		}
	}

}
