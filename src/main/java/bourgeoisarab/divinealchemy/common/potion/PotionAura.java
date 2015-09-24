package bourgeoisarab.divinealchemy.common.potion;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;

public class PotionAura extends ModPotion implements IDivinePotion {

	public static final int duration = 100;
	private Potion normalPotion;

	public PotionAura(int id, boolean isBadEffect, int colour, Potion potion) {
		super(id, isBadEffect, colour);
		normalPotion = potion;
		setPotionName(normalPotion.getName() + "Aura");
	}

	public Potion getPotionCounterpart() {
		return normalPotion;
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect potionEffect, int amplifier) {
		int radius = getRadius(amplifier);
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius);
		List entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, box);
		for (Object i : entities) {
			EntityLivingBase e = (EntityLivingBase) i;
			double distance = e.getDistance(entity.posX, entity.posY, entity.posZ);
			if (distance <= radius && e != entity) {
				PotionEffect effect = new PotionEffect(normalPotion.getId(), duration, getAmplifier(amplifier));
				PotionEffect activeEffect = entity.getActivePotionEffect(normalPotion);
				if (activeEffect == null || activeEffect.getDuration() <= duration - 50) {
					e.addPotionEffect(effect);
				}
			}
		}
	}

	/**
	 * @param amplifier or aura effect
	 * @return radius around entity for effect to be applied
	 */
	public int getRadius(int amplifier) {
		switch (amplifier) {
			case 0:
				return 4;
			case 3:
				return 32;
			default:
				return 16;
		}
	}

	/**
	 * @param amplifier of aura effect
	 * @return amplifier of normal effect to be applied
	 */
	public int getAmplifier(int amplifier) {
		return amplifier < 2 ? 0 : 1;
	}
}
