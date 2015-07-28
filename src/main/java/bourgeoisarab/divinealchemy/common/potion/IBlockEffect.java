package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public interface IBlockEffect {

	public void applyBlockEffect(World world, int x, int y, int z, EntityLivingBase entity, PotionEffect effect);

}
