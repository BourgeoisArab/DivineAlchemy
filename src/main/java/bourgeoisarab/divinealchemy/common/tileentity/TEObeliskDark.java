package bourgeoisarab.divinealchemy.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ITickable;
import bourgeoisarab.divinealchemy.common.energy.EnergyBuffer;
import bourgeoisarab.divinealchemy.utility.Log;

public class TEObeliskDark extends TEPowerProvider implements ITickable {

	public static List<TerribleObeliskEffect> evilThings = new ArrayList<TerribleObeliskEffect>();
	static {
		// TODO make these configurable
		evilThings.add(TerribleObeliskEffect.grassDecay);
		evilThings.add(TerribleObeliskEffect.leafDecay);
		evilThings.add(TerribleObeliskEffect.cropDecay);
		evilThings.add(TerribleObeliskEffect.dirtDecay);
		evilThings.add(TerribleObeliskEffect.woodDecay);
		evilThings.add(TerribleObeliskEffect.creatureDamage);
		evilThings.add(TerribleObeliskEffect.creatureKill);
	}

	protected int range = 12;
	protected int rangeSq = 144;

	public TEObeliskDark() {
		buffer = new EnergyBuffer(Integer.MAX_VALUE);
	}

	@Override
	public void update() {
		if (worldObj.getWorldTime() % 20 == 0) {
			doEvilThing();
			buffer.add(10);
		}
	}

	protected void doEvilThing() {
		for (TerribleObeliskEffect e : evilThings) {
			if (e.performTask(this, range, false)) {
				buffer.add(e.energy / 2);
			}
		}
	}

	public void drainAllLife() {
		boolean running = true;
		while (running) {
			running = false;
			for (TerribleObeliskEffect e : evilThings) {
				if (e.performTask(this, range, true)) {
					running = true;
					Log.info("loop");
				}
			}
		}
	}

}
