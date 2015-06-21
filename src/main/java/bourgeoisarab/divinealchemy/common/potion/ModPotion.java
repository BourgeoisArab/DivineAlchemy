package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class ModPotion extends Potion {

	public static ModPotion potionFlight = new ModPotion(50, false, 0xAAAAFF).setPotionName("potion.flight").setIcon("minecraft:textures/items/feather.png");
	public static ModPotion potionEffectResist = new ModPotion(51, false, 0xAAFFAA).setPotionName("potion.effectResist");
	public static ModPotion potionDay = new ModPotion(52, false, 0xFFFF00).setPotionName("potion.day");

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
		if (icon == null) {
			return true;
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(icon);
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertex(0, 0, 0);
		t.addVertex(1, 0, 0);
		t.addVertex(1, 1, 0);
		t.addVertex(0, 1, 0);
		t.draw();
		return false;
	}

	public static Potion getPotion(int id) {
		return id > 0 && id < Potion.potionTypes.length ? Potion.potionTypes[id] : null;
	}

}
