package bourgeoisarab.divinealchemy.common.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.ExplosionEvent;

import org.apache.commons.lang3.ArrayUtils;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.Log;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTPlayerHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class DAEventHooks {

	@SubscribeEvent
	public void onFoodEaten(PlayerUseItemEvent.Finish event) {
		ItemStack stack = event.item;
		if (stack.getItem() instanceof ItemFood) {
			if (stack.stackTagCompound == null) {
				return;
			}

			Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
			PotionProperties properties = NBTEffectHelper.getPropertiesFromNBT(stack.stackTagCompound);
			if (effects != null) {
				ModPotionHelper.addEffectsToEntity(effects.getEffects(), event.entityPlayer);
			}

			if (properties != null && properties.isPersistent) {
				if (!event.entityPlayer.getEntityData().hasKey(NBTNames.PERSISTENT_IDS)) {
					event.entityPlayer.getEntityData().setIntArray(NBTNames.PERSISTENT_IDS, ModPotionHelper.potionsToIntArray(effects.getEffects())[0]);
				} else {
					event.entityPlayer.getEntityData().setIntArray(NBTNames.PERSISTENT_IDS,
							ModPotionHelper.mergeIntArrays(event.entityPlayer.getEntityData().getIntArray(NBTNames.PERSISTENT_IDS), ModPotionHelper.potionsToIntArray(effects.getEffects())[0]));
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemConsumed(PlayerUseItemEvent.Tick event) {
		ItemStack stack = event.item;
		if (stack != null) {
			Item item = stack.getItem();
			if (item.getItemUseAction(stack) == EnumAction.eat || item.getItemUseAction(stack) == EnumAction.drink) {
				if (event.entityPlayer.getActivePotionEffect(ModPotion.potionSealedMouth) != null) {
					if (event.duration <= item.getMaxDamage(stack) / 2 || event.duration <= 2) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(PlayerEvent.Clone event) {
		// Transfer of death-persistent potion IDs
		int[] originalIDs = event.original.getEntityData().getIntArray(NBTNames.PERSISTENT_IDS);
		List<PotionEffect> effects = new ArrayList<PotionEffect>();

		for (int id : originalIDs) {
			PotionEffect effect = event.original.getActivePotionEffect(ModPotion.getPotion(id));
			if (effect != null) {
				effects.add(new PotionEffect(effect.getPotionID(), effect.getDuration(), effect.getAmplifier()));
			}
		}

		int[] newIDs = new int[effects.size()];
		for (int i = 0; i < newIDs.length; i++) {
			newIDs[i] = effects.get(i).getPotionID();
		}

		event.entityPlayer.getEntityData().setIntArray(NBTNames.PERSISTENT_IDS, newIDs);
		// Transfer of previously active persistent effects to be applied after respawn
		NBTEffectHelper.setEffectsForNBT(event.entityPlayer.getEntityData(), new Effects(effects, new ArrayList<Boolean>()));
	}

	@SubscribeEvent
	public void onRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		// Reapplying previously active persistent effects
		Effects effects = NBTEffectHelper.getEffectsFromNBT(player.getEntityData());
		if (effects != null) {
			ModPotionHelper.addEffectsToEntity(effects.getEffects(), player);
		}

		// Soul stuff
		float soul = NBTPlayerHelper.getSoulAmount(player);
		if (soul > 0.0F) {
			player.attackEntityFrom(DamageSource.outOfWorld, player.getMaxHealth() * (1 - soul));
		} else {
			// Respawn in the nether without all items
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityUpdate(LivingUpdateEvent event) {
		EntityLivingBase entity = event.entityLiving;

		// PotionEffects
		int[] persistentIDs = new int[]{};
		if (entity.getEntityData().hasKey(NBTNames.PERSISTENT_IDS)) {
			persistentIDs = entity.getEntityData().getIntArray(NBTNames.PERSISTENT_IDS);
		}
		Iterator<PotionEffect> it = entity.getActivePotionEffects().iterator();
		while (it.hasNext()) {
			PotionEffect effect = it.next();
			Potion p = ModPotion.getPotion(effect.getPotionID());
			if (p != null && p.isInstant()) {
				ArrayList<ItemStack> list = new ArrayList<ItemStack>();
				list.add(new ItemStack(Items.brewing_stand));
				effect.setCurativeItems(list);
				entity.curePotionEffects(new ItemStack(Items.brewing_stand));
			}
			if (p instanceof ModPotion) {
				((ModPotion) p).applyEffect(entity, effect, effect.getAmplifier());
			}
			if (ArrayUtils.contains(persistentIDs, effect.getPotionID())) {
				effect.setCurativeItems(new ArrayList<ItemStack>());
			}
		}

		// Resetting to normal trades if not trading
		if (entity instanceof EntityVillager) {
			EntityVillager villager = (EntityVillager) entity;
			if (!villager.isTrading() && villager.getEntityData().hasKey(NBTNames.OLD_TRADES)) {
				NBTTagCompound tag = new NBTTagCompound();
				villager.writeEntityToNBT(tag);
				tag.setTag("Offers", villager.getEntityData().getCompoundTag(NBTNames.OLD_TRADES));
				villager.readEntityFromNBT(tag);
				villager.getEntityData().removeTag(NBTNames.OLD_TRADES);
			}
		}
	}

	@SubscribeEvent
	public void onJump(LivingJumpEvent event) {
		EntityLivingBase entity = event.entityLiving;
		if (entity.getActivePotionEffect(ModPotion.potionTemporal) != null) {
			entity.motionY *= 2;
			// TODO
		}
	}

	@SubscribeEvent
	public void onItemThrow(ItemTossEvent event) {
		event.entityItem.getEntityData().setString(event.player.getCommandSenderName(), NBTNames.THROWER);
	}

	@SubscribeEvent
	public void onParticleSpawn(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntitySpellParticleFX) {
			if (event.entity.worldObj.isRemote && DivineAlchemy.proxy.getClientPlayer().getActivePotionEffect(ModPotion.potionParticle) != null) {
				event.entity.setDead();
				// int partialTicks = 100;
				// // float x = (float)stityFX.interpPosZ - particle.prevPosZ);
				// double offset = 0.5;
				// // AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x - offset, y - offset, z - offset, x + offset, y + offset, z + offset);
				// // List<EntityLivingBase> entities = particle.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
				// // for (EntityLivingBase player : entities) {
				// // if (player != null && player.getActivePotionEffect(ModPotion.potionParticle) != null) {
				// // particle.setDead();
				// // }
				// // }
				// float x = (float) -(particle.prevPosX + (particle.posX - particle.prevPosX) * partialTicks - EntityFX.interpPosX);
				// float y = (float) -(particle.prevPosY + (particle.posY - particle.prevPosY) * partialTicks - EntityFX.interpPosY);
				// float z = (float) -(particle.prevPosZ + (particle.posZ - particle.prevPosZ) * partialTicks - EntityFX.interpPosZ);
				// if (particle.worldObj.isRemote) {
				// EntityPlayer player = DivineAlchemy.proxy.getClientPlayer();
				// Log.info("Particle: " + x + ", " + y + ", " + z);
				// Log.info("Player: " + player.posX + ", " + player.posY + ", " + player.posZ);
				// }
				// TODO: Kill only the ones in the player's face; not all of them
			}
		}
	}

	public void renderParticle(Tessellator t, float partialTicks, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
		// float minU = (float) particleTextureIndexX / 16.0F;
		// float maxU = minU + 0.0624375F;
		// float minV = (float) particleTextureIndexY / 16.0F;
		// float maxV = minV + 0.0624375F;
		// float scale = 0.1F * particleScale;
		//
		// if (particleIcon != null) {
		// minU = particleIcon.getMinU();
		// maxU = particleIcon.getMaxU();
		// minV = particleIcon.getMinV();
		// maxV = particleIcon.getMaxV();
		// }
		//
		// float x = (float) (prevPosX + (posX - prevPosX) * (double) partialTicks - EntityFX.interpPosX);
		// float y = (float) (prevPosY + (posY - prevPosY) * (double) partialTicks - EntityFX.interpPosY);
		// float z = (float) (prevPosZ + (posZ - prevPosZ) * (double) partialTicks - EntityFX.interpPosZ);
		// t.addVertexWithUV(x - rotationX * scale - rotationYZ * scale, y - rotationXZ * scale, z - rotationZ * scale - rotationXY * scale, maxU, maxV);
		// t.addVertexWithUV(x - rotationX * scale + rotationYZ * scale, y + rotationXZ * scale, z - rotationZ * scale + rotationXY * scale, maxU, minV);
		// t.addVertexWithUV(x + rotationX * scale + rotationYZ * scale, y + rotationXZ * scale, z + rotationZ * scale + rotationXY * scale, minU, minV);
		// t.addVertexWithUV(x + rotationX * scale - rotationYZ * scale, y - rotationXZ * scale, z + rotationZ * scale - rotationXY * scale, minU, maxV);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onExplosion(ExplosionEvent.Start event) {
		float size = event.explosion.explosionSize;
		double x = event.explosion.explosionX, y = event.explosion.explosionY, z = event.explosion.explosionZ;
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x - size, y - size, z - size, x + size, y + size, z + size);
		List<EntityLivingBase> entities = event.world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
		List<EntityPlayer> players = new ArrayList<EntityPlayer>();
		for (EntityLivingBase entity : entities) {
			if (entity.getActivePotionEffect(ModPotion.potionExplodeAbsorb) != null) {
				if (entity instanceof EntityPlayer) {
					players.add((EntityPlayer) entity);
				}
				event.setCanceled(true);
			}
		}
		for (EntityPlayer player : players) {
			NBTPlayerHelper.addAbsorbedExplosion(player, size / 4 / players.size());
		}
	}

	@SubscribeEvent
	public void onVillagerTrade(EntityInteractEvent event) {
		float divinity = NBTPlayerHelper.getAbsDivinity(event.entityPlayer);
		if (event.target instanceof EntityVillager && divinity > 0.4F) {
			EntityVillager villager = (EntityVillager) event.target;
			MerchantRecipeList oldRecipes = villager.getRecipes(event.entityPlayer);
			villager.getEntityData().setTag(NBTNames.OLD_TRADES, oldRecipes.getRecipiesAsTags());
			float discount = 1.0F - (divinity - 0.3F) * 1.4285F;
			for (int i = 0; i < oldRecipes.size(); i++) {
				MerchantRecipe recipe = (MerchantRecipe) oldRecipes.get(i);
				ItemStack buy = recipe.getItemToBuy();
				buy.stackSize = (int) (buy.stackSize * discount);
				if (buy.stackSize < 1 && divinity < 1.0F) {
					buy.stackSize = 1;
				}

				ItemStack buySecond = recipe.getSecondItemToBuy();
				if (buySecond != null) {
					buySecond.stackSize = (int) (buySecond.stackSize * discount);
					if (buySecond.stackSize < 1 && divinity < 1.0F) {
						buySecond.stackSize = 1;
					}

				}
			}
		}
	}

	@SubscribeEvent
	public void onLightning(EntityStruckByLightningEvent event) {
		World world = event.entity.worldObj;
		if (event.entity instanceof EntityPlayer && !world.isRemote) {
			EntityPlayer player = (EntityPlayer) event.entity;
			float divinity = NBTPlayerHelper.getDivinity(player);
			if (divinity <= -0.5F) {
				Log.info("Second lightning strike");
				// if (world.rand.nextFloat() < Math.abs(divinity)) {
				// world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ));
				// }
			}
		}
	}

	@SubscribeEvent
	public void livingDrops(LivingDropsEvent event) {
		EntityLivingBase entity = event.entityLiving;
		if (!entity.worldObj.isRemote) {
			if (entity.getEntityData().hasKey(NBTNames.BUTCHER)) {
				World world = entity.worldObj;
				EntityPlayer player = world.getPlayerEntityByName(entity.getEntityData().getString(NBTNames.BUTCHER));
				float divinity = NBTPlayerHelper.getAbsDivinity(player);
				if (world.rand.nextFloat() < divinity * 1.42857F) {
					ItemStack stack = new ItemStack(ModItems.organ);
					int organ = world.rand.nextInt(ModItems.organ.organs.length);
					if (entity instanceof EntityPlayer || entity instanceof EntityVillager) {
						stack.setItemDamage(ModItems.organ.getMetaValue(organ, 0));
					} else if (entity instanceof EntityCow || entity instanceof EntitySheep || entity instanceof EntityHorse || entity instanceof EntityPig) {
						stack.setItemDamage(ModItems.organ.getMetaValue(organ, 1));
					} else if (entity instanceof EntityZombie) {
						stack.setItemDamage(ModItems.organ.getMetaValue(organ, 2));
					}
					EntityItem item = new EntityItem(world, entity.posX, entity.posY, entity.posZ, stack);
					event.drops.add(item);
				}
				NBTPlayerHelper.setDivnity(player, NBTPlayerHelper.getProcessedSigmoid(divinity, 0.12F, 0.75F));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event) {
		EntityPlayer player = event.entityPlayer;
		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.butcherKnife) {
			event.target.getEntityData().setString(NBTNames.BUTCHER, player.getCommandSenderName());
		}
	}

	@SubscribeEvent
	public void onItemUpdate(ItemTooltipEvent event) {
		ItemStack stack = event.itemStack;
		List<String> list = event.toolTip;
		if (stack.getItem() instanceof ItemFood && !NBTEffectHelper.getHiddenFoodEffects(stack)) {
			if (PotionProperties.getBlessed(stack.getItemDamage())) {
				list.add("Blessed");
			}
			if (PotionProperties.getBlessed(stack.getItemDamage())) {
				list.add("Cursed");
			}
			Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
			if (effects != null) {
				for (int i = 0; i < effects.size(); i++) {
					Potion potion = ModPotion.getPotion(effects.getEffect(i).getPotionID());
					String s1 = I18n.format(potion.getName());
					s1 = s1 + " " + I18n.format("enchantment.level." + (effects.getEffect(i).getAmplifier() + 1));
					list.add((effects.getSideEffect(i) ? EnumChatFormatting.DARK_RED : "") + s1 + (potion.isInstant() ? "" : " (" + Potion.getDurationString(effects.getEffect(i)) + ")"));
				}
			}
		}
	}
}
