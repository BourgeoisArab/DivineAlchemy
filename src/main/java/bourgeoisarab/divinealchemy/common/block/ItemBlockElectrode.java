package bourgeoisarab.divinealchemy.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockElectrode extends ItemBlock {

	public ItemBlockElectrode(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}

}
