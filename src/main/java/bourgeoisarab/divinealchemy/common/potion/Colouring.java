package bourgeoisarab.divinealchemy.common.potion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A simple class for storing data about extra colouring added to potion liquids
 */
public class Colouring {

	private List<Integer> dyes;

	public static final String[] dyeNames = new String[]{"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue",
			"dyeMagenta", "dyeOrange", "dyeWhite"};

	public Colouring() {
		dyes = new ArrayList<Integer>();
	}

	public static int getDyeType(ItemStack stack) {
		if (stack == null) {
			return -1;
		}
		if (stack.getItem() == Items.dye) {
			return stack.getItemDamage();
		}
		for (int i = 0; i < 16; i++) {
			for (ItemStack target : OreDictionary.getOres(dyeNames[i])) {
				if (OreDictionary.itemMatches(target, stack, false)) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int getDyeColour(int type) {
		return type >= 0 && type < ItemDye.dyeColors.length ? ItemDye.dyeColors[type] : -1;
	}

	public static int getDyeColour(ItemStack stack) {
		return getDyeColour(getDyeType(stack));
	}

	public List<Integer> getColours() {
		return dyes;
	}

	public int[] getColourArray() {
		int[] array = new int[dyes.size()];
		for (int i = 0; i < dyes.size(); i++) {
			array[i] = dyes.get(i);
		}
		return array;
	}

	public Colouring setColours(List<Integer> colours) {
		if (colours == null) {
			dyes.clear();
			return this;
		}
		dyes = colours;
		return this;
	}

	public Colouring setColours(int[] colours) {
		dyes.clear();
		if (colours == null) {
			return this;
		}
		for (int i : colours) {
			dyes.add(i);
		}
		return this;
	}

	public boolean add(ItemStack stack, boolean add) {
		int dyeType = getDyeType(stack);
		if (dyeType != -1) {
			return add(getDyeColour(dyeType), add);
		}
		return false;
	}

	public boolean add(int colour, boolean add) {
		if (colour >= 0) {
			if (add && dyes.size() < Byte.MAX_VALUE) {
				dyes.add(colour);
			}
			return true;
		}
		return false;
	}

	public void clear() {
		dyes.clear();
	}

	public Colouring copy() {
		Colouring newColours = new Colouring();
		for (int i : dyes) {
			newColours.add(i, true);
		}
		return newColours;
	}

}
