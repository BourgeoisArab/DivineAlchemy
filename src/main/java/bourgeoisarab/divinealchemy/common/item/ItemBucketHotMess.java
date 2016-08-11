package bourgeoisarab.divinealchemy.common.item;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;

public class ItemBucketHotMess extends ItemBucket {

	public ItemBucketHotMess(Block block) {
		super(block);
		setUnlocalizedName("bucketHotMess");
		setContainerItem(Items.bucket);
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

}
