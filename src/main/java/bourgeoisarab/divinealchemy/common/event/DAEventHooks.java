package bourgeoisarab.divinealchemy.common.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.apache.commons.lang3.ArrayUtils;

import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionPerformEffect;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.common.tileentity.IEffectProvider;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.InventoryHelper;
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

			List<PotionEffect> effects = NBTEffectHelper.getEffectsFromStack(stack).getEffects();
			PotionProperties properties = NBTEffectHelper.getPropertiesFromNBT(stack.stackTagCompound);
			ModPotionHelper.addEffectsToEntity(effects, event.entityPlayer);

			if (properties.isPersistent) {
				if (!event.entityPlayer.getEntityData().hasKey(Ref.NBT.PERSISTENT_IDS)) {
					event.entityPlayer.getEntityData().setIntArray(Ref.NBT.PERSISTENT_IDS, ModPotionHelper.potionsToIntArray(effects)[0]);
				} else {
					event.entityPlayer.getEntityData().setIntArray(Ref.NBT.PERSISTENT_IDS,
							ModPotionHelper.mergeIntArrays(event.entityPlayer.getEntityData().getIntArray(Ref.NBT.PERSISTENT_IDS), ModPotionHelper.potionsToIntArray(effects)[0]));
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

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTankClick(PlayerInteractEvent event) {
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			TileEntity entity = event.world.getTileEntity(event.x, event.y, event.z);
			if (entity instanceof IFluidHandler && !(entity instanceof IEffectProvider)) {
				IFluidHandler tile = (IFluidHandler) entity;
				ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[event.face];
				FluidStack fluid = getFluid(tile, dir);

				// Effects e = new Effects();
				// e.add(new PotionEffect(10, 530, 1), false);
				// NBTEffectHelper.setEffectsForFluid(fluid, e);
				EntityPlayer player = event.entityPlayer;
				ItemStack stack = player.getCurrentEquippedItem();

				if (stack == null) {
					Log.info(NBTEffectHelper.getEffectsFromFluid(fluid));
					return;
				}

				if (fluid == null && tile.canFill(dir, ModFluids.fluidPotion)) {
					Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
					PotionProperties properties = new PotionProperties(stack.getItemDamage());
					if (stack != null && effects != null) {
						int capacity = 0;
						ItemStack newStack = null;
						if (stack.getItem() == ModItems.itemPotionBottle) {
							capacity = 333;
							newStack = new ItemStack(Items.glass_bottle);
						} else if (stack.getItem() == ModItems.itemBucketPotion) {
							capacity = FluidContainerRegistry.BUCKET_VOLUME;
							newStack = new ItemStack(Items.bucket);
						}
						if (newStack != null) {
							tile.fill(dir, NBTEffectHelper.setEffectsForFluid(new FluidStack(ModFluids.fluidPotion, capacity), effects), true);
							player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
							// event.setCanceled(true);
						}
					}
				} else if (tile.canDrain(dir, ModFluids.fluidPotion)) {
					FluidStack f = getFluid(tile, dir);
					Effects effects = NBTEffectHelper.getEffectsFromFluid(f);
					PotionProperties properties = NBTEffectHelper.getPropertiesFromNBT(f != null ? f.tag : null);
					if (stack != null) {
						ItemStack newStack = null;
						int capacity = 0;
						if (stack.getItem() == Items.glass_bottle) {
							newStack = new ItemStack(ModItems.itemPotionBottle, 1, properties != null ? properties.getMetaValue() : 0);
							capacity = 333;
						} else if (stack.getItem() == Items.bucket) {
							newStack = new ItemStack(ModItems.itemBucketPotion, 1, properties != null ? properties.getMetaValue() : 0);
							capacity = FluidContainerRegistry.BUCKET_VOLUME;
						}
						if (newStack != null) {
							FluidStack drained = tile.drain(dir, capacity, false);
							if (drained != null && drained.amount >= capacity) {
								tile.drain(dir, capacity, true);
								InventoryHelper.addStackToInventory(event.entityPlayer, stack, newStack, true);
								event.setCanceled(true);
							}
						}
					}
				}
			}
		}
	}

	private FluidStack getFluid(IFluidHandler tile, ForgeDirection dir) {
		FluidTankInfo[] info = tile.getTankInfo(dir);
		if (info.length < 1) {
			return null;
		}
		return info[0].fluid;
	}

	@SubscribeEvent
	public void onPlayerDeath(PlayerEvent.Clone event) {
		int[] originalIDs = event.original.getEntityData().getIntArray(Ref.NBT.PERSISTENT_IDS);
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

		event.entityPlayer.getEntityData().setIntArray(Ref.NBT.PERSISTENT_IDS, newIDs);
		NBTEffectHelper.setEffectsForNBT(event.entityPlayer.getEntityData(), new Effects(effects, new ArrayList<Boolean>()));
	}

	@SubscribeEvent
	public void onRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		// Persisting Effects
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

	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
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
				// add flight task to entity
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
		// if (entity instanceof EntityPlayer) {
		// if (entity.getActivePotionEffect(ModPotion.potionParticle) != null) {
		//
		// }
		// }
	}

	@SubscribeEvent
	public void onItemThrow(ItemTossEvent event) {
		event.entityItem.getEntityData().setString(event.player.getCommandSenderName(), Ref.NBT.THROWER);
	}

	@SubscribeEvent
	public void onParticleSpawn(EntityEvent.EntityConstructing event) {
		if (event.entity instanceof EntitySpellParticleFX) {
			EntitySpellParticleFX particle = (EntitySpellParticleFX) event.entity;
			int partialTicks = 100;
			// float x = (float)stityFX.interpPosZ - particle.prevPosZ);
			double offset = 0.5;
			// AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x - offset, y - offset, z - offset, x + offset, y + offset, z + offset);
			// List<EntityLivingBase> entities = particle.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
			// for (EntityLivingBase player : entities) {
			// if (player != null && player.getActivePotionEffect(ModPotion.potionParticle) != null) {
			// particle.setDead();
			// }
			// }
			float x = (float) -(particle.prevPosX + (particle.posX - particle.prevPosX) * partialTicks - EntityFX.interpPosX);
			float y = (float) -(particle.prevPosY + (particle.posY - particle.prevPosY) * partialTicks - EntityFX.interpPosY);
			float z = (float) -(particle.prevPosZ + (particle.posZ - particle.prevPosZ) * partialTicks - EntityFX.interpPosZ);
			// if (particle.worldObj.isRemote) {
			// EntityPlayer player = DivineAlchemy.proxy.getClientPlayer();
			// Log.info("Particle: " + x + ", " + y + ", " + z);
			// Log.info("Player: " + player.posX + ", " + player.posY + ", " + player.posZ);
			// }
			// TODO: Kill only the ones in the player's face; not all of them
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
}
