package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import bourgeoisarab.divinealchemy.network.MessageCancelFlight;
import bourgeoisarab.divinealchemy.network.NetworkHandler;

public class PotionFlight extends ModPotion {

	public PotionFlight(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap map, int i) {
		super.removeAttributesModifiersFromEntity(entity, map, i);
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
			NetworkHandler.sendTo(new MessageCancelFlight(), (EntityPlayerMP) player);
		} else if (entity instanceof EntityLiving) {
			EntityLiving entityLiving = (EntityLiving) entity;
			// EntityAIBase task = null;
			// for (int i = 0; i < living.tasks.taskEntries.size(); i++) {
			// if (living.tasks.taskEntries.get(i) instanceof EntityAIFlight) {
			// task = (EntityAIBase) living.tasks.taskEntries.get(i);
			// }
			// }
			// living.tasks.removeTask(task);
		}
	}

}
