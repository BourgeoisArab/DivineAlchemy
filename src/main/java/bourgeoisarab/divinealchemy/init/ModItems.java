package bourgeoisarab.divinealchemy.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import bourgeoisarab.divinealchemy.common.item.ItemAmulet;
import bourgeoisarab.divinealchemy.common.item.ItemBottlePotion;
import bourgeoisarab.divinealchemy.common.item.ItemBucketHotMess;
import bourgeoisarab.divinealchemy.common.item.ItemBucketPotion;
import bourgeoisarab.divinealchemy.common.item.ItemButcherKnife;
import bourgeoisarab.divinealchemy.common.item.ItemEnderBottle;
import bourgeoisarab.divinealchemy.common.item.ItemEnergyStorage;
import bourgeoisarab.divinealchemy.common.item.ItemEssenceCrystal;
import bourgeoisarab.divinealchemy.common.item.ItemInstillationTome;
import bourgeoisarab.divinealchemy.common.item.ItemPedestalCrystal;

public class ModItems {

	public static ItemInstillationTome instillationTome;
	public static ItemBottlePotion bottlePotion;
	public static ItemEnderBottle bottleEnder;
	public static ItemBucketPotion bucketPotion;
	public static ItemBucketHotMess bucketHotMess;
	public static ItemEssenceCrystal essenceCrystal;
	public static ItemAmulet amulet;
	// public static ItemOrgan organ;
	public static ItemButcherKnife butcherKnife;
	public static ItemEnergyStorage crystalBasic;
	public static ItemEnergyStorage crystalMedium;
	public static ItemEnergyStorage crystalBig;
	public static ItemPedestalCrystal pedestalCrystal;

	public static void init() {
		instillationTome = new ItemInstillationTome();
		bottlePotion = new ItemBottlePotion();
		bottleEnder = new ItemEnderBottle();
		bucketPotion = new ItemBucketPotion();
		bucketHotMess = new ItemBucketHotMess(ModBlocks.hotMess);
		essenceCrystal = new ItemEssenceCrystal();
		amulet = new ItemAmulet();
		// organ = new ItemOrgan();
		butcherKnife = new ItemButcherKnife();
		crystalBasic = (ItemEnergyStorage) new ItemEnergyStorage(1000).setUnlocalizedName("crystalBasic");
		crystalMedium = (ItemEnergyStorage) new ItemEnergyStorage(5000).setUnlocalizedName("crystalMedium");
		crystalBig = (ItemEnergyStorage) new ItemEnergyStorage(10000).setUnlocalizedName("crystalBig");
		pedestalCrystal = new ItemPedestalCrystal();
	}

	public static void registerItems() {
		GameRegistry.registerItem(instillationTome, instillationTome.getUnlocalizedName());
		GameRegistry.registerItem(bottlePotion, bottlePotion.getUnlocalizedName());
		GameRegistry.registerItem(bottleEnder, bottleEnder.getUnlocalizedName());
		GameRegistry.registerItem(bucketPotion, bucketPotion.getUnlocalizedName());
		GameRegistry.registerItem(bucketHotMess, bucketHotMess.getUnlocalizedName());
		GameRegistry.registerItem(essenceCrystal, essenceCrystal.getUnlocalizedName());
		// GameRegistry.registerItem(organ, organ.getUnlocalizedName());
		GameRegistry.registerItem(butcherKnife, butcherKnife.getUnlocalizedName());
		GameRegistry.registerItem(crystalBasic, crystalBasic.getUnlocalizedName());
		GameRegistry.registerItem(crystalMedium, crystalMedium.getUnlocalizedName());
		GameRegistry.registerItem(crystalBig, crystalBig.getUnlocalizedName());
		GameRegistry.registerItem(amulet, amulet.getUnlocalizedName());
		GameRegistry.registerItem(pedestalCrystal, pedestalCrystal.getUnlocalizedName());

		FluidContainerRegistry.registerFluidContainer(ModFluids.hotMess, new ItemStack(bucketHotMess), new ItemStack(Items.bucket));
	}

}
