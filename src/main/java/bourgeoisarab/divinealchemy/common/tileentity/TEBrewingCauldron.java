package bourgeoisarab.divinealchemy.common.tileentity;

import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.network.MessagePotionEffect;
import bourgeoisarab.divinealchemy.network.NetworkHandler;
import bourgeoisarab.divinealchemy.utility.Log;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class TEBrewingCauldron extends TileEntity implements IFluidTank, IFluidHandler, IBrewingCauldron {

	private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);

	private final int totalBoilTime = 50;
	private int boilTime = -1;
	private boolean boiling = false;

	private float instability;
	private final float INSTABILITY_CONSTANT = 0.06944444444F;

	private List<PotionEffect> effects = new ArrayList<PotionEffect>();
	private List<PotionEffect> sideEffects = new ArrayList<PotionEffect>();
	private PotionIngredient[] ingredients = new PotionIngredient[16];
	public boolean unstable = true;
	public boolean persistent = false;
	public boolean splash;

	public TEBrewingCauldron() {

	}

	public void setBoil(boolean boil) {
		if (!boil) {
			boilTime = -1;
			boiling = false;
		} else {
			if (boilTime >= totalBoilTime) {
				boiling = true;
			} else if (boilTime < 0) {
				boilTime = 0;
			} else if (boilTime >= 0) {
				boilTime += 10;
			}
		}
	}

	public boolean isBoiling() {
		return boiling;
	}

	@Override
	public void addEffect(PotionEffect effect) {
		if (effects.size() < 16 && !effects.contains(effect)) {
			effects.add(effect);
			System.out.println(effects);
		}
	}

	@Override
	public void addSideEffect(PotionEffect effect) {
		if (getFluid() == null || effect == null) {
			return;
		}
		// TODO
	}

	@Override
	public List<PotionEffect> getEffects() {
		return effects;
	}

	@Override
	public float getInstability() {
		return getCauldronInstability() /* + (effects.size() * INSTABILITY_CONSTANT * getCauldronInstability()) */;
	}

	@Override
	public float getCauldronInstability() {
		switch (worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
			case 1:
				return 0.60F;
			case 2:
				return 0.50F;
			case 3:
				return 0.25F;
			case 4:
				return 0.125F;
			default:
				return 0.75F;
		}
	}

	@Override
	public int getMaxDuration() {
		return (int) (Math.pow(2, worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) * 1200);
	}

	@Override
	public void clearInstability() {
		sideEffects = new ArrayList<PotionEffect>();
		unstable = false;
		List<PotionEffect> newEffects = new ArrayList<PotionEffect>();
		for (int i = 0; i < effects.size(); i++) {
			newEffects.add(new PotionEffect(effects.get(i).getPotionID(), getMaxDuration(), effects.get(i).getAmplifier()));
		}
		effects = newEffects;
	}

	@Override
	public void setInstability(float instability) {
		Log.info("Nu-uh-uh!");
	}

	/**
	 * Converts items in the inventory to a list of effects
	 */
	@Override
	public void finaliseEffects() {
		if (!worldObj.isRemote) {
			for (int i = 0; i < ingredients.length; i++) {
				PotionIngredient ing = ingredients[i];
				if (ing != null) {
					ing.applyEffect(this);
				}
			}
			NetworkHandler.sendToAll(new MessagePotionEffect(xCoord, yCoord, zCoord, effects));
		}
	}

	/**
	 * Sorts the stored effects in order of priority
	 */
	public void sortEffects() {
		List<PotionIngredient> sorted = new ArrayList<PotionIngredient>();
		int priority = getHighestPriority(Integer.MAX_VALUE);
		while (sorted.size() < ingredients.length) {
			sorted.addAll(getIngredientsWithPriority(getHighestPriority(priority)));
			priority = getHighestPriority(priority);
		}
	}

	/**
	 * @param the previous highest priority that was already processed
	 * @return
	 */
	private int getHighestPriority(int priority) {
		int highest = Integer.MIN_VALUE;
		for (PotionIngredient i : ingredients) {
			highest = i.getPriority() < priority ? Math.max(i.getPriority(), highest) : highest;
		}
		return highest;
	}

	private List<PotionIngredient> getIngredientsWithPriority(int priority) {
		List<PotionIngredient> list = new ArrayList<PotionIngredient>();
		for (PotionIngredient i : ingredients) {
			if (i.getPriority() == priority) {
				list.add(i);
			}
		}
		return list;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt);

		boiling = nbt.getBoolean("Boiling");

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
		tank.writeToNBT(nbt);

		nbt.setBoolean("Boiling", boiling);

		NBTTagCompound tagEffects = new NBTTagCompound();
		for (int i = 0; i < effects.size(); i++) {
			PotionEffect e = effects.get(i);
			tagEffects.setIntArray("PotionEffect" + i, new int[]{e.getPotionID(), e.getDuration(), e.getAmplifier()});
		}
		nbt.setTag("Effects", tagEffects);
	}

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
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
			return null;
		}
		return drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (!isInventoryEmpty()) {
			finaliseEffects();
		}
		if (getFluidAmount() - maxDrain == 0) {
			effects.clear();
			boiling = false;
		}
		return tank.drain(maxDrain, doDrain);
	}

	public boolean isInventoryEmpty() {
		for (PotionIngredient i : ingredients) {
			if (i != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if (tank.getFluid() != null) {
			if (tank.getFluid().getFluid() == FluidRegistry.WATER && tank.getFluidAmount() < FluidContainerRegistry.BUCKET_VOLUME) {
				return true;
			}
			return false;
		} else
			return true;
	}

	public boolean canFill(Fluid fluid) {
		return canFill(ForgeDirection.UP, fluid);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}

	private void setFluid(FluidStack f) {
		tank.setFluid(f);
	}

	@Override
	public Packet getDescriptionPacket() {
		Packet packet = super.getDescriptionPacket();
		NBTTagCompound nbtTag = packet != null ? ((S35PacketUpdateTileEntity) packet).func_148857_g() : new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(networkManager, packet);
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public void updateEntity() {
		World world = worldObj;
		if (world.getWorldTime() % 10 == 0) {
			Block block = world.getBlock(xCoord, yCoord - 1, zCoord);

			if ((block == Blocks.lava || block == Blocks.fire) && getFluid() != null) {
				setBoil(true);
			} else {
				setBoil(false);
			}
		}
	}

	/**
	 * WARNING: Should not be used lightly; only usage should be in networking
	 */
	@Override
	public void setEffects(List<PotionEffect> effects) {
		this.effects = effects;
	}

	@Override
	public boolean addIngredient(ItemStack stack) {
		PotionIngredient ing = PotionIngredient.getIngredient(stack);
		if (ing == null) {
			return false;
		}

		if (getFluid().getFluid() == FluidRegistry.WATER) {
			setFluid(new FluidStack(ModFluids.fluidPotion, getFluidAmount()));
		} else if (getFluid().getFluid() == ModFluids.fluidHotMess) {
			return false;
		}

		while (stack.stackSize > 0) {
			for (int i = 0; i < ingredients.length; i++) {
				if (ingredients[i] == null) {
					ingredients[i] = ing;
					if (stack.stackSize <= 1) {
						return true;
					}
				}
			}
			stack.stackSize--;
		}
		return false;
	}

	public int countIngredient(PotionIngredient ingredient) {
		int count = 0;
		for (PotionIngredient i : ingredients) {
			if (i != null && ingredient.getEffectID() == i.getEffectID()) {
				count++;
			}
		}
		return count;
	}

	@Override
	public PotionIngredient[] getIngredients() {
		return ingredients;
	}

	public void makeHotMess() {
		setFluid(new FluidStack(ModFluids.fluidHotMess, getFluidAmount()));
		effects.clear();
		sideEffects.clear();
		ingredients = new PotionIngredient[16];
	}

	public void setSplash(boolean splash) {
		this.splash = splash;
	}

	public boolean getSplash() {
		return splash;
	}

}
