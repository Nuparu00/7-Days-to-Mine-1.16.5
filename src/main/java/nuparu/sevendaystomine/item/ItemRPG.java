package nuparu.sevendaystomine.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.RocketEntity;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModEnchantments;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.ApplyRecoilMessage;

public class ItemRPG extends ItemGun {

	public ItemRPG() {
		super();
		this.setMaxAmmo(1);
		this.setFullDamage(50f);
		this.setSpeed(2f);
		this.setRecoil(14f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(5000);
		this.setDelay(2);
		this.setType(EnumGun.LAUNCHER);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
		this.setIdleAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_idle"));
		this.setShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rpg_shoot"));
		this.setReloadAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"pistol_reload"));
		this.setAimAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_aim_idle"));
		this.setAimShootAnimationKey(new ResourceLocation(SevenDaysToMine.MODID,"rifle_aim_idle"));
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.ROCKET.get();
	}

	@Override
	public float getShotSoundVolume() {
		return 1F;
	}

	@Override
	public float getShotSoundPitch() {
		return 1F / (random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F;
	}

	@Override
	public SoundEvent getReloadSound() {
		return ModSounds.HUNTING_RIFLE_RELOAD.get();
	}

	@Override
	public SoundEvent getShotSound() {
		return ModSounds.HUNTING_RIFLE_SHOT.get();
	}

	@Override
	public SoundEvent getDrySound() {
		return ModSounds.PISTOL_DRYSHOT.get();
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {

		ItemStack itemstack = playerIn.getItemInHand(handIn);

		/*
		 * if (handIn == Hand.OFF_HAND && (gunWield != EnumWield.DUAL &&
		 * playerIn.getMainHandItem() != ItemStack.EMPTY)) { return new
		 * ActionResult<ItemStack>(ActionResult.FAIL, itemstack); }
		 */

		if ((getWield() == EnumWield.TWO_HAND && !playerIn.getItemInHand(getOtherHand(handIn)).isEmpty())) {
			return ActionResult.fail(itemstack);
		}
		if (itemstack.isEmpty() || itemstack.getOrCreateTag() == null) {
			return ActionResult.fail(itemstack);
		}
		CompoundNBT nbt = itemstack.getOrCreateTag();
		if (!nbt.contains("Ammo") || !nbt.contains("Capacity") || !nbt.contains("NextFire")) {
			return ActionResult.fail(itemstack);
		}
		if (itemstack.getOrCreateTag().getLong("NextFire") > worldIn.getGameTime()
				|| itemstack.getOrCreateTag().getBoolean("Reloading")) {
			return ActionResult.fail(itemstack);
		}

		int ammo = nbt.getInt("Ammo");
		boolean flag = playerIn.isCreative();
		if (ammo > 0 || flag) {
			float velocity = getSpeed() * (1f+((float)getQuality(itemstack) / (float)CommonConfig.maxQuality.get()));
			boolean sparking = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SPARKING.get(), itemstack) != 0;
			for (int i = 0; i <  getProjectiles()*(EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MULTISHOT.get(), itemstack)+1); i++) {
			/*	EntityRocket shot = new EntityRocket(worldIn, playerIn, velocity, ((float) getSpread(playerIn, handIn) / (playerIn.isCrouching() ? 1.5f : 1f)));
				if (!worldIn.isClientSide()) {
					shot.setDamage(getFinalDamage(itemstack));
					worldIn.addFreshEntity(shot);
				}*/

				RocketEntity shot = new RocketEntity(playerIn,worldIn);
				float spread = ((float) getSpread(playerIn, handIn) / (playerIn.isCrouching() ? 1.5f : 1f));
				shot.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, velocity,spread*23.5f);
				shot.setSparking(sparking);
				if (!worldIn.isClientSide()) {
					shot.setDamage(getFinalDamage(itemstack));
					worldIn.addFreshEntity(shot);
				}
				Vector3d lookVector = playerIn.getLookAngle();
				playerIn.setDeltaMovement(playerIn.getDeltaMovement().subtract(lookVector).multiply(0.1,0.1,0.1));
			}
			if(playerIn instanceof ServerPlayerEntity) {
			itemstack.hurt(1,worldIn.random, (ServerPlayerEntity) playerIn);
			}
			worldIn.playSound(null, playerIn.blockPosition(), getShotSound(), SoundCategory.PLAYERS, getShotSoundVolume(),
					getShotSoundPitch());
			//playerIn.swing(handIn);
			if (playerIn instanceof ServerPlayerEntity) {
				PacketManager.sendTo(PacketManager.applyRecoil,new ApplyRecoilMessage(getRecoil(),handIn==Hand.MAIN_HAND, false), (ServerPlayerEntity) playerIn);
			}


			if (!flag) {
				itemstack.getOrCreateTag().putInt("Ammo", ammo - 1);
			}

			itemstack.getOrCreateTag().putLong("NextFire", worldIn.getGameTime() + getDelay());

			return ActionResult.success(itemstack);
		} else {
			worldIn.playSound(null, playerIn.blockPosition(), getDrySound(), SoundCategory.PLAYERS, 0.3F,
					1.0F / (random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			itemstack.getOrCreateTag().putLong("NextFire", worldIn.getGameTime() + (getDelay() / 2));
		}
		return ActionResult.fail(itemstack);
	}

}
