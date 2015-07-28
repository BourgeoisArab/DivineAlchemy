package bourgeoisarab.divinealchemy.init;

import net.minecraft.block.Block;
import bourgeoisarab.divinealchemy.common.block.BlockBrewingCauldron;
import bourgeoisarab.divinealchemy.common.block.BlockHotMess;
import bourgeoisarab.divinealchemy.common.block.BlockInfusedBrick;
import bourgeoisarab.divinealchemy.common.block.BlockPotion;
import bourgeoisarab.divinealchemy.common.block.ItemBlockBrewingCauldron;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static Block blockBrewingCauldron;
	public static Block blockPotion;
	public static Block blockHotMess;

	public static Block blockInfusedBrick;
	// public static BlockPotionVat blockPotionVat;

	public static Block blockGenerator;
	public static Block blockElectrode;

	public static void init() {
		blockBrewingCauldron = new BlockBrewingCauldron();
		blockPotion = new BlockPotion();
		blockInfusedBrick = new BlockInfusedBrick();
		blockHotMess = new BlockHotMess();
		// blockPotionVat = new BlockPotionVat();

	}

	public static void registerBlocks() {
		GameRegistry.registerBlock(blockBrewingCauldron, ItemBlockBrewingCauldron.class, blockBrewingCauldron.getUnlocalizedName());
		GameRegistry.registerBlock(blockPotion, blockPotion.getUnlocalizedName());
		GameRegistry.registerBlock(blockInfusedBrick, blockInfusedBrick.getUnlocalizedName());
		GameRegistry.registerBlock(blockHotMess, blockHotMess.getUnlocalizedName());
		// GameRegistry.registerBlock(blockPotionVat, blockPotionVat.getUnlocalizedName());

		if (ConfigHandler.CoFHCompat) {
			GameRegistry.registerBlock(blockGenerator, blockGenerator.getUnlocalizedName());
			GameRegistry.registerBlock(blockElectrode, blockElectrode.getUnlocalizedName());
		}
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TEBrewingCauldron.class, "tileEntityBrewingCauldron");
		GameRegistry.registerTileEntity(TEPotion.class, "tileEntityPotion");
		// GameRegistry.registerTileEntity(TEPotionVat.class, "tileEntityPotionVat");

	}

}
