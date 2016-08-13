package bourgeoisarab.divinealchemy.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockBrewingCauldron;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockObelisk;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockPotionTank;
import bourgeoisarab.divinealchemy.client.renderer.entity.RenderFactorySplashPotion;
import bourgeoisarab.divinealchemy.client.renderer.model.ModelColoured3D;
import bourgeoisarab.divinealchemy.common.entity.EntitySplashPotion;
import bourgeoisarab.divinealchemy.common.event.DAClientEventHooks;
import bourgeoisarab.divinealchemy.common.tileentity.TEBrewingCauldron;
import bourgeoisarab.divinealchemy.common.tileentity.TEObelisk;
import bourgeoisarab.divinealchemy.common.tileentity.TEPotionTank;
import bourgeoisarab.divinealchemy.init.ModBlocks;
import bourgeoisarab.divinealchemy.init.ModItems;
import bourgeoisarab.divinealchemy.reference.Ref;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		OBJLoader.instance.addDomain(Ref.MODID);
		registerModels();
		registerFluidModels();
		registerRenderers();
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
		MinecraftForge.EVENT_BUS.register(new DAClientEventHooks());
	}

	private void registerInventoryModel(Item item, int meta, String modelName, boolean forceColor) {
		ModelResourceLocation model = new ModelResourceLocation(Ref.Location.PREFIX + modelName, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, model);
		if (forceColor) {
			ModelColoured3D.registerModel(model);
		}
	}

	private void registerInventoryModel(Item item, int meta, String modelName) {
		registerInventoryModel(item, meta, modelName, false);
	}

	private void registerInventoryModel(Block item, int meta, String modelName) {
		registerInventoryModel(Item.getItemFromBlock(item), meta, modelName, false);
	}

	private void registerModels() {
		for (int i = 0; i < 5; i++) {
			registerInventoryModel(ModBlocks.brewingCauldron, i, "brewingCauldron_tier" + (i + 1));
		}

		registerInventoryModel(ModItems.instillationTome, 0, "instillationTome");
		registerInventoryModel(ModItems.bottleEnder, 0, "bottleEnder");

		for (int i = 0; i < Potion.potionTypes.length; i++) {
			registerInventoryModel(ModItems.essenceCrystal, i, "essenceCrystal");
		}

		registerInventoryModel(ModItems.butcherKnife, 0, "butcherKnife");
		registerInventoryModel(ModItems.bucketHotMess, 0, "bucketHotMess");
		registerInventoryModel(ModItems.bucketPotion, 0, "bucketPotion");
		registerInventoryModel(ModItems.bottlePotion, 0, "bottlePotion");
		registerInventoryModel(ModItems.bottleEnder, 0, "bottleEnder");
		registerInventoryModel(ModBlocks.potionTank, 0, "potionTank");
		registerInventoryModel(ModItems.crystalBasic, 0, "crystal1", true);
		registerInventoryModel(ModItems.crystalMedium, 0, "crystal2", true);
		registerInventoryModel(ModItems.crystalBig, 0, "crystal3", true);
		registerInventoryModel(ModBlocks.obelisk, 0, "obeliskBrick1");
		registerInventoryModel(ModBlocks.obelisk, 1, "obeliskReceptacle1");
		registerInventoryModel(ModBlocks.obelisk, 4, "obeliskBrick2");
		registerInventoryModel(ModBlocks.obelisk, 5, "obeliskReceptacle2");
		registerInventoryModel(ModBlocks.obelisk, 8, "obeliskBrick3");
		registerInventoryModel(ModBlocks.obelisk, 9, "obeliskReceptacle3");
		registerInventoryModel(ModBlocks.deadDirt, 0, "deadDirt");
		registerInventoryModel(ModBlocks.deadWood, 0, "deadWood");
		registerInventoryModel(ModItems.amulet, 0, "amulet");

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.obeliskDark), 1, new ModelResourceLocation(Ref.MODID + ":tile.obeliskDark", "receptacle=true"));

	}

	private void registerFluidModels() {
		Item item = Item.getItemFromBlock(ModBlocks.hotMess);
		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {

			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(Ref.MODID + ":BlockFluidStuff2");
			}
		});

		ModelLoader.setCustomStateMapper(ModBlocks.hotMess, new StateMapperBase() {

			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Ref.MODID + ":BlockFluidStuff2");
			}
		});

		item = Item.getItemFromBlock(ModBlocks.potion);
		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {

			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(Ref.MODID + ":BlockFluidStuff", "potion");
			}
		});

		ModelLoader.setCustomStateMapper(ModBlocks.potion, new StateMapperBase() {

			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Ref.MODID + ":BlockFluidStuff", "potion");
			}
		});
	}

	static class MeshDefinition implements ItemMeshDefinition {

		private ModelResourceLocation model;

		public MeshDefinition(String s) {
			model = new ModelResourceLocation(s);
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return model;
		}

	}

	private void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TEBrewingCauldron.class, new RendererBlockBrewingCauldron());
		ClientRegistry.bindTileEntitySpecialRenderer(TEPotionTank.class, new RendererBlockPotionTank());
		ClientRegistry.bindTileEntitySpecialRenderer(TEObelisk.class, new RendererBlockObelisk());

		RenderingRegistry.registerEntityRenderingHandler(EntitySplashPotion.class, new RenderFactorySplashPotion());
		// RenderingRegistry.registerEntityRenderingHandler(EntityAmuletSpell.class, new RendererFactoryAmuletSpell());
		// RenderingRegistry.registerEntityRenderingHandler(EntitySpecialCreeper.class, new RenderSpecialCreeper());
		// RenderingRegistry.registerEntityRenderingHandler(EntityPlayerClone.class, new RendererPlayerClone());
	}

	@Override
	public Minecraft getClient() {
		return Minecraft.getMinecraft();
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

	public static RenderManager getRenderManager() {
		return Minecraft.getMinecraft().getRenderManager();
	}

}
