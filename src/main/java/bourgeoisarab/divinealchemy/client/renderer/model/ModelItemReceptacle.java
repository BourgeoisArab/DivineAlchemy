package bourgeoisarab.divinealchemy.client.renderer.model;

import bourgeoisarab.divinealchemy.common.tileentity.TEItemReceptacle;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class ModelItemReceptacle extends ModelBase {

	private float SF = 0.0625F;
	private IModelCustom modelReceptacle;

	public ModelItemReceptacle() {
		modelReceptacle = AdvancedModelLoader.loadModel(new ResourceLocation(Ref.MODID + ":models/modelItemReceptacle.obj"));
	}

	public void render(TEItemReceptacle entity, double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 1.5F);
		GL11.glScalef(SF, SF, SF);
		ResourceLocation texture = new ResourceLocation(Ref.MODID + ":textures/blocks/brick_infused.png");
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		modelReceptacle.renderAll();
		GL11.glPopMatrix();
	}

}
