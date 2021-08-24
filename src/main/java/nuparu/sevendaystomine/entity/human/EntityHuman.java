package nuparu.sevendaystomine.entity.human;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.Event;
import nuparu.sevendaystomine.entity.human.dialogue.DialogueDataManager;
import nuparu.sevendaystomine.events.HumanResponseEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.AddSubtitleMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.dialogue.Dialogues;
import nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

public class EntityHuman extends CreatureEntity {
	// Should not be used outside of the Entity class - especially on client side!!
	private EnumSex sex = EnumSex.UNKNOWN;
	private ResourceLocation res = new ResourceLocation("textures/entity/steve.png");

	private static final DataParameter<Dialogues> DIALOGUES = EntityDataManager.<Dialogues>defineId(EntityHuman.class,
			Utils.DIALOGUES);

	private static final DataParameter<String> CURRENT_DIALOGUE = EntityDataManager.<String>defineId(EntityHuman.class,
			DataSerializers.STRING);

	private static final DataParameter<String> TEXTURE = EntityDataManager.<String>defineId(EntityHuman.class,
			DataSerializers.STRING);

	private static final DataParameter<String> SEX = EntityDataManager.<String>defineId(EntityHuman.class,
			DataSerializers.STRING);

	public EntityHuman(EntityType<? extends EntityHuman> type, World world) {
		super(type, world);
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DIALOGUES, Dialogues.EMPTY);
		this.entityData.define(CURRENT_DIALOGUE, String.valueOf("none"));
		this.entityData.define(TEXTURE, String.valueOf("textures/entity/steve.png"));
		this.entityData.define(SEX, String.valueOf(EnumSex.UNKNOWN.getName()));
	}

	public void setDialogues(Dialogues dialogues) {
		this.entityData.set(DIALOGUES, dialogues);
	}

	public Dialogues getDialogues() {
		return this.entityData.get(DIALOGUES);
	}

	public void setCurrentDialogue(String dialogues) {
		this.entityData.set(CURRENT_DIALOGUE, dialogues);
	}

	public String getCurrentDialogue() {
		return this.entityData.get(CURRENT_DIALOGUE);
	}

	public void setTexture(String tex) {
		this.entityData.set(TEXTURE, tex);
		this.setRes(new ResourceLocation(tex));
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	public void setSex(String sex) {
		this.entityData.set(SEX, sex);
		this.sex = EnumSex.getByName(sex);
	}

	public String getSex() {
		return this.entityData.get(SEX);
	}

	public EnumSex getSexAsEnum() {
		return this.sex;
	}

	public boolean canTalkTo(PlayerEntity player) {
		return true;
	}

	public void onDialogue(String dialogue, PlayerEntity player) {
		Event event = new HumanResponseEvent(this, player, dialogue);
		if (!MinecraftForge.EVENT_BUS.post(event)) {
			if (!this.level.isClientSide()) {
				PacketManager.sendTo(PacketManager.addSubtitle,new AddSubtitleMessage(this, dialogue, 40), (ServerPlayerEntity) player);
			}
		}
	}

	public void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
		if (!getSex().isEmpty()) {
			nbt.putString("sex", getSex());
		}
		if (!getTexture().isEmpty()) {
			nbt.putString("texture", getTexture());
		}
		if (!getCurrentDialogue().isEmpty()) {
			nbt.putString("current_dialogue", getCurrentDialogue());
		}
		if (getDialogues() != null) {
			nbt.putString("dialogues", getDialogues().getKey().toString());
		}
	}

	public void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("sex", Constants.NBT.TAG_STRING)) {
			setSex(nbt.getString("sex"));
		}
		if (nbt.contains("texture", Constants.NBT.TAG_STRING)) {
			setTexture(nbt.getString("texture"));
		}
		if (nbt.contains("current_dialogue", Constants.NBT.TAG_STRING)) {
			setCurrentDialogue(nbt.getString("current_dialogue"));
		}
		if (nbt.contains("dialogues", Constants.NBT.TAG_STRING)) {
			setDialogues(DialogueDataManager.instance.get(new ResourceLocation(nbt.getString("dialogues"))));
		}
	}

	public void attack(Entity p_71059_1_) {
		if (p_71059_1_.isAttackable()) {
			if (!p_71059_1_.skipAttackInteraction(this)) {
				float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
				float f1;
				if (p_71059_1_ instanceof LivingEntity) {
					f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)p_71059_1_).getMobType());
				} else {
					f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), CreatureAttribute.UNDEFINED);
				}

				float f2 = 1;
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = f2 > 0.9F;
					boolean flag1 = false;
					int i = 0;
					i = i + EnchantmentHelper.getKnockbackBonus(this);
					if (this.isSprinting() && flag) {
						this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, this.getSoundSource(), 1.0F, 1.0F);
						++i;
						flag1 = true;
					}

					boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.onClimbable() && !this.isInWater() && !this.hasEffect(Effects.BLINDNESS) && !this.isPassenger() && p_71059_1_ instanceof LivingEntity;
					flag2 = flag2 && !this.isSprinting();

					f = f + f1;
					boolean flag3 = false;
					double d0 = (double)(this.walkDist - this.walkDistO);
					if (flag && !flag2 && !flag1 && this.onGround && d0 < (double)this.getSpeed()) {
						ItemStack itemstack = this.getItemInHand(Hand.MAIN_HAND);
						if (itemstack.getItem() instanceof SwordItem) {
							flag3 = true;
						}
					}

					float f4 = 0.0F;
					boolean flag4 = false;
					int j = EnchantmentHelper.getFireAspect(this);
					if (p_71059_1_ instanceof LivingEntity) {
						f4 = ((LivingEntity)p_71059_1_).getHealth();
						if (j > 0 && !p_71059_1_.isOnFire()) {
							flag4 = true;
							p_71059_1_.setSecondsOnFire(1);
						}
					}

					Vector3d vector3d = p_71059_1_.getDeltaMovement();
					boolean flag5 = p_71059_1_.hurt(DamageSource.mobAttack(this), f);
					if (flag5) {
						if (i > 0) {
							if (p_71059_1_ instanceof LivingEntity) {
								((LivingEntity)p_71059_1_).knockback((float)i * 0.5F, (double) MathHelper.sin(this.yRot * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(this.yRot * ((float)Math.PI / 180F))));
							} else {
								p_71059_1_.push((double)(-MathHelper.sin(this.yRot * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.yRot * ((float)Math.PI / 180F)) * (float)i * 0.5F));
							}

							this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
							this.setSprinting(false);
						}

						if (flag3) {
							float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * f;

							for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, p_71059_1_.getBoundingBox().inflate(1.0D, 0.25D, 1.0D))) {
								if (livingentity != this && livingentity != p_71059_1_ && !this.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).isMarker()) && this.distanceToSqr(livingentity) < 9.0D) {
									livingentity.knockback(0.4F, (double)MathHelper.sin(this.yRot * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(this.yRot * ((float)Math.PI / 180F))));
									livingentity.hurt(DamageSource.mobAttack(this), f3);
								}
							}

							this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, this.getSoundSource(), 1.0F, 1.0F);
						}

						if (p_71059_1_ instanceof ServerPlayerEntity && p_71059_1_.hurtMarked) {
							((ServerPlayerEntity)p_71059_1_).connection.send(new SEntityVelocityPacket(p_71059_1_));
							p_71059_1_.hurtMarked = false;
							p_71059_1_.setDeltaMovement(vector3d);
						}

						if (flag2) {
							this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 1.0F, 1.0F);
						}

						if (!flag2 && !flag3) {
							if (flag) {
								this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, this.getSoundSource(), 1.0F, 1.0F);
							} else {
								this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, this.getSoundSource(), 1.0F, 1.0F);
							}
						}

						this.setLastHurtMob(p_71059_1_);
						if (p_71059_1_ instanceof LivingEntity) {
							EnchantmentHelper.doPostHurtEffects((LivingEntity)p_71059_1_, this);
						}

						EnchantmentHelper.doPostDamageEffects(this, p_71059_1_);
						ItemStack itemstack1 = this.getMainHandItem();
						Entity entity = p_71059_1_;
						if (p_71059_1_ instanceof net.minecraftforge.entity.PartEntity) {
							entity = ((net.minecraftforge.entity.PartEntity<?>) p_71059_1_).getParent();
						}

						if (!this.level.isClientSide && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
							ItemStack copy = itemstack1.copy();
							itemstack1.hurt(1,random,null);
							if (itemstack1.isEmpty()) {
								this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
							}
						}

						if (p_71059_1_ instanceof LivingEntity) {
							float f5 = f4 - ((LivingEntity)p_71059_1_).getHealth();
							if (j > 0) {
								p_71059_1_.setSecondsOnFire(j * 4);
							}

							if (this.level instanceof ServerWorld && f5 > 2.0F) {
								int k = (int)((double)f5 * 0.5D);
								((ServerWorld)this.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, p_71059_1_.getX(), p_71059_1_.getY(0.5D), p_71059_1_.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
							}
						}

					} else {
						this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, this.getSoundSource(), 1.0F, 1.0F);
						if (flag4) {
							p_71059_1_.clearFire();
						}
					}
				}

			}
		}
	}


	public ResourceLocation getRes() {
		return res;
	}

	public void setRes(ResourceLocation res) {
		this.res = res;
	}

	protected SoundEvent getSwimSound() {
		return SoundEvents.PLAYER_SWIM;
	}

	protected SoundEvent getSwimSplashSound() {
		return SoundEvents.PLAYER_SPLASH;
	}

	protected SoundEvent getSwimHighSpeedSplashSound() {
		return SoundEvents.PLAYER_SPLASH_HIGH_SPEED;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
		return ModSounds.HUMAN_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.PLAYER_DEATH;
	}

	public enum EnumSex {
		MALE("male"), FEMALE("female"), UNKNOWN("unknown");

		private String name;

		EnumSex(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static EnumSex getByName(String name) {
			for (EnumSex sex : EnumSex.values()) {
				if (sex.name.equals(name)) {
					return sex;
				}
			}
			return UNKNOWN;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
