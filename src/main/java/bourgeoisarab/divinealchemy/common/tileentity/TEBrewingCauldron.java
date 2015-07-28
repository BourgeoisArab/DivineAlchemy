package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.Random;

import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.block.BlockBrewingCauldron;
import bourgeoisarab.divinealchemy.init.ModFluids;

public class TEBrewingCauldron extends TEPotionBrewerBase implements IBrewingCauldron {

	private final int totalBoilTime = 50;
	private int boilTime = -1;
	private boolean boiling = false;

	public static final float INSTABILITY = 0.015625F;

	public TEBrewingCauldron() {

	}

	@Override
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

	@Override
	public boolean isBoiling() {
		return boiling;
	}

	@Override
	public float getInstability() {
		return getCauldronInstability() + ingredients.countIngredients(false) * INSTABILITY;
	}

	@Override
	public float getCauldronInstability() {
		switch (worldObj.getBlockMetadata(xCoord, yCoord, zCoord)) {
			case 1:
				return 0.90F;
			case 2:
				return 0.85F;
			case 3:
				return 0.75F;
			case 4:
				return 0.50F;
			default:
				return 0.95F;
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
	public void updateEntity() {
		super.updateEntity();
		if (tank.getFluid() == null || tank.getFluidAmount() == 0) {
			clearEffects();
			setBoil(false);
		}
		if (tank.getFluid() != null) {
			((BlockBrewingCauldron) getBlockType()).checkBoil(worldObj, xCoord, yCoord, zCoord);
		}
		if (worldObj.getWorldTime() % 10 == 0) {
			if (boilTime >= 0 && boilTime < totalBoilTime) {
				boilTime += 10;
			}
		}
		if (worldObj.isRemote && isBoiling() && worldObj.rand.nextFloat() < 0.5F) {
			int x = xCoord, y = yCoord, z = zCoord;
			Random rand = worldObj.rand;
			float level = tank.getFluidAmount();
			worldObj.spawnParticle("splash", x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			worldObj.spawnParticle("smoke", x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			EntityBubbleFX bubble = new EntityBubbleFX(worldObj, x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			DivineAlchemy.proxy.getClient().effectRenderer.addEffect(bubble);
			EntitySplashFX splash = new EntitySplashFX(worldObj, x + (rand.nextFloat() * 0.625 + 0.1875), y - 0.6875 + level / (1.6 * tank.getCapacity()), z + (rand.nextFloat() * 0.625 + 0.1875), 0.0, 0.0, 0.0);
			DivineAlchemy.proxy.getClient().effectRenderer.addEffect(splash);
		}
	}

	public void makeHotMess() {
		tank.setFluid(new FluidStack(ModFluids.fluidHotMess, tank.getFluidAmount()));
		clearEffects();
		sendUpdateToClient();
	}

}
