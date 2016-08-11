package bourgeoisarab.divinealchemy.proxy;

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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockBrewingCauldron;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockObelisk;
import bourgeoisarab.divinealchemy.client.renderer.RendererBlockPotionTank;
import bourgeoisarab.divinealchemy.client.renderer.entity.RenderEntitySplashPotion;
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
	}

	@Override
	public void init() {
		registerRenderers();
	}

	@Override
	public void postInit() {
		MinecraftForge.EVENT_BUS.register(new DAClientEventHooks());
	}

	private void registerModels() {
		Item item = GameRegistry.findItem(Ref.MODID, ModBlocks.brewingCauldron.getUnlocalizedName());
		ModelResourceLocation model = new ModelResourceLocation(Ref.MODID + ":brewingCauldron_tier1", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		model = new ModelResourceLocation(Ref.MODID + ":brewingCauldron_tier2", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 1, model);

		model = new ModelResourceLocation(Ref.MODID + ":brewingCauldron_tier3", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 2, model);

		model = new ModelResourceLocation(Ref.MODID + ":brewingCauldron_tier4", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 3, model);

		model = new ModelResourceLocation(Ref.MODID + ":brewingCauldron_tier5", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 4, model);

		item = ModItems.instillationTome;
		model = new ModelResourceLocation(Ref.MODID + ":instillationTome", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = ModItems.bottleEnder;
		model = new ModelResourceLocation(Ref.MODID + ":bottleEnder", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = ModItems.essenceCrystal;
		model = new ModelResourceLocation(Ref.MODID + ":essenceCrystal", "inventory");
		for (int i = 0; i < Potion.potionTypes.length; i++) {
			ModelLoader.setCustomModelResourceLocation(item, i, model);
		}

		item = ModItems.butcherKnife;
		model = new ModelResourceLocation(Ref.MODID + ":butcherKnife", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = ModItems.bucketHotMess;
		model = new ModelResourceLocation(Ref.MODID + ":bucketHotMess", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = ModItems.bucketPotion;
		model = new ModelResourceLocation(Ref.MODID + ":bucketPotion", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = ModItems.bottlePotion;
		model = new ModelResourceLocation(Ref.MODID + ":bottlePotion", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = ModItems.bottleEnder;
		model = new ModelResourceLocation(Ref.MODID + ":bottleEnder", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = GameRegistry.findItem(Ref.MODID, ModBlocks.potionTank.getUnlocalizedName());
		model = new ModelResourceLocation(Ref.MODID + ":potionTank", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = ModItems.crystalBasic;
		model = new ModelResourceLocation(Ref.MODID + ":crystal1");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
		ModelColoured3D.registerModel(model);

		item = ModItems.crystalMedium;
		model = new ModelResourceLocation(Ref.MODID + ":crystal2");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
		ModelColoured3D.registerModel(model);

		item = ModItems.crystalBig;
		model = new ModelResourceLocation(Ref.MODID + ":crystal3");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
		ModelColoured3D.registerModel(model);

		item = Item.getItemFromBlock(ModBlocks.obelisk);
		model = new ModelResourceLocation(Ref.MODID + ":obeliskBrick1", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
		model = new ModelResourceLocation(Ref.MODID + ":obeliskBrick2", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 4, model);
		model = new ModelResourceLocation(Ref.MODID + ":obeliskBrick3", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 8, model);
		model = new ModelResourceLocation(Ref.MODID + ":obeliskReceptacle1", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 1, model);
		model = new ModelResourceLocation(Ref.MODID + ":obeliskReceptacle2", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 5, model);
		model = new ModelResourceLocation(Ref.MODID + ":obeliskReceptacle3", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 9, model);

		item = Item.getItemFromBlock(ModBlocks.deadDirt);
		model = new ModelResourceLocation(Ref.MODID + ":deadDirt", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);

		item = Item.getItemFromBlock(ModBlocks.deadWood);
		model = new ModelResourceLocation(Ref.MODID + ":deadWood", "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
	}

	private void registerFluidModels() {
		Item item = Item.getItemFromBlock(ModBlocks.hotMess);
		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {

			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(Ref.MODID + ":BlockFluidStuff", "hotmess");
			}
		});

		ModelLoader.setCustomStateMapper(ModBlocks.hotMess, new StateMapperBase() {

			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Ref.MODID + ":BlockFluidStuff", "hotmess");
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

	private void registerRenderers() {
		RendererBlockObelisk renderObelisk = new RendererBlockObelisk();
		RendererBlockBrewingCauldron renderCauldron = new RendererBlockBrewingCauldron();

		ClientRegistry.bindTileEntitySpecialRenderer(TEBrewingCauldron.class, renderCauldron);
		ClientRegistry.bindTileEntitySpecialRenderer(TEPotionTank.class, new RendererBlockPotionTank());
		ClientRegistry.bindTileEntitySpecialRenderer(TEObelisk.class, renderObelisk);
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.obelisk), 0, TEObelisk.class);

		// MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.brewingCauldron), new RendererItemBrewingCauldron(new TEBrewingCauldron(), renderCauldron));
		// MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.obelisk), new RendererItemObelisk(new TEObelisk(), renderObelisk));

		// RenderingRegistry.registerEntityRenderingHandler(EntitySpecialCreeper.class, new RenderSpecialCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntitySplashPotion.class, new RenderEntitySplashPotion(getRenderManager()));
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

}
