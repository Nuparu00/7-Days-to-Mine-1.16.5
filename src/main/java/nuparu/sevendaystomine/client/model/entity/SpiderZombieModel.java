package nuparu.sevendaystomine.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import nuparu.sevendaystomine.entity.SpiderZombieEntity;

public class SpiderZombieModel<T extends SpiderZombieEntity> extends AgeableModel<T> implements IHasArm, IHasHead {
	ModelRenderer head;
	ModelRenderer body;
	ModelRenderer rightarm;
	ModelRenderer leftarm;
	ModelRenderer rightlegup;
	ModelRenderer leftlegup;
	ModelRenderer rightlegdown;
	ModelRenderer leftlegdown;
	ModelRenderer headwear;

	public SpiderZombieModel() {
		this(0);
	}

	public SpiderZombieModel(float modelSize) {
		texWidth = 64;
		texHeight = 64;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8, modelSize);
		head.setPos(0F, 9F, -5F);
		head.setTexSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4F, 0F, -2F, 8, 12, 4, modelSize);
		body.setPos(0F, 9F, -5F);
		body.setTexSize(64, 64);
		body.mirror = true;
		setRotation(body, 1.396263F, 0F, 0F);
		rightarm = new ModelRenderer(this, 40, 16);
		rightarm.addBox(-3F, -2F, -2F, 4, 15, 4, modelSize);
		rightarm.setPos(-5F, 11F, -4F);
		rightarm.setTexSize(64, 64);
		rightarm.mirror = true;
		setRotation(rightarm, 0F, 0F, 0F);
		leftarm = new ModelRenderer(this, 40, 16);
		leftarm.addBox(0F, -2F, -2F, 4, 15, 4, modelSize);
		leftarm.setPos(4F, 11F, -4F);
		leftarm.setTexSize(64, 64);
		setRotation(leftarm, 0F, 0F, 0F);
		rightlegup = new ModelRenderer(this, 0, 16);
		rightlegup.addBox(-2F, 0F, -2F, 4, 9, 4, modelSize);
		rightlegup.setPos(-2F, 12F, 6F);
		rightlegup.setTexSize(64, 64);
		rightlegup.mirror = true;
		setRotation(rightlegup, -0.7853982F, 0F, 0F);
		leftlegup = new ModelRenderer(this, 0, 16);
		leftlegup.addBox(-2F, 0F, -2F, 4, 9, 4, modelSize);
		leftlegup.setPos(2F, 12F, 6F);
		leftlegup.setTexSize(64, 64);
		leftlegup.mirror = true;
		setRotation(leftlegup, -0.7853982F, 0F, 0F);
		rightlegdown = new ModelRenderer(this, 0, 32);
		rightlegdown.addBox(-2F, 5F, 2F, 4, 4, 6);
		rightlegdown.setPos(-2F, 12F, 6F);
		rightlegdown.setTexSize(64, 64);
		rightlegdown.mirror = true;
		setRotation(rightlegdown, 0F, 0F, 0F);
		leftlegdown = new ModelRenderer(this, 0, 32);
		leftlegdown.addBox(-2F, 5F, 2F, 4, 4, 6);
		leftlegdown.setPos(2F, 12F, 6F);
		leftlegdown.setTexSize(64, 64);
		leftlegdown.mirror = true;
		setRotation(leftlegdown, 0F, 0F, 0F);
		headwear = new ModelRenderer(this, 32, 0);
		headwear.addBox(-4F, -8F, -4F, 8, 8, 8, modelSize + 0.5f);
		headwear.setPos(0F, 9F, -5F);
		headwear.setTexSize(64, 64);
		headwear.mirror = true;
		setRotation(headwear, 0F, 0F, 0F);

	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.leftlegup.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount - 0.75f;
		this.rightlegup.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount - 0.75f;
		this.rightarm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.75F;
		this.leftarm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.headwear.yRot = this.head.yRot;
		this.headwear.xRot = this.head.xRot;
		this.leftlegdown.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount - 0.75f;
		this.rightlegdown.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount - 0.75f;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		this.headParts().forEach((p_228228_8_) -> p_228228_8_.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
		this.bodyParts().forEach((p_228227_8_) -> p_228227_8_.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
	}

	public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public ModelRenderer getHead() {
		return this.head;
	}

	@Override
	public void translateToHand(HandSide hand, MatrixStack p_225599_2_) {
		(hand == HandSide.RIGHT ? this.rightarm : this.leftarm).translateAndRotate(p_225599_2_);
	}

	@Override
	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(this.body, this.rightarm, this.leftarm, this.rightlegup, this.leftlegup,
				this.rightlegdown, this.leftlegdown, this.headwear);
	}

}
