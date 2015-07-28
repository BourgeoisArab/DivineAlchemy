package bourgeoisarab.divinealchemy.common.entity;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ISplashEffect;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class EntitySplashPotion extends EntityThrowable {

	private Effects effects = new Effects();

	public EntitySplashPotion(World world) {
		super(world);
	}

	public EntitySplashPotion(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	public EntitySplashPotion(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntitySplashPotion(World world, EntityLivingBase thrower, Effects effects) {
		this(world, thrower);
		this.effects = effects;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if (!worldObj.isRemote) {
			if (effects != null && effects.size() > 0) {
				AxisAlignedBB bb = boundingBox.expand(4.0D, 2.0D, 4.0D);
				List entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
				if (entities != null && entities.size() > 0) {
					for (int i = 0; i < entities.size(); i++) {
						EntityLivingBase entity = (EntityLivingBase) entities.get(i);
						double distance = getDistanceSqToEntity(entity);
						if (distance < 16.0D) {
							double affect = 1.0D - Math.sqrt(distance) / 4.0D;
							if (entity == pos.entityHit) {
								affect = 1.0D;
							}
							for (PotionEffect j : effects.getEffects()) {
								Potion potion = ModPotion.getPotion(j.getPotionID());
								if (potion.isInstant()) {
									potion.affectEntity(getThrower(), entity, j.getAmplifier(), affect);
								} else {
									PotionEffect effect = new PotionEffect(j.getPotionID(), (int) (affect * j.getDuration() + 0.5D), j.getAmplifier());
									entity.addPotionEffect(effect);
								}
								if (potion instanceof ISplashEffect) {
									((ISplashEffect) potion).applySplashEffect(worldObj, posX, posY, posZ, this, j);
								}
							}
						}
					}
				}
			}
			// TODO: particles
			// worldObj.playAuxSFX(2002, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), this.getPotionDamage());
			worldObj.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "game.potion.smash", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F, false);
			setDead();
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		effects = NBTEffectHelper.getEffectsFromNBT(nbt);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		NBTEffectHelper.setEffectsForNBT(nbt, effects);
	}

	public Effects getEffects() {
		return effects;
	}

	public EntitySplashPotion setEffects(Effects effects) {
		this.effects = effects;
		return this;
	}

}
