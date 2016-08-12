package bourgeoisarab.divinealchemy.reference;

public final class Ref {

	public static final String MODID = "divinealchemy";
	public static final String NAME = "Divine Alchemy";
	public static final String VERSION = "1.8-1.0";
	public static final String CLIENT_PROXY = "bourgeoisarab.divinealchemy.proxy.ClientProxy";
	public static final String SERVER_PROXY = "bourgeoisarab.divinealchemy.proxy.ServerProxy";

	public static class Location {

		public static final String PREFIX = MODID + ":";
		public static final String MODELS = PREFIX + "models/";
		public static final String TEXTURES = PREFIX + "textures/";
		public static final String ITEMS = TEXTURES + "items/";
		public static final String BLOCKS = TEXTURES + "blocks/";
	}

	public static class Config {

		public static final String CATEGORY_COMPAT = "Compatability";
		public static final String CATEGORY_POTION_IDS = "Potion IDs (if dynamic assignment is on, this will be ignored)";
		public static final String WORLDGEN = "WorldGen";
	}

}
