package bourgeoisarab.divinealchemy.common.event;

import java.util.Collection;

import net.minecraft.init.Items;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import bourgeoisarab.divinealchemy.common.save.Divinity;

public class StartupEventHooks {

	@SubscribeEvent
	public void registerContainer(FluidContainerRegistry.FluidContainerRegisterEvent event) {
		if (event.data.emptyContainer.getItem() == Items.glass_bottle) {
			event.data.fluid.amount = 333;
		}
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event) {
		Divinity.get(event.world);
	}

	@SubscribeEvent
	public void textureStitch(TextureStitchEvent.Pre event) {
		Collection<Fluid> fluids = FluidRegistry.getRegisteredFluids().values();
		for (Fluid f : fluids) {
			if (event.map.getTextureExtry(f.getStill().toString()) == null) {
				event.map.registerSprite(f.getStill());
			}
			if (event.map.getTextureExtry(f.getFlowing().toString()) == null) {
				event.map.registerSprite(f.getFlowing());
			}
		}
	}
}
