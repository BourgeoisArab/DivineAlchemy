package bourgeoisarab.divinealchemy.common.tileentity;

public interface IBrewingCauldron extends IEffectBrewingThingy {

	public void setBoil(boolean boil);

	public boolean isBoiling();

	public float getCauldronInstability();

}
