package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionDay extends ModPotion {

	public PotionDay(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect) {
		if (entity.worldObj.getWorldTime() > 12000) {
			entity.worldObj.setWorldTime(1000);
		}
	}

}
