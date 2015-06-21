package bourgeoisarab.divinealchemy.utility;

import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import scala.actors.threadpool.Arrays;

public class ColourHelper {

	public static final int[] potionColour = new int[]{134, 37, 255};
	public static final int[] waterColour = new int[]{134, 37, 255};

	public static float[] getColourFromEffects(List<PotionEffect> effects) {
		List<Integer> potionIDs = new ArrayList<Integer>();
		if (effects == null) {
			return null;
		}
		for (PotionEffect i : effects) {
			potionIDs.add(i.getPotionID());
		}
		return getColourFromPotions(potionIDs);
	}

	public static float[] getColourFromEffects(PotionEffect[] effects) {
		return getColourFromEffects(Arrays.asList(effects));
	}

	public static float[] getColourFromPotions(List<Integer> potionIDs) {
		float[] colours = getFloatColours(potionColour);
		if (potionIDs == null || potionIDs.size() < 1) {
			return colours;
		}
		float r = 0;
		float g = 0;
		float b = 0;
		if (potionIDs.size() > 0) {
			for (int i : potionIDs) {
				int[] colour = separateColours(Potion.potionTypes[i].getLiquidColor());
				r += colour[0];
				g += colour[1];
				b += colour[2];
			}
			colours[0] = r / potionIDs.size() / 255;
			colours[1] = g / potionIDs.size() / 255;
			colours[2] = b / potionIDs.size() / 255;
		}
		return colours;
	}

	public static float[] getColourFromPotions(Integer[] potionIDs) {
		return getColourFromPotions(Arrays.asList(potionIDs));
	}

	public static float[] getColourFromIngredients(PotionIngredient[] ingredients) {
		List<Integer> potionIDs = new ArrayList<Integer>();
		for (PotionIngredient i : ingredients) {
			if (i != null) {
				potionIDs.add(i.getEffectID());
			}
		}
		return getColourFromPotions(potionIDs);
	}

	public static int combineColours(float[] colour) {
		if (colour == null || colour.length != 3) {
			return -1;
		}
		return combineColours(getIntColours(colour));
	}

	/**
	 * @param colour compound colour (eg. 0xFFFFFF or 16777215 for white)
	 * @return int[] array with RGB values from 0 to 255
	 */
	public static int[] separateColours(int colour) {
		int r = colour >> 16;
		colour -= r << 16;
		int g = colour >> 8;
		colour -= g << 8;
		int b = colour;
		return new int[]{r, g, b};
	}

	public static int combineColours(int[] colour) {
		if (colour == null || colour.length != 3) {
			return -1;
		}
		return (colour[0] << 16) + (colour[1] << 8) + colour[2];
	}

	public static int[] getIntColours(float[] colours) {
		return new int[]{getIntColour(colours[0]), getIntColour(colours[1]), getIntColour(colours[2])};
	}

	public static float[] getFloatColours(int[] colours) {
		float[] colour = new float[]{getFloatColour(colours[0]), getFloatColour(colours[1]), getFloatColour(colours[2])};
		return colour;
	}

	public static int getIntColour(float colour) {
		if (colour <= 0.0F) {
			return 0;
		} else if (colour >= 1.0F) {
			return 255;
		} else {
			return (int) (colour * 255);
		}
	}

	public static float getFloatColour(int colour) {
		if (colour <= 0) {
			return 0.0F;
		} else if (colour >= 255) {
			return 1.0F;
		} else {
			return colour / 255;
		}
	}
}
