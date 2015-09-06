package bourgeoisarab.divinealchemy.common.entity.ai.clone;

import net.minecraft.entity.player.EntityPlayer;

public abstract class EntityAIPlayerWander extends EntityAIPlayerTask {

	public EntityAIPlayerWander(EntityPlayer player) {
		super(player);
	}

	// private double xPosition;
	// private double yPosition;
	// private double zPosition;
	// private double speed;
	//
	// public EntityAIPlayerWander(EntityPlayer player) {
	// super(player);
	// }
	//
	// /**
	// * Returns whether the EntityAIBase should begin execution.
	// */
	// @Override
	// public boolean shouldExecute() {
	// if (player.getAge() >= 100) {
	// return false;
	// } else if (player.getRNG().nextInt(120) != 0) {
	// return false;
	// } else {
	// Vec3 vec3 = RandomPositionGenerator.findRandomTarget(player, 10, 7);
	//
	// if (vec3 == null) {
	// return false;
	// } else {
	// xPosition = vec3.xCoord;
	// yPosition = vec3.yCoord;
	// zPosition = vec3.zCoord;
	// return true;
	// }
	// }
	// }
	//
	// /**
	// * Returns whether an in-progress EntityAIBase should continue executing
	// */
	// @Override
	// public boolean continueExecuting() {
	// return !player.getNavigator().noPath();
	// }
	//
	// /**
	// * Execute a one shot task or start executing a continuous task
	// */
	// @Override
	// public void startExecuting() {
	// player.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
	// }

}
