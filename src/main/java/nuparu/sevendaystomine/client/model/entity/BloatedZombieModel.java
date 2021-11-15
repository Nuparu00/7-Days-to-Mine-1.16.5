package nuparu.sevendaystomine.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import nuparu.sevendaystomine.entity.BloatedZombieEntity;

public class BloatedZombieModel<T extends BloatedZombieEntity> extends AgeableModel<T> implements IHasArm, IHasHead {
	public BipedModel.ArmPose leftArmPose = BipedModel.ArmPose.EMPTY;
	public BipedModel.ArmPose rightArmPose = BipedModel.ArmPose.EMPTY;
	public boolean crouching;
	public float swimAmount;

	public ModelRenderer body;
	public ModelRenderer shape1;
	public ModelRenderer shape2;
	public ModelRenderer shape11;
	public ModelRenderer shape4;
	public ModelRenderer shape9;
	public ModelRenderer shape10;
	public ModelRenderer shape3;
	public ModelRenderer shape12;
	public ModelRenderer leftArm;
	public ModelRenderer head;
	public ModelRenderer shape6;
	public ModelRenderer shape5;
	public ModelRenderer shape7;
	public ModelRenderer shape8;
	public ModelRenderer rightArm;
	public ModelRenderer shape13;
	public ModelRenderer leftLeg;
	public ModelRenderer shape14;
	public ModelRenderer shape15;
	public ModelRenderer rightLeg;
	public ModelRenderer shape16;
	public ModelRenderer shape17;

	public boolean isSneak;

	public BloatedZombieModel() {
		this(0);
	}

