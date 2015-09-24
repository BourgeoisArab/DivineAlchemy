package bourgeoisarab.divinealchemy.common.event;

import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerRegisterEvent;
import bourgeoisarab.divinealchemy.common.fluid.EffectContainerRegistry;
import bourgeoisarab.divinealchemy.init.ModFluids;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RuntimeEventHooks {

	@SubscribeEvent
	public void registerContainer(FluidContainerRegisterEvent event) {
		FluidContainerData data = event.data;
		if (data.fluid.getFluid() == ModFluids.potion) {
			EffectContainerRegistry.register(data.filledContainer, data.emptyContainer);
		}
	}

}
