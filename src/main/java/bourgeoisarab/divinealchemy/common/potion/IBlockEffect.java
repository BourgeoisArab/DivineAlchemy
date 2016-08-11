package bourgeoisarab.divinealchemy.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockEffect {

	public void applyBlockEffect(World world, BlockPos pos, EntityLivingBase entity, PotionEffect effect);

}
