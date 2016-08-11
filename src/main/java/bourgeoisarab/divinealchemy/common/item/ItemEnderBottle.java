package bourgeoisarab.divinealchemy.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.PotionProperties;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionTank;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModFluids;
import bourgeoisarab.divinealchemy.reference.NBTNames;
import bourgeoisarab.divinealchemy.utility.ColourHelper;
import bourgeoisarab.divinealchemy.utility.ModPotionHelper;
import bourgeoisarab.divinealchemy.utility.nbt.NBTEffectHelper;

public class ItemEnderBottle extends Item {

	public ItemEnderBottle() {
		setUnlocalizedName("enderBottle");
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabDivineAlchemy);
		}
		setMaxStackSize(1);
	}

	public void setCoords(ItemStack stack, BlockPos coords) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (coords != null) {
			stack.getTagCompound().setLong(NBTNames.COORDINATES, coords.toLong());
		} else {
			stack.getTagCompound().removeTag(NBTNames.COORDINATES);
		}
	}

	public BlockPos getCoords(ItemStack stack) {
		if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey(NBTNames.COORDINATES)) {
			return null;
		}
		return BlockPos.fromLong(stack.getTagCompound().getLong(NBTNames.COORDINATES));
	}

	public TEPotionTank getTank(World world, BlockPos pos) {
		if (pos != null) {
			TileEntity te = world.getTileEntity(pos);
			return (TEPotionTank) (te instanceof TEPotionTank ? te : null);
		}
		return null;
	}

	public FluidStack getFluid(TEPotionTank tank) {
		return tank != null ? tank.tank.getFluid() : null;
	}

	public void drainTank(TEPotionTank tank) {
		FluidStack fluid = tank.tank.getFluid();
		if (fluid != null) {
			fluid.amount -= 333;
		}
		if (fluid.amount % FluidContainerRegistry.BUCKET_VOLUME == 1) {
			fluid.amount -= 1;
		}
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		FluidStack fluid = getFluid(getTank(DivineAlchemy.proxy.getClientWorld(), getCoords(stack)));
		if (pass == 0 && fluid != null && fluid.amount > 0 && fluid.getFluid() == ModFluids.potion) {
			Effects effects = NBTEffectHelper.getEffects(fluid);
			if (effects == null) {
				return ColourHelper.potionColourInt;
			}
			Colouring colours = NBTEffectHelper.getColouring(getFluid(getTank(DivineAlchemy.proxy.getClientWorld(), getCoords(stack))));
			int colour = ColourHelper.combineColours(ColourHelper.getColourFromEffects(effects.getEffects(), colours));
			return colour == -1 ? ColourHelper.potionColourInt : colour;
		}
		return 0x88FF88;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
		TEPotionTank tank = getTank(player.worldObj, getCoords(stack));
		if (tank == null) {
			list.add(I18n.format("item.enderBottle.unbound"));
			return;
		}
		FluidStack fluid = getFluid(tank);
		if (fluid != null && fluid.amount > 0) {
			PotionProperties properties = NBTEffectHelper.getProperties(fluid);
			if (properties != null) {
				if (properties.isSplash) {
					list.add(I18n.format("item.potion.splash"));
				}
				if (properties.isBlessed) {
					list.add(I18n.format("item.potion.blessed"));
				}
				if (properties.isCursed) {
					list.add(I18n.format("item.potion.cursed"));
				}
			}
			Effects effects = NBTEffectHelper.getEffects(fluid);
			if (effects != null) {
				for (int i = 0; i < effects.size(); i++) {
					Potion potion = ModPotion.getPotion(effects.getEffect(i).getPotionID());
					String s1 = I18n.format(potion.getName());

					s1 = s1 + " " + I18n.format("enchantment.level." + (effects.getEffect(i).getAmplifier() + 1));

					list.add((effects.getSideEffect(i) ? EnumChatFormatting.DARK_RED : "") + s1 + (potion.isInstant() ? "" : " (" + Potion.getDurationString(effects.getEffect(i)) + ")"));
				}
			}
		}

		// TODO: Hold shift
		// if (DivineAlchemy.proxy.getClient().gameSettings.keyBindSneak.isPressed()) {
		if (stack.getTagCompound() != null || stack.getTagCompound().hasKey(NBTNames.COORDINATES)) {
			BlockPos coords = getCoords(stack);
			if (coords != null) {
				list.add(I18n.format("item.enderBottle.bound", coords.getX() + ", " + coords.getY() + ", " + coords.getZ()));
			}
			list.add(tank.tank.getFluidAmount() + "mB / " + tank.tank.getCapacity() + "mB");
		}
		// }
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player) {
		TEPotionTank tank = getTank(world, getCoords(stack));
		if (tank == null) {
			return stack;
		}
		FluidStack fluid = getFluid(tank);
		Effects e = NBTEffectHelper.getEffects(fluid);
		if (fluid == null || e == null) {
			return stack;
		}
		List<PotionEffect> effects = e.getEffects();

		ModPotionHelper.addEffectsToEntity(effects, player);
		PotionProperties p = NBTEffectHelper.getProperties(fluid);
		if (p != null && p.isPersistent) {
			if (!player.getEntityData().hasKey("AI.PersistentIDs")) {
				player.getEntityData().setIntArray("AI.PersistentIDs", ModPotionHelper.potionsToIntArray(effects)[0]);
			} else {
				player.getEntityData().setIntArray("AI.PersistentIDs", ModPotionHelper.mergeIntArrays(player.getEntityData().getIntArray("AI.PersistentIDs"), ModPotionHelper.potionsToIntArray(effects)[0]));
			}
		}
		drainTank(tank);
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.isSneaking()) {
			setCoords(stack, null);
			return stack;
		}

		MovingObjectPosition pos = getMovingObjectPositionFromPlayer(world, player, true);
		if (pos != null) {
			TileEntity te = world.getTileEntity(pos.getBlockPos());
			if (te instanceof TEPotionTank) {
				setCoords(stack, pos.getBlockPos());
				return stack;
			}
		}

		TEPotionTank potionTank = getTank(world, getCoords(stack));
		FluidStack fluid = getFluid(potionTank);
		if (fluid != null && fluid.amount >= 333) {
			PotionProperties properties = NBTEffectHelper.getProperties(fluid);
			if (properties != null) {
				if (!NBTEffectHelper.getProperties(fluid.tag).isSplash) {
					player.setItemInUse(stack, getMaxItemUseDuration(stack));
				} else {
					drainTank(potionTank);
					world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
					if (!world.isRemote) {
						world.spawnEntityInWorld(new EntitySplashPotion(world, player, NBTEffectHelper.getEffects(fluid)));
					}
				}
			}
		}
		return stack;
	}

}
