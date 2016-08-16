package bourgeoisarab.divinealchemy.common.save;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import bourgeoisarab.divinealchemy.utility.Log;

import com.google.common.collect.Maps;

public class Divinity extends WorldSavedData {

	public static String key = "DivineAlchemy.world.divinity";

	private Map<UUID, Float> values = Maps.newHashMap();

	public Divinity() {
		this(key);
	}

	public Divinity(String s) {
		super(s);
	}

	public static Divinity get(World world) {
		if (world.getMapStorage() == null) {
			return null;
		}
		Divinity result = (Divinity) world.getMapStorage().loadData(Divinity.class, key);
		if (result == null) {
			result = new Divinity();
			result.markDirty();
			world.getMapStorage().setData(key, result);
		}
		return result;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		Set<String> names = nbt.getKeySet();
		for (String s : names) {
			if (s.length() == 1 && nbt.hasKey(s, NBTBase.NBT_TYPES.length - 2)) {
				NBTTagCompound tag = nbt.getCompoundTag(s);
				UUID id = new UUID(tag.getLong("M"), tag.getLong("L"));
				values.put(id, tag.getFloat("D"));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		Set<UUID> set = values.keySet();
		Iterator<UUID> it = set.iterator();
		for (int i = 0; i < set.size(); i++) {
			NBTTagCompound tag = new NBTTagCompound();
			UUID id = it.next();
			tag.setLong("M", id.getMostSignificantBits());
			tag.setLong("L", id.getLeastSignificantBits());
			tag.setFloat("D", values.get(id));
			nbt.setTag(String.valueOf(i), tag);
		}
	}

	public void initialiseData(UUID player) {
		if (values.get(player) == null) {
			values.put(player, 0.0F);
			markDirty();
		}
	}

	public void setDivinity(UUID player, float divinity) {
		if (player == null) {
			Log.warn("Attempted to set divinity for null player");
			return;
		}
		values.put(player, divinity);
		markDirty();
	}

	public float getDivinity(UUID player) {
		Float f = values.get(player);
		if (f == null) {
			return 0.0F;
		}
		return f;
	}

}
