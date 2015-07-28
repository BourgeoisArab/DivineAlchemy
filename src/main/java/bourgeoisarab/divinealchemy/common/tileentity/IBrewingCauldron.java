package bourgeoisarab.divinealchemy.common.tileentity;

public interface IBrewingCauldron extends IPotionBrewer {

	public void setBoil(boolean boil);

	public boolean isBoiling();

	public float getCauldronInstability();

}
