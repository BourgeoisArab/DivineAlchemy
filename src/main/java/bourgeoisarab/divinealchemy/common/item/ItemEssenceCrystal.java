package bourgeoisarab.divinealchemy.common.item;

import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;
import bourgeoisarab.divinealchemy.common.potion.ingredient.IngredientEssenceCrystal;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.reference.Ref;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEssenceCrystal extends Item {

	public ItemEssenceCrystal() {
		if (ConfigHandler.creativeTab) {
			setCreativeTab(DivineAlchemy.tabAInstillation);
		}
		setMaxDamage(0);
		setHasSubtypes(true);
		setUnlocalizedName("itemEssenceCrystal");
		setTextureName(Ref.Location.PREFIX + "essenceCrystal");
	}

	public static void init() {
		for (int i = 0; i < Potion.potionTypes.length; i++) {
			if (Potion.potionTypes[i] != null) {
				registerCrystal(i);
			}
		}
	}

	public static void registerCrystal(int meta) {
		PotionIngredient.registerIngredient(new IngredientEssenceCrystal(new ItemStack(ModItems.itemEssenceCrystal, 1, meta), 0, meta).setMetaSensitive(true));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int i) {
		Potion potion = ModPotion.getPotion(stack.getItemDamage());
		return potion == null ? 0xFFFFFF : potion.getLiquidColor();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		if (ModPotion.getPotion(stack.getItemDamage()) != null) {
			list.add(I18n.format(ModPotion.getPotion(stack.getItemDamage()).getName()));
		}
	}

}
