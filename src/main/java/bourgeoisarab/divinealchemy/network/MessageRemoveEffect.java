package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.utility.Log;

public class MessageRemoveEffect extends MessageBase<MessageRemoveEffect> {

	public int entityID;
	public byte potionID;
	public byte amplifier;

	public MessageRemoveEffect() {

	}

	public MessageRemoveEffect(EntityLivingBase entity, int id, int amplifier) {
		entityID = entity.getEntityId();
		potionID = (byte) id;
		this.amplifier = (byte) amplifier;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		potionID = buf.readByte();
		amplifier = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeByte(potionID);
		buf.writeByte(entityID);
	}

	@Override
	public void handleClientSide(MessageRemoveEffect msg, EntityPlayer player) {
		World world = DivineAlchemy.proxy.getClientWorld();
		Potion potion = ModPotion.getPotion(msg.potionID);
		if (potion instanceof ModPotion) {
			Entity e = world.getEntityByID(msg.entityID);
			if (e instanceof EntityLivingBase) {
				((ModPotion) potion).removeEffect((EntityLivingBase) e, msg.amplifier);
			}
		}
	}

	@Override
	public void handleServerSide(MessageRemoveEffect msg, EntityPlayer player) {
		Log.warn("MessageRemoveEffect handled server side");
	}

}
