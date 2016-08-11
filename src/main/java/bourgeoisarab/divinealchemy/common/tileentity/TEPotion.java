package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class TEPotion extends TEDivineAlchemy implements IEffectProvider {

	// private PotionProperties properties = new PotionProperties();
	// private Effects effects = new Effects();
	// private Colouring colouring = new Colouring();
	private FluidStack fluid;

	private float instability = 0.9F;

	public TEPotion() {
		setFluid(new FluidStack(ModFluids.potion, FluidContainerRegistry.BUCKET_VOLUME));
	}

	public FluidStack getFluid() {
		return fluid;
	}

	public TEPotion setFluid(FluidStack fluid) {
		if (fluid == null) {
			fluid = new FluidStack(ModFluids.potion, FluidContainerRegistry.BUCKET_VOLUME);
		}
		this.fluid = fluid;
		return this;
	}

	@Override
	public Effects getEffects() {
		return NBTEffectHelper.getEffects(fluid);
	}

	@Override
	public void setEffects(Effects effects) {
		if (effects == null) {
			effects = new Effects();
		}
		NBTEffectHelper.setEffects(fluid, effects);
	}

	@Override
	public float getInstability() {
		return instability;
	}

	@Override
	public void setInstability(float instability) {
		this.instability = instability;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		// effects = NBTEffectHelper.getEffectsFromNBT(nbt);
		// properties = NBTEffectHelper.getPropertiesFromNBT(nbt);
		// colouring = NBTEffectHelper.getColouringFromNBT(nbt);
		fluid = FluidStack.loadFluidStackFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		// NBTEffectHelper.setEffectsForNBT(nbt, effects);
		// NBTEffectHelper.setPropertiesForNBT(nbt, properties);
		// NBTEffectHelper.setColouringForNBT(nbt, colouring);
		fluid.writeToNBT(nbt);
	}

	@Override
	public PotionProperties getProperties() {
		return NBTEffectHelper.getProperties(fluid);
	}

	@Override
	public PotionProperties setProperties(PotionProperties properties) {
		if (properties == null) {
			properties = new PotionProperties();
		}
		NBTEffectHelper.setProperties(fluid.tag, properties);
		return properties;
	}

	@Override
	public Colouring getColouring() {
		return NBTEffectHelper.getColouring(fluid);
	}

	@Override
	public Colouring setColouring(Colouring colouring) {
		if (colouring == null) {
			colouring = new Colouring();
		}
		NBTEffectHelper.setColouring(fluid, colouring);
		return colouring;
	}
}
