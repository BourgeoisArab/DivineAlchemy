package bourgeoisarab.divinealchemy.init;

import net.minecraft.block.Block;
import bourgeoisarab.divinealchemy.common.block.BlockBrewingCauldron;
import bourgeoisarab.divinealchemy.common.block.BlockHotMess;
import bourgeoisarab.divinealchemy.common.block.BlockPotion;
import bourgeoisarab.divinealchemy.common.block.BlockPotionVat;
import bourgeoisarab.divinealchemy.common.block.ItemBlockBrewingCauldron;
import bourgeoisarab.divinealchemy.common.block.multiblock.BlockInfusedBrick;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionVat;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static Block brewingCauldron;
	public static Block potion;
	public static Block hotMess;
	public static Block infusedBrick;
	public static Block potionVat;

	public static void init() {
		brewingCauldron = new BlockBrewingCauldron();
		potion = new BlockPotion();
		infusedBrick = new BlockInfusedBrick();
		hotMess = new BlockHotMess();
		potionVat = new BlockPotionVat();

	}

	public static void registerBlocks() {
		GameRegistry.registerBlock(brewingCauldron, ItemBlockBrewingCauldron.class, brewingCauldron.getUnlocalizedName());
		GameRegistry.registerBlock(potion, potion.getUnlocalizedName());
		GameRegistry.registerBlock(infusedBrick, infusedBrick.getUnlocalizedName());
		GameRegistry.registerBlock(hotMess, hotMess.getUnlocalizedName());
		GameRegistry.registerBlock(potionVat, potionVat.getUnlocalizedName());
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TEBrewingCauldron.class, "tileEntityBrewingCauldron");
		GameRegistry.registerTileEntity(TEPotion.class, "tileEntityPotion");
		GameRegistry.registerTileEntity(TEPotionVat.class, "tileEntityPotionVat");

	}

}