	public BloatedZombieModel(float scale) {
		texWidth = 64;
		texHeight = 64;

		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4F, 0F, -2F, 8, 12, 4, scale);
		body.setPos(0F, 0F, 0F);
		body.setTexSize(64, 64);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		shape1 = new ModelRenderer(this, 0, 33);
		shape1.addBox(-4F, 6F, -3F, 5, 5, 1, scale);
		shape1.setPos(0F, 0F, 0F);
		shape1.setTexSize(64, 64);
		shape1.mirror = true;
		setRotation(shape1, 0F, 0F, 0F);
		shape2 = new ModelRenderer(this, 0, 40);
		shape2.addBox(0F, 1F, 2F, 4, 5, 1, scale);
		shape2.setPos(0F, 0F, 0F);
		shape2.setTexSize(64, 64);
		shape2.mirror = true;
		setRotation(shape2, 0F, 0F, 0F);
		shape11 = new ModelRenderer(this, 0, 47);
		shape11.addBox(-3F, 3F, -3F, 3, 3, 1, scale);
		shape11.setPos(0F, 0F, 0F);
		shape11.setTexSize(64, 64);
		shape11.mirror = true;
		setRotation(shape11, 0F, 0F, 0F);
		shape4 = new ModelRenderer(this, 0, 52);
		shape4.addBox(-1F, 4F, -4F, 2, 2, 1, scale);
		shape4.setPos(0F, 0F, 0F);
		shape4.setTexSize(64, 64);
		shape4.mirror = true;
		setRotation(shape4, 0F, 0F, 0F);
		shape9 = new ModelRenderer(this, 0, 56);
		shape9.addBox(0F, 0F, 0F, 4, 5, 1, scale);
		shape9.setPos(0F, 0F, 0F);
		shape9.setTexSize(64, 64);
		shape9.mirror = true;
		setRotation(shape9, 0F, 0F, 0F);
		shape10 = new ModelRenderer(this, 13, 33);
		shape10.addBox(-4F, 7F, 2F, 4, 4, 1, scale);
		shape10.setPos(0F, 0F, 0F);
		shape10.setTexSize(64, 64);
		shape10.mirror = true;
		setRotation(shape10, 0F, 0F, 0F);
		shape3 = new ModelRenderer(this, 24, 33);
		shape3.addBox(0F, 2F, -3F, 4, 4, 1, scale);
		shape3.setPos(0F, 0F, 0F);
		shape3.setTexSize(64, 64);
		shape3.mirror = true;
		setRotation(shape3, 0F, 0F, 0F);
		shape12 = new ModelRenderer(this, 11, 40);
		shape12.addBox(1F, 7F, -3F, 3, 3, 1, scale);
		shape12.setPos(0F, 0F, 0F);
		shape12.setTexSize(64, 64);
		shape12.mirror = true;
		setRotation(shape12, 0F, 0F, 0F);
		leftArm = new ModelRenderer(this, 44, 45);
		leftArm.addBox(-1F, -2F, -2F, 4, 12, 4, scale);
		leftArm.setPos(5F, 2F, 0F);
		leftArm.setTexSize(64, 64);
		leftArm.mirror = true;
		setRotation(leftArm, 0F, 0F, 0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8, scale);
		head.setPos(0F, 0F, 0F);
		head.setTexSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		shape6 = new ModelRenderer(this, 11, 45);
		shape6.addBox(0.5F, -6.5F, 1.5F, 4, 5, 3, scale);
		shape6.setPos(0F, 0F, 0F);
		shape6.setTexSize(64, 64);
		shape6.mirror = true;
		setRotation(shape6, 0F, 0F, 0F);
		shape5 = new ModelRenderer(this, 11, 54);
		shape5.addBox(0.5F, -2.5F, -4.5F, 4, 3, 3, scale);
		shape5.setPos(0F, 0F, 0F);
		shape5.setTexSize(64, 64);
		shape5.mirror = true;
		setRotation(shape5, 0F, 0F, 0F);
		shape7 = new ModelRenderer(this, 35, 33);
		shape7.addBox(-4.466667F, -5F, -4.5F, 2, 4, 5, scale);
		shape7.setPos(0F, 0F, 0F);
		shape7.setTexSize(64, 64);
		shape7.mirror = true;
		setRotation(shape7, 0F, 0F, 0F);
		shape8 = new ModelRenderer(this, 33, 0);
		shape8.addBox(-1F, -8.5F, -4.5F, 4, 2, 4, scale);
		shape8.setPos(0F, 0F, 0F);
		shape8.setTexSize(64, 64);
		shape8.mirror = true;
		setRotation(shape8, 0F, 0F, 0F);
		rightArm = new ModelRenderer(this, 40, 16);
		rightArm.addBox(-3F, -2F, -2F, 4, 12, 4, scale);
		rightArm.setPos(-5F, 2F, 0F);
		rightArm.setTexSize(64, 64);
		rightArm.mirror = true;
		setRotation(rightArm, 0F, 0F, 0F);
		shape13 = new ModelRenderer(this, 33, 7);
		shape13.addBox(-3.5F, 0F, -1F, 1, 2, 2, scale);
		shape13.setPos(0F, 0F, 0F);
		shape13.setTexSize(64, 64);
		shape13.mirror = true;
		setRotation(shape13, 0F, 0F, 0F);
		leftLeg = new ModelRenderer(this, 0, 16);
		leftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
		leftLeg.setPos(2F, 12F, 0F);
		leftLeg.setTexSize(64, 64);
		leftLeg.mirror = true;
		setRotation(leftLeg, 0F, 0F, 0F);
		shape14 = new ModelRenderer(this, 40, 7);
		shape14.addBox(0.2F, 2F, -2.5F, 2, 2, 1, scale);
		shape14.setPos(0F, 0F, 0F);
		shape14.setTexSize(64, 64);
		shape14.mirror = true;
		setRotation(shape14, 0F, 0F, 0F);
		shape15 = new ModelRenderer(this, 40, 11);
		shape15.addBox(1.4F, 5F, 0.2F, 1, 2, 2, scale);
		shape15.setPos(0F, 0F, 0F);
		shape15.setTexSize(64, 64);
		shape15.mirror = true;
		setRotation(shape15, 0F, 0F, 0F);
		rightLeg = new ModelRenderer(this, 27, 45);
		rightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
		rightLeg.setPos(-2F, 12F, 0F);
		rightLeg.setTexSize(64, 64);
		rightLeg.mirror = true;
		setRotation(rightLeg, 0F, 0F, 0F);
		shape16 = new ModelRenderer(this, 50, 0);
		shape16.addBox(0F, 6F, -2.5F, 2, 2, 1, scale);
		shape16.setPos(0F, 0F, 0F);
		shape16.setTexSize(64, 64);
		shape16.mirror = true;
		setRotation(shape16, 0F, 0F, 0F);
		shape17 = new ModelRenderer(this, 50, 4);
		shape17.addBox(-1F, 3F, 1.5F, 2, 2, 1, scale);
		shape17.setPos(0F, 0F, 0F);
		shape17.setTexSize(64, 64);
		shape17.mirror = true;
		setRotation(shape17, 0F, 0F, 0F);

		body.addChild(shape1);
		body.addChild(shape2);
		body.addChild(shape3);
		body.addChild(shape4);
		body.addChild(shape9);
		body.addChild(shape10);
		body.addChild(shape11);
		body.addChild(shape12);

		head.addChild(shape5);
		head.addChild(shape6);
		head.addChild(shape7);
		head.addChild(shape8);

		rightArm.addChild(shape13);

		leftLeg.addChild(shape14);
		leftLeg.addChild(shape15);

