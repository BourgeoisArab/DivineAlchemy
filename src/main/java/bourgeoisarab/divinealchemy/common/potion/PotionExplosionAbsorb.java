package bourgeoisarab.divinealchemy.common.potion;

public class PotionExplosionAbsorb extends ModPotion implements IEvilPotion {

	public PotionExplosionAbsorb(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
		setPotionName("potion.explodeabsorb");
	}

	@Override
	public float getMinEvilness() {
		return -0.25F;
	}

}
