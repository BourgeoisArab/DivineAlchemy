package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionTank;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.utility.InventoryHelper;

public class BlockPotionTank extends Block {

	public BlockPotionTank() {
		super(Material.rock);
		setBlockName("potionTank");
		setBlockTextureName("minecraft:glass");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setHardness(1.0F);
		setResistance(2.0F);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offsetX, float offsetY, float offsetZ) {
		ItemStack stack = player.getCurrentEquippedItem();
		TEPotionTank tile = (TEPotionTank) world.getTileEntity(x, y, z);
		ItemStack newStack = InventoryHelper.processContainer(stack, tile, ForgeDirection.getOrientation(side));

		return false;
	}
}
