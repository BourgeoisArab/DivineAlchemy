package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import bourgeoisarab.divinealchemy.common.item.ItemPedestalCrystal;
import bourgeoisarab.divinealchemy.init.ModItems;

public class TEPedestal extends TEPowered implements IInventory {

	protected ItemStack stack;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		stack = ItemStack.loadItemStackFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (stack != null) {
			stack.writeToNBT(nbt);
		}
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public String getName() {
		return "Pedestal";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return index == 0 ? stack : null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (index == 0) {
			stack.stackSize -= count;
			markDirty();
			return stack;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index == 0) {
			ItemStack toReturn = stack;
			stack = null;
			markDirty();
			return toReturn;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index == 0) {
			this.stack = stack;
			if (this.stack == null) {
				buffer.setMaxEnergy(0);
			} else if (this.stack.getItem() == ModItems.pedestalCrystal) {
				buffer.setMaxEnergy(1000);
			}
			markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 0 && this.stack == null && stack.getItem() instanceof ItemPedestalCrystal;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		stack = null;
		markDirty();
	}

}