		rightLeg.addChild(shape16);
		rightLeg.addChild(shape17);

	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		boolean flag = entity.getFallFlyingTicks() > 4;
		boolean flag1 = entity.isVisuallySwimming();
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		if (flag) {
			this.head.xRot = (-(float) Math.PI / 4F);
		} else if (this.swimAmount > 0.0F) {
			if (flag1) {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (-(float) Math.PI / 4F));
			} else {
				this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, headPitch * ((float) Math.PI / 180F));
			}
		} else {
			this.head.xRot = headPitch * ((float) Math.PI / 180F);
		}

		this.body.yRot = 0.0F;
		this.rightArm.z = 0.0F;
		this.rightArm.x = -5.0F;
		this.leftArm.z = 0.0F;
		this.leftArm.x = 5.0F;
		float f = 1.0F;
		if (flag) {
			f = (float) entity.getDeltaMovement().lengthSqr();
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
		this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.zRot = 0.0F;
		this.leftLeg.zRot = 0.0F;
		if (this.riding) {
			this.rightArm.xRot += (-(float) Math.PI / 5F);
			this.leftArm.xRot += (-(float) Math.PI / 5F);
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float) Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		}

		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		boolean flag2 = entity.getMainArm() == HandSide.RIGHT;
		boolean flag3 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
		if (flag2 != flag3) {
			this.poseLeftArm(entity);
			this.poseRightArm(entity);
		} else {
			this.poseRightArm(entity);
			this.poseLeftArm(entity);
		}

		this.setupAttackAnimation(entity, ageInTicks);
		if (this.crouching) {
			this.body.xRot = 0.5F;
			this.rightArm.xRot += 0.4F;
			this.leftArm.xRot += 0.4F;
			this.rightLeg.z = 4.0F;
			this.leftLeg.z = 4.0F;
			this.rightLeg.y = 12.2F;
			this.leftLeg.y = 12.2F;
			this.head.y = 4.2F;
			this.body.y = 3.2F;
			this.leftArm.y = 5.2F;
			this.rightArm.y = 5.2F;
		} else {
			this.body.xRot = 0.0F;
			this.rightLeg.z = 0.1F;
			this.leftLeg.z = 0.1F;
			this.rightLeg.y = 12.0F;
			this.leftLeg.y = 12.0F;
			this.head.y = 0.0F;
			this.body.y = 0.0F;
			this.leftArm.y = 2.0F;
			this.rightArm.y = 2.0F;
		}

		ModelHelper.bobArms(this.rightArm, this.leftArm, ageInTicks);
		if (this.swimAmount > 0.0F) {
			float f1 = limbSwing % 26.0F;
			HandSide handside = this.getAttackArm(entity);
			float f2 = handside == HandSide.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			float f3 = handside == HandSide.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
			if (f1 < 14.0F) {
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, 0.0F);
				this.rightArm.xRot = MathHelper.lerp(f2, this.rightArm.xRot, 0.0F);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float) Math.PI);
				this.rightArm.yRot = MathHelper.lerp(f2, this.rightArm.yRot, (float) Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot,
						(float) Math.PI + 1.8707964F * this.quadraticArmUpdate(f1) / this.quadraticArmUpdate(14.0F));
				this.rightArm.zRot = MathHelper.lerp(f2, this.rightArm.zRot,
						(float) Math.PI - 1.8707964F * this.quadraticArmUpdate(f1) / this.quadraticArmUpdate(14.0F));
			} else if (f1 >= 14.0F && f1 < 22.0F) {
				float f6 = (f1 - 14.0F) / 8.0F;
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot, ((float) Math.PI / 2F) * f6);
				this.rightArm.xRot = MathHelper.lerp(f2, this.rightArm.xRot, ((float) Math.PI / 2F) * f6);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float) Math.PI);
				this.rightArm.yRot = MathHelper.lerp(f2, this.rightArm.yRot, (float) Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, 5.012389F - 1.8707964F * f6);
				this.rightArm.zRot = MathHelper.lerp(f2, this.rightArm.zRot, 1.2707963F + 1.8707964F * f6);
			} else if (f1 >= 22.0F && f1 < 26.0F) {
				float f4 = (f1 - 22.0F) / 4.0F;
				this.leftArm.xRot = this.rotlerpRad(f3, this.leftArm.xRot,
						((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f4);
				this.rightArm.xRot = MathHelper.lerp(f2, this.rightArm.xRot,
						((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f4);
				this.leftArm.yRot = this.rotlerpRad(f3, this.leftArm.yRot, (float) Math.PI);
				this.rightArm.yRot = MathHelper.lerp(f2, this.rightArm.yRot, (float) Math.PI);
				this.leftArm.zRot = this.rotlerpRad(f3, this.leftArm.zRot, (float) Math.PI);
				this.rightArm.zRot = MathHelper.lerp(f2, this.rightArm.zRot, (float) Math.PI);
			}

			float f7 = 0.3F;
			float f5 = 0.33333334F;
			this.leftLeg.xRot = MathHelper.lerp(this.swimAmount, this.leftLeg.xRot,
					0.3F * MathHelper.cos(limbSwing * 0.33333334F + (float) Math.PI));
			this.rightLeg.xRot = MathHelper.lerp(this.swimAmount, this.rightLeg.xRot,
					0.3F * MathHelper.cos(limbSwing * 0.33333334F));
		}
	}

	private void poseRightArm(T p_241654_1_) {
		switch (this.rightArmPose) {
		case EMPTY:
			this.rightArm.yRot = 0.0F;
			break;
		case BLOCK:
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
			this.rightArm.yRot = (-(float) Math.PI / 6F);
			break;
		case ITEM:
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
			this.rightArm.yRot = 0.0F;
			break;
		case THROW_SPEAR:
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
			this.rightArm.yRot = 0.0F;
			break;
		case BOW_AND_ARROW:
			this.rightArm.yRot = -0.1F + this.head.yRot;
			this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
			this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			break;
		case CROSSBOW_CHARGE:
			ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, p_241654_1_, true);
			break;
		case CROSSBOW_HOLD:
			ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
		}

	}

	private void poseLeftArm(T p_241655_1_) {
		switch (this.leftArmPose) {
		case EMPTY:
			this.leftArm.yRot = 0.0F;
			break;
		case BLOCK:
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
			this.leftArm.yRot = ((float) Math.PI / 6F);
			break;
		case ITEM:
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
			this.leftArm.yRot = 0.0F;
			break;
		case THROW_SPEAR:
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
			this.leftArm.yRot = 0.0F;
			break;
		case BOW_AND_ARROW:
			this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
			this.leftArm.yRot = 0.1F + this.head.yRot;
			this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			break;
		case CROSSBOW_CHARGE:
			ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, p_241655_1_, false);
			break;
		case CROSSBOW_HOLD:
			ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
		}

	}

	protected void setupAttackAnimation(T p_230486_1_, float p_230486_2_) {
		if (!(this.attackTime <= 0.0F)) {
			HandSide handside = this.getAttackArm(p_230486_1_);
			ModelRenderer modelrenderer = this.getArm(handside);
			float f = this.attackTime;
			this.body.yRot = MathHelper.sin(MathHelper.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
			if (handside == HandSide.LEFT) {
				this.body.yRot *= -1.0F;
			}

			this.rightArm.z = MathHelper.sin(this.body.yRot) * 5.0F;
			this.rightArm.x = -MathHelper.cos(this.body.yRot) * 5.0F;
			this.leftArm.z = -MathHelper.sin(this.body.yRot) * 5.0F;
			this.leftArm.x = MathHelper.cos(this.body.yRot) * 5.0F;
			this.rightArm.yRot += this.body.yRot;
			this.leftArm.yRot += this.body.yRot;
			this.leftArm.xRot += this.body.yRot;
			f = 1.0F - this.attackTime;
			f = f * f;
			f = f * f;
			f = 1.0F - f;
			float f1 = MathHelper.sin(f * (float) Math.PI);
			float f2 = MathHelper.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
			modelrenderer.xRot = (float) ((double) modelrenderer.xRot - ((double) f1 * 1.2D + (double) f2));
			modelrenderer.yRot += this.body.yRot * 2.0F;
			modelrenderer.zRot += MathHelper.sin(this.attackTime * (float) Math.PI) * -0.4F;
		}
	}

	protected float rotlerpRad(float p_205060_1_, float p_205060_2_, float p_205060_3_) {
		float f = (p_205060_3_ - p_205060_2_) % ((float) Math.PI * 2F);
		if (f < -(float) Math.PI) {
			f += ((float) Math.PI * 2F);
		}

		if (f >= (float) Math.PI) {
			f -= ((float) Math.PI * 2F);
		}

		return p_205060_2_ + p_205060_1_ * f;
	}

	private float quadraticArmUpdate(float p_203068_1_) {
		return -65.0F * p_203068_1_ + p_203068_1_ * p_203068_1_;
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
		(hand == HandSide.RIGHT ? this.rightArm : this.leftArm).translateAndRotate(p_225599_2_);
	}

	protected HandSide getAttackArm(T p_217147_1_) {
		HandSide handside = p_217147_1_.getMainArm();
		return p_217147_1_.swingingArm == Hand.MAIN_HAND ? handside : handside.getOpposite();
	}

	protected ModelRenderer getArm(HandSide p_187074_1_) {
		return p_187074_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
	}

	@Override
	protected Iterable<ModelRenderer> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelRenderer> bodyParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.body,
				this.shape1, this.shape2, this.shape11, this.shape4, this.shape9, this.shape10, this.shape3,
				this.shape6, this.shape5, this.shape7, this.shape8, this.shape13, this.shape14, this.shape15,
				this.shape16, this.shape17);
	}

}
