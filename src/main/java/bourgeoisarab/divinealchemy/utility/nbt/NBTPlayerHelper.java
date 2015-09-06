package bourgeoisarab.divinealchemy.utility.nbt;

import net.minecraft.entity.player.EntityPlayer;
import bourgeoisarab.divinealchemy.reference.NBTNames;

public class NBTPlayerHelper {

	public static final float POTION_BREW_MODIFIER = 0.01F;
	public static final float BREWING_MAX = 0.5F;

	/**
	 * Calculates the total divinity the player should have. Doesn't actually change it
	 * 
	 * @param currentDivinity
	 * @param modifier
	 * @param modifierMax the maximum divinity that can be reached with that task
	 * @return final divinity
	 */
	public static float getProcessedDivinity(float currentDivinity, float modifier, float modifierMax) {
		return currentDivinity + (equation(currentDivinity + modifier, modifierMax) - equation(currentDivinity, modifierMax));
	}

	public static float equation(float n, float asymptote) {
		return (float) (2 * asymptote / (1 + Math.pow(10, -4 * n)) - asymptote);
	}

	public static void setDivnity(EntityPlayer player, float divinity) {
		if (player != null) {
			player.getEntityData().setFloat(NBTNames.DIVINITY, divinity);
		}
	}

	public static float getAbsDivinity(EntityPlayer player) {
		return Math.abs(getDivinity(player));
	}

	public static float getDivinity(EntityPlayer player) {
		if (player == null) {
			return 0.0F;
		}
		if (!player.getEntityData().hasKey(NBTNames.DIVINITY)) {
			player.getEntityData().setFloat(NBTNames.DIVINITY, 0.0F);
		}
		return player.getEntityData().getFloat(NBTNames.DIVINITY);
	}

	public static void addDivinity(EntityPlayer player, float divinity) {
		setDivnity(player, getDivinity(player) + divinity);
	}

	public static void setSoulAmount(EntityPlayer player, float amount) {
		player.getEntityData().setFloat(NBTNames.SOUL_AMOUNT, amount);
	}

	public static float getSoulAmount(EntityPlayer player) {
		if (player == null) {
			return 0.0F;
		}
		if (!player.getEntityData().hasKey(NBTNames.SOUL_AMOUNT)) {
			setSoulAmount(player, 1.0F);
		}
		return player.getEntityData().getFloat(NBTNames.SOUL_AMOUNT);
	}

	public static void setAbsorbedExplosion(EntityPlayer player, float size) {
		if (player != null) {
			player.getEntityData().setFloat(NBTNames.EXPLOSIONS_ABSORBED, size);
		}
	}

	public static float getAbsorbedExplosion(EntityPlayer player) {
		if (player == null) {
			return 0.0F;
		}
		if (!player.getEntityData().hasKey(NBTNames.EXPLOSIONS_ABSORBED)) {
			setAbsorbedExplosion(player, 0.0F);
		}
		return player.getEntityData().getFloat(NBTNames.EXPLOSIONS_ABSORBED);
	}

	public static void addAbsorbedExplosion(EntityPlayer player, float size) {
		setAbsorbedExplosion(player, getAbsorbedExplosion(player) + size);
	}
}
