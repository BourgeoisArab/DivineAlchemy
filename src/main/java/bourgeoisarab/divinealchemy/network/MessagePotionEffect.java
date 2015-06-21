package bourgeoisarab.divinealchemy.network;

import io.netty.buffer.ByteBuf;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.utility.NBTHelper;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class MessagePotionEffect extends MessagePotisionedBase<MessagePotionEffect> {

	public NBTTagCompound effect;

	public MessagePotionEffect() {

	}

	public MessagePotionEffect(int x, int y, int z, NBTTagCompound effect) {
		super(x, y, z);
		this.effect = effect;
	}

	public MessagePotionEffect(int x, int y, int z, List<PotionEffect> effect) {
		this(x, y, z, NBTHelper.setEffectsForNBT(new NBTTagCompound(), effect));
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		effect = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeTag(buf, effect);
	}

	@Override
	public void handleClientSide(MessagePotionEffect msg, int x, int y, int z, EntityPlayer player) {
		World world = DivineAlchemy.proxy.getClientWorld();
		if (world.getTileEntity(x, y, z) instanceof TEBrewingCauldron) {
			TEBrewingCauldron tile = (TEBrewingCauldron) world.getTileEntity(x, y, z);
			tile.setEffects(NBTHelper.getEffectsFromNBT(msg.effect));
		}
	}

	@Override
	public void handleServerSide(MessagePotionEffect msg, int x, int y, int z, EntityPlayer player) {

	}

}
