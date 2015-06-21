package bourgeoisarab.divinealchemy.reference;

public final class Ref {

	public static final String MODID = "divinealchemy";
	public static final String NAME = "Divine Alchemy";
	public static final String VERSION = "1.7.10-1.0";
	public static final String CLIENT_PROXY = "bourgeoisarab.divinealchemy.proxy.ClientProxy";
	public static final String SERVER_PROXY = "bourgeoisarab.divinealchemy.proxy.ServerProxy";

	public static class Location {
		public static final String PREFIX = MODID + ":";
		public static final String MODELS = PREFIX + "models/";
		public static final String TEXTURES = PREFIX + "textures/";
		public static final String ITEMS = TEXTURES + "items/";
		public static final String BLOCKS = TEXTURES + "blocks/";
	}

	public static class NBT {
		public static final String EFFECTS_TAG = "AIEffects";
		public static final String EFFECT = "PotionEffect";
		public static final String PERSISTENT_IDS = "AIPersistentIDs";
		public static final String PERSISTENT = "AIPersistent";
		public static final String FOOD_ID = "AIFoodID";
		public static final String FOOD_LEVEL = "AIFoodLevel";
		public static final String FOOD_SATURATION = "AIFoodSaturation";
		public static final String SOUL_AMOUNT = "AISoulAmount";
	}

}
