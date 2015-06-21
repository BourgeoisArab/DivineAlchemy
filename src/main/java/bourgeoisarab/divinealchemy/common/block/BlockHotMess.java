package bourgeoisarab.divinealchemy.common.block;

import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHotMess extends BlockFluidClassic {

	@SideOnly(Side.CLIENT)
	protected IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon flowingIcon;

	public BlockHotMess() {
		super(ModFluids.fluidHotMess, Material.water);
		setBlockName("blockHotMess");
		ModFluids.fluidHotMess.setBlock(this);
		setQuantaPerBlock(4);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		stillIcon = register.registerIcon(Ref.MODID + ":hotmess_still");
		flowingIcon = register.registerIcon(Ref.MODID + ":hotmess_flow");
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (!world.isRemote) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entityLiving = (EntityLivingBase) entity;
				if ((entityLiving.getActivePotionEffects().size() <= 0 || world.getWorldTime() % 40 == 0) && entityLiving.getActivePotionEffects().size() < 5) {
					Potion potion = ModPotionHelper.getRandomPotion(world.rand);
					entityLiving.addPotionEffect(new PotionEffect(potion.id, 60, 0));
				}
			}
		}
	}

}
