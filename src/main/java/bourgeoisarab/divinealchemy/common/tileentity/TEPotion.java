package bourgeoisarab.divinealchemy.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class TEPotion extends TileEntityBaseDA implements IEffectProvider {

	private PotionProperties properties = new PotionProperties();
	private Effects effects = new Effects();
	private Colouring colouring = new Colouring();

	private float instability = 0.9F;

	public TEPotion() {

	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public Effects getEffects() {
		return effects;
	}

	@Override
	public void setEffects(Effects effects) {
		this.effects = effects;
		if (this.effects == null) {
			this.effects = new Effects();
		}
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
		effects = NBTEffectHelper.getEffectsFromNBT(nbt);
		properties = NBTEffectHelper.getPropertiesFromNBT(nbt);
		colouring = NBTEffectHelper.getColouringFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTEffectHelper.setEffectsForNBT(nbt, effects);
		NBTEffectHelper.setPropertiesForNBT(nbt, properties);
		NBTEffectHelper.setColouringForNBT(nbt, colouring);
	}

	@Override
	public PotionProperties getProperties() {
		return properties;
	}

	@Override
	public PotionProperties setProperties(PotionProperties properties) {
		this.properties = properties;
		return properties;
	}

	@Override
	public Colouring getColouring() {
		return colouring;
	}

	@Override
	public Colouring setColouring(Colouring colour) {
		if (colour != null) {
			colouring = colour;
		}
		return colouring;
	}
}
