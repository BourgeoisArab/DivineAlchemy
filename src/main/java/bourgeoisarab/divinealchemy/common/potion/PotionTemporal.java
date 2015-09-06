package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.utility.Log;

public class PotionTemporal extends ModPotion {

	public static final String SLOWED = "slowed";
	public static final String LAST_X = "lastMotionX";
	public static final String LAST_Y = "lastMotionY";
	public static final String LAST_Z = "lastMotionZ";

	public PotionTemporal(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
		setPotionName("potion.temporal");
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect) {
		// Log.info("x: " + entity.motionX);
		// Log.info("y: " + entity.motionY);
		// Log.info("z: " + entity.motionZ);
		NBTTagCompound tag = entity.getEntityData();
		double multiplier = Math.pow(4, -effect.getAmplifier() - 1);
		if (tag.getBoolean(SLOWED)) {
			double lastMotionX = tag.getDouble(LAST_X);
			double lastMotionY = tag.getDouble(LAST_Y);
			double lastMotionZ = tag.getDouble(LAST_Z);
			Log.info(lastMotionX + ", " + lastMotionY + ", " + lastMotionZ);
			entity.motionX = lastMotionX + (entity.motionX - lastMotionX) * multiplier;
			entity.motionY = lastMotionY + (entity.motionY - lastMotionY) * multiplier;
			entity.motionZ = lastMotionZ + (entity.motionZ - lastMotionZ) * multiplier;
		} else {
			entity.motionX *= multiplier;
			entity.motionY *= multiplier;
			entity.motionZ *= multiplier;
			tag.setBoolean(SLOWED, true);
		}
		tag.setDouble(LAST_X, entity.motionX);
		tag.setDouble(LAST_Y, entity.motionY);
		tag.setDouble(LAST_Z, entity.motionZ);
	}

	@Override
	public void removeEffect(EntityLivingBase entity, int amplifier) {
		NBTTagCompound tag = entity.getEntityData();
		tag.setDouble(LAST_X, 0.0F);
		tag.setDouble(LAST_Y, 0.0F);
		tag.setDouble(LAST_Z, 0.0F);
		tag.setBoolean(SLOWED, false);
	}
}
