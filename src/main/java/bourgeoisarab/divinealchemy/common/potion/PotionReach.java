package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.utility.Log;

public class PotionReach extends ModPotion {

	public PotionReach(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
		setPotionName("potion.reach");
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			player.theItemInWorldManager.setBlockReachDistance(5.0D + (effect.getAmplifier() + 1) * 2);
			Log.info(player.theItemInWorldManager.getBlockReachDistance());
		}
	}

	@Override
	public void removeEffect(EntityLivingBase entity, int amplifier) {
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			player.theItemInWorldManager.setBlockReachDistance(5.0D);
		}
	}

}
