package bourgeoisarab.divinealchemy.common.potion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import bourgeoisarab.divinealchemy.common.block.BrewingSetup;
import bourgeoisarab.divinealchemy.common.event.DAEventHooks;
import bourgeoisarab.divinealchemy.network.MessageRemoveEffect;
import bourgeoisarab.divinealchemy.network.NetworkHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.Log;

public class ModPotion extends Potion {

	public static ModPotion potionFlight = new PotionFlight("flight", false, 0xAAAAFF);
	public static ModPotion potionEffectResist = new ModPotion("effectResist", false, 0xAAFFAA).setPotionName("potion.effectResist");
	public static ModPotion potionDay = new ModPotion("day", false, 0xFFFF00).setPotionName("potion.day");
	public static ModPotion potionWitherAura = new PotionAura("auraWither", true, 0xFFFFFF, wither);
	public static ModPotion potionRegenAura = new PotionAura("auraRegen", false, 0xFFFFFF, regeneration);
	public static ModPotion potionInvisiblityAura = new PotionAura("auraInvisibility", false, 0xFFFFFF, invisibility);
	public static ModPotion potionPoisonAura = new PotionAura("auraPoison", true, 0xFFFFFF, poison);
	public static ModPotion potionBlindnessAura = new PotionAura("auraBlindness", true, 0xFFFFFF, blindness);
	public static ModPotion potionNauseaAura = new PotionAura("auraNausea", true, 0xFFFFFF, confusion);
	public static ModPotion potionHungerAura = new PotionAura("auraHunger", true, 0xFFFFFF, hunger);
	public static ModPotion potionNegativeEffectResist = new ModPotion("effectResistNeg", false, 0xAAFFAA).setPotionName("potion.negativeEffectResist");
	public static ModPotion potionParticle = new PotionParticle("particle", false, 0x888888).setPotionName("potion.particle");
	public static ModPotion potionFiendFyre = new PotionFiendFyre("fiendFyre", true, 0x884400);
	public static ModPotion potionSealedMouth = new ModPotion("sealedMouth", true, 0x000000).setPotionName("potion.sealedmouth");
	public static ModPotion potionReach = new PotionReach("reach", false, 0xFFFFFF);
	public static ModPotion potionExplodeAbsorb = new PotionExplosionAbsorb("explodeAbsorb", false, 0x888888);
	public static ModPotion potionDeafness = new ModPotion("deafness", true, 0x284488).setPotionName("potion.deaf");
	public static ModPotion potionTemporal = new PotionTemporal("temporal", true, 0x444444);
	public static ModPotion potionClone = new PotionClone("clone", false, 0xFFFFFF);
	public static ModPotion potionSmite = new PotionSmite("smite", true, 0xFFFFFF);
	public static ModPotion potionMagnet = new PotionMagnet("magnet", false, 0xFFFFFF);

	public static List<Potion> badEffects = new ArrayList<Potion>();
	public static List<Potion> goodEffects = new ArrayList<Potion>();
	static {
		for (Potion p : Potion.potionTypes) {
			if (p != null) {
				if (p.isBadEffect()) {
					badEffects.add(p);
				} else {
					goodEffects.add(p);
				}
			}
		}
	}

	public final BrewingSetup brewingSetup;

	public ModPotion(String name, boolean isBadEffect, int colour, BrewingSetup setup) {
		super(new ResourceLocation(Ref.Location.TEXTURES + "potion/" + name + ".png"), isBadEffect, colour);
		brewingSetup = setup;
	}

	public ModPotion(String name, boolean isBadEffect, int colour) {
		this(name, isBadEffect, colour, BrewingSetup.defaultSetup);
	}

	@Override
	public ModPotion setIconIndex(int u, int v) {
		return (ModPotion) super.setIconIndex(u, v);
	}

	@Override
	public ModPotion setPotionName(String name) {
		return (ModPotion) super.setPotionName(name);
	}

	// @Override
	// @SideOnly(Side.CLIENT)
	// public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
	// if (icon != null) {
	// mc.renderEngine.bindTexture(icon);
	// // TODO
	// // net.minecraft.client.renderer.Tessellator t = net.minecraft.client.renderer.Tessellator.instance;
	// // GL11.glDisable(GL11.GL_LIGHTING);
	// //
	// // t.startDrawingQuads();
	// // t.setColorOpaque_I(0xFFFFFF);
	// // t.addVertexWithUV(x, y, 0, 0, 0);
	// // t.addVertexWithUV(x + 16, y, 0, 0, 0);
	// // t.addVertexWithUV(x + 16, y + 16, 0, 0, 0);
	// // t.addVertexWithUV(x, y + 16, 0, 0, 0);
	// // t.draw();
	// mc.ingameGUI.drawRect(0, 0, 32, 32, 0xFF00FF);
	// mc.ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 32, 32);
	// }
	// }

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
	public void applyEffect(EntityLivingBase entity, PotionEffect effect, int amplifier) {

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

	public void punishForFailedSetup(EntityPlayer player) {
	}

	public static Potion getRandomPotion(Random rand, boolean isBadEffect) {
		return isBadEffect ? badEffects.get(rand.nextInt(badEffects.size())) : goodEffects.get(rand.nextInt(goodEffects.size()));
	}

}
