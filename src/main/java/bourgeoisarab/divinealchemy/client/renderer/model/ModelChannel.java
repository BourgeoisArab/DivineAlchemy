package bourgeoisarab.divinealchemy.client.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import bourgeoisarab.divinealchemy.common.tileentity.TEChannel;
import bourgeoisarab.divinealchemy.reference.Ref;
import cpw.mods.fml.client.FMLClientHandler;

public class ModelChannel extends ModelBase {

	private float SF = 0.0625F;
	private IModelCustom modelChannel;

	public ModelChannel() {
		modelChannel = AdvancedModelLoader.loadModel(new ResourceLocation(Ref.MODID + ":models/modelChannel.obj"));
	}

	public void render(TEChannel entity, double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glScalef(SF, SF, SF);
		ResourceLocation texture = new ResourceLocation(Ref.MODID + ":textures/blocks/brick_infused.png");
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		modelChannel.renderAll();
		GL11.glPopMatrix();
	}

}
