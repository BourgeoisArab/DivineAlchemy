package bourgeoisarab.divinealchemy.common.tome;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TomeEntry {

	public final TomeCategory category;
	public final String name;

	public TomeEntry(TomeCategory category, String name) {
		this.category = category;
		this.name = name;
	}

	@SideOnly(Side.CLIENT)
	public String getDisplayName() {
		return I18n.format(name);
	}

}
