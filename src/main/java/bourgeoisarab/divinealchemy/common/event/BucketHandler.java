package bourgeoisarab.divinealchemy.common.event;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidTankInfo;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BucketHandler {

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		ItemStack result;
		result = fillBucket(event.world, event.target.blockX, event.target.blockY, event.target.blockZ, event.target.sideHit);
		if (result == null) {
			return;
		}
		event.result = result;
		event.setResult(Result.ALLOW);
	}

	private ItemStack fillBucket(World world, int x, int y, int z, int side) {
		Block block = world.getBlock(x, y, z);
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
		if (block == ModBlocks.potion && world.getBlockMetadata(x, y, z) == 0) {
			TEPotion tile = (TEPotion) world.getTileEntity(x, y, z);
			Effects effects = tile.getEffects();
			Colouring colouring = tile.getColouring();
			world.setBlockToAir(x, y, z);
			return NBTEffectHelper.setColouringForStack(NBTEffectHelper.setEffectsForStack(new ItemStack(ModItems.bucketPotion, 1, tile.getProperties().getMetaValue()), effects), colouring);
		} else if (block == ModBlocks.hotMess && world.getBlockMetadata(x, y, z) == 0) {
			world.setBlockToAir(x, y, z);
			return new ItemStack(ModItems.bucketHotMess);
		}
		return null;
	}

	private FluidTankInfo getTankWithPotion(FluidTankInfo[] info) {
		for (FluidTankInfo i : info) {
			if (i != null && i.fluid != null && i.fluid.getFluid() == ModFluids.potion) {
				return i;
			}
		}
		return null;
	}
}
