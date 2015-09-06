package bourgeoisarab.divinealchemy.common.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.utility.Log;

import com.mojang.authlib.GameProfile;

public class EntityPlayerClonePlayer extends EntityPlayer {

	public EntityPlayer master;
	/**
	 * Amplifier of the clone potion effect
	 */
	public int cloneTier = 0;
	public EntityAITasks tasks;
	public PathNavigate navigator;

	public EntityPlayerClonePlayer(World world) {
		this((EntityPlayer) world.playerEntities.get(0), 0);
	}

	public EntityPlayerClonePlayer(EntityPlayer master, int cloneTier) {
		super(master.worldObj, new GameProfile(master.getUniqueID(), master.getDisplayName()));
		this.master = master;
		this.cloneTier = cloneTier;
		setSize(master.width, master.height);
		eyeHeight = getDefaultEyeHeight();
		tasks = new EntityAITasks(master.worldObj != null ? master.worldObj.theProfiler : null);
	}

	public static List<EntityPlayerClonePlayer> getClones(EntityPlayer master) {
		List<EntityPlayerClonePlayer> clones = new ArrayList<EntityPlayerClonePlayer>();
		for (Entity entity : (List<Entity>) master.worldObj.loadedEntityList) {
			if (entity instanceof EntityPlayerClonePlayer) {
				clones.add((EntityPlayerClonePlayer) entity);
			}
		}
		return clones;
	}

	public void resetCoordinates() {
		int range = 8;
		// posX = master.posX + (worldObj.rand.nextInt(range * 2) - range);
		// posZ = master.posZ + (worldObj.rand.nextInt(range * 2) - range);
		// posY = worldObj.getHeightValue((int) posX, (int) posZ) + 2;
		posX = master.posX;
		posY = master.posY;
		posZ = master.posZ;
		Log.info(master.posX + ", " + master.posY + ", " + master.posZ);
		// rotationYaw = worldObj.rand.nextFloat() * 360;
		// rotationYawHead = worldObj.rand.nextFloat() * 360;
	}

	public PotionEffect getCloneEffect() {
		return master != null ? master.getActivePotionEffect(ModPotion.potionClone) : null;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		// if (getDistanceToEntity(master) < 2.0F && worldObj.getWorldTime() % 10 == 0) {
		// Log.info(posX - master.posX + ", " + (posY - master.posY) + ", " + (posZ - master.posZ));
		// }
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		// if (source == DamageSource.inWall) {
		// try {
		// EntityPlayer p = null;
		// p.setDead();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		if (cloneTier == 0) {
			damage = getHealth();
		}
		return super.attackEntityFrom(source, damage);
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return super.isEntityInsideOpaqueBlock();
		// for (int i = 0; i < 8; ++i) {
		// float f = ((i >> 0) % 2 - 0.5F) * width * 0.8F;
		// float f1 = ((i >> 1) % 2 - 0.5F) * 0.1F;
		// float f2 = ((i >> 2) % 2 - 0.5F) * width * 0.8F;
		// int j = MathHelper.floor_double(posX + f);
		// int k = MathHelper.floor_double(posY + getEyeHeight() + f1);
		// int l = MathHelper.floor_double(posZ + f2);
		//
		// if (worldObj.getBlock(j, k, l).isNormalCube()) {
		// // return true;
		// return false;
		// }
		// }
		// return false;
	}

	@Override
	public void addChatMessage(IChatComponent chat) {

	}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
		// return new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY + 0.5D), MathHelper.floor_double(posZ));
	}

}
