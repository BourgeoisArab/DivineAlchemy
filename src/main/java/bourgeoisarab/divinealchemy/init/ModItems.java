package bourgeoisarab.divinealchemy.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.common.item.ItemBucketHotMess;
import bourgeoisarab.divinealchemy.common.item.ItemBucketPotion;
import bourgeoisarab.divinealchemy.common.item.ItemEssenceCrystal;
import bourgeoisarab.divinealchemy.common.item.ItemInstillationTome;
import bourgeoisarab.divinealchemy.common.item.ItemPotionBottle;
import bourgeoisarab.divinealchemy.common.item.ItemPotionFood;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemInstillationTome itemInstillationTome;
	public static ItemPotionBottle itemPotionBottle;
	public static ItemBucketPotion itemBucketPotion;
	public static ItemBucketHotMess itemBucketHotMess;
	public static ItemPotionFood itemPotionFood;
	public static ItemEssenceCrystal itemEssenceCrystal;

	public static void init() {
		itemInstillationTome = new ItemInstillationTome();
		itemPotionBottle = new ItemPotionBottle();
		itemBucketPotion = new ItemBucketPotion(ModBlocks.blockPotion);
		itemBucketHotMess = new ItemBucketHotMess(ModBlocks.blockHotMess);
		itemPotionFood = new ItemPotionFood();
		itemEssenceCrystal = new ItemEssenceCrystal();
		ItemEssenceCrystal.init();
	}

	public static void registerItems() {
		GameRegistry.registerItem(itemInstillationTome, itemInstillationTome.getUnlocalizedName());
		GameRegistry.registerItem(itemPotionBottle, itemPotionBottle.getUnlocalizedName());
		GameRegistry.registerItem(itemBucketPotion, itemBucketPotion.getUnlocalizedName());
		GameRegistry.registerItem(itemBucketHotMess, itemBucketHotMess.getUnlocalizedName());
		GameRegistry.registerItem(itemPotionFood, itemPotionFood.getUnlocalizedName());
		GameRegistry.registerItem(itemEssenceCrystal, itemEssenceCrystal.getUnlocalizedName());
		FluidContainerRegistry.registerFluidContainer(new FluidStack(ModFluids.fluidPotion, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketPotion), new ItemStack(Items.bucket));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(ModFluids.fluidPotion, 333), new ItemStack(itemPotionBottle), new ItemStack(Items.glass_bottle));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(ModFluids.fluidHotMess, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketHotMess), new ItemStack(Items.bucket));
	}

}
