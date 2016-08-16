package bourgeoisarab.divinealchemy.common.tileentity;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import bourgeoisarab.divinealchemy.common.block.BrewingSetup;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public abstract class TEPotionBrewer extends TEPowered implements IFluidHandler {

	public final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
	public Effects effects = new Effects();
	public float instability = 0.0F;

	@Override
	public void update() {
		super.update();
		if (worldObj.getWorldTime() % updateRate == 0) {
			if (buffer.isFull()) {
				completePotion();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		tank.writeToNBT(nbt);
		NBTEffectHelper.setEffects(nbt, effects);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt);
		effects = NBTEffectHelper.getEffects(nbt);
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

	public abstract boolean isHeated();

	public PotionProperties getProperties() {
		return NBTEffectHelper.getProperties(tank.getFluid());
	}

	public PotionProperties setProperties(PotionProperties properties) {
		if (properties != null) {
			NBTEffectHelper.setProperties(tank.getFluid(), properties);
		}
		sendUpdateToClient();
		return properties;
	}

	public Colouring getColouring() {
		return NBTEffectHelper.getColouring(tank.getFluid());
	}

	public Colouring setColouring(Colouring colour) {
		if (colour != null) {
			NBTEffectHelper.setColouring(tank.getFluid(), colour);
		} else {
			NBTEffectHelper.setColouring(tank.getFluid(), new Colouring());
		}
		sendUpdateToClient();
		return colour;
	}

	public abstract int getMaxDuration();

	public abstract int getMaxAmplifier();

	public void completePotion() {
		if (isRunning) {
			isRunning = false;
			if (tank.getFluid().getFluid() == FluidRegistry.WATER) {
				tank.setFluid(new FluidStack(ModFluids.potion, tank.getFluidAmount()));
			}
			NBTEffectHelper.setEffects(tank.getFluid(), effects);
			effects.clear();
			markDirty();
			if (worldObj.isRemote) {
				for (int i = 0; i < 20; i++) {
					double x = 2D * (worldObj.rand.nextDouble() - 0.5D);
					double z = 2D * (worldObj.rand.nextDouble() - 0.5D);
					worldObj.spawnParticle(EnumParticleTypes.SPELL_INSTANT, getPos().getX() + x, getPos().getY() + 1, pos.getZ() + z, -x, 0, -z);
				}
			}
		}
	}

	public void addIngredient(PotionIngredient ing, EntityPlayer player) {
		if (ing != null && !worldObj.isRemote) {
			BrewingSetup setup = ing.getBrewingSetup();
			Log.info("getting brewing setup");
			if (setup.checkSetup(worldObj, getPos(), this) && isHeated()) {
				Log.info("setup is valid");
				List<TEPedestal> pedestals = setup.getPedestals(worldObj, getPos(), this);
				// int[] counts = getCrystalCounts(pedestals);
				// int amplifier = Math.min(getMaxAmplifier(), Math.min(counts[0], ing.getMaxAmplifier()));
				// int duration = Math.min(getMaxDuration(), counts[1]);
				int duration = 600;
				int amplifier = 2;
				// List<TEObelisk> obelisks = setup.getObelisks(worldObj, getPos(), this);
				Log.info("adding effects");
				Log.info("pedestals:" + pedestals);
				effects.add(new PotionEffect(ing.getPotionID(), duration, amplifier), false);
				for (int i = 0; i < 3; i++) {
					Potion p = ModPotion.getRandomPotion(worldObj.rand, !ing.getPotion().isBadEffect);
					effects.add(new PotionEffect(p.getId(), worldObj.rand.nextInt(duration / 2) + duration / 2, worldObj.rand.nextBoolean() ? amplifier : amplifier - 1), true);
				}
				isRunning = true;
				for (TEPedestal tile : pedestals) {
					tile.isRunning = true;
				}
				Log.info("all things running");
			} else {
				if (ing.getPotion() instanceof ModPotion) {
					((ModPotion) ing.getPotion()).punishForFailedSetup(player);
				}
			}
			sendUpdateToClient();
		}
	}

	/**
	 * @return int[]{amplifier, duration, instability}
	 */
	public int[] getCrystalCounts(List<TEPedestal> pedestals) {
		int[] data = new int[3];
		for (TEPedestal tile : pedestals) {
			ItemStack stack = tile.getStackInSlot(0);
			if (stack != null && stack.getItem() == ModItems.pedestalCrystal) {
				data[ModItems.pedestalCrystal.getType(stack).ordinal()]++;
			}
		}
		return data;
	}

	public boolean addDye(ItemStack stack, boolean add) {
		if (tank.getFluid() != null && tank.getFluid().getFluid() == ModFluids.potion) {
			Colouring c = NBTEffectHelper.getColouring(tank.getFluid());
			boolean b = c.add(stack, add);
			NBTEffectHelper.setColouring(tank.getFluid(), c);
			return b;
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
}
