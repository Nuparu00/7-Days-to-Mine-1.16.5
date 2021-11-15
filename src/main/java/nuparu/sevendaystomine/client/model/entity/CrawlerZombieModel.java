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
import nuparu.sevendaystomine.entity.CrawlerZombieEntity;

public class CrawlerZombieModel<T extends CrawlerZombieEntity> extends AgeableModel<T> implements IHasArm, IHasHead {
	ModelRenderer head;
	ModelRenderer body;
	ModelRenderer rightarm;
	ModelRenderer leftarm;
	ModelRenderer rightleg;
	ModelRenderer leftleg;

	public CrawlerZombieModel() {
		texWidth = 64;
		texHeight = 64;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setPos(0F, 23F, -7F);
		head.setTexSize(64, 64);
		head.mirror = true;
		setRotation(head, 0.1115358F, 0F, 0F);
		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4F, 0F, -2F, 8, 12, 4);
		body.setPos(0F, 22F, -7F);
		body.setTexSize(64, 64);
		body.mirror = true;
		setRotation(body, 1.570796F, 0F, 0F);
		rightarm = new ModelRenderer(this, 40, 32);
		rightarm.addBox(-3F, -2F, -2F, 4, 12, 4);
		rightarm.setPos(-5F, 22F, -5F);
		rightarm.setTexSize(64, 64);
		rightarm.mirror = true;
		setRotation(rightarm, -1.607975F, 0.1487144F, 0.2602503F);
		leftarm = new ModelRenderer(this, 40, 16);
		leftarm.addBox(-1F, -2F, -2F, 4, 12, 4);
		leftarm.setPos(5F, 22F, -5F);
		leftarm.setTexSize(64, 64);
		leftarm.mirror = true;
		setRotation(leftarm, -1.45926F, -0.1487144F, -0.2602503F);
		rightleg = new ModelRenderer(this, 0, 16);
		rightleg.addBox(-2F, 0F, -2F, 4, 12, 4);
		rightleg.setPos(-2F, 22F, 5F);
		rightleg.setTexSize(64, 64);
		rightleg.mirror = true;
		setRotation(rightleg, 1.719511F, -0.1115358F, 0F);
		leftleg = new ModelRenderer(this, 0, 32);
		leftleg.addBox(-2F, 0F, -2F, 4, 7, 4);
		leftleg.setPos(2F, 22F, 5F);
		leftleg.setTexSize(64, 64);
		leftleg.mirror = true;
		setRotation(leftleg, 1.607975F, 0.1487144F, 0F);

	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.leftarm.xRot = MathHelper.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount - 90;
		this.rightarm.xRot = MathHelper.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount - 90;
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
		return ImmutableList.of(this.body, this.rightarm, this.leftarm, this.rightleg, this.leftleg);
	}

}
