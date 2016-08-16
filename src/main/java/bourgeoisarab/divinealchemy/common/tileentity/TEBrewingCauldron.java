package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import bourgeoisarab.divinealchemy.common.block.BlockBrewingCauldron;
import bourgeoisarab.divinealchemy.init.ModFluids;

public class TEBrewingCauldron extends TEPotionBrewer {

	public final Block[] boilBlocks = new Block[]{Blocks.lava, Blocks.fire};

	// public static final float INSTABILITY = 0.5F / ConfigHandler.maxEffects;

	public TEBrewingCauldron() {
		tank.setCapacity(FluidContainerRegistry.BUCKET_VOLUME * 1);
	}

	public boolean canBoil() {
		FluidStack fluid = tank.getFluid();
		return fluid != null && fluid.amount > 0 && (fluid.getFluid() == FluidRegistry.WATER || fluid.getFluid() == ModFluids.potion);
	}

	// @Override
	// public float getCauldronInstability() {
	// switch (getTier()) {
	// case 0:
	// default:
	// return 0.95F;
	// case 1:
	// return 0.90F;
	// case 2:
	// return 0.85F;
	// case 3:
	// return 0.75F;
	// case 4:
	// return 0.50F;
	// }
	// }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	}

	@Override
	public void update() {
		super.update();

		if (isRunning() && worldObj.rand.nextFloat() < 1.0F) {
			float x = pos.getX() + (worldObj.rand.nextFloat() * 0.625F + 0.1875F);
			float y = pos.getY() + (0.6875F * ((float) tank.getFluidAmount() / tank.getCapacity()) + 0.1875F);
			float z = pos.getZ() + (worldObj.rand.nextFloat() * 0.625F + 0.1875F);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0, 0.0, 0.0);
		}
	}

	@Override
	public boolean isHeated() {
		return ArrayUtils.contains(boilBlocks, worldObj.getBlockState(getPos().offset(EnumFacing.DOWN)).getBlock());
	}

	// public void makeHotMess() {
	// tank.setFluid(new FluidStack(ModFluids.hotMess, tank.getFluidAmount()));
	// clearEffects();
	// sendUpdateToClient();
	// }

	@Override
	public int getMaxDuration() {
		return (worldObj.getBlockState(pos).getValue(BlockBrewingCauldron.PROPERTY_TIER) + 1) * 1200;
	}

	@Override
	public int getMaxAmplifier() {
		return worldObj.getBlockState(pos).getValue(BlockBrewingCauldron.PROPERTY_TIER);
	}

}
