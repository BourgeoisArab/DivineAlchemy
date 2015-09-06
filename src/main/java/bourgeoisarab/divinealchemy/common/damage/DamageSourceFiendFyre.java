package bourgeoisarab.divinealchemy.common.damage;

import net.minecraft.util.DamageSource;

public class DamageSourceFiendFyre extends DamageSource {

	public static DamageSource instance = new DamageSourceFiendFyre();

	public DamageSourceFiendFyre() {
		super("fiendFyre");
		setDamageBypassesArmor();
		setDamageIsAbsolute();
		setMagicDamage();
	}

}
