package de.cristelknight.doapi.client.render.feature;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.cristelknight.doapi.common.item.ICustomArmor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class CustomArmorFeatureRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public final CustomArmorManager<T> ARMORS;

	public CustomArmorFeatureRenderer(RenderLayerParent<T, M> context, EntityModelSet modelSet) {
		super(context);
		this.ARMORS = new CustomArmorManager<>(modelSet);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		this.renderArmorPiece(poseStack, multiBufferSource, livingEntity, EquipmentSlot.CHEST, i);
		this.renderArmorPiece(poseStack, multiBufferSource, livingEntity, EquipmentSlot.LEGS, i);
		this.renderArmorPiece(poseStack, multiBufferSource, livingEntity, EquipmentSlot.FEET, i);
		this.renderArmorPiece(poseStack, multiBufferSource, livingEntity, EquipmentSlot.HEAD, i);
	}

	private void renderArmorPiece(PoseStack poseStack, MultiBufferSource multiBufferSource, T livingEntity, EquipmentSlot slot, int light) {
		ItemStack itemStack = livingEntity.getItemBySlot(slot);
		if (!(itemStack.getItem() instanceof ICustomArmor customArmor)) return;
		if (!(itemStack.getItem() instanceof ArmorItem armorItem)) return;
		if (armorItem.getEquipmentSlot() != slot) return;

		EntityModel<T> model = this.ARMORS.getModel(armorItem, slot);

		if (model instanceof HumanoidModel<T> humanoidModel) {
			this.getParentModel().copyPropertiesTo(humanoidModel);
			this.setPartVisibility(humanoidModel, slot);
		}

		boolean foil = itemStack.hasFoil();

		poseStack.pushPose();
		if (slot == EquipmentSlot.HEAD) {
			((HeadedModel) this.getParentModel()).getHead().translateAndRotate(poseStack);
		}
		poseStack.scale(1F, 1F, 1F);
		poseStack.translate(0, customArmor.getYOffset(), 0);


		if (itemStack.is(ItemTags.DYEABLE)) {
			int c = itemStack.get(DataComponents.DYED_COLOR).rgb();
			float r = (float) (c >> 16 & 0xFF) / 255.0f;
			float g = (float) (c >> 8 & 0xFF) / 255.0f;
			float b = (float) (c & 0xFF) / 255.0f;
			this.renderModel(poseStack, multiBufferSource, light, foil, model, r, g, b, this.ARMORS.getTexture(armorItem, null));
			this.renderModel(poseStack, multiBufferSource, light, foil, model, 1.0f, 1.0f, 1.0f, this.ARMORS.getTexture(armorItem, "overlay"));
		} else {
			this.renderModel(poseStack, multiBufferSource, light, foil, model, 1.0f, 1.0f, 1.0f, this.ARMORS.getTexture(armorItem, null));
		}
		poseStack.popPose();
	}

	private void renderModel(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, boolean foil, EntityModel<T> model, float f, float g, float h, ResourceLocation resourceLocation) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(resourceLocation), foil);
		int rInt = (int)(f * 255);
		int gInt = (int)(g * 255);
		int bInt = (int)(h * 255);

		int argbColor = (255 << 24) | (rInt << 16) | (gInt << 8) | bInt;

		model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, argbColor);
	}

	private void setPartVisibility(HumanoidModel<T> humanoidModel, EquipmentSlot equipmentSlot) {
		humanoidModel.setAllVisible(false);
		switch (equipmentSlot) {
			case HEAD -> {
				humanoidModel.head.visible = true;
				humanoidModel.hat.visible = true;
			}
			case CHEST -> {
				humanoidModel.body.visible = true;
				humanoidModel.rightArm.visible = true;
				humanoidModel.leftArm.visible = true;
			}
			case LEGS -> {
				humanoidModel.body.visible = true;
				humanoidModel.rightLeg.visible = true;
				humanoidModel.leftLeg.visible = true;
			}
			case FEET -> {
				humanoidModel.rightLeg.visible = true;
				humanoidModel.leftLeg.visible = true;
			}
		}
	}
}

