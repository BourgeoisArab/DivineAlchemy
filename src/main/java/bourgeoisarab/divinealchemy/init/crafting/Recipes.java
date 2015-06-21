package bourgeoisarab.divinealchemy.init.crafting;

import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

	public static void init() {
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockBrewingCauldron, 1, 0), new Object[]{"IBI", "ICI", "III", 'I', Items.iron_ingot, 'B', Items.brewing_stand, 'C', Items.cauldron});
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockBrewingCauldron, 1, 1), new Object[]{"I I", "ICI", "III", 'I', Items.gold_ingot, 'C', new ItemStack(ModBlocks.blockBrewingCauldron, 1, 0)});
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockBrewingCauldron, 1, 2), new Object[]{"I I", "ICI", "III", 'I', Items.diamond, 'C', new ItemStack(ModBlocks.blockBrewingCauldron, 1, 1)});
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockBrewingCauldron, 1, 3), new Object[]{"I I", "ICI", "III", 'I', Items.emerald, 'C', new ItemStack(ModBlocks.blockBrewingCauldron, 1, 2)});
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockBrewingCauldron, 1, 4), new Object[]{"I I", "ICI", "III", 'I', Items.nether_star, 'C', new ItemStack(ModBlocks.blockBrewingCauldron, 1, 3)});
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockInfusedBrick, 4), new Object[]{"SCS", "CPC", "SCS", 'S', Blocks.sand, 'C', Items.clay_ball, 'P', ModItems.itemPotionBottle});
		GameRegistry.addRecipe(new ItemStack(ModBlocks.blockInfusedBrick, 4), new Object[]{"CSC", "SPS", "CSC", 'S', Blocks.sand, 'C', Items.clay_ball, 'P', ModItems.itemPotionBottle});
		// for (int i = 0; i < 5; i++) {
		// GameRegistry.addRecipe(new ItemStack(ModBlocks.blockPotionVat, 1, i),
		// new Object[]{"BDB", "ICI", "BIB", 'B', ModBlocks.blockInfusedBrick,
		// 'D', Items.diamond, 'I', Items.iron_ingot, 'C',
		// new ItemStack(ModBlocks.blockBrewingCauldron, 1, i)});
		// }
		GameRegistry.addRecipe(new ItemStack(ModItems.itemInstillationTome), new Object[]{"PWP", "WBW", "PWP", 'P', Items.blaze_powder, 'W', Items.nether_wart, 'B', Items.book});
		GameRegistry.addRecipe(new ItemStack(ModItems.itemInstillationTome), new Object[]{"WPW", "PBP", "WPW", 'P', Items.blaze_powder, 'W', Items.nether_wart, 'B', Items.book});

		GameRegistry.addRecipe(new PotionFoodCrafting());
		RecipeSorter.register("Food Crafting", PotionFoodCrafting.class, Category.SHAPELESS, "");
	}

}
