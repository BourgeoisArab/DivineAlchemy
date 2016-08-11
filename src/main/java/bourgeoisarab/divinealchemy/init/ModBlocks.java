package bourgeoisarab.divinealchemy.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import bourgeoisarab.divinealchemy.common.block.BlockBrewingCauldron;
import bourgeoisarab.divinealchemy.common.block.BlockDeadDirt;
import bourgeoisarab.divinealchemy.common.block.BlockDeadWood;
import bourgeoisarab.divinealchemy.common.block.BlockHotMess;
import bourgeoisarab.divinealchemy.common.block.BlockObelisk;
import bourgeoisarab.divinealchemy.common.block.BlockObeliskDark;
import bourgeoisarab.divinealchemy.common.block.BlockPotion;
import bourgeoisarab.divinealchemy.common.block.BlockPotionTank;
import bourgeoisarab.divinealchemy.common.item.ItemBlockBrewingCauldron;
import bourgeoisarab.divinealchemy.common.item.ItemBlockObelisk;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.common.tileentity.TEObelisk;
import bourgeoisarab.divinealchemy.common.tileentity.TEObeliskDark;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionTank;

public class ModBlocks {

	public static BlockBrewingCauldron brewingCauldron;
	public static BlockPotion potion;
	public static BlockHotMess hotMess;
	public static BlockPotionTank potionTank;
	public static BlockObelisk obelisk;
	public static BlockObeliskDark obeliskDark;
	public static BlockDeadDirt deadDirt;
	public static BlockDeadWood deadWood;

	public static void init() {
		brewingCauldron = new BlockBrewingCauldron();
		potion = new BlockPotion();
		hotMess = new BlockHotMess();
		potionTank = new BlockPotionTank();
		obelisk = new BlockObelisk();
		obeliskDark = new BlockObeliskDark();
		deadDirt = new BlockDeadDirt();
		deadWood = new BlockDeadWood();
	}

	public static void registerBlocks() {
		GameRegistry.registerBlock(brewingCauldron, ItemBlockBrewingCauldron.class, brewingCauldron.getUnlocalizedName());
		GameRegistry.registerBlock(potion, potion.getUnlocalizedName());
		GameRegistry.registerBlock(hotMess, hotMess.getUnlocalizedName());
		GameRegistry.registerBlock(potionTank, potionTank.getUnlocalizedName());
		GameRegistry.registerBlock(obelisk, ItemBlockObelisk.class, obelisk.getUnlocalizedName());
		GameRegistry.registerBlock(obeliskDark, obeliskDark.getUnlocalizedName());
		GameRegistry.registerBlock(deadDirt, deadDirt.getUnlocalizedName());
		GameRegistry.registerBlock(deadWood, deadWood.getUnlocalizedName());
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TEBrewingCauldron.class, "TEBrewingCauldron");
		GameRegistry.registerTileEntity(TEPotion.class, "TEPotion");
		GameRegistry.registerTileEntity(TEPotionTank.class, "TEPotionTank");
		GameRegistry.registerTileEntity(TEObelisk.class, "TEObelisk");
		GameRegistry.registerTileEntity(TEObeliskDark.class, "TEObeliskDark");
	}

}
