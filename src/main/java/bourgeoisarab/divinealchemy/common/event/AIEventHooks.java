package bourgeoisarab.divinealchemy.common.event;

import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import bourgeoisarab.divinealchemy.utility.NBTHelper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
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
	public void onPlayerDeath(PlayerEvent.Clone event) {
		int[] origIDs = event.original.getEntityData().getIntArray(Ref.NBT.PERSISTENT_IDS);
		List<PotionEffect> effects = new ArrayList<PotionEffect>();

		for (int i = 0; i < origIDs.length; i++) {
			PotionEffect effect = event.original.getActivePotionEffect(Potion.potionTypes[origIDs[i]]);
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
		EntityLivingBase entity = event.entityLiving;
		if (entity.getActivePotionEffect(ModPotion.potionFlight) != null) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				player.capabilities.allowFlying = true;
			} else if (entity instanceof EntityLiving) {
				// TODO
				EntityLiving living = (EntityLiving) entity;
				// living.tasks.addTask(0, new EntityAIFlight(living));
				if (entity.getActivePotionEffect(ModPotion.potionFlight).getDuration() <= 5) {
					// EntityAIBase task = null;
					// for (int i = 0; i < living.tasks.taskEntries.size(); i++) {
					// if (living.tasks.taskEntries.get(i) instanceof EntityAIFlight) {
					// task = (EntityAIBase) living.tasks.taskEntries.get(i);
					// }
					// }
					// living.tasks.removeTask(task);
				}
			}
		} else {
			if (entity instanceof EntityPlayer) {
				((EntityPlayer) entity).capabilities.allowFlying = false;
				((EntityPlayer) entity).capabilities.isFlying = false;
			}
		}
		if (entity.getActivePotionEffect(ModPotion.potionEffectResist) != null && entity.getActivePotionEffects().size() > 1) {
			// PotionEffect effect = new PotionEffect(entity.getActivePotionEffect(ModPotion.potionEffectResist));
			PotionEffect effect = entity.getActivePotionEffect(ModPotion.potionEffectResist);
			entity.clearActivePotions();
			entity.addPotionEffect(effect);
			// entity.getActivePotionEffect(ModPotion.potionEffectResist).setCurativeItems(new ArrayList());
			// List curativeItems = ((PotionEffect) entity.getActivePotionEffects().iterator().next()).getCurativeItems();
			// if (curativeItems != null && curativeItems.size() > 0) {
			// entity.curePotionEffects((ItemStack) curativeItems.get(0));
			// }
		}
		if (entity.getActivePotionEffect(ModPotion.potionDay) != null && entity.worldObj.getWorldTime() > 12000) {
			entity.worldObj.setWorldTime(1000);
		}
	}
}
