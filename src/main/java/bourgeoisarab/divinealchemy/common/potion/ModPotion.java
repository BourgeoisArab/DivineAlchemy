package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import bourgeoisarab.divinealchemy.common.event.DAEventHooks;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.network.MessageRemoveEffect;
import bourgeoisarab.divinealchemy.network.NetworkHandler;
import bourgeoisarab.divinealchemy.utility.Log;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModPotion extends Potion {

	public static ModPotion potionFlight = new PotionFlight(ConfigHandler.potionIDs[0], false, 0xAAAAFF);
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
	public static ModPotion potionParticle = new ModPotion(ConfigHandler.potionIDs[11], false, 0x888888).setPotionName("potion.particle");
	public static ModPotion potionFiendFyre = new PotionFiendFyre(ConfigHandler.potionIDs[12], true, 0x884400);
	public static ModPotion potionSealedMouth = new ModPotion(ConfigHandler.potionIDs[13], true, 0x000000).setPotionName("potion.sealedmouth");
	public static ModPotion potionReach = new PotionReach(ConfigHandler.potionIDs[14], false, 0xFFFFFF);
	public static ModPotion potionExplodeAbsorb = new PotionExplosionAbsorb(ConfigHandler.potionIDs[15], false, 0x888888);
	public static ModPotion potionDeafness = new ModPotion(ConfigHandler.potionIDs[16], true, 0x284488).setPotionName("potion.deaf");
	public static ModPotion potionTemporal = new PotionTemporal(ConfigHandler.potionIDs[17], true, 0x444444);
	public static ModPotion potionClone = new PotionClone(ConfigHandler.potionIDs[18], false, 0xFFFFFF);

	public ResourceLocation icon = null;

	public ModPotion(int id, boolean isBadEffect, int colour) {
		super(id, isBadEffect, colour);
	}

	@Override
	public ModPotion setIconIndex(int u, int v) {
		return (ModPotion) super.setIconIndex(u, v);
	}

	@Override
	public ModPotion setPotionName(String name) {
		return (ModPotion) super.setPotionName(name);
	}

	public ModPotion setIcon(String location) {
		icon = new ResourceLocation(location);
		return this;
	}

	@Override
	public boolean hasStatusIcon() {
		return icon == null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
		if (icon != null) {
			mc.renderEngine.bindTexture(icon);
			// net.minecraft.client.renderer.Tessellator t = net.minecraft.client.renderer.Tessellator.instance;
			// GL11.glDisable(GL11.GL_LIGHTING);
			//
			// t.startDrawingQuads();
			// t.setColorOpaque_I(0xFFFFFF);
			// t.addVertexWithUV(x, y, 0, 0, 0);
			// t.addVertexWithUV(x + 16, y, 0, 0, 0);
			// t.addVertexWithUV(x + 16, y + 16, 0, 0, 0);
			// t.addVertexWithUV(x, y + 16, 0, 0, 0);
			// t.draw();
			mc.ingameGUI.drawRect(0, 0, 32, 32, 0xFF00FF);
			mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 32, 32);
		}
	}

	@Override
	public boolean shouldRenderInvText(PotionEffect effect) {
		return true;
	}

	/**
	 * Called when the PotionEffect is to be removed from the given entity
	 * 
	 * @param entity
	 * @param amplifier
	 */
	public void removeEffect(EntityLivingBase entity, int amplifier) {

	}

	/**
	 * Called every tick the potion is active through {@link DAEventHooks}
	 * 
	 * @param entity to which the effect is to be applied
	 * @param effect
	 */
	public void applyEffect(EntityLivingBase entity, PotionEffect effect) {

	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap map, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, map, amplifier);
		removeEffect(entity, amplifier);
		NetworkHandler.sendToAll(new MessageRemoveEffect(entity, id, amplifier));
	}

	public static Potion getPotion(int id) {
		if (id < 0 || id >= potionTypes.length) {
			Log.error("Tried to get Potion object from invalid ID: " + id);
			return null;
		}
		return potionTypes[id];
	}

}
