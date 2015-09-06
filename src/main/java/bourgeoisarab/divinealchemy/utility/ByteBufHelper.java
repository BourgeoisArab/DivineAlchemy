package bourgeoisarab.divinealchemy.utility;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import bourgeoisarab.divinealchemy.common.potion.Colouring;
import bourgeoisarab.divinealchemy.common.potion.Effects;
import bourgeoisarab.divinealchemy.common.potion.Ingredients;
import bourgeoisarab.divinealchemy.common.potion.ingredient.PotionIngredient;
import bourgeoisarab.divinealchemy.init.ConfigHandler;
import cpw.mods.fml.common.network.ByteBufUtils;

public class ByteBufHelper {

	public static void writeFluid(ByteBuf buf, FluidStack fluid) {
		if (fluid != null) {
			ByteBufUtils.writeUTF8String(buf, FluidRegistry.getFluidName(fluid));
			buf.writeInt(fluid.amount);
			ByteBufUtils.writeTag(buf, fluid.tag);
		} else {
			ByteBufUtils.writeUTF8String(buf, "");
			buf.writeInt(-1);
			ByteBufUtils.writeTag(buf, null);
		}
	}

	public static FluidStack readFluid(ByteBuf buf) {
		FluidStack fluid = null;
		String name = ByteBufUtils.readUTF8String(buf);
		if (FluidRegistry.getFluid(name) != null) {
			fluid = new FluidStack(FluidRegistry.getFluid(name), buf.readInt());
		} else {
			buf.readInt();
		}
		return fluid;
	}

	public static void writeEffects(ByteBuf buf, Effects effects) {
		buf.writeByte(effects.size());
		for (int i = 0; i < effects.size(); i++) {
			PotionEffect e = effects.getEffect(i);
			if (e != null) {
				buf.writeByte(e.getPotionID());
				buf.writeInt(e.getDuration());
				buf.writeByte(e.getAmplifier());
				buf.writeBoolean(effects.getSideEffect(i));
			} else {
				buf.writeByte(-1);
				buf.writeInt(-1);
				buf.writeByte(-1);
				buf.writeBoolean(false);
			}
		}
	}

	public static Effects readEffects(ByteBuf buf) {
		int size = buf.readByte();
		Effects effects = new Effects();
		for (int i = 0; i < size; i++) {
			int value = buf.readByte();
			if (value >= 0) {
				effects.add(new PotionEffect(value, buf.readInt(), buf.readByte()), buf.readBoolean());
			} else {
				buf.readInt();
				buf.readByte();
				buf.readBoolean();
			}
		}
		return effects;
	}

	public static void writeIngredients(ByteBuf buf, Ingredients ingredients) {
		for (int i = 0; i < ConfigHandler.maxEffects; i++) {
			PotionIngredient ing = ingredients.getIngredient(i);
			if (ing != null) {
				buf.writeShort(ing.id);
				buf.writeBoolean(ingredients.getSide(i));
			} else {
				buf.writeShort(-1);
				buf.writeBoolean(false);
			}
		}
	}

	public static Ingredients readIngredients(ByteBuf buf) {
		Ingredients ingredients = new Ingredients();
		for (int i = 0; i < ConfigHandler.maxEffects; i++) {
			int value = buf.readShort();
			if (value != -1) {
				ingredients.add(PotionIngredient.getIngredient(value), buf.readBoolean());
			} else {
				buf.readShort();
				buf.readBoolean();
			}
		}
		return ingredients;
	}

	public static void writeColouring(ByteBuf buf, Colouring colouring) {
		List<Integer> colours = colouring.getColours();
		buf.writeByte(colours.size());
		for (int i = 0; i < colours.size(); i++) {
			Integer colour = colours.get(i);
			if (colour != null) {
				buf.writeInt(colour);
			} else {
				buf.writeInt(-1);
			}
		}
	}

	public static Colouring readColouring(ByteBuf buf) {
		int size = buf.readByte();
		Colouring colouring = new Colouring();
		for (int i = 0; i < size; i++) {
			int colour = buf.readInt();
			if (colour >= 0) {
				colouring.add(colour, true);
			}
		}
		return colouring;
	}
}
