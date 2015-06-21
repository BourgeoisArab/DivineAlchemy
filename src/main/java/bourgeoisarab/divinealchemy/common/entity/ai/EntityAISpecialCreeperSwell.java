package bourgeoisarab.divinealchemy.common.entity.ai;

import bourgeoisarab.divinealchemy.common.entity.EntitySpecialCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISpecialCreeperSwell extends EntityAIBase {

	EntitySpecialCreeper swellingCreeper;
	EntityLivingBase creeperAttackTarget;

	public EntityAISpecialCreeperSwell(EntitySpecialCreeper entity) {
		swellingCreeper = entity;
		setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = swellingCreeper.getAttackTarget();
		return swellingCreeper.getCreeperState() > 0 || entitylivingbase != null && swellingCreeper.getDistanceSqToEntity(entitylivingbase) < 9.0D;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		swellingCreeper.getNavigator().clearPathEntity();
		creeperAttackTarget = swellingCreeper.getAttackTarget();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		creeperAttackTarget = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		if (creeperAttackTarget == null) {
			swellingCreeper.setCreeperState(-1);
		} else if (swellingCreeper.getDistanceSqToEntity(creeperAttackTarget) > 49.0D) {
			swellingCreeper.setCreeperState(-1);
		} else if (!swellingCreeper.getEntitySenses().canSee(creeperAttackTarget)) {
			swellingCreeper.setCreeperState(-1);
		} else {
			swellingCreeper.setCreeperState(1);
		}
	}
}
