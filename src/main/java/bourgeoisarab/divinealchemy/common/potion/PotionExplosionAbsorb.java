package bourgeoisarab.divinealchemy.common.potion;

public class PotionExplosionAbsorb extends ModPotion implements IEvilPotion {

	public PotionExplosionAbsorb(String name, boolean isBadEffect, int colour) {
		super(name, isBadEffect, colour);
		setPotionName("potion.explodeabsorb");
	}

	@Override
	public float getMinEvilness() {
		return -0.25F;
	}

}
