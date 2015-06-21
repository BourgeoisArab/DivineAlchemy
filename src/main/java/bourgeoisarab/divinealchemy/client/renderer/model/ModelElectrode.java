package bourgeoisarab.divinealchemy.client.renderer.model;

import bourgeoisarab.divinealchemy.common.tileentity.TEElectrode;
import bourgeoisarab.divinealchemy.reference.Ref;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class ModelElectrode extends ModelBase {

	private final float SF = 0.0625F;
	private IModelCustom model;

	public ModelElectrode() {
		model = AdvancedModelLoader.loadModel(new ResourceLocation(Ref.MODID + ":models/modelElectrode.obj"));
	}

	public void renderElectrode(TEElectrode entity, double x, double y, double z) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GL11.glScalef(SF, SF, SF);

		ResourceLocation texture = new ResourceLocation(Ref.MODID + ":textures/model/electrodeIron.png");

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		model.renderAll();

		GL11.glPopMatrix();
	}

}
