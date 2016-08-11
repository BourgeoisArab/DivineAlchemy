package bourgeoisarab.divinealchemy.common.event;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModItems;

public class BucketHandler {

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		ItemStack result;
		result = fillBucket(event.world, event.target.getBlockPos(), event.target.sideHit);
		if (result == null) {
			return;
		}
		event.result = result;
		event.setResult(Result.ALLOW);
	}

	private ItemStack fillBucket(World world, BlockPos pos, EnumFacing side) {
		Block block = world.getBlockState(pos).getBlock();
		// if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof IFluidHandler) {
		// IFluidHandler tile = (IFluidHandler) world.getTileEntity(x, y, z);
		// ForgeDirection dir = ForgeDirection.getOrientation(side);
		// FluidTankInfo info = getTankWithPotion(tile.getTankInfo(dir));
		// if (info != null) {
		// if (info.fluid.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
		// FluidStack drained = tile.drain(dir, new FluidStack(ModFluids.fluidPotion, FluidContainerRegistry.BUCKET_VOLUME), true);
		// return NBTEffectHelper.setEffectsForStack(new ItemStack(ModItems.itemBucketPotion), NBTEffectHelper.getEffectsFromFluid(drained));
		// }
		// }
		// }
		if (block == ModBlocks.potion && ModBlocks.potion.isSourceBlock(world, pos)) {
			TEPotion tile = (TEPotion) world.getTileEntity(pos);
			world.setBlockToAir(pos);
			ItemStack returnStack = new ItemStack(ModItems.bucketPotion);
			ModItems.bucketPotion.fill(returnStack, tile.getFluid(), true);
			return returnStack;
		} else if (block == ModBlocks.hotMess && ModBlocks.hotMess.isSourceBlock(world, pos)) {
			world.setBlockToAir(pos);
			return new ItemStack(ModItems.bucketHotMess);
		}
		return null;
	}

	// private FluidTankInfo getTankWithPotion(FluidTankInfo[] info) {
	// for (FluidTankInfo i : info) {
	// if (i != null && i.fluid != null && i.fluid.getFluid() == ModFluids.potion) {
	// return i;
	// }
	// }
	// return null;
	// }
}
