package bourgeoisarab.divinealchemy.init;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;

	public static boolean hiddenFoodSpiking;
	public static boolean CoFHCompat;
	public static String player;
	public static int interval;
	public static boolean creativeTab = true;

	public static void init(File configFile) {
		config = new Configuration(configFile);

		try {
			config.load();

			hiddenFoodSpiking = config.getBoolean("Hidden Food Spiking", config.CATEGORY_GENERAL, true, "True: spiked foods will look like vanilla");
			CoFHCompat = config.getBoolean("Enable CoFH compatability", "Compatability", true, "");
			player = config.getString("Player", config.CATEGORY_GENERAL, "coolhobo77", "The subject's player name");
			interval = config.getInt("Interval", config.CATEGORY_GENERAL, 200, 1, Integer.MAX_VALUE, "");
			try {
				Class.forName("cofh.api.energy.IEnergyHandler");
			} catch (Exception e) {
				// If CoFHCore cannot be found, this is false regardless of config
				CoFHCompat = false;
			}

		} catch (Exception e) {

		} finally {
			config.save();
		}

	}

}
