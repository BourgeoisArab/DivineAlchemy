package bourgeoisarab.divinealchemy.common.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import org.apache.commons.lang3.ArrayUtils;

import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionPerformEffect;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import bourgeoisarab.divinealchemy.utility.NBTHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class AIEventHooks {

	@SubscribeEvent
	public void onFoodEaten(PlayerUseItemEvent.Finish event) {
		ItemStack stack = event.item;
		if (stack.getItem() instanceof ItemFood) {
			if (stack.stackTagCompound == null) {
				return;
			}

			List<PotionEffect> effects = NBTHelper.getEffectsFromStack(stack);
			ModPotionHelper.addEffectsToEntity(effects, event.entityPlayer);

			if (stack.stackTagCompound.hasKey("AI.Persistent")) {
				return;
			}

			if (stack.stackTagCompound.getBoolean("AI.Persistent")) {
				if (!event.entityPlayer.getEntityData().hasKey("AI.PersistentIDs")) {
					event.entityPlayer.getEntityData().setIntArray("AI.PersistentIDs", ModPotionHelper.potionsToIntArray(effects)[0]);
				} else {
					event.entityPlayer.getEntityData().setIntArray("AI.PersistentIDs",
							ModPotionHelper.mergeIntArrays(event.entityPlayer.getEntityData().getIntArray("AI.PersistentIDs"), ModPotionHelper.potionsToIntArray(effects)[0]));
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onMilkDrink(PlayerUseItemEvent.Start event) {
		// if (event.item.getItem() instanceof ItemBucketMilk) {
		// event.setCanceled(true);
		// }
	}

	@SubscribeEvent
	public void onItemToss(ItemTossEvent event) {
		// event.entityItem.getEntityData().setString(Ref.NBT.ITEM_THROWER, event.player.getCommandSenderName());
	}

	@SubscribeEvent
	public void onPlayerDeath(PlayerEvent.Clone event) {
		int[] origIDs = event.original.getEntityData().getIntArray(Ref.NBT.PERSISTENT_IDS);
		List<PotionEffect> effects = new ArrayList<PotionEffect>();

		for (int origID : origIDs) {
			PotionEffect effect = event.original.getActivePotionEffect(Potion.potionTypes[origID]);
			if (effect != null) {
				effects.add(new PotionEffect(effect.getPotionID(), effect.getDuration(), effect.getAmplifier()));
			}
		}

		int[] newIDs = new int[effects.size()];
		for (int i = 0; i < newIDs.length; i++) {
			newIDs[i] = effects.get(i).getPotionID();
		}

		event.entityPlayer.getEntityData().setIntArray(Ref.NBT.PERSISTENT_IDS, newIDs);
		NBTHelper.setEffectsForNBT(event.entityPlayer.getEntityData(), effects);
	}

	@SubscribeEvent
	public void onRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		// Persisting Effects
		ModPotionHelper.addEffectsToEntity(NBTHelper.getEffectsFromNBT(player.getEntityData()), player);

		// Soul stuff
		float soul = NBTHelper.getSoulAmount(player);
		if (soul > 0.0F) {
			player.attackEntityFrom(DamageSource.outOfWorld, player.getMaxHealth() * (1 - soul));
		} else {

		}
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		// Log.info(ModPotion.potionNegativeEffectResist.id);
		EntityLivingBase entity = event.entityLiving;
		int[] persistentIDs = new int[]{};
		if (entity.getEntityData().hasKey(Ref.NBT.PERSISTENT_IDS)) {
			persistentIDs = entity.getEntityData().getIntArray(Ref.NBT.PERSISTENT_IDS);
		}
		Iterator<PotionEffect> it = entity.getActivePotionEffects().iterator();
		while (it.hasNext()) {
			PotionEffect effect = it.next();
			if (ModPotion.getPotion(effect.getPotionID()) instanceof PotionPerformEffect) {
				((PotionPerformEffect) ModPotion.getPotion(effect.getPotionID())).performEffect(entity, effect);
			}
			if (ArrayUtils.contains(persistentIDs, effect.getPotionID())) {
				effect.setCurativeItems(new ArrayList<ItemStack>());
			}
		}
		if (entity.getActivePotionEffect(ModPotion.potionFlight) != null) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.capabilities.allowFlying = true;
			} else if (entity instanceof EntityLiving) {
				EntityLiving living = (EntityLiving) entity;
				// living.tasks.addTask(0, new EntityAIFlight(living));
			}
		}
		if (entity.getActivePotionEffect(ModPotion.potionEffectResist) != null && entity.getActivePotionEffects().size() > 1) {
			PotionEffect effect = entity.getActivePotionEffect(ModPotion.potionEffectResist);
			entity.clearActivePotions();
			entity.addPotionEffect(effect);
		}
		if (entity.getActivePotionEffect(ModPotion.potionNegativeEffectResist) != null && entity.getActivePotionEffects().size() > 1) {
			Iterator<PotionEffect> i = entity.getActivePotionEffects().iterator();
			ItemStack cure = new ItemStack(Blocks.dragon_egg);
			while (i.hasNext()) {
				PotionEffect effect = i.next();
				if (ModPotion.getPotion(effect.getPotionID()).isBadEffect()) {
					effect.addCurativeItem(cure);
				}
			}
			entity.curePotionEffects(cure);
		}
		if (entity.getActivePotionEffect(ModPotion.potionDay) != null && entity.worldObj.getWorldTime() > 12000) {
			entity.worldObj.setWorldTime(1000);
		}
	}
}
