package bourgeoisarab.divinealchemy.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import bourgeoisarab.divinealchemy.common.potion.ISplashEffect;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.reference.NBTNames;

public class EntityAmuletSpell extends EntityThrowable {

	protected int potion;
	protected int amplifier;

	public EntityAmuletSpell(World world) {
		super(world);
	}

	public EntityAmuletSpell(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	public EntityAmuletSpell(World world, EntityLivingBase thrower, int potion, int amplifier) {
		super(world, thrower);
		this.potion = potion;
		this.amplifier = amplifier;
	}

	public EntityAmuletSpell(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (inGround) {
			setDead();
		} else if (isAirBorne) {
			worldObj.spawnParticle(EnumParticleTypes.SPELL_INSTANT, posX, posY, posZ, 0, 0, 0, ModPotion.getPotion(potion).getLiquidColor());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		byte[] data = nbt.getByteArray(NBTNames.EFFECT);
		potion = data[0];
		if (potion < 0) {
			potion += 256;
		}
		amplifier = data[1];
		if (amplifier < 0) {
			potion += 256;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setByteArray(NBTNames.EFFECT, new byte[]{(byte) potion, (byte) amplifier});
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if (!worldObj.isRemote) {
			Potion p = ModPotion.getPotion(potion);
			if (p instanceof ISplashEffect) {
				((ISplashEffect) p).applySplashEffect(worldObj, posX, posY, posZ, 0, amplifier);
			}
			if (pos.entityHit != null && pos.entityHit instanceof EntityLivingBase) {
				p.affectEntity(this, getThrower(), (EntityLivingBase) pos.entityHit, amplifier, 1.0D);
			}
		}
	}

	public int getParticleColor() {
		return ModPotion.getPotion(potion).getLiquidColor();
	}

}
