package bourgeoisarab.divinealchemy.common.item;

import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.NBTHelper;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBucketPotion extends ItemBucket {

	@SideOnly(Side.CLIENT)
	public IIcon overlay;

	public ItemBucketPotion(Block block) {
		super(block);
		setUnlocalizedName("itemBucketPotion");
		setContainerItem(Items.bucket);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Ref.MODID + ":bucket_base");
		overlay = iconRegister.registerIcon(Ref.MODID + ":bucket_overlay");
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if (pass == 0) {
			return 0xFFFFFF;
		}
		int colour = ColourHelper.combineColours(ColourHelper.getColourFromEffects(NBTHelper.getEffectsFromStack(stack)));
		return colour == -1 ? ColourHelper.combineColours(ColourHelper.potionColour) : colour;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
		return pass == 0 ? itemIcon : overlay;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		List<PotionEffect> effects = NBTHelper.getEffectsFromStack(stack);

		if (effects != null) {
			for (PotionEffect i : effects) {
				Potion potion = Potion.potionTypes[i.getPotionID()];
				String s1 = I18n.format(potion.getName(), new Object[0]);

				list.add(s1 + " " + I18n.format("enchantment.level." + (i.getAmplifier() + 1), new Object[0]));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		if (NBTHelper.getEffectsFromStack(stack) != null) {
			if (NBTHelper.getEffectsFromStack(stack).size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ItemStack stackReturn = super.onItemRightClick(stack, world, player);

		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, player, false);

		if (movingobjectposition == null) {
			return stack;
		}

		int x = movingobjectposition.blockX;
		int y = movingobjectposition.blockY;
		int z = movingobjectposition.blockZ;
		if (movingobjectposition.sideHit == 0) --y;

		if (movingobjectposition.sideHit == 1) ++y;

		if (movingobjectposition.sideHit == 2) --z;

		if (movingobjectposition.sideHit == 3) ++z;

		if (movingobjectposition.sideHit == 4) --x;

		if (movingobjectposition.sideHit == 5) ++x;

		if (world.getBlock(x, y, z) == ModBlocks.blockPotion) {
			((TEPotion) world.getTileEntity(x, y, z)).setEffects(NBTHelper.getEffectsFromStack(stack));
		}

		return stackReturn;
	}

}
