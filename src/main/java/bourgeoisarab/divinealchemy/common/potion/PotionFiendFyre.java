package bourgeoisarab.divinealchemy.common.potion;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PotionFiendFyre extends ModPotion {

	public PotionFiendFyre(String name, boolean isBadEffect, int colour) {
		super(name, isBadEffect, colour);
		setPotionName("potion.fiendfyre");
	}

	@Override
	public void applyEffect(EntityLivingBase entity, PotionEffect potionEffect, int amplifier) {
		World world = entity.worldObj;
		entity.extinguish();
		if (entity.worldObj.getWorldTime() % 10 == 0) {
			entity.attackEntityFrom(DamageSource.inFire, 2.0F);
		}

		if (entity.getCollisionBoundingBox() == null) {
			return;
		} else {
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getCollisionBoundingBox());
			for (EntityLivingBase e : entities) {
				if (e != entity) {
					e.addPotionEffect(new PotionEffect(id, 60, 0));
				}
			}
		}
	}

}
