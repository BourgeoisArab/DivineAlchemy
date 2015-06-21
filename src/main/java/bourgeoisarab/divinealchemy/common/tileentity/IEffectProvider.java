package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.List;

import net.minecraft.potion.PotionEffect;

public interface IEffectProvider {

	public List<PotionEffect> getEffects();

	public void setEffects(List<PotionEffect> effects);

	public float getInstability();

	public void setInstability(float instability);

}
