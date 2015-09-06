package bourgeoisarab.divinealchemy.client.renderer.entity;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;

import bourgeoisarab.divinealchemy.common.entity.EntityPlayerClone;

import com.mojang.authlib.GameProfile;

public class RendererPlayerClone extends RendererLivingEntity {

	private static final ResourceLocation steveTextures = new ResourceLocation("textures/entity/steve.png");
	public ModelBiped modelBipedMain;
	public ModelBiped modelArmorChestplate;
	public ModelBiped modelArmor;

	public RendererPlayerClone() {
		super(new ModelBiped(0.0F), 0.5F);
		modelBipedMain = (ModelBiped) mainModel;
		modelArmorChestplate = new ModelBiped(1.0F);
		modelArmor = new ModelBiped(0.5F);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityPlayerClone player, int slot, float partialTicks) {
		ItemStack itemstack = player.inventory.armorItemInSlot(3 - slot);

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemArmor) {
				ItemArmor itemarmor = (ItemArmor) item;
				bindTexture(RenderBiped.getArmorResource(player, itemstack, slot, null));
				ModelBiped modelbiped = slot == 2 ? modelArmor : modelArmorChestplate;
				modelbiped.bipedHead.showModel = slot == 0;
				modelbiped.bipedHeadwear.showModel = slot == 0;
				modelbiped.bipedBody.showModel = slot == 1 || slot == 2;
				modelbiped.bipedRightArm.showModel = slot == 1;
				modelbiped.bipedLeftArm.showModel = slot == 1;
				modelbiped.bipedRightLeg.showModel = slot == 2 || slot == 3;
				modelbiped.bipedLeftLeg.showModel = slot == 2 || slot == 3;
				modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(player, itemstack, slot, modelbiped);
				setRenderPassModel(modelbiped);
				modelbiped.onGround = mainModel.onGround;
				modelbiped.isRiding = mainModel.isRiding;
				modelbiped.isChild = mainModel.isChild;

				// Move outside if to allow for more then just CLOTH
				int j = itemarmor.getColor(itemstack);
				if (j != -1) {
					float f1 = (j >> 16 & 255) / 255.0F;
					float f2 = (j >> 8 & 255) / 255.0F;
					float f3 = (j & 255) / 255.0F;
					GL11.glColor3f(f1, f2, f3);

					if (itemstack.isItemEnchanted()) {
						return 31;
					}

					return 16;
				}

				GL11.glColor3f(1.0F, 1.0F, 1.0F);

				if (itemstack.isItemEnchanted()) {
					return 15;
				}

				return 1;
			}
		}

		return -1;
	}

	protected void func_82408_c(EntityPlayerClone player, int slot, float partialTicks) {
		ItemStack itemstack = player.inventory.armorItemInSlot(3 - slot);

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemArmor) {
				bindTexture(RenderBiped.getArmorResource(player, itemstack, slot, "overlay"));
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all probabilty, the class Render is
	 * generic (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(EntityPlayerClone player, double x, double y, double z, float f1, float f2) {
		// Log.info("rendering player clone");
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		ItemStack itemstack = player.inventory.getCurrentItem();
		modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = itemstack != null ? 1 : 0;

		if (itemstack != null && player.getItemInUseCount() > 0) {
			EnumAction enumaction = itemstack.getItemUseAction();

			if (enumaction == EnumAction.block) {
				modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 3;
			} else if (enumaction == EnumAction.bow) {
				modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = true;
			}
		}

		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = player.isSneaking();
		double d3 = y - player.yOffset;

		if (player.isSneaking()) {
			d3 -= 0.125D;
		}

		super.doRender(player, x, d3, z, f1, f2);
		modelArmorChestplate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = false;
		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = false;
		modelArmorChestplate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 0;
		passSpecialRender(player, x, y, z);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityPlayerClone player) {
		if (player.master instanceof AbstractClientPlayer) {
			return ((AbstractClientPlayer) player.master).getLocationSkin();
		}
		return steveTextures;
	}

	protected void renderEquippedItems(EntityPlayerClone player, float partialTicks) {

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		super.renderEquippedItems(player, partialTicks);
		super.renderArrowsStuckInEntity(player, partialTicks);
		ItemStack itemstack = player.inventory.armorItemInSlot(3);

		if (itemstack != null) {
			GL11.glPushMatrix();
			modelBipedMain.bipedHead.postRender(0.0625F);
			float f1;

			if (itemstack.getItem() instanceof ItemBlock) {
				net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
				boolean is3D = customRenderer != null
						&& customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D);

				if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
					f1 = 0.625F;
					GL11.glTranslatef(0.0F, -0.25F, 0.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(f1, -f1, -f1);
				}

				renderManager.itemRenderer.renderItem(player, itemstack, 0);
			} else if (itemstack.getItem() == Items.skull) {
				f1 = 1.0625F;
				GL11.glScalef(f1, -f1, -f1);
				GameProfile gameprofile = null;

				if (itemstack.hasTagCompound()) {
					NBTTagCompound nbttagcompound = itemstack.getTagCompound();

					if (nbttagcompound.hasKey("SkullOwner", 10)) {
						gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
					} else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
						gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));
					}
				}

				TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack.getItemDamage(), gameprofile);
			}

			GL11.glPopMatrix();
		}

		float f2;

		if (player.getCommandSenderName().equals("deadmau5")) {
			bindTexture(getEntityTexture(player));

			for (int j = 0; j < 2; ++j) {
				float f9 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
				float f10 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
				GL11.glPushMatrix();
				GL11.glRotatef(f9, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(f10, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.375F * (j * 2 - 1), 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.375F, 0.0F);
				GL11.glRotatef(-f10, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-f9, 0.0F, 1.0F, 0.0F);
				f2 = 1.3333334F;
				GL11.glScalef(f2, f2, f2);
				modelBipedMain.renderEars(0.0625F);
				GL11.glPopMatrix();
			}
		}

		// Capes
		// boolean flag = player.func_152122_n();
		//
		float f4;
		//
		// if (flag && !player.isInvisible() && !player.getHideCape()) {
		// bindTexture(player.getLocationCape());
		// GL11.glPushMatrix();
		// GL11.glTranslatef(0.0F, 0.0F, 0.125F);
		// double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
		// double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
		// double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
		// f4 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
		// double d1 = MathHelper.sin(f4 * (float) Math.PI / 180.0F);
		// double d2 = -MathHelper.cos(f4 * (float) Math.PI / 180.0F);
		// float f5 = (float) d4 * 10.0F;
		//
		// if (f5 < -6.0F) {
		// f5 = -6.0F;
		// }
		//
		// if (f5 > 32.0F) {
		// f5 = 32.0F;
		// }
		//
		// float f6 = (float) (d3 * d1 + d0 * d2) * 100.0F;
		// float f7 = (float) (d3 * d2 - d0 * d1) * 100.0F;
		//
		// if (f6 < 0.0F) {
		// f6 = 0.0F;
		// }
		//
		// float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
		// f5 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f8;
		//
		// if (player.isSneaking()) {
		// f5 += 25.0F;
		// }
		//
		// GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
		// GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
		// GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
		// GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		// modelBipedMain.renderCloak(0.0625F);
		// GL11.glPopMatrix();
		// }

		ItemStack itemstack1 = player.inventory.getCurrentItem();

		if (itemstack1 != null) {
			GL11.glPushMatrix();
			modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			EnumAction enumaction = null;

			if (player.getItemInUseCount() > 0) {
				enumaction = itemstack1.getItemUseAction();
			}

			net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(itemstack1, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
			boolean is3D = customRenderer != null
					&& customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack1, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D);

			if (is3D || itemstack1.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack1.getItem()).getRenderType())) {
				f2 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f2 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-f2, -f2, f2);
			} else if (itemstack1.getItem() == Items.bow) {
				f2 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f2, -f2, f2);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (itemstack1.getItem().isFull3D()) {
				f2 = 0.625F;

				if (itemstack1.getItem().shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block) {
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(f2, -f2, f2);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				f2 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f2, f2, f2);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float f3;
			int k;
			float f12;

			if (itemstack1.getItem().requiresMultipleRenderPasses()) {
				for (k = 0; k < itemstack1.getItem().getRenderPasses(itemstack1.getItemDamage()); ++k) {
					int i = itemstack1.getItem().getColorFromItemStack(itemstack1, k);
					f12 = (i >> 16 & 255) / 255.0F;
					f3 = (i >> 8 & 255) / 255.0F;
					f4 = (i & 255) / 255.0F;
					GL11.glColor4f(f12, f3, f4, 1.0F);
					renderManager.itemRenderer.renderItem(player, itemstack1, k);
				}
			} else {
				k = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
				float f11 = (k >> 16 & 255) / 255.0F;
				f12 = (k >> 8 & 255) / 255.0F;
				f3 = (k & 255) / 255.0F;
				GL11.glColor4f(f11, f12, f3, 1.0F);
				renderManager.itemRenderer.renderItem(player, itemstack1, 0);
			}

			GL11.glPopMatrix();
		}
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityPlayerClone player, float partialTicks) {
		float f1 = 0.9375F;
		GL11.glScalef(f1, f1, f1);
	}

	// protected void func_96449_a(EntityPlayerClone player, double x, double y, double z, String s, float f1, double f2) {
	// if (f2 < 100.0D) {
	// Scoreboard scoreboard = player.getWorldScoreboard();
	// ScoreObjective scoreobjective = scoreboard.func_96539_a(2);
	//
	// if (scoreobjective != null) {
	// Score score = scoreboard.func_96529_a(player.getCommandSenderName(), scoreobjective);
	//
	// if (player.isPlayerSleeping()) {
	// func_147906_a(player, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y - 1.5D, z, 64);
	// } else {
	// func_147906_a(player, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
	// }
	//
	// y += getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * f1;
	// }
	// }
	//
	// super.func_96449_a(player, x, y, z, s, f1, f2);
	// }

	public void renderFirstPersonArm(EntityPlayer player) {
		float f = 1.0F;
		GL11.glColor3f(f, f, f);
		modelBipedMain.onGround = 0.0F;
		modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
		modelBipedMain.bipedRightArm.render(0.0625F);
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityPlayerClone player, double x, double y, double z) {
		if (player.isEntityAlive() && player.isPlayerSleeping()) {
			super.renderLivingAt(player, x, y, z);
			// TODO
			// super.renderLivingAt(player, x + player.field_71079_bU, y + player.field_71082_cx, z + player.field_71089_bV);
		} else {
			super.renderLivingAt(player, x, y, z);
		}
	}

	protected void rotateCorpse(EntityPlayerClone player, float x, float y, float z) {
		if (player.isEntityAlive() && player.isPlayerSleeping()) {
			GL11.glRotatef(getDeathMaxRotation(player), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.rotateCorpse(player, x, y, z);
		}
	}

	@Override
	protected void func_96449_a(EntityLivingBase entity, double x, double y, double z, String s, float f1, double f2) {
		super.func_96449_a(entity, x, y, z, s, f1, f2);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float partialTicks) {
		this.preRenderCallback((EntityPlayerClone) entity, partialTicks);
	}

	@Override
	protected void func_82408_c(EntityLivingBase entity, int slot, float partialTicks) {
		this.func_82408_c((EntityPlayerClone) entity, slot, partialTicks);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase entity, int slot, float partialTicks) {
		return this.shouldRenderPass((EntityPlayerClone) entity, slot, partialTicks);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase entity, float partialTicks) {
		this.renderEquippedItems((EntityPlayerClone) entity, partialTicks);
	}

	@Override
	protected void rotateCorpse(EntityLivingBase entity, float x, float y, float z) {
		this.rotateCorpse((EntityPlayerClone) entity, x, y, z);
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	@Override
	protected void renderLivingAt(EntityLivingBase entity, double x, double y, double z) {
		this.renderLivingAt((EntityPlayerClone) entity, x, y, z);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all probabilty, the class Render is
	 * generic (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(EntityLivingBase entity, double x, double y, double z, float f1, float f2) {
		this.doRender((EntityPlayerClone) entity, x, y, z, f1, f2);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityPlayerClone) entity);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all probabilty, the class Render is
	 * generic (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2) {
		this.doRender((EntityPlayerClone) entity, x, y, z, f1, f2);
	}
}
