package bourgeoisarab.divinealchemy.utility;

import java.util.List;

import net.minecraft.potion.PotionEffect;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;

public class ColourHelper {

	public static final int[] potionColourIntArr = new int[]{134, 37, 255};
	public static final int potionColourInt = 0x8625FF;

	// public static final int[] waterColour = new int[]{134, 37, 255};

	public static float[] getColourFromEffects(List<PotionEffect> effects, Colouring dyes) {
		float[] colours = getFloatColours(potionColourIntArr);
		if (effects == null || effects.size() < 1) {
			return colours;
		}
		float r = 0;
		float g = 0;
		float b = 0;
		int count = 0;
		for (PotionEffect i : effects) {
			int[] colour = separateColours(ModPotion.getPotion(i.getPotionID()).getLiquidColor());
			int weight = i.getAmplifier() + 1;
			r += colour[0] * weight;
			g += colour[1] * weight;
			b += colour[2] * weight;
			count += weight;
		}
		if (dyes != null) {
			List<Integer> c = dyes.getColours();
			for (int i : c) {
				int[] colour = separateColours(i);
				r += colour[0];
				g += colour[1];
				b += colour[2];
				count++;
			}
		}
		colours[0] = r / count / 255;
		colours[1] = g / count / 255;
		colours[2] = b / count / 255;

		return colours;
	}

	public static float[] getColourFromIngredients(PotionIngredient[] ingredients, Colouring dyes) {
		float[] colours = getFloatColours(potionColourIntArr);
		if (ingredients == null || ingredients.length < 1) {
			return colours;
		}
		float r = 0;
		float g = 0;
		float b = 0;

		int count = 0;
		for (PotionIngredient i : ingredients) {
			if (i != null) {
				int[] colour = separateColours(i.getColour());
				r += colour[0];
				g += colour[1];
				b += colour[2];
				count++;
			}
		}
		if (dyes != null) {
			List<Integer> c = dyes.getColours();
			for (int i : c) {
				int[] colour = separateColours(i);
				r += colour[0];
				g += colour[1];
				b += colour[2];
				count++;
			}
		}
		colours[0] = r / count / 255;
		colours[1] = g / count / 255;
		colours[2] = b / count / 255;
		return colours;
	}

	public static float[] getColourFromDyes(Colouring c) {
		List<Integer> colours = c.getColours();
		if (colours.size() > 0) {
			int total = 0;
			for (int i : colours) {
				total += i;
			}
			return getFloatColours(separateColours(total / colours.size()));
		}
		return new float[]{0.0F, 0.0F, 0.0F};
	}

	public static float[] averageColours(float[] c1, float[] c2) {
		if (c1 == null || c2 == null || c1.length != 3 || c2.length != 3) {
			return null;
		}
		return new float[]{(c1[0] + c2[0]) / 2, (c1[1] + c2[1]) / 2, (c1[2] + c2[2]) / 2};
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
		return new float[]{getFloatColour(colours[0]), getFloatColour(colours[1]), getFloatColour(colours[2])};
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
			return colour / 255F;
		}
	}
}
