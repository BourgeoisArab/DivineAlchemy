package bourgeoisarab.divinealchemy.common.item;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemBucketHotMess extends ItemBucket {

	public ItemBucketHotMess(Block block) {
		super(block);
		setUnlocalizedName("itemBucketHotMess");
		setContainerItem(Items.bucket);
		setTextureName(Ref.Location.PREFIX + "bucket_hotmess");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
	}

}
