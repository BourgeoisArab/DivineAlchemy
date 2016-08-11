package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class PotionTemporal extends ModPotion {

	public static final String SLOWED = "slowed";
	public static final String LAST_X = "lastMotionX";
	public static final String LAST_Z = "lastMotionZ";

	public PotionTemporal(String name, boolean isBadEffect, int colour) {
		super(name, isBadEffect, colour);
		setPotionName("potion.temporal");
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		NBTTagCompound tag = entity.getEntityData();
		double multiplier = Math.pow(2, -amplifier - 1);
		if (tag.getBoolean(SLOWED)) {
			double lastMotionX = tag.getDouble(LAST_X);
			double lastMotionZ = tag.getDouble(LAST_Z);
			entity.motionX = lastMotionX + (entity.motionX - lastMotionX) * multiplier - lastMotionX;
			entity.motionZ = lastMotionZ + (entity.motionZ - lastMotionZ) * multiplier - lastMotionZ;
		} else {
			entity.motionX = entity.motionX * multiplier - entity.motionX;
			entity.motionZ = entity.motionZ * multiplier - entity.motionZ;
			tag.setBoolean(SLOWED, true);
		}
		if (!entity.onGround) {
			entity.motionY += 0.0734 * 0.25 * (amplifier + 1);
		}
		tag.setDouble(LAST_X, entity.motionX);
		tag.setDouble(LAST_Z, entity.motionZ);
	}

	@Override
	public void removeEffect(EntityLivingBase entity, int amplifier) {
		NBTTagCompound tag = entity.getEntityData();
		tag.removeTag(LAST_X);
		tag.removeTag(LAST_Z);
		tag.removeTag(SLOWED);
	}
}
