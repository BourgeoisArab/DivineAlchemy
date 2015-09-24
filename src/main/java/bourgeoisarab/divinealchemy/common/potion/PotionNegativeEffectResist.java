package bourgeoisarab.divinealchemy.common.potion;

import java.util.Iterator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class PotionNegativeEffectResist extends ModPotion {

	public PotionNegativeEffectResist(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect potionEffect, int amplifier) {
		if (entity.getActivePotionEffects().size() > 1) {
			Iterator<PotionEffect> i = entity.getActivePotionEffects().iterator();
			ItemStack cure = new ItemStack(Blocks.dragon_egg);
			while (i.hasNext()) {
				PotionEffect effect = i.next();
				if (ModPotion.getPotion(effect.getPotionID()).isBadEffect()) {
					effect.addCurativeItem(cure);
				}
			}
			entity.curePotionEffects(cure);
		}
	}

}
