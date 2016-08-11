package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModFluids;

public class TEBrewingCauldron extends TEPotionBrewer {

	public static final List<Block> boilblocks = new ArrayList<Block>();
	static {
		boilblocks.add(Blocks.lava);
		boilblocks.add(Blocks.fire);
	}

	private static final int totalBoilTime = 50;
	private int boilTime = -1;
	private boolean boiling = false;

	public static final float INSTABILITY = 0.5F / ConfigHandler.maxEffects;

	public TEBrewingCauldron() {
		tank.setCapacity(FluidContainerRegistry.BUCKET_VOLUME * 1);
	}

	public boolean canBoil() {
		FluidStack fluid = tank.getFluid();
		return fluid != null && fluid.amount > 0 && (fluid.getFluid() == FluidRegistry.WATER || fluid.getFluid() == ModFluids.potion);
	}

	public boolean checkFuelSource() {
		Block block = worldObj.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock();
		boolean boil = false;
		FluidStack fluid = tank.getFluid();
		if (boilblocks.contains(block)) {
			// setRequiresPower(false);
		} else if (canBoil()) {
			// setRequiresPower(true);
		}
		return boil;
	}

	// @Override
	public void setBoil(boolean boil) {
		if (!boil) {
			boilTime = -1;
			boiling = false;
		} else if (boilTime < 0) {
			boilTime = 0;
		}
		if (boilTime >= totalBoilTime) {
			boiling = true;
			boilTime = totalBoilTime;
		}
		sendUpdateToClient();
	}

	// @Override
	public boolean isBoiling() {
		return boiling;
	}

	// @Override
	// public float getInstability() {
	// return getCauldronInstability() + ingredients.countIngredients(false) * INSTABILITY;
	// }

	// @Override
	public float getCauldronInstability() {
		switch (getTier()) {
			case 0:
			default:
				return 0.95F;
			case 1:
				return 0.90F;
			case 2:
				return 0.85F;
			case 3:
				return 0.75F;
			case 4:
				return 0.50F;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		boiling = nbt.getBoolean("Boiling");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("Boiling", boiling);
	}

	@Override
	public void update() {
		super.update();
		// if (!canBoil()) {
		// clearEffects();
		// setBoil(false);
		// setRequiresPower(false);
		// } else if (isRunning()) {
		// setBoil(true);
		// } else {
		// setBoil(false);
		// }

		if (worldObj.getWorldTime() % 10 == 0) {
			if (boilTime >= 0 && boilTime < totalBoilTime) {
				boilTime += 10;
			}
			if (worldObj.getWorldTime() % 100 == 0) {
				checkFuelSource();
			}
		}
		if (isBoiling() && worldObj.rand.nextFloat() < 0.6F) {
			int x = pos.getX(), y = pos.getY(), z = pos.getZ();
			Random rand = worldObj.rand;
			float level = tank.getFluidAmount();
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + (rand.nextFloat() * 0.625 + 0.1875), y + (0.6875 * ((double) level / tank.getCapacity()) + 0.1875), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0,
					0.0);
		}
	}

	@Override
	public boolean isRunning() {
		// TODO:
		return true;
	}

	// public void makeHotMess() {
	// tank.setFluid(new FluidStack(ModFluids.hotMess, tank.getFluidAmount()));
	// clearEffects();
	// sendUpdateToClient();
	// }

}
