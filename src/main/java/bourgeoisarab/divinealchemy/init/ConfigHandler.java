package bourgeoisarab.divinealchemy.init;

import java.io.File;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.Log;

public class ConfigHandler {

	public static Configuration config;

	public static boolean hiddenFoodSpiking;
	public static int maxEffects;

	public static boolean CoFHCompat;

	public static String player;
	public static int interval;

	public static boolean creativeTab = true;

	public static boolean useDynamicPotionIDs;
	public static final int totalPotionCount = 19;
	public static int[] potionIDs = new int[totalPotionCount];

	public static void init(File configFile) {
		config = new Configuration(configFile);

		try {
			config.load();

			hiddenFoodSpiking = config.getBoolean("Hidden Food Spiking", config.CATEGORY_GENERAL, true, "True: spiked foods will look like vanilla");
			maxEffects = config.getInt("MaxEffects", config.CATEGORY_GENERAL, 16, 4, 24, "Maximum number of effects on a single item (excluding side effects)");
			CoFHCompat = config.getBoolean("Enable CoFH compatability", Ref.Config.CATEGORY_COMPAT, true, "");
			player = config.getString("Player", config.CATEGORY_GENERAL, "coolhobo77", "The subject's player name");
			interval = config.getInt("Interval", config.CATEGORY_GENERAL, 200, 1, Integer.MAX_VALUE, "");

			useDynamicPotionIDs = config.getBoolean("DynamicIDs", Ref.Config.CATEGORY_POTION_IDS, true, "Use dynamic assignment for potion IDs");

			if (!useDynamicPotionIDs) {
				potionIDs[0] = config.getInt("Flight", Ref.Config.CATEGORY_POTION_IDS, 50, 1, 127, "");
				potionIDs[1] = config.getInt("MagicResist", Ref.Config.CATEGORY_POTION_IDS, 51, 1, 127, "");
				potionIDs[2] = config.getInt("Day", Ref.Config.CATEGORY_POTION_IDS, 52, 1, 127, "");
				potionIDs[3] = config.getInt("WitherAura", Ref.Config.CATEGORY_POTION_IDS, 53, 1, 127, "");
				potionIDs[4] = config.getInt("RegenAura", Ref.Config.CATEGORY_POTION_IDS, 54, 1, 127, "");
				potionIDs[5] = config.getInt("InvisiblityAura", Ref.Config.CATEGORY_POTION_IDS, 55, 1, 127, "");
				potionIDs[6] = config.getInt("PoisonAura", Ref.Config.CATEGORY_POTION_IDS, 56, 1, 127, "");
			} else {
				Log.info("Dynamic potion ID assignment is enabled.");
				int index = getFirstEmptyIndex();
				if (index > 0) {
					for (int i = 0; i < totalPotionCount; i++) {
						potionIDs[i] = index + i;
					}
					Log.info("Assigned potion IDs: from " + index + " to " + (index + totalPotionCount - 1));
				} else {
					Log.info("Empty space to fit " + totalPotionCount + " potion IDs was not found. IDs will be assigned to whatever empty slot is discovered.");
					Log.info("It is recommended that you use manual ID assignment at this point.");
					int remainingPotions = totalPotionCount;
					for (int i = 32; i < Potion.potionTypes.length; i++) {
						if (Potion.potionTypes[i] == null) {
							potionIDs[i - 32] = i;
							remainingPotions--;
						}
					}
					if (remainingPotions > 0) {
						Log.fatal("There aren't enough potion ID slots available. Try disabling some effects, or removing some mods.");
					}
					String s = "";
					for (int i = 0; i < potionIDs.length - 1; i++) {
						s = s + potionIDs[i] + ", ";
					}
					s = s + potionIDs[potionIDs.length - 1];
					Log.info("Just in case you are a sensible human being, and wish to use manual assignment, here are some suitable potion IDs to use:");
					Log.info(s);
				}
			}

			try {
				Class.forName("cofh.api.energy.IEnergyHandler");
			} catch (Exception e) {
				// If CoFHCore cannot be found, this is false regardless of config
				Log.info("CoFHCore has not been located. Compatability is disabled.");
				CoFHCompat = false;
			}

		} catch (Exception e) {

		} finally {
			config.save();
		}

	}

	public static int getFirstEmptyIndex() {
		int emptySlotCount = 0;
		for (int i = 32; i < Potion.potionTypes.length; i++) {
			if (Potion.potionTypes[i] == null) {
				emptySlotCount++;
			} else {
				emptySlotCount = 0;
			}
			if (emptySlotCount >= totalPotionCount) {
				return i;
			}
		}
		return -1;
	}

}
