package bourgeoisarab.divinealchemy.client.renderer.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;

public class ModelColoured3D implements IBakedModel {

	public static final List<ModelResourceLocation> registeredLocations = new ArrayList<ModelResourceLocation>();

	public static void registerModel(ModelResourceLocation model) {
		registeredLocations.add(model);
	}

	private IBakedModel model;
	private List<BakedQuad> quads = new ArrayList<BakedQuad>();

	public ModelColoured3D(IBakedModel base) {
		model = base;
		for (BakedQuad quad : model.getGeneralQuads()) {
			quads.add(new BakedQuad(quad.getVertexData(), 0xFFFFFF, quad.getFace())); // The colour is to ensure hasTintIndex() returns true
		}
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing side) {
		return model.getFaceQuads(side);
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return quads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return model.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return model.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}

}
