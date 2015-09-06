package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import bourgeoisarab.divinealchemy.common.block.multiblock.BlockMultiBlock;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.Log;

public class TEPotionVat extends TEPotionBrewerBase implements IInventory {

	public static final int[] minSize = new int[]{5, 3, 5};

	public ForgeDirection orientation;
	private InventoryBasic inv = new InventoryBasic("Potion Vat", true, 16);
	private int[] dimensions = new int[3];
	private boolean isMultiBlock = false;

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger(NBTNames.ORIENTATION, orientation.ordinal());
		nbt.setIntArray(NBTNames.DIMENSIONS, dimensions);
		nbt.setBoolean(NBTNames.MULTIBLOCK, isMultiBlock);
		NBTTagCompound invTag = new NBTTagCompound();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null) {
				stack.writeToNBT(invTag);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		orientation = ForgeDirection.VALID_DIRECTIONS[nbt.getInteger(NBTNames.ORIENTATION)];
		dimensions = nbt.getIntArray(NBTNames.DIMENSIONS);
		isMultiBlock = nbt.getBoolean(NBTNames.MULTIBLOCK);
	}

	public void setMultiBlock(boolean multiBlock, int x, int y, int z) {
		isMultiBlock = multiBlock;
		if (isMultiBlock) {
			dimensions[0] = x;
			dimensions[1] = y;
			dimensions[2] = z;
		} else {
			dimensions[0] = 0;
			dimensions[1] = 0;
			dimensions[2] = 0;
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
	}

	public void checkMultiBlock() {
		int[] dims = checkDimensions();
		boolean foundInvalidBlock = false;
		if (dims[0] * 2 + 1 < minSize[0] || dims[1] * 2 + 1 < minSize[1] || dims[2] < minSize[2]) {
			foundInvalidBlock = true;
		}
		ForgeDirection right = getSide();
		ForgeDirection back = orientation.getOpposite();
		// Checks 4 walls of multiblock
		if (!foundInvalidBlock) {
			y:
			for (int i = 0; i < dims[1] * 2 + 1; i++) {
				int y = yCoord - dims[1] + i;
				int x = xCoord + right.offsetX * dims[0];
				int z = zCoord + right.offsetZ * dims[0];
				ForgeDirection loopDir = right;
				for (int k = 1; k < 5; k++) {
					loopDir = loopDir.getRotation(ForgeDirection.UP);
					for (int j = 0; j < (k % 2 == 0 ? dims[0] * 2 + 1 : dims[2]) - 1; j++) {
						Block block = worldObj.getBlock(x, y, z);
						if (!(block instanceof BlockMultiBlock) && (x != xCoord || y != yCoord || z != zCoord)) {
							foundInvalidBlock = true;
							break y;
						}
						x += loopDir.offsetX;
						z += loopDir.offsetZ;
					}
				}
			}
		}

		if (!foundInvalidBlock) {
			// Checks bottom floor and inside of multiblock
			y:
			for (int i = 0; i < dims[1] * 2 + 1; i++) {
				int y = yCoord - dims[1] + i;
				for (int j = 1; j < dims[2] - 1; j++) {
					int x = xCoord + right.offsetX * (dims[0] - 1) + back.offsetX * j;
					int z = zCoord + right.offsetZ * (dims[0] - 1) + back.offsetZ * j;
					for (int k = 0; k < dims[0] * 2 - 1; k++) {
						Block block = worldObj.getBlock(x, y, z);
						if (i == 0 && !(block instanceof BlockMultiBlock) || i > 0 && block != Blocks.air) {
							Log.info(block.getUnlocalizedName() + " at " + x + ", " + y + ", " + z);
							foundInvalidBlock = true;
							break y;
						}
						x += right.getOpposite().offsetX;
						z += right.getOpposite().offsetZ;
					}
				}
			}
		}

		if (foundInvalidBlock) {
			setMultiBlock(false, 0, 0, 0);
			Log.info("You suck at multiblocks!");
		} else {
			setMultiBlock(true, dims[0] * 2 + 1, dims[1] * 2 + 1, dims[2]);
			Log.info("Multiblocks are awesome!");
		}

	}

	public int[] checkDimensions() {
		int side = getLengthOfBlocks(getSide());
		int down = getLengthOfBlocks(ForgeDirection.DOWN);
		int back = getLengthOfBlocks(xCoord, yCoord - down, zCoord, orientation.getOpposite()) + 1;
		Log.info("Dimensions found: " + side + ", " + down + ", " + back);
		return new int[]{side, down, back};
	}

	private int getLengthOfBlocks(ForgeDirection dir) {
		return getLengthOfBlocks(xCoord, yCoord, zCoord, dir);
	}

	private int getLengthOfBlocks(int x, int y, int z, ForgeDirection dir) {
		int length = 0;
		x += dir.offsetX;
		y += dir.offsetY;
		z += dir.offsetZ;
		Block block = worldObj.getBlock(x, y, z);
		while (block instanceof BlockMultiBlock) {
			length++;
			ForgeDirection toBend = dir.getRotation(ForgeDirection.UP);
			if (dir == ForgeDirection.DOWN) {
				toBend = orientation.getOpposite();
			} else if (dir == orientation.getOpposite()) {
				toBend = ForgeDirection.UP;
			}
			if (worldObj.getBlock(x + toBend.offsetX, y + toBend.offsetY, z + toBend.offsetZ) instanceof BlockMultiBlock) {
				break;
			}
			x += dir.offsetX;
			y += dir.offsetY;
			z += dir.offsetZ;
			block = worldObj.getBlock(x, y, z);
		}
		return length;
	}

	/**
	 * @return ForgeDirection to the right of the orientation
	 */
	private ForgeDirection getSide() {
		return orientation.getRotation(ForgeDirection.UP);
	}

	@Override
	public int getSizeInventory() {
		return inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return inv.decrStackSize(slot, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return inv.getStackInSlotOnClosing(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv.setInventorySlotContents(slot, stack);
	}

	@Override
	public String getInventoryName() {
		return inv.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return inv.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return inv.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return inv.isUseableByPlayer(player);
	}

	@Override
	public void openInventory() {
		inv.openInventory();
	}

	@Override
	public void closeInventory() {
		inv.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return inv.isItemValidForSlot(slot, stack);
	}

}
