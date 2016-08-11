package bourgeoisarab.divinealchemy.common.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.Ingredients;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public abstract class TEPotionBrewer extends TEPowered implements IFluidHandler {

	public final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
	public Ingredients ingredients = new Ingredients();
	public Effects effects = new Effects();
	public PotionProperties properties = new PotionProperties();
	public Colouring colouring = new Colouring();
	public float instability = 0.0F;

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		tank.writeToNBT(nbt);
		NBTEffectHelper.setIngredients(nbt, ingredients);
		NBTEffectHelper.setEffects(nbt, effects);
		NBTEffectHelper.setColouring(nbt, colouring);
		NBTEffectHelper.setProperties(nbt, properties);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt);
		ingredients = NBTEffectHelper.getIngredients(nbt);
		effects = NBTEffectHelper.getEffects(nbt);
		colouring = NBTEffectHelper.getColouring(nbt);
		properties = NBTEffectHelper.getProperties(nbt);
	}

	@Override
	public void writeToPacket(ByteBuf buf) {
	}

	@Override
	public void readFromPacket(ByteBuf buf) {
	}

	// @Override
	// public Effects getEffects() {
	// return effects;
	// }

	// @Override
	// public void setEffects(Effects effects) {
	// if (effects != null) {
	// this.effects = effects;
	// } else {
	// this.effects.clear();
	// }
	// sendUpdateToClient();
	// }

	// @Override
	// public float getInstability() {
	// return 0;
	// }

	// @Override
	// public void setInstability(float instability) {
	// this.instability = instability;
	// sendUpdateToClient();
	// }

	// @Override
	public PotionProperties getProperties() {
		return properties;
	}

	// @Override
	public PotionProperties setProperties(PotionProperties properties) {
		if (properties != null) {
			this.properties = properties;
		} else {
			this.properties = new PotionProperties();
		}
		sendUpdateToClient();
		return this.properties;
	}

	// @Override
	public Colouring getColouring() {
		return colouring;
	}

	// @Override
	public Colouring setColouring(Colouring colour) {
		if (colour != null) {
			colouring = colour;
		} else {
			colouring.clear();
		}
		sendUpdateToClient();
		return colouring;
	}

	// @Override
	// public boolean addIngredient(PotionIngredient ing, boolean side) {
	// if (!effects.empty()) {
	// return false;
	// }
	// boolean r = ingredients.add(ing, side);
	// sendUpdateToClient();
	// return r;
	// }

	// @Override
	// public int removeIngredient(PotionIngredient ing, int amount) {
	// int removed = ingredients.remove(ing, amount);
	// sendUpdateToClient();
	// return removed;
	// }

	// @Override
	// public Ingredients getIngredients() {
	// return ingredients;
	// }

	// @Override
	// public void setIngredients(Ingredients ingredients) {
	// if (ingredients != null) {
	// this.ingredients = ingredients;
	// } else {
	// this.ingredients.clear();
	// }
	// sendUpdateToClient();
	// }

	// @Override
	// public void addEffect(PotionEffect effect, boolean side) {
	// effects.add(effect, side);
	// sendUpdateToClient();
	// }

	// @Override
	// public int getMaxDuration() {
	// return (int) (Math.pow(2, getTier()) * 1200);
	// }

	// @Override
	// public void clearInstability() {
	// properties.isStable = true;
	// instability = 0.0F;
	// }

	// @Override
	// public void finaliseEffects() {
	// if (ingredients.countIngredients() <= 0 || tank.getFluid() == null || tank.getFluid().getFluid() != ModFluids.potion) {
	// return;
	// }
	// if (!worldObj.isRemote) {
	// for (int i = 0; i < ingredients.getIngredients().length; i++) {
	// if (ingredients.getIngredient(i) != null) {
	// ingredients.getIngredient(i).applyEffect(this, worldObj.rand, ingredients.getSide(i));
	// }
	// }
	// NBTEffectHelper.setEffects(tank.getFluid(), effects);
	// NBTEffectHelper.setColouring(tank.getFluid(), colouring);
	// NBTEffectHelper.setProperties(tank.getFluid().tag, properties);
	// sendUpdateToClient();
	// }
	// }

	// @Override
	// public void clearEffects() {
	// effects.clear();
	// ingredients.clear();
	// colouring.clear();
	// properties.clear();
	// sendUpdateToClient();
	// }

	// @Override
	public int getTier() {
		return getBlockMetadata();
	}

	// @Override
	// public int countIngredient(PotionIngredient ing) {
	// int count = 0;
	// for (PotionIngredient i : ingredients.getIngredients()) {
	// if (i != null && ing.getPotionID() == i.getPotionID()) {
	// count++;
	// }
	// }
	// return count;
	// }

	// @Override
	public boolean addDye(ItemStack stack, boolean add) {
		if (tank.getFluid() != null && tank.getFluid().getFluid() == ModFluids.potion) {
			return colouring.add(stack, add);
		}
		return false;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (resource != null && resource.getFluid() != ModFluids.potion) {
			return tank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
			return null;
		}
		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		if (tank.getFluid() == null) {
			return null;
		}
		FluidStack drain = tank.drain(maxDrain, doDrain);
		if (tank.getFluidAmount() <= 0) {
			tank.setFluid(null);
		}
		sendUpdateToClient();
		return drain;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		if (fluid == FluidRegistry.WATER || fluid == ModFluids.hotMess) {
			if (tank.getFluid() != null) {
				if (tank.getFluid().getFluid() == fluid && tank.getFluidAmount() < FluidContainerRegistry.BUCKET_VOLUME) {
					return true;
				}
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return tank.getFluid() != null ? tank.getFluid().getFluid() == fluid : false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}

	// /**
	// / * Sorts the stored effects in order of priority
	// / */
	// public void sortEffects() {
	// List<PotionIngredient> sorted = new ArrayList<PotionIngredient>();
	// int priority = getHighestPriority(Integer.MAX_VALUE);
	// while (sorted.size() < ingredients.length) {
	// sorted.addAll(getIngredientsWithPriority(getHighestPriority(priority)));
	// priority = getHighestPriority(priority);
	// }
	// for (int i = 0; i < ingredients.length; i++) {
	// ingredients[i] = sorted.get(i);
	// }
	// }
	//
	// /**
	// * @param the previous highest priority that was already processed
	// * @return
	// */
	// private int getHighestPriority(int priority) {
	// int highest = Integer.MIN_VALUE;
	// for (PotionIngredient i : ingredients.getIngredients()) {
	// if (i != null) {
	// highest = i.getPriority() <= priority ? Math.max(i.getPriority(), highest) : highest;
	// }
	// }
	// return highest;
	// }
	//
	// protected List<PotionIngredient> getIngredientsWithPriority(int priority) {
	// List<PotionIngredient> list = new ArrayList<PotionIngredient>();
	// for (PotionIngredient i : ingredients.getIngredients()) {
	// if (i != null && i.getPriority() == priority) {
	// list.add(i);
	// }
	// }
	// return list;
	// }
}
