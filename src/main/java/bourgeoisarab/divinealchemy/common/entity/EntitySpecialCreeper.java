package bourgeoisarab.divinealchemy.common.entity;

import bourgeoisarab.divinealchemy.common.entity.ai.EntityAISpecialCreeperSwell;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySpecialCreeper extends EntityMob {

	/**
	 * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go weird)
	 */
	private int lastActiveTime;
	/**
	 * The amount of time since the creeper was close enough to the player to ignite
	 */
	private int timeSinceIgnited;
	private int fuseTime = 30;
	private int explosionRadius = 3;

	public EntitySpecialCreeper(World world) {
		super(world);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAISpecialCreeperSwell(this));
		tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		tasks.addTask(5, new EntityAIWander(this, 0.8D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * The number of iterations PathFinder.getSafePoint will execute before giving up.
	 */
	@Override
	public int getMaxSafePointTries() {
		return getAttackTarget() == null ? 3 : 3 + (int) (getHealth() - 1.0F);
	}

	@Override
	protected void fall(float distance) {
		super.fall(distance);
		timeSinceIgnited = (int) (timeSinceIgnited + distance * 1.5F);

		if (timeSinceIgnited > fuseTime - 5) {
			timeSinceIgnited = fuseTime - 5;
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) -1));
		dataWatcher.addObject(17, Byte.valueOf((byte) 0));
		dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);

		if (dataWatcher.getWatchableObjectByte(17) == 1) {
			nbt.setBoolean("powered", true);
		}

		nbt.setShort("Fuse", (short) fuseTime);
		nbt.setByte("ExplosionRadius", (byte) explosionRadius);
		nbt.setBoolean("ignited", func_146078_ca());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		dataWatcher.updateObject(17, Byte.valueOf((byte) (nbt.getBoolean("powered") ? 1 : 0)));

		if (nbt.hasKey("Fuse", 99)) {
			fuseTime = nbt.getShort("Fuse");
		}

		if (nbt.hasKey("ExplosionRadius", 99)) {
			explosionRadius = nbt.getByte("ExplosionRadius");
		}

		if (nbt.getBoolean("ignited")) {
			func_146079_cb();
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (isEntityAlive()) {
			lastActiveTime = timeSinceIgnited;

			if (func_146078_ca()) {
				setCreeperState(1);
			}

			int i = getCreeperState();

			if (i > 0 && timeSinceIgnited == 0) {
				playSound("creeper.primed", 1.0F, 0.5F);
			}

			timeSinceIgnited += i;

			if (timeSinceIgnited < 0) {
				timeSinceIgnited = 0;
			}

			if (timeSinceIgnited >= fuseTime) {
				timeSinceIgnited = fuseTime;
				explode();
			}
		}

		super.onUpdate();
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.creeper.say";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.creeper.death";
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);

		if (source.getEntity() instanceof EntitySkeleton) {
			int i = Item.getIdFromItem(Items.record_13);
			int j = Item.getIdFromItem(Items.record_wait);
			int k = i + rand.nextInt(j - i + 1);
			dropItem(Item.getItemById(k), 1);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return true;
	}

	/**
	 * Returns true if the creeper is powered by a lightning bolt.
	 */
	public boolean getPowered() {
		return dataWatcher.getWatchableObjectByte(17) == 1;
	}

	/**
	 * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
	 */
	@SideOnly(Side.CLIENT)
	public float getCreeperFlashIntensity(float tick) {
		return (lastActiveTime + (timeSinceIgnited - lastActiveTime) * tick) / (fuseTime - 2);
	}

	@Override
	protected Item getDropItem() {
		return Items.gunpowder;
	}

	/**
	 * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
	 */
	public int getCreeperState() {
		return dataWatcher.getWatchableObjectByte(16);
	}

	/**
	 * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
	 */
	public void setCreeperState(int state) {
		dataWatcher.updateObject(16, Byte.valueOf((byte) state));
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	@Override
	public void onStruckByLightning(EntityLightningBolt lightning) {
		super.onStruckByLightning(lightning);
		dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	@Override
	protected boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();

		if (itemstack != null && itemstack.getItem() == Items.flint_and_steel) {
			worldObj.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
			player.swingItem();

			if (!worldObj.isRemote) {
				func_146079_cb();
				itemstack.damageItem(1, player);
				return true;
			}
		}

		return super.interact(player);
	}

	private void explode() {
		if (!worldObj.isRemote) {
			boolean flag = worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
			worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
			EntityPlayer player = worldObj.getClosestVulnerablePlayerToEntity(this, explosionRadius);
			// if (getPowered()) {
			// worldObj.createExplosion(this, posX, posY, posZ, explosionRadius * 2, false);
			// } else {
			// worldObj.createExplosion(this, posX, posY, posZ, explosionRadius, false);
			// }

			List entities = worldObj.getEntitiesWithinAABBExcludingEntity(this,
					AxisAlignedBB.getBoundingBox(posX - explosionRadius, posY - explosionRadius, posZ - explosionRadius, posX + explosionRadius, posY + explosionRadius, posZ + explosionRadius));
			for (int i = 0; i < entities.size(); i++) {
				Object entity = entities.get(i);
				if (entity != null && entity instanceof EntityPlayer) {
					if (ConfigHandler.CoFHCompat) {
						InventoryPlayer inv = ((EntityPlayer) entity).inventory;
						dischargeRandomItem(getEnergyItems(inv), inv);
					}
				}
			}
			setDead();
		}
	}

	public boolean func_146078_ca() {
		return dataWatcher.getWatchableObjectByte(18) != 0;
	}

	public void func_146079_cb() {
		dataWatcher.updateObject(18, Byte.valueOf((byte) 1));
	}

	private List<Integer> getEnergyItems(InventoryPlayer inv) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof IEnergyContainerItem) {
				if (((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack) > 0) {
					list.add(i);
				}
			}
		}
		return list;
	}

	/**
	 * @param colours of slot numbers with energy items
	 */
	private void dischargeRandomItem(List<Integer> items, InventoryPlayer inv) {
		if (/* !worldObj.isRemote && */items.size() > 0) {
			ItemStack stack = inv.getStackInSlot(items.get(worldObj.rand.nextInt(items.size())));
			if (stack != null && stack.getItem() instanceof IEnergyContainerItem) {
				IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
				while (item.getEnergyStored(stack) > 0) {
					item.extractEnergy(stack, item.getEnergyStored(stack), false);
				}
			}
		}
	}

}
