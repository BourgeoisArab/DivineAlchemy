package bourgeoisarab.divinealchemy.common.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import org.apache.commons.lang3.ArrayUtils;

import bourgeoisarab.divinealchemy.common.tileentity.TEObeliskDark;
import bourgeoisarab.divinealchemy.init.ModBlocks;

public class WorldGenObeliskDark implements IWorldGenerator {

	public static int[] BIOME_IDS;
	public static Block[] BLOCKS = new Block[]{Blocks.grass, Blocks.dirt};
	public static Block[] FORBIDDEN_BLOCKS = new Block[]{ModBlocks.deadDirt, ModBlocks.deadWood, Blocks.water, Blocks.sand};
	public boolean justGenerated = false;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int biomeID = world.getBiomeGenForCoords(new BlockPos(chunkX * 16, 0, chunkZ * 16)).biomeID;
		if (!justGenerated && ArrayUtils.contains(BIOME_IDS, biomeID) && biomeID == world.getBiomeGenForCoords(new BlockPos(chunkX * 16 + 15, 0, chunkZ * 16 + 15)).biomeID) {
			List<BlockPos> blocks = getTopBlocks(world, chunkX, chunkZ);
			if (blocks.size() > 0) {
				BlockPos pos = blocks.get(random.nextInt(blocks.size()));
				world.setBlockState(pos, ModBlocks.obeliskDark.getStateFromMeta(0));
				((TEObeliskDark) world.getTileEntity(pos.offset(EnumFacing.UP, 3))).drainAllLife();
				justGenerated = true;
				return;
			}
		}
		justGenerated = false;
	}

	private List<BlockPos> getTopBlocks(World world, int chunkX, int chunkZ) {
		List<BlockPos> list = new ArrayList<BlockPos>();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				BlockPos pos = world.getHeight(new BlockPos(chunkX * 16 + x, 0, chunkZ * 16 + z));
				Block block = world.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock();
				if (ArrayUtils.contains(FORBIDDEN_BLOCKS, block)) {
					list.clear();
					return list;
				}
				if (ArrayUtils.contains(BLOCKS, block)) {
					list.add(pos);
				}
			}
		}
		return list;
	}
}
