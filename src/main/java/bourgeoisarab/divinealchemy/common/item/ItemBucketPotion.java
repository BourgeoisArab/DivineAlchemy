package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotion;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.reference.Ref;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;
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
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Ref.MODID + ":bucket_base");
		overlay = iconRegister.registerIcon(Ref.MODID + ":bucket_overlay");
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if (pass == 0) {
			return 0xFFFFFF;
		}
		Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
		if (effects == null) {
			return ColourHelper.potionColourInt;
		}
		Colouring colouring = NBTEffectHelper.getColouringFromStack(stack);
		int colour = ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), colouring));
		return colour == -1 ? ColourHelper.potionColourInt : colour;
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
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		Effects effects = NBTEffectHelper.getEffectsFromStack(stack);
		if (effects != null) {
			for (int i = 0; i < effects.size(); i++) {
				Potion potion = ModPotion.getPotion(effects.getEffect(i).getPotionID());
				String s1 = I18n.format(potion.getName());
				list.add((effects.getSideEffect(i) ? EnumChatFormatting.DARK_RED : "") + s1 + " " + I18n.format("enchantment.level." + (effects.getEffect(i).getAmplifier() + 1)));
			}
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		if (NBTEffectHelper.getEffectsFromStack(stack) != null) {
			if (NBTEffectHelper.getEffectsFromStack(stack).size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ItemStack stackReturn = super.onItemRightClick(stack, world, player);

		MovingObjectPosition pos = getMovingObjectPositionFromPlayer(world, player, false);
		if (pos == null) {
			return stack;
		}

		ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);

		int x = pos.blockX + dir.offsetX;
		int y = pos.blockY + dir.offsetY;
		int z = pos.blockZ + dir.offsetZ;

		if (world.getBlock(x, y, z) == ModBlocks.blockPotion) {
			((TEPotion) world.getTileEntity(x, y, z)).setEffects(NBTEffectHelper.getEffectsFromStack(stack));
			((TEPotion) world.getTileEntity(x, y, z)).setColouring(NBTEffectHelper.getColouringFromStack(stack));
		}
		world.markBlockForUpdate(x, y, z);
		return stackReturn;
	}

}
