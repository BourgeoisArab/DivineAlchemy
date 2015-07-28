package bourgeoisarab.divinealchemy.common.potion;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PotionFiendFyre extends PotionPerformEffect {

	public PotionFiendFyre(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	@Override
	public void performEffect(EntityLivingBase entity, PotionEffect potionEffect) {
		World world = entity.worldObj;
		entity.extinguish();
		if (entity.worldObj.getWorldTime() % 10 == 0) {
			entity.attackEntityFrom(DamageSource.inFire, 2.0F);
		}
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.boundingBox);
		for (EntityLivingBase e : entities) {
			if (e != entity) {
				e.addPotionEffect(new PotionEffect(id, 60, 0));
			}
		}
	}

}
