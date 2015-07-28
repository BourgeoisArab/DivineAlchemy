package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.utility.Log;

public class PotionReach extends PotionPerformEffect {

	public PotionReach(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	@Override
	public void performEffect(EntityLivingBase entity, PotionEffect effect) {
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			player.theItemInWorldManager.setBlockReachDistance(5.0D + (effect.getAmplifier() + 1) * 2);
			Log.info(player.theItemInWorldManager.getBlockReachDistance());
		}
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap map, int i) {
		super.removeAttributesModifiersFromEntity(entity, map, i);
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			player.theItemInWorldManager.setBlockReachDistance(5.0D);
		}
	}

}
