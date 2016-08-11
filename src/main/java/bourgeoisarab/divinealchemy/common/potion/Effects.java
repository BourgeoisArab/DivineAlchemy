package bourgeoisarab.divinealchemy.common.potion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.utility.Log;

public class Effects {

	private List<PotionEffect> effects;
	private List<Boolean> sideValues;

	public Effects() {
		effects = new ArrayList<PotionEffect>();
		sideValues = new ArrayList<Boolean>();
	}

	public Effects(Effects effects) {
		this.effects = effects.getEffects();
		Log.info(this.effects);
		sideValues = effects.getSideEffects();
		Log.info(sideValues);
	}

	public Effects(List<PotionEffect> effect, List<Boolean> side) {
		effects = effect;
		sideValues = side;
		if (effects == null) {
			effects = new ArrayList<PotionEffect>();
		}
		if (sideValues == null) {
			sideValues = new ArrayList<Boolean>();
		}
		while (effects.size() > sideValues.size()) {
			sideValues.add(false);
		}
		while (effects.size() < sideValues.size()) {
			sideValues.remove(sideValues.size() - 1);
		}
	}

	public List<PotionEffect> getEffects() {
		return effects;
	}

	public List<Boolean> getSideEffects() {
		return sideValues;
	}

	public void clear() {
		effects.clear();
		sideValues.clear();
	}

	public int size() {
		if (effects.size() != sideValues.size()) {
			Log.warn("Effect list size is not equal to side effect list size. Report this");
		}
		return effects.size();
	}

	public boolean empty() {
		if (effects.size() > 0 || sideValues.size() > 0) {
			return false;
		}
		return true;
	}

	public void add(PotionEffect effect, boolean side) {
		if (effect != null) {
			effects.add(effect);
			sideValues.add(side);
		} else {
			Log.warn("Tried to add a null PotionEffect to Effects");
		}
	}

	public void set(int index, PotionEffect effect, boolean side) {
		if (effect != null) {
			effects.add(index, effect);
			sideValues.add(index, side);
		} else {
			Log.warn("Tried to set a null PotionEffect to Effects at index " + index);
		}
	}

	public PotionEffect getEffect(int index) {
		return index >= 0 && index < effects.size() ? effects.get(index) : null;
	}

	public boolean getSideEffect(int index) {
		return index >= 0 && index < sideValues.size() ? sideValues.get(index) : false;
	}

	public boolean contains(int potionID) {
		for (PotionEffect i : effects) {
			if (i.getPotionID() == potionID) {
				return true;
			}
		}
		return false;
	}

	public int getEffectIndex(int potionID) {
		for (int i = 0; i < effects.size(); i++) {
			if (effects.get(i).getPotionID() == potionID) {
				return i;
			}
		}
		return -1;
	}

	public PotionEffect getEffectFromID(int potionID) {
		return getEffect(getEffectIndex(potionID));
	}

	@Override
	public String toString() {
		String s = getClass().getName() + ":[";
		if (effects.size() <= 0) {
			return s + "]";
		}
		for (int i = 0; i < effects.size() - 1; i++) {
			if (effects.get(i) != null) {
				s = s + effects.get(i) + "(" + sideValues.get(i) + ")";
			}
			if (effects.get(i + 1) != null) {
				s = s + ",";
			}
		}
		return s + effects.get(effects.size() - 1) + " (" + sideValues.get(effects.size() - 1) + ")" + "]";
	}

	@Override
	public int hashCode() {
		int code = 1;
		code = 31 * code + effects.hashCode();
		code = 31 * code + sideValues.hashCode();
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Effects) {
			Effects e = (Effects) o;
			return effects.equals(e.effects) && sideValues.equals(e.sideValues);
		}
		return false;
	}
}
