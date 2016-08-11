package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionDay extends ModPotion {

	public PotionDay(String name, boolean isBadEffect, int colour) {
		super(name, isBadEffect, colour);
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		if (entity.worldObj.getWorldTime() > 12000) {
			entity.worldObj.setWorldTime(1000);
		}
	}

}
