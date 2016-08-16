package bourgeoisarab.divinealchemy.init;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import bourgeoisarab.divinealchemy.common.worldgen.WorldGenObeliskDark;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.Log;

public class ConfigHandler {

	public static Configuration config;

	public static boolean hiddenFoodSpiking;
	public static int maxEffects;
	public static float maxNaturalPower;

	public static boolean CoFHCompat;

	public static String player;
	public static int interval;

	public static boolean creativeTab = true;

	public static boolean useDynamicPotionIDs;

	public static int amuletRepairCost;

	// public static final int totalPotionCount = 21;
	// public static final int[] potionIDs = new int[totalPotionCount];

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

			Property p = config.get(Ref.Config.WORLDGEN, "ObeliskBiomes", new int[]{4, 5, 6, 18, 19, 21, 22, 27, 28, 29}, "Biome IDs, in which Desolate Obelisks can spawn");
			WorldGenObeliskDark.BIOME_IDS = p.getIntList();

			amuletRepairCost = config.getInt("AmuletRepairCost", Ref.Config.ENERGY, 100, 0, Integer.MAX_VALUE, "Energy required to repair an amulet by 1 point");

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

}
