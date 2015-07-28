package bourgeoisarab.divinealchemy.common.tileentity;

import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;

public interface IEffectProvider {

	public Effects getEffects();

	public void setEffects(Effects effects);

	public float getInstability();

	public void setInstability(float instability);

	public PotionProperties getProperties();

	public PotionProperties setProperties(PotionProperties properties);

	public Colouring getColouring();

	public Colouring setColouring(Colouring colour);

}
