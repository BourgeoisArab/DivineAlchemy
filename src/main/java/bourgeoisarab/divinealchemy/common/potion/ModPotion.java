package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.utility.Log;

public class ModPotion extends Potion {

	public static ModPotion potionFlight = new PotionFlight(ConfigHandler.potionIDs[0], false, 0xAAAAFF).setPotionName("potion.flight").setIcon("minecraft:textures/items/feather.png");
	public static ModPotion potionEffectResist = new ModPotion(ConfigHandler.potionIDs[1], false, 0xAAFFAA).setPotionName("potion.effectResist");
	public static ModPotion potionDay = new ModPotion(ConfigHandler.potionIDs[2], false, 0xFFFF00).setPotionName("potion.day");
	public static ModPotion potionWitherAura = new PotionAura(ConfigHandler.potionIDs[3], true, 0xFFFFFF, wither);
	public static ModPotion potionRegenAura = new PotionAura(ConfigHandler.potionIDs[4], false, 0xFFFFFF, regeneration);
	public static ModPotion potionInvisiblityAura = new PotionAura(ConfigHandler.potionIDs[5], false, 0xFFFFFF, invisibility);
	public static ModPotion potionPoisonAura = new PotionAura(ConfigHandler.potionIDs[6], true, 0xFFFFFF, poison);
	public static ModPotion potionBlindnessAura = new PotionAura(ConfigHandler.potionIDs[7], true, 0xFFFFFF, blindness);
	public static ModPotion potionNauseaAura = new PotionAura(ConfigHandler.potionIDs[8], true, 0xFFFFFF, confusion);
	public static ModPotion potionHungerAura = new PotionAura(ConfigHandler.potionIDs[9], true, 0xFFFFFF, hunger);
	public static ModPotion potionNegativeEffectResist = new ModPotion(ConfigHandler.potionIDs[10], false, 0xAAFFAA).setPotionName("potion.negativeEffectResist");

	public ResourceLocation icon = null;

	public ModPotion(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	@Override
	public ModPotion setIconIndex(int i, int i2) {
		return (ModPotion) super.setIconIndex(i, i2);
	}

	@Override
	public ModPotion setPotionName(String name) {
		return (ModPotion) super.setPotionName(name);
	}

	public ModPotion setIcon(String location) {
		icon = new ResourceLocation(location);
		return this;
	}

	public ModPotion setIcon(ResourceLocation location) {
		icon = location;
		return this;
	}

	@Override
	public boolean hasStatusIcon() {
		// if (icon == null) {
		return true;
		// }
		// Minecraft.getMinecraft().renderEngine.bindTexture(icon);
		// Tessellator t = Tessellator.instance;
		// t.startDrawingQuads();
		// t.addVertexWithUV(0, 0, 0, 0, 0);
		// t.addVertexWithUV(1, 0, 0, 1, 0);
		// t.addVertexWithUV(1, 1, 0, 1, 1);
		// t.addVertexWithUV(0, 1, 0, 0, 1);
		// t.draw();
		// return false;
	}

	public static Potion getPotion(int id) {
		if (id < 0 || id >= potionTypes.length) {
			Log.error("Tried to get Potion object from invalid ID: " + id);
			return null;
		}
		return potionTypes[id];
	}

}
