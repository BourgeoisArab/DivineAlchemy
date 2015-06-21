package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;

public class TEPotion extends TileEntity implements IEffectProvider {

	private List<PotionEffect> effects = new ArrayList<PotionEffect>();
	private float instability = 0.9F;

	public TEPotion() {
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public List<PotionEffect> getEffects() {
		return effects;
	}

	@Override
	public void setEffects(List<PotionEffect> effects) {
		this.effects = effects;
		if (this.effects == null) {
			this.effects = new ArrayList<PotionEffect>();
		}
		// this.effects.add(new PotionEffect(Potion.wither.id, 0, 2));
	}

	@Override
	public float getInstability() {
		return instability;
	}

	@Override
	public void setInstability(float instability) {
		this.instability = instability;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		NBTTagCompound tagEffects = nbt.getCompoundTag("Effects");
		for (int i = 0; i < 16; i++) {
			try {
				int[] e = tagEffects.getIntArray("PotionEffect" + i);
				effects.add(new PotionEffect(e[0], e[1], e[2]));
			} catch (Exception e) {
				break;
			}
		}
		nbt.setTag("Effects", tagEffects);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (effects == null) {
			return;
		}
		NBTTagCompound tagEffects = new NBTTagCompound();
		for (int i = 0; i < effects.size(); i++) {
			PotionEffect e = effects.get(i);
			tagEffects.setIntArray("PotionEffect" + i, new int[]{e.getPotionID(), e.getDuration(), e.getAmplifier()});
		}
		nbt.setTag("Effects", tagEffects);
	}

	@Override
	public Packet getDescriptionPacket() {
		Packet packet = super.getDescriptionPacket();
		NBTTagCompound nbt = packet != null ? ((S35PacketUpdateTileEntity) packet).func_148857_g() : new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(networkManager, packet);
		readFromNBT(packet.func_148857_g());
	}
}
