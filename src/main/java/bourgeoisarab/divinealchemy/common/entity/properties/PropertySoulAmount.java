package bourgeoisarab.divinealchemy.common.entity.properties;

import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PropertySoulAmount implements IExtendedEntityProperties {

	@Override
	public void saveNBTData(NBTTagCompound nbt) {

	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {

	}

	@Override
	public void init(Entity entity, World world) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			player.getEntityData().setFloat(Ref.NBT.SOUL_AMOUNT, 1.0F);
		}
	}

}
