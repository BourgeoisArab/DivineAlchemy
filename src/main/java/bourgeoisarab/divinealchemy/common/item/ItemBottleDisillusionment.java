package bourgeoisarab.divinealchemy.common.item;

import net.minecraftforge.fluids.ItemFluidContainer;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class ItemBottleDisillusionment extends ItemFluidContainer {

	public ItemBottleDisillusionment() {
		super(0, 333);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setMaxStackSize(1);
		setTextureName("minecraft:glass_bottle");
	}

}
