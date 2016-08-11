package bourgeoisarab.divinealchemy.common.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.utility.Log;

public class EntityPlayerClone extends EntityCreature {

	public final EntityPlayer master;
	public int cloneTier = 0;
	public InventoryPlayer inventory;
	/** This is the item that is in use when the player is holding down the useItemButton (e.g., bow, food, sword) */
	private ItemStack itemInUse;
	/** This field starts off equal to getMaxItemUseDuration and is decremented on each tick */
	private int itemInUseCount;

	public EntityPlayerClone(World world) {
		super(world);
		setSize(0.8F, 1.8F);
		master = DivineAlchemy.proxy.getClientPlayer();
		try {
			inventory = master.inventory;
		} catch (NullPointerException e) {
			inventory = world.playerEntities.get(0).inventory;
			e.printStackTrace();
		}
	}

	public EntityPlayerClone(EntityPlayer master, int cloneTier) {
		super(master.worldObj);
		this.master = master;
		setSize(master.width, master.height);
		if (getCloneEffect() != null) {
			cloneTier = getCloneEffect().getAmplifier();
		}
		inventory = master.inventory;
		itemInUse = master.getItemInUse();
		setCustomNameTag(master.getDisplayName().toString());
		setAlwaysRenderNameTag(true);
		Iterator<PotionEffect> i = master.getActivePotionEffects().iterator();
		while (i.hasNext()) {
			PotionEffect e = i.next();
			if (e.getPotionID() != ModPotion.potionClone.id) {
				addPotionEffect(e);
			}
		}
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
		tasks.addTask(2, new EntityAIOpenDoor(this, true));
		tasks.addTask(3, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		tasks.addTask(3, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
		tasks.addTask(3, new EntityAIWander(this, 0.6D));
		tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	}

	public void resetCoordinates() {
		int range = 8;
		posX = master.posX + (worldObj.rand.nextInt(range * 2) - range);
		posZ = master.posZ + (worldObj.rand.nextInt(range * 2) - range);
		posY = worldObj.getHeight(new BlockPos(posX, 0, posZ)).getY();
		rotationYaw = worldObj.rand.nextFloat() * 360;
		rotationYawHead = worldObj.rand.nextFloat() * 360;
	}

	public PotionEffect getCloneEffect() {
		return master != null ? master.getActivePotionEffect(ModPotion.potionClone) : null;
	}

	public ItemStack getItemInUse() {
		return itemInUse;
	}

	public int getItemInUseCount() {
		return itemInUseCount;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		// if (getDistanceToEntity(master) > 24) {
		// resetCoordinates();
		// }
		if (!worldObj.isRemote) {
			Log.info(posX + ", " + posY + ", " + posZ);
			// Log.info("side check");
		}

	}

	@Override
	public void setDead() {
		super.setDead();
		// if (getCloneEffect() != null && getHealth() > 0.5F) {
		// EntityPlayerClone newClone = new EntityPlayerClone(master);
		// newClone.copyDataFrom(this, true);
		// worldObj.spawnEntityInWorld(newClone);
		// }

	}

	public static List<EntityPlayerClone> getClones(EntityPlayer master) {
		List<EntityPlayerClone> clones = new ArrayList<EntityPlayerClone>();
		for (Entity entity : master.worldObj.loadedEntityList) {
			if (entity instanceof EntityPlayerClone) {
				clones.add((EntityPlayerClone) entity);
			}
		}
		return clones;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		if (cloneTier == 0) {
			damage = getHealth();
		}
		return super.attackEntityFrom(source, damage);
	}

}
