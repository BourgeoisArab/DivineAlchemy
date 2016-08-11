package bourgeoisarab.divinealchemy.common.event;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import bourgeoisarab.divinealchemy.DivineAlchemy;
import bourgeoisarab.divinealchemy.client.renderer.model.ModelColoured3D;
import bourgeoisarab.divinealchemy.common.potion.ModPotion;

public class DAClientEventHooks {

	@SubscribeEvent
	public void bakeModel(ModelBakeEvent event) {
		for (ModelResourceLocation model : ModelColoured3D.registeredLocations) {
			Object object = event.modelRegistry.getObject(model);
			if (object instanceof IBakedModel) {
				IBakedModel existingModel = (IBakedModel) object;
				ModelColoured3D newModel = new ModelColoured3D(existingModel);
				event.modelRegistry.putObject(model, newModel);
			}
		}
	}

	// @SubscribeEvent
	public void renderEntity(RenderLivingEvent event) {
		EntityLivingBase entity = event.entity;
		if (entity.getActivePotionEffect(ModPotion.potionFiendFyre) != null) {
			// TODO: fiend fyre
		}
	}

	@SubscribeEvent
	public void playSound(PlaySoundEvent event) {
		EntityPlayer player = DivineAlchemy.proxy.getClientPlayer();
		if (player != null && player.getActivePotionEffect(ModPotion.potionDeafness) != null) {
			event.result = null;
		}
	}

}
