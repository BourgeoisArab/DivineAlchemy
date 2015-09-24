package bourgeoisarab.divinealchemy.common.item;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;

public class ItemBucketHotMess extends ItemBucket {

	public ItemBucketHotMess(Block block) {
		super(block);
		setUnlocalizedName("bucketHotMess");
		setContainerItem(Items.bucket);
		setTextureName(Ref.Location.PREFIX + "bucket_hotmess");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

}
