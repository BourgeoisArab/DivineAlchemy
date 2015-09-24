package bourgeoisarab.divinealchemy.common.potion;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.entity.EntityPlayerClone;
import bourgeoisarab.divinealchemy.reference.Ref;

public class PotionClone extends ModPotion {

	public PotionClone(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
		setPotionName("potion.clone");
		setIcon(Ref.Location.PREFIX + "textures/potion/tome.png");
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {
		if (entity instanceof EntityPlayerMP && !entity.worldObj.isRemote && entity.worldObj.getWorldTime() % 10 == 0) {
			World world = entity.worldObj;
			EntityPlayerMP player = (EntityPlayerMP) entity;
			List<EntityPlayerClone> clones = EntityPlayerClone.getClones(player);
			if (clones.size() < effect.getAmplifier() + 1) {
				EntityPlayerClone clone = new EntityPlayerClone(player, effect.getAmplifier());
				clone.resetCoordinates();
				world.spawnEntityInWorld(clone);
			} else if (clones.size() > effect.getAmplifier() + 1) {
				EntityPlayerClone excessClone = clones.get(0);
				// excessClone.attackEntityFrom(DamageSource.outOfWorld, excessClone.getHealth());
			}
		}
	}

	@Override
	public void removeEffect(EntityLivingBase entity, int amplifier) {
		if (entity instanceof EntityPlayer) {
			for (EntityPlayerClone clone : EntityPlayerClone.getClones((EntityPlayer) entity)) {
				clone.attackEntityFrom(DamageSource.outOfWorld, clone.getHealth());
			}
		}
	}
}
