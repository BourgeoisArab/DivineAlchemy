package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import bourgeoisarab.divinealchemy.common.item.ItemEnergyStorage;

public class TEObelisk extends TEPowerProvider implements ITickable {

	public static List<TerribleObeliskEffect> evilThings = new ArrayList<TerribleObeliskEffect>();

	static {
		// TODO make these configurable
		evilThings.add(TerribleObeliskEffect.grassDecay);
		evilThings.add(TerribleObeliskEffect.leafDecay);
		evilThings.add(TerribleObeliskEffect.cropDecay);
		evilThings.add(TerribleObeliskEffect.dirtDecay);
		evilThings.add(TerribleObeliskEffect.woodDecay);
		evilThings.add(TerribleObeliskEffect.creatureDamage);
		evilThings.add(TerribleObeliskEffect.creatureKill);
	}

	protected byte stageOfEvilness = 0;
	protected ItemStack stack;
	public int range = 8;
	public int rangeSq = 64;

	public ItemStack getStoredItem() {
		return stack;
	}

	@Override
	public void update() {
		if (worldObj.getWorldTime() % 10 == 0) {
			// drainEnergy(500);
			if (!buffer.isFull()) {
				doEvilThing();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (stack != null) {
			nbt.setTag("Stack", stack.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Stack"));
	}

	public void setStoredItem(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public int drainEnergy(int amount) {
		int energy = 0;
		if (stack != null) {
			energy += ((ItemEnergyStorage) stack.getItem()).extractEnergy(stack, amount);
		}
		if (energy < amount) {
			energy += super.drainEnergy(amount - energy);
		}
		return energy;
	}

	protected void doEvilThing() {
		if (stageOfEvilness == 0) {
			stageOfEvilness = 1;
		}
		boolean moveToNextStage = true;
		for (int i = 0; i < evilThings.size(); i++) {
			TerribleObeliskEffect e = evilThings.get(i);
			if (e.stage == stageOfEvilness) {
				if (e.performTask(this, range, false)) {
					buffer.add(e.energy);
					moveToNextStage = false;
				}
			}
			if (buffer.isFull()) {
				break;
			}
		}
		if (moveToNextStage) {
			if (stageOfEvilness < getMaxEvilStage()) {
				stageOfEvilness++;
			} else {
				doWorstEvilThing();
			}
		}
	}

	private byte getMaxEvilStage() {
		byte n = 1;
		for (TerribleObeliskEffect e : evilThings) {
			if (e.stage > n) {
				n = (byte) e.stage;
			}
		}
		return n;
	}

	protected void doWorstEvilThing() {
		if (!worldObj.isRemote) {
			worldObj.newExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), range, true, true);
		}
	}
}
