package bourgeoisarab.divinealchemy.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.common.item.ItemBottlePotion;
import bourgeoisarab.divinealchemy.common.item.ItemBucketHotMess;
import bourgeoisarab.divinealchemy.common.item.ItemBucketPotion;
import bourgeoisarab.divinealchemy.common.item.ItemButcherKnife;
import bourgeoisarab.divinealchemy.common.item.ItemEssenceCrystal;
import bourgeoisarab.divinealchemy.common.item.ItemInstillationTome;
import bourgeoisarab.divinealchemy.common.item.ItemOrgan;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemInstillationTome instillationTome;
	public static ItemBottlePotion bottlePotion;
	public static ItemBucketPotion bucketPotion;
	public static ItemBucketHotMess bucketHotMess;
	public static ItemEssenceCrystal essenceCrystal;
	public static ItemOrgan organ;
	public static ItemButcherKnife butcherKnife;

	public static void init() {
		instillationTome = new ItemInstillationTome();
		bottlePotion = new ItemBottlePotion();
		bucketPotion = new ItemBucketPotion(ModBlocks.potion);
		bucketHotMess = new ItemBucketHotMess(ModBlocks.hotMess);
		essenceCrystal = new ItemEssenceCrystal();
		organ = new ItemOrgan();
		butcherKnife = new ItemButcherKnife();
	}

	public static void registerItems() {
		GameRegistry.registerItem(instillationTome, instillationTome.getUnlocalizedName());
		GameRegistry.registerItem(bottlePotion, bottlePotion.getUnlocalizedName());
		GameRegistry.registerItem(bucketPotion, bucketPotion.getUnlocalizedName());
		GameRegistry.registerItem(bucketHotMess, bucketHotMess.getUnlocalizedName());
		GameRegistry.registerItem(essenceCrystal, essenceCrystal.getUnlocalizedName());
		GameRegistry.registerItem(organ, organ.getUnlocalizedName());
		GameRegistry.registerItem(butcherKnife, butcherKnife.getUnlocalizedName());

		FluidContainerRegistry.registerFluidContainer(ModFluids.potion, new ItemStack(bucketPotion), new ItemStack(Items.bucket));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(ModFluids.potion, 333), new ItemStack(bottlePotion), new ItemStack(Items.glass_bottle));
		FluidContainerRegistry.registerFluidContainer(ModFluids.hotMess, new ItemStack(bucketHotMess), new ItemStack(Items.bucket));
	}

}
