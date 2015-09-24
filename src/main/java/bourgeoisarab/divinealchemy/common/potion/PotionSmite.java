package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.potion.PotionEffect;

public class PotionSmite extends ModPotion {

	public PotionSmite(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
		setPotionName("potion.smite");
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		for (int i = 0; i < Math.pow(2, amplifier); i++) {
			entity.worldObj.addWeatherEffect(new EntityLightningBolt(entity.worldObj, entity.posX, entity.posY, entity.posZ));
		}
	}

}
