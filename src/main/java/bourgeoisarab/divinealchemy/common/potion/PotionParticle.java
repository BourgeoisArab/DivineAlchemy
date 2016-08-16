package bourgeoisarab.divinealchemy.common.potion;

import java.util.Collection;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionParticle extends ModPotion {

	public PotionParticle(String name, boolean isBadEffect, int colour) {
		super(name, isBadEffect, colour);
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		Collection<PotionEffect> effects = entity.getActivePotionEffects();
		for (PotionEffect e : effects) {
			if (e.getIsShowParticles()) {
				e.showParticles = false;
			}
		}
	}
}
