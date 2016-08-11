package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import bourgeoisarab.divinealchemy.common.potion.IDivinePotion;
import bourgeoisarab.divinealchemy.common.potion.IEvilPotion;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;

public class BlockHotMess extends BlockFluidClassic {

	public BlockHotMess() {
		super(ModFluids.hotMess, Material.water);
		setUnlocalizedName("hotmess");
		ModFluids.hotMess.setBlock(this);
		setQuantaPerBlock(4);
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!world.isRemote) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entityLiving = (EntityLivingBase) entity;
				if ((entityLiving.getActivePotionEffects().size() <= 0 || world.getWorldTime() % 20 == 0) && entityLiving.getActivePotionEffects().size() < 5) {
					Potion potion = ModPotionHelper.getRandomPotion(world.rand);
					while (potion instanceof IEvilPotion || potion instanceof IDivinePotion) {
						potion = ModPotionHelper.getRandomPotion(world.rand);
					}
					entityLiving.addPotionEffect(new PotionEffect(potion.id, 60, 0));
				}
			}
		}
	}

}
