package bourgeoisarab.divinealchemy.common.potion;

import bourgeoisarab.divinealchemy.common.tileentity.IPotionBrewer;

public class PotionProperties {

	/**
	 * Whether the potion is unstable. Only used in {@link IPotionBrewer}
	 */
	public boolean isStable;
	/**
	 * Whether the bottle will be a splash potion; flag = 1
	 */
	public boolean isSplash;
	/**
	 * Whether the effects will persist after death; flag = 2
	 */
	public boolean isPersistent;
	/**
	 * Whether the potion is blessed; flag = 4
	 */
	public boolean isBlessed;
	/**
	 * Whether the potion is cursed; flag = 8
	 */
	public boolean isCursed;

	public PotionProperties() {
		this(false, false, false, false, false);
	}

	public PotionProperties(int meta) {
		this(false, getSplash(meta), getPersistent(meta), getBlessed(meta), getCursed(meta));
	}

	public PotionProperties(boolean stable, int meta) {
		this(stable, getSplash(meta), getPersistent(meta), getBlessed(meta), getCursed(meta));
	}

	public PotionProperties(boolean stable, boolean splash, boolean persistent, boolean blessed, boolean cursed) {
		isStable = stable;
		isSplash = splash;
		isPersistent = persistent;
		isBlessed = blessed;
		isCursed = cursed;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PotionProperties) {
			PotionProperties p = (PotionProperties) obj;
			return p.isStable == isStable && p.isSplash == isSplash && p.isPersistent == isPersistent && p.isBlessed == isBlessed && p.isCursed == isCursed;
		}
		return false;
	}

	public void clear() {
		isStable = false;
		isSplash = false;
		isPersistent = false;
		isBlessed = false;
		isCursed = false;
	}

	public static int getMetaValue(boolean splash, boolean persistent, boolean blessed, boolean cursed) {
		int meta = 0;
		if (splash) {
			meta += 1;
		}
		if (persistent) {
			meta += 2;
		}
		if (blessed) {
			meta += 4;
		}
		if (cursed) {
			meta += 8;
		}
		return meta;
	}

	public int getMetaValue() {
		return getMetaValue(isSplash, isPersistent, isBlessed, isCursed);
	}

	public static boolean getSplash(int meta) {
		return (meta & 1) == 1;
	}

	public static boolean getPersistent(int meta) {
		return (meta & 2) == 2;
	}

	public static boolean getBlessed(int meta) {
		return (meta & 4) == 4;
	}

	public static boolean getCursed(int meta) {
		return (meta & 8) == 8;
	}

}
