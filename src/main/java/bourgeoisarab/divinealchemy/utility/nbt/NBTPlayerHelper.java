package bourgeoisarab.divinealchemy.utility.nbt;

import net.minecraft.entity.player.EntityPlayer;
import bourgeoisarab.divinealchemy.reference.Ref;

public class NBTPlayerHelper {

	public static final float POTION_BREW_MODIFIER = 0.0002F;

	public static void setDivnity(EntityPlayer player, float divnity) {
		if (player == null || player.getEntityData() == null) {
			return;
		}
		player.getEntityData().setFloat(Ref.NBT.DIVINITY, divnity);
	}

	public static float getDivinity(EntityPlayer player) {
		if (player == null) {
			return 0.0F;
		}
		if (!player.getEntityData().hasKey(Ref.NBT.DIVINITY)) {
			player.getEntityData().setFloat(Ref.NBT.DIVINITY, 0.0F);
		}
		return player.getEntityData().getFloat(Ref.NBT.DIVINITY);
	}

	public static void addDivinity(EntityPlayer player, float divinity) {
		if (player == null) {
			return;
		}
		setDivnity(player, getDivinity(player) + divinity);
	}

	public static void setSoulAmount(EntityPlayer player, float amount) {
		player.getEntityData().setFloat(Ref.NBT.SOUL_AMOUNT, amount);
	}

	public static float getSoulAmount(EntityPlayer player) {
		if (player == null) {
			return 0.0F;
		}
		if (!player.getEntityData().hasKey(Ref.NBT.SOUL_AMOUNT)) {
			setSoulAmount(player, 1.0F);
		}
		return player.getEntityData().getFloat(Ref.NBT.SOUL_AMOUNT);
	}

	public static void setAbsorbedExplosion(EntityPlayer player, float size) {
		if (player == null) {
			return;
		}
		player.getEntityData().setFloat(Ref.NBT.EXPLOSIONS_ABSORBED, size);
	}

	public static float getAbsorbedExplosion(EntityPlayer player) {
		if (player == null) {
			return 0.0F;
		}
		if (!player.getEntityData().hasKey(Ref.NBT.EXPLOSIONS_ABSORBED)) {
			setAbsorbedExplosion(player, 0.0F);
		}
		return player.getEntityData().getFloat(Ref.NBT.EXPLOSIONS_ABSORBED);
	}

	public static void addAbsorbedExplosion(EntityPlayer player, float size) {
		if (player == null) {
			return;
		}
		setAbsorbedExplosion(player, getAbsorbedExplosion(player) + size);
	}
}
