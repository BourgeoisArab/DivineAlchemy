package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEObeliskDark;
import bourgeoisarab.divinealchemy.common.tileentity.TEPowerProvider;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class BlockObeliskDark extends BlockPowerProvider {

	public static PropertyBool PROPERTY_TYPE = PropertyBool.create("receptacle");

	public BlockObeliskDark() {
		super(Material.rock);
		setUnlocalizedName("obeliskDark");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[]{PROPERTY_TYPE});
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PROPERTY_TYPE, meta % 2 == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTY_TYPE) ? 1 : 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TEPowerProvider createNewTileEntity(World world, int meta) {
		if (meta % 2 == 1) {
			return new TEObeliskDark();
		}
		return null;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.setBlockState(pos.offset(EnumFacing.UP), state);
		world.setBlockState(pos.offset(EnumFacing.UP, 2), state.withProperty(PROPERTY_TYPE, true));
	}

}
