package bourgeoisarab.divinealchemy.common.item;

import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BucketHandler {

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		ItemStack result = fillBucket(event.world, event.target.blockX, event.target.blockY, event.target.blockZ);
		if (result == null) {
			return;
		}
		event.result = result;
		event.setResult(Result.ALLOW);
	}

	private ItemStack fillBucket(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if (block != null && block == ModBlocks.blockPotion && world.getBlockMetadata(x, y, z) == 0) {
			ItemStack stack = new ItemStack(ModItems.itemBucketPotion);
			NBTHelper.setEffectsForStack(stack, ((TEPotion) world.getTileEntity(x, y, z)).getEffects());
			world.setBlockToAir(x, y, z);
			return stack;
		} else if (block != null && block == ModBlocks.blockHotMess && world.getBlockMetadata(x, y, z) == 0) {
			ItemStack stack = new ItemStack(ModItems.itemBucketHotMess);
			world.setBlockToAir(x, y, z);
			return stack;
		} else
			return null;
	}
}
