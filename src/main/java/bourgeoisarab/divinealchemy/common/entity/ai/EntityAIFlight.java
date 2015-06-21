package bourgeoisarab.divinealchemy.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIFlight extends EntityAIBase {

	private EntityLiving entity;
	private EntityLivingBase attackTarget;
	private boolean inFlight;

	public EntityAIFlight(EntityLivingBase entity) {
		this.entity = (EntityMob) entity;
		setMutexBits(0);
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void updateTask() {
		if (!entity.isAirBorne) {
			inFlight = false;
		}

		if (inFlight) {
			hover();
		}

		if (entity instanceof EntityMob) {
			EntityMob mob = (EntityMob) entity;
			followAttackTarget(mob, mob.getAttackTarget());
		} else if (entity instanceof EntityAnimal) {
			EntityAnimal animal = (EntityAnimal) entity;
		} else if (entity instanceof EntityVillager) {
			EntityVillager villager = (EntityVillager) entity;
			flyAway();
		}
	}

	private void hover() {
		entity.fallDistance = 0.0F;
		if (entity.motionY < 0.0F) {
			entity.motionY = 0.0F;
		}
	}

	private void followAttackTarget(EntityMob mob, EntityLivingBase target) {
		if (target == null || mob == null) {
			return;
		}
		if (target.posY > mob.posY) {
			mob.moveFlying(0.0F, 1.0F, 0.0F);
		}
	}

	private void flyAway() {
		entity.moveFlying(0.0F, 0.5F, 0.0F);
	}
}
