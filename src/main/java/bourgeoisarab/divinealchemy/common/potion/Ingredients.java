package bourgeoisarab.divinealchemy.common.potion;

import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class Ingredients {

	private PotionIngredient[] ingredients;
	private boolean[] sideValues;

	public Ingredients() {
		ingredients = new PotionIngredient[ConfigHandler.maxEffects];
		sideValues = new boolean[ConfigHandler.maxEffects];
	}

	public Ingredients(Ingredients ing) {
		ingredients = ing.getIngredients();
		sideValues = ing.getSideValues();
	}

	public Ingredients(PotionIngredient[] ingredients, boolean[] sideValues) {
		this.ingredients = ingredients;
		this.sideValues = sideValues;
		if (ingredients == null) {
			this.ingredients = new PotionIngredient[ConfigHandler.maxEffects];
		}
		if (sideValues == null) {
			this.sideValues = new boolean[ConfigHandler.maxEffects];
		}

	}

	public PotionIngredient[] getIngredients() {
		return ingredients;
	}

	public boolean[] getSideValues() {
		return sideValues;
	}

	public void clear() {
		ingredients = new PotionIngredient[ConfigHandler.maxEffects];
		sideValues = new boolean[ConfigHandler.maxEffects];
	}

	public boolean empty() {
		for (PotionIngredient i : ingredients) {
			if (i != null) {
				return false;
			}
		}
		return true;
	}

	public boolean add(PotionIngredient ing, boolean side) {
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i] == null) {
				ingredients[i] = ing;
				sideValues[i] = side;
				return true;
			}
		}
		return false;
	}

	public void set(int index, PotionIngredient ing, boolean side) {
		if (index >= 0 && index < ingredients.length) {
			ingredients[index] = ing;
			sideValues[index] = side;
		}
	}

	public void remove(int index) {
		if (index >= 0 && index < ingredients.length) {
			ingredients[index] = null;
			sideValues[index] = false;
		}
	}

	public int remove(PotionIngredient ing, int amount) {
		int count = 0;
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i] == ing) {
				ingredients[i] = null;
				sideValues[i] = false;
				count++;
			}
			if (count >= amount) {
				break;
			}
		}
		return count;
	}

	public PotionIngredient getIngredient(int index) {
		return index >= 0 && index < ingredients.length ? ingredients[index] : null;
	}

	public boolean getSide(int index) {
		return index >= 0 && index < sideValues.length ? sideValues[index] : false;
	}

	public int countIngredients() {
		int count = 0;
		for (PotionIngredient i : ingredients) {
			if (i != null) {
				count++;
			}
		}
		return count;
	}

	public int countIngredients(boolean sideEffect) {
		int count = 0;
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i] != null && sideValues[i] == sideEffect) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String toString() {
		String s = getClass().getName() + ": [";
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i] != null) {
				s = s + ingredients[i] + " (" + sideValues[i] + ")";
			}
			if (i < ingredients.length - 1 && ingredients[i + 1] != null) {
				s = s + ", ";
			}
		}
		return s + "]";
	}

}
