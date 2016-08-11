package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEObeliskDark;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.NBTNames;

/**
 * Base class for items that store magical energy
 * 
 * @author Jakoubeck
 */
public class ItemEnergyStorage extends Item {

	protected final int maxEnergy;

	public ItemEnergyStorage(int maxEnergy) {
		this.maxEnergy = maxEnergy;
		setMaxStackSize(1);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

	protected boolean initTagCompound(ItemStack stack) {
		if (!(stack.getItem() instanceof ItemEnergyStorage)) {
			return false;
		}
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return true;
	}

	public void setEnergy(ItemStack stack, int amount) {
		if (!initTagCompound(stack)) {
			return;
		}
		stack.getTagCompound().setInteger(NBTNames.ENERGY_STORED, amount);
	}

	public int getEnergy(ItemStack stack) {
		if (!initTagCompound(stack)) {
			return 0;
		}
		return stack.getTagCompound().getInteger(NBTNames.ENERGY_STORED);
	}

	/**
	 * @param maximum to be extracted
	 * @return how much was successfully extracted
	 */
	public int extractEnergy(ItemStack stack, int max) {
		if (!initTagCompound(stack)) {
			return 0;
		}
		int stored = stack.getTagCompound().getInteger(NBTNames.ENERGY_STORED);
		int extracted = Math.min(max, stored);
		setEnergy(stack, stored - extracted);
		return extracted;
	}

	/**
	 * @param maximum to be added
	 * @return how much was successfully added
	 */
	public int addEnergy(ItemStack stack, int max) {
		if (!initTagCompound(stack)) {
			return 0;
		}
		int stored = stack.getTagCompound().getInteger(NBTNames.ENERGY_STORED);
		int added = Math.min(max, maxEnergy - stored);
		setEnergy(stack, stored + added);
		return added;
	}

	@Override
	public int getDamage(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger(NBTNames.ENERGY_STORED, 0);
			stack.setTagCompound(nbt);
		}
		return maxEnergy - stack.getTagCompound().getInteger(NBTNames.ENERGY_STORED);
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return true;
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, 0);
	}

	@Override
	public int getMaxDamage() {
		return maxEnergy;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getDamage(stack) != 0;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		int charge = (int) (15F * getDamage(stack) / getMaxDamage());
		return 0xFF0000 + (charge << 12) + (charge << 8) + (charge << 4) + charge;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		extractEnergy(stack, maxEnergy / 50);
		// addEnergy(stack, maxEnergy);
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
		ItemStack stack = new ItemStack(item, 1, 0);
		addEnergy(stack, maxEnergy);
		subItems.add(stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
		if (world.getWorldTime() % 20 == 0) {
			for (TileEntity tile : world.loadedTileEntityList) {
				if (tile instanceof TEObeliskDark && tile.getDistanceSq(entity.posX, entity.posY, entity.posZ) <= 144) {
					addEnergy(stack, ((TEObeliskDark) tile).drainEnergy(50));
				}
			}
		}
	}
}
