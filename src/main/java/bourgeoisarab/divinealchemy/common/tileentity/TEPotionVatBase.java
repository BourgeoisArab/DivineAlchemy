package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public abstract class TEPotionVatBase extends TileEntity implements IFluidTank, IInventory {
	
	public boolean isMultiBlock;
	public int[] multiBlockSize;
	public final int capacity = 10000;
	protected int tier;
	protected FluidTank tank;
	protected InventoryBasic inv;
	
	public TEPotionVatBase() {
		multiBlockSize = new int[3];
		inv = new InventoryBasic("TEPotionVat", true, 16);
	}
	
	public abstract int getEnergyLevel();
	
	@Override
	public FluidStack getFluid() {
		return tank.getFluid();
	}

	@Override
	public int getFluidAmount() {
		return tank.getFluidAmount();
	}

	@Override
	public int getCapacity() {
		return tank.getCapacity();
	}

	@Override
	public FluidTankInfo getInfo() {
		return tank.getInfo();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	public int getTier() {
		return tier;
	}

	@Override
	public int getSizeInventory() {
		return this.inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inv.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int number) {
		return this.inv.decrStackSize(slot, number);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return this.inv.getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inv.setInventorySlotContents(slot, stack);
	}

	@Override
	public String getInventoryName() {
		return this.inv.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.inv.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return this.inv.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.inv.isUseableByPlayer(player);
	}

	@Override
	public void openInventory() {
		this.inv.openInventory();
		
	}

	@Override
	public void closeInventory() {
		this.inv.closeInventory();		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return this.inv.isItemValidForSlot(slot, stack);
	}

}
