package bourgeoisarab.divinealchemy.common.potion;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;

public class PotionMagnet extends ModPotion {

	public PotionMagnet(String name, boolean isBadEffect, int colour) {
		super(name, isBadEffect, colour);
		setPotionName("potion.magnet");
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		int radius = (int) Math.pow(2, amplifier + 1);
		double acceleration = Math.pow(2, amplifier) * 0.1;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			AxisAlignedBB bb = AxisAlignedBB.fromBounds(player.posX - radius, player.posY - radius, player.posZ - radius, player.posX + radius, player.posY + radius, player.posZ + radius);
			List<EntityXPOrb> orbs = player.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, bb);
			for (EntityXPOrb orb : orbs) {
				orb.motionX += acceleration * (player.posX - orb.posX - radius);
				orb.motionY += acceleration * (player.posY - orb.posY - radius);
				orb.motionZ += acceleration * (player.posZ - orb.posZ - radius);
			}
			List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, bb);
			for (EntityItem i : items) {
				ItemStack stack = i.getEntityItem();
				if (canAddStack(player.inventory, stack)) {
					i.motionX += acceleration * (player.posX - i.posX - radius);
					i.motionY += acceleration * (player.posY - i.posY - radius);
					i.motionZ += acceleration * (player.posZ - i.posZ - radius);
				}
			}
		}

	}

	private boolean canAddStack(InventoryPlayer inv, ItemStack stack) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack j = inv.getStackInSlot(i);
			if (j == null) {
				return true;
			} else if (j.isItemEqual(stack) && j.stackSize < j.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}
}
