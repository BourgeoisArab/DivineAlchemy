package bourgeoisarab.divinealchemy.client.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.reference.Ref;
import cpw.mods.fml.client.FMLClientHandler;

public class ModelBrewingCauldron extends ModelBase {

	private float SF = 0.0625F;
	private IModelCustom modelCauldron;

	// private IModelCustom modelFluidLevel;

	public ModelBrewingCauldron() {
		modelCauldron = AdvancedModelLoader.loadModel(new ResourceLocation(Ref.MODID + ":models/modelBrewingCauldron.obj"));
	}

	public void render(TEBrewingCauldron entity, double x, double y, double z) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GL11.glScalef(SF, SF, SF);
		ResourceLocation texture = new ResourceLocation(Ref.MODID + ":textures/model/cauldronModelTexture" + entity.getBlockMetadata() + ".png");
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		modelCauldron.renderAll();
		GL11.glPopMatrix();
	}

}
