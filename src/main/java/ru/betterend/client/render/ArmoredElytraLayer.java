package ru.betterend.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import ru.betterend.BetterEnd;
import ru.betterend.item.ArmoredElytra;
import ru.betterend.item.model.ArmoredElytraModel;
import ru.betterend.registry.EndItems;

public class ArmoredElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
	private final ArmoredElytraModel<T> elytraModel = new ArmoredElytraModel<>();

	public ArmoredElytraLayer(RenderLayerParent<T, M> renderLayerParent) {
		super(renderLayerParent);
	}

	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		if (itemStack.getItem() instanceof ArmoredElytra) {
			ResourceLocation wingsTexture = ((ArmoredElytra) itemStack.getItem()).getWingTexture();
			if (livingEntity instanceof AbstractClientPlayer) {
				AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) livingEntity;
				if (abstractClientPlayer.isElytraLoaded() && abstractClientPlayer.getElytraTextureLocation() != null) {
					wingsTexture = abstractClientPlayer.getElytraTextureLocation();
				} else if (abstractClientPlayer.isCapeLoaded() && abstractClientPlayer.getCloakTextureLocation() != null && abstractClientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
					wingsTexture = abstractClientPlayer.getCloakTextureLocation();
				}
			}

			poseStack.pushPose();
			poseStack.translate(0.0D, 0.0D, 0.125D);
			getParentModel().copyPropertiesTo(elytraModel);
			elytraModel.setupAnim(livingEntity, f, g, j, k, l);
			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(wingsTexture), false, itemStack.hasFoil());
			elytraModel.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			poseStack.popPose();
		}
	}
}
