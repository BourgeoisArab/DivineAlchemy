package bourgeoisarab.divinealchemy.utility.nbt;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.save.Divinity;
import bourgeoisarab.divinealchemy.reference.NBTNames;

public class NBTPlayerHelper {

	public static final float POTION_BREW_MODIFIER = 0.01F;
	public static final float BREWING_MAX = 0.5F;

	@Deprecated
	public static EntityPlayer getPlayer(UUID uuid) {
		if (uuid == null) {
			return null;
		}
		List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayerMP player : players) {
			if (player.getUniqueID().equals(uuid)) {
				return player;
			}
		}
		return null;
	}

	public static EntityPlayer getPlayer(UUID uuid, World world) {
		if (uuid == null) {
			return null;
		}
		List<EntityPlayer> players = world.playerEntities;
		for (EntityPlayer player : players) {
			if (player.getUniqueID().equals(uuid)) {
				return player;
			}
		}
		return null;
	}

	public static void writeUUID(UUID id, NBTTagCompound nbt, String name) {
		if (id != null && name != null) {
			nbt.setLong(name + "M", id.getMostSignificantBits());
			nbt.setLong(name + "L", id.getLeastSignificantBits());
		}
	}

	public static UUID readUUID(NBTTagCompound nbt, String name) {
		return new UUID(nbt.getLong(name + "M"), nbt.getLong(name + "L"));
	}

	/**
	 * Calculates the total divinity the player should have. Doesn't actually change it
	 * 
	 * @param currentDivinity
	 * @param modifier
	 * @param asymptote the maximum divinity that can be reached with that task
	 * @return final divinity
	 */
	public static float getProcessedSigmoid(float currentDivinity, float modifier, float asymptote) {
		return currentDivinity + (sigmoid(currentDivinity + modifier, asymptote) - sigmoid(currentDivinity, asymptote));
	}

	public static float sigmoid(float x, float a) {
		return (float) (2 * a / (1 + Math.pow(10, -4 * x)) - a);
	}

	public static void setDivnity(EntityPlayer player, float divinity) {
		if (player != null) {
			Divinity data = Divinity.get(player.worldObj);
			data.setDivinity(player.getUniqueID(), Math.min(divinity, 1.0F));
			// player.getEntityData().setFloat(NBTNames.DIVINITY, Math.min(divinity, 1.0F));
		}
	}

	public static float getAbsDivinity(EntityPlayer player) {
		return Math.abs(getDivinity(player));
	}

	public static float getDivinity(EntityPlayer player) {
		if (player == null) {
			return 0.0F;
		}
		// if (!player.getEntityData().hasKey(NBTNames.DIVINITY)) {
		// player.getEntityData().setFloat(NBTNames.DIVINITY, 0.0F);
		// }
		return Divinity.get(player.worldObj).getDivinity(player.getUniqueID());
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
