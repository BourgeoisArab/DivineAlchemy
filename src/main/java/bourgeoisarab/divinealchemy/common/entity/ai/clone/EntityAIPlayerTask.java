package bourgeoisarab.divinealchemy.common.entity.ai.clone;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class EntityAIPlayerTask extends EntityAIBase {

	protected final EntityPlayer player;

	public EntityAIPlayerTask(EntityPlayer player) {
		this.player = player;
	}

}
