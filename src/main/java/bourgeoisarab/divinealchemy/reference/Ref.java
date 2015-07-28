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

		public static final String INGREDIENTS_TAG = "DAIngredients";
		public static final String INGREDIENTS = "IngredientIDs";
		public static final String SIDE_EFFECT = "SideEffect";
		public static final String EFFECTS_TAG = "DAEffects";
		public static final String EFFECT = "PotionEffect";
		public static final String PROPERTIES = "PotionProperties";
		public static final String PERSISTENT_IDS = "PersistentIDs";
		public static final String PERSISTENT = "Persistent";
		public static final String FOOD_ID = "DAFoodID";
		public static final String FOOD_LEVEL = "DAFoodLevel";
		public static final String FOOD_SATURATION = "DAFoodSaturation";
		public static final String SOUL_AMOUNT = "SoulAmount";
		public static final String DIVINITY = "Divinity";
		public static final String INSTABILITY = "Instability";
		public static final String THROWER = "Thrower";
		public static final String COLOURS = "Colouring";
		public static final String EXPLOSIONS_ABSORBED = "AbsorbedExplosions";

	}

	public static class Config {

		public static final String CATEGORY_COMPAT = "Compatability";
		public static final String CATEGORY_POTION_IDS = "Potion IDs (if dynamic assignment is on, this will be ignored)";
	}

}
