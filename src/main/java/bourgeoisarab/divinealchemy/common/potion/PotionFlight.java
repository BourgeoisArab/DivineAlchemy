package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.common.entity.ai.EntityAIFlight;

public class PotionFlight extends ModPotion {

	public PotionFlight(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
		setPotionName("potion.flight");
		setIcon("minecraft:textures/items/feather.png");
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			player.capabilities.allowFlying = true;
		} else if (entity instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) entity;
			// add flight task to entity
		}
	}

	@Override
	public void removeEffect(EntityLivingBase entity, int amplifier) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
		} else if (entity instanceof EntityLiving) {
			EntityLiving entityLiving = (EntityLiving) entity;
			EntityAIBase task = null;
			for (int i = 0; i < entityLiving.tasks.taskEntries.size(); i++) {
				if (entityLiving.tasks.taskEntries.get(i) instanceof EntityAIFlight) {
					task = (EntityAIBase) entityLiving.tasks.taskEntries.get(i);
				}
			}
			entityLiving.tasks.removeTask(task);
		}
	}

}
