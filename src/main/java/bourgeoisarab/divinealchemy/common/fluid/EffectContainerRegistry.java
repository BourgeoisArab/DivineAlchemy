package bourgeoisarab.divinealchemy.common.fluid;

import java.util.Map;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Maps;

public class EffectContainerRegistry {

	private static Map<Container, Container> containers = Maps.newHashMap();

	private static class Container {

		ItemStack container;

		private Container(ItemStack container) {
			this.container = container;
		}

		@Override
		public int hashCode() {
			int code = 1;
			code = 31 * code + container.getItem().hashCode();
			code = 31 * code + container.getItemDamage();
			return code;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Container)) {
				return false;
			}
			Container ck = (Container) o;
			if (container.getItem() != ck.container.getItem() || container.getItemDamage() != ck.container.getItemDamage()) {
				return false;
			}
			return true;
		}
	}

	public static void register(ItemStack filled, ItemStack empty) {
		if (filled == null || empty == null) {
			throw new IllegalArgumentException();
		}
		containers.put(new Container(filled), new Container(empty));
	}

	public static boolean isFilledContainer(ItemStack stack) {
		return containers.containsKey(new Container(stack));
	}

	public static boolean isEmptyContainer(ItemStack stack) {
		return containers.containsValue(new Container(stack));
	}

}
