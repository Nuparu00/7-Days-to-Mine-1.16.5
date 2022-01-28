package nuparu.sevendaystomine.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.client.animation.Animation;
import nuparu.sevendaystomine.client.animation.Animations;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.init.ModEnchantments;
import nuparu.sevendaystomine.entity.ShotEntity;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.ApplyRecoilMessage;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings({"unused"})
public class ItemGun extends Item implements IQuality, IReloadable {

    private SoundEvent shotSound = null;
    private SoundEvent drySound = null;
    private SoundEvent reloadSound = null;

    private int maxAmmo = 0;
    private float fullDamage = 0f;
    private float speed = 0f;

    private float recoil = 0f;
    private float counterDef = 0f;

    private float cross = 10F;
    private float spread = 10f;
    private int reloadTime = 1500;
    private int delay = 0;
    private float fovFactor = 1;
    private boolean scoped = false;

    private int projectiles = 1;
    private int shots = 1;

    private EnumGun type;
    private EnumLength length;
    private EnumWield wield;
    private Vector3d aimPosition = new Vector3d(0, 0, 0);

    private Animation idleAnimation;
    private Animation shootAnimation;
    private Animation reloadAnimation;
    private Animation aimAnimation;
    private Animation aimShootAnimation;

    private ResourceLocation idleAnimationKey;
    private ResourceLocation shootAnimationKey;
    private ResourceLocation reloadAnimationKey;
    private ResourceLocation aimAnimationKey;
    private ResourceLocation aimShootAnimationKey;


    public ItemGun() {
        super(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_COMBAT).setNoRepair());
    }

    public Item getReloadItem(ItemStack stack) {
        return null;
    }

    public SoundEvent getReloadSound() {
        return reloadSound;
    }

    public ItemGun setReloadSound(SoundEvent reloadSound) {
        this.reloadSound = reloadSound;
        return this;
    }

    public SoundEvent getShotSound() {
        return shotSound;
    }

    public ItemGun setShotSound(SoundEvent shotSound) {
        this.shotSound = shotSound;
        return this;
    }

    public SoundEvent getDrySound() {
        return drySound;
    }

    public ItemGun setDrySound(SoundEvent drySound) {
        this.drySound = drySound;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public ItemGun setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public float getRecoil() {
        return recoil;
    }

    public ItemGun setRecoil(float recoil) {
        this.recoil = recoil;
        return this;
    }

    public float getCross() {
        return cross;
    }

    public ItemGun setCross(float cross) {
        this.spread = cross * 3.14f;
        this.cross = cross;
        return this;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public ItemGun setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
        return this;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public ItemGun setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
        return this;
    }

    public int getReloadTime(ItemStack stack) {
        return (int) Math.ceil((float) getReloadTime()
                * (1f - EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.FAST_RELOAD.get(), stack) / 10f));
    }

    public float getFullDamage() {
        return fullDamage;
    }

    public ItemGun setFullDamage(float fullDamage) {
        this.fullDamage = fullDamage;
        return this;
    }

    public EnumGun getType() {
        return type;
    }

    public ItemGun setType(EnumGun type) {
        this.type = type;
        return this;
    }

    public EnumLength getLength() {
        return length;
    }

    public ItemGun setLength(EnumLength length) {
        this.length = length;
        return this;
    }

    public EnumWield getWield() {
        return wield;
    }

    public ItemGun setWield(EnumWield wield) {
        this.wield = wield;
        return this;
    }

    public float getFinalDamage(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
            return getFullDamage();
        }
        CompoundNBT nbt = stack.getOrCreateTag();
        if (EnumQualityState.isQualitySystemOn() && nbt != null) {
            if (nbt.contains("Quality")) {
                return getFullDamage() * ((float) nbt.getInt("Quality") / (float) CommonConfig.maxQuality.get());
            }
        }
        return getFullDamage();
    }

    public int getQuality(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt != null) {
            if (nbt.contains("Quality")) {
                return nbt.getInt("Quality");
            }
        }
        return 0;
    }

    public EnumQuality getQualityTierFromStack(ItemStack stack) {
        return getQualityTierFromInt(getQuality(stack));
    }

    public EnumQuality getQualityTierFromInt(int quality) {
        return EnumQuality.getFromQuality(quality);
    }

    public void setQuality(ItemStack stack, int quality) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putInt("Quality", quality);
    }

    @Override
    public void onCraftedBy(ItemStack itemstack, World world, PlayerEntity player) {
        if (this.getQuality(itemstack) <= 0) {
            setQuality(itemstack,
                    (int) Math.min(Math.floor(player.totalExperience / CommonConfig.xpPerQuality.get()),
                            CommonConfig.maxQuality.get()));
        }
        initNBT(itemstack);
    }

    public void initNBT(ItemStack itemstack) {
        CompoundNBT nbt = itemstack.getOrCreateTag();
        nbt.putInt("Capacity", getMaxAmmo());
        nbt.putInt("Ammo", 0);
        nbt.putInt("ReloadTime", 90000);
        nbt.putBoolean("Reloading", false);
        nbt.putLong("NextFire", 0);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        int i = 0;
        if (stack.getOrCreateTag() != null) {
            i = getQuality(stack);
        }
        return super.getMaxDamage(stack) + i;
    }

    @Override
    public ITextComponent getName(ItemStack itemstack) {
        ITextComponent textComponent = super.getName(itemstack);
        if (!EnumQualityState.isQualitySystemOn()) {
            return textComponent;
        }
        EnumQuality quality = getQualityTierFromStack(itemstack);
        StringTextComponent stringTextComponent = new StringTextComponent(textComponent.getString());
        stringTextComponent.setStyle(textComponent.getStyle().withColor(quality.color));
        return stringTextComponent;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                                ITooltipFlag flagIn) {
        if (EnumQualityState.isQualitySystemOn()) {
            int quality = getQuality(stack);
            EnumQuality tier = getQualityTierFromInt(quality);
            TranslationTextComponent qualityTitle = new TranslationTextComponent(
                    "stat.quality." + tier.name().toLowerCase());
            IFormattableTextComponent qualityValue = new TranslationTextComponent("stat.quality", quality);

            Style style = qualityTitle.getStyle().withColor(tier.color);
            qualityTitle.setStyle(style);
            qualityValue.setStyle(style);
            tooltip.add(qualityTitle);
            tooltip.add(qualityValue);
        }

        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt != null && nbt.contains("Ammo") && nbt.contains("Capacity")) {

            TranslationTextComponent ammo = new TranslationTextComponent("stat.ammo", nbt.getInt("Ammo"));
            Style style = ammo.getStyle().withColor(TextFormatting.YELLOW);

            TranslationTextComponent capacity = new TranslationTextComponent("stat.capacity",
                    nbt.getInt("Capacity"));
            ammo.setStyle(style);
            capacity.setStyle(style);
            tooltip.add(ammo);
            tooltip.add(capacity);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab)) {
            PlayerEntity player = Minecraft.getInstance().player;
            ItemStack stack = new ItemStack(this, 1);
            if (player != null) {
                setQuality(stack,
                        (int) Math.min(
                                Math.max(Math.floor(player.totalExperience / CommonConfig.xpPerQuality.get()), 1),
                                CommonConfig.maxQuality.get()));
                stack.getOrCreateTag().putInt("Capacity", getMaxAmmo());
                stack.getOrCreateTag().putInt("Ammo", 0);
                stack.getOrCreateTag().putInt("ReloadTime", 90000);
                stack.getOrCreateTag().putBoolean("Reloading", false);
                stack.getOrCreateTag().putLong("NextFire", 0);
            }
            items.add(stack);
        }
    }

    public Hand getOtherHand(Hand original) {
        if (original == Hand.MAIN_HAND) {
            return Hand.OFF_HAND;
        }
        return Hand.MAIN_HAND;
    }

    @Override
    public void onReloadStart(World world, PlayerEntity player, ItemStack stack, int reloadTime) {
        if (stack == null || world == null || player == null)
            return;
        if (stack.getOrCreateTag() == null) {
            player.sendMessage(new TranslationTextComponent("gun.warning"), Util.NIL_UUID);
            return;
        }
        stack.getOrCreateTag().putLong("NextFire", world.getGameTime() + (long) Math.ceil((reloadTime / 1000d) * 20));
    }

    @Override
    public void onReloadEnd(World world, PlayerEntity player, ItemStack stack, ItemStack bullet) {
        if (stack == null || player == null)
            return;
        CompoundNBT nbt = stack.getOrCreateTag();
        if (bullet != null && !bullet.isEmpty() && stack != null && nbt.contains("Capacity") && nbt.contains("Ammo")) {

            nbt.putBoolean("Reloading", false);
            int toReload = (int) Math
                    .ceil((double) (getCapacity(stack, player) - nbt.getInt("Ammo")) / (double) getShotsPerAmmo());

            if (toReload < 1)
                return;

            int reload = Math.min(toReload, Utils.getItemCount(player.inventory, bullet.getItem()));

            setAmmo(stack, player, getAmmo(stack, player) + reload * getShotsPerAmmo());
            Utils.clearMatchingItems(player.inventory, bullet.getItem(), reload);
        } else if (!nbt.contains("Capacity") && !nbt.contains("Ammo")) {
            player.sendMessage(new TranslationTextComponent("gun.warning"), Util.NIL_UUID);
        }
    }

    @Override
    public int getAmmo(ItemStack stack, PlayerEntity player) {
        if (stack == null || stack.isEmpty() || stack.getOrCreateTag() == null
                || !stack.getOrCreateTag().contains("Ammo"))
            return -1;
        return stack.getOrCreateTag().getInt("Ammo");
    }

    @Override
    public int getCapacity(ItemStack stack, PlayerEntity player) {
        if (stack.getOrCreateTag() == null)
            return this.getMaxAmmo();
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Capacity"))
            return this.getMaxAmmo();
        return (int) Math.ceil((nbt.getInt("Capacity")
                * (1d + EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.BIG_MAG.get(), stack) / 10d)
                * (1d - EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SMALL_MAG.get(), stack) / 10d)));
    }

    @Override
    public void setAmmo(ItemStack stack, PlayerEntity player, int ammo) {
        stack.getOrCreateTag().putInt("Ammo", ammo);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if ((getWield() == EnumWield.TWO_HAND && !player.getItemInHand(getOtherHand(hand)).isEmpty())) {
            return ActionResult.fail(itemstack);
        }
        if (itemstack.isEmpty() || itemstack.getOrCreateTag() == null) {
            return ActionResult.fail(itemstack);
        }
        CompoundNBT nbt = itemstack.getOrCreateTag();
        if (!nbt.contains("Ammo") || !nbt.contains("Capacity") || !nbt.contains("NextFire")) {
            return ActionResult.fail(itemstack);
        }
        if (itemstack.getOrCreateTag().getLong("NextFire") > world.getGameTime()
                || itemstack.getOrCreateTag().getBoolean("Reloading")) {
            return ActionResult.fail(itemstack);
        }

        int ammo = nbt.getInt("Ammo");
        boolean flag = player.isCreative();

        if ((ammo > 0 || flag) && (!(EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.JAMMING.get(), itemstack) > 0)
                || world.random.nextInt(20) != 0)) {

            float velocity = getSpeed() * (1f + ((float) getQuality(itemstack) / (float) CommonConfig.maxQuality.get()));
            boolean explosive = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.EXPLOSIVE.get(), itemstack) != 0;
            boolean sparking = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SPARKING.get(), itemstack) != 0;
            if (sparking && explosive && (player instanceof ServerPlayerEntity)
                    && itemstack.getItem() instanceof ItemShotgun) {
                ModTriggers.GUN_TRIGGER.trigger((ServerPlayerEntity) player, new Predicate() {
                    @Override
                    public boolean test(Object o) {
                        return true;
                    }
                });
            }
            for (int i = 0; i < projectiles
                    * (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MULTISHOT.get(), itemstack) + 1); i++) {
                ShotEntity shot = new ShotEntity(player,world);
                float spread = ((float) getSpread(player, hand) / (player.isCrouching() ? 1.5f : 1f));
                shot.shootFromRotation(player, player.xRot, player.yRot, 0.0F, velocity,spread*23.5f);
                shot.setExplosive(explosive);
                shot.setSparking(sparking);
                if (!world.isClientSide()) {
                    shot.setDamage(getFinalDamage(itemstack));
                    System.out.println(getFinalDamage(itemstack));
                    world.addFreshEntity(shot);
                }
            }
            if (player instanceof ServerPlayerEntity) {
                itemstack.hurt(1, random, (ServerPlayerEntity) player);
            }
            world.playSound(null, player.blockPosition(), getShotSound(), SoundCategory.PLAYERS, getShotSoundVolume(),
                    getShotSoundPitch());
            //player.swing(hand);

            // SevenDaysToMine.proxy.addRecoil(getRecoil(), playerIn);
            if (player instanceof ServerPlayerEntity) {
                PacketManager.sendTo(PacketManager.applyRecoil, new ApplyRecoilMessage(getRecoil(), hand == Hand.MAIN_HAND, true), (ServerPlayerEntity) player);
            }
            if (!flag) {
                itemstack.getOrCreateTag().putInt("Ammo", ammo - 1);
            }

            itemstack.getOrCreateTag().putLong("NextFire", world.getGameTime() + getDelay());

            return ActionResult.success(itemstack);
        } else {
            world.playSound(null, player.blockPosition(), getDrySound(), SoundCategory.PLAYERS, 0.3F,
                    1.0F / (random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
            itemstack.getOrCreateTag().putLong("NextFire", world.getGameTime() + (getDelay() / 2));
        }
        return ActionResult.fail(itemstack);
    }

    public float getShotSoundVolume() {
        return 0.3F;
    }

    public float getShotSoundPitch() {
        return 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F;
    }

    public double getSpread(PlayerEntity player, Hand hand) {

        float mult = 0.045f;

        if (Utils.isPlayerDualWielding(player)) {
            mult += 0.05f;
        } else if (!player.getItemInHand(getOtherHand(hand)).isEmpty()) {
            mult += 0.1f;
        }

        ItemStack stack = player.getItemInHand(hand);
        int quality = getQuality(stack);
        double spread_local = spread * mult
                * (2d - (EnumQualityState.isQualitySystemOn() ? (double) quality / (CommonConfig.maxQuality.get()) : 1));
        return ((spread_local * (Math.abs(player.getDeltaMovement().x) + Math.abs(player.getDeltaMovement().y)
                + Math.abs(player.getDeltaMovement().z)))
                * (1 - (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MARKSMAN.get(), stack)
                - EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SHAKING.get(), stack)) * 0.2));
    }

    public double getCross(PlayerEntity player, Hand hand) {
        float mult = 1f;

        if (Utils.isPlayerDualWielding(player)) {
            mult += 0.11f;
        } else if (!player.getItemInHand(getOtherHand(hand)).isEmpty()) {
            mult += 0.5f;
        }

        ItemStack stack = player.getItemInHand(hand);
        int quality = getQuality(stack);

        return (spread * mult
                * (1.2d - (EnumQualityState.isQualitySystemOn() ? (double) quality / (CommonConfig.maxQuality.get() + 1) : 1)
                * 0.9))
                * (1 - (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MARKSMAN.get(), stack)
                - EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SHAKING.get(), stack)) * 0.2);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
        if (worldIn.isClientSide()) {
            SevenDaysToMine.proxy.onGunStop(getUseDuration(stack) - timeLeft);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.<Attribute, AttributeModifier>create();
        if (slot == EquipmentSlotType.MAINHAND) {
            double value = this.getFinalDamage(stack) - 1;
            map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", value,
                    AttributeModifier.Operation.ADDITION));
        }
        return map;

    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        switch (getQualityTierFromStack(stack)) {
            case FLAWLESS:
                return 0xA300A3;
            case GREAT:
                return 0x4545CC;
            case FINE:
                return 0x37A337;
            case GOOD:
                return 0xB2B23C;
            case POOR:
                return 0xF09900;
            case FAULTY:
                return 0x89713C;
            case NONE:
            default:
                return super.getRGBDurabilityForDisplay(stack);
        }
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public float getFOVFactor(ItemStack stack) {
        return fovFactor;
    }

    public ItemGun setFOVFactor(float factor) {
        fovFactor = factor;
        return this;
    }

    public boolean getScoped() {
        return this.scoped;
    }

    public ItemGun setScoped(boolean scoped) {
        this.scoped = scoped;
        return this;
    }

    public int getProjectiles() {
        return projectiles;
    }

    public ItemGun setProjectiles(int projectiles) {
        this.projectiles = projectiles;
        return this;
    }

    public float getCounterDef() {
        return counterDef;
    }

    /*
     * Not really sure what this actually does. Just keep it at 0
     */
    public ItemGun setCounterDef(float counterDef) {
        this.counterDef = counterDef;
        return this;
    }

    public int getDelay() {
        return delay;
    }

    /*
     * Sets delay between shots?
     */
    public ItemGun setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public int getShotsPerAmmo() {
        return this.shots;
    }

    public void setShotsPerAmmo(int shots) {
        this.shots = shots;
    }

    public Vector3d getAimPosition() {
        return this.aimPosition;
    }

    public ItemGun setAimPosition(Vector3d aimPosition) {
        this.aimPosition = aimPosition;
        return this;
    }

    public ItemGun setAimPosition(double x, double y, double z) {
        return setAimPosition(new Vector3d(x, y, z));
    }

    public Vector3d getMuzzleFlashPositionMain() {
        return Vector3d.ZERO;
    }

    public Vector3d getMuzzleFlashPositionSide() {
        return Vector3d.ZERO;
    }

    @Nullable
    public Vector3d getMuzzleFlashAimPosition() {
        return Vector3d.ZERO;
    }

    public double getMuzzleFlashSize() {
        return 0.25;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState p_195938_1_, World p_195938_2_, BlockPos p_195938_3_,
                                  PlayerEntity p_195938_4_) {
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return -1;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
                             LivingEntity entityLiving) {
        worldIn.setBlockAndUpdate(pos, state);
        return false;
    }


    public ResourceLocation getIdleAnimationKey() {
        return idleAnimationKey;
    }

    public ItemGun setIdleAnimationKey(ResourceLocation idleAnimationKey) {
        this.idleAnimationKey = idleAnimationKey;
        return this;
    }

    public ResourceLocation getShootAnimationKey() {
        return shootAnimationKey;
    }

    public ItemGun setShootAnimationKey(ResourceLocation shootAnimationKey) {
        this.shootAnimationKey = shootAnimationKey;
        return this;
    }

    public ResourceLocation getReloadAnimationKey() {
        return reloadAnimationKey;
    }

    public ItemGun setReloadAnimationKey(ResourceLocation reloadAnimationKey) {
        this.reloadAnimationKey = reloadAnimationKey;
        return this;
    }

    public ResourceLocation getAimAnimationKey() {
        return aimAnimationKey;
    }

    public ItemGun setAimAnimationKey(ResourceLocation aimAnimationKey) {
        this.aimAnimationKey = aimAnimationKey;
        return this;
    }

    public ResourceLocation getAimShootAnimationKey() {
        return aimShootAnimationKey;
    }

    public ItemGun setAimShootAnimationKey(ResourceLocation aimShootAnimationKey) {
        this.aimShootAnimationKey = aimShootAnimationKey;
        return this;
    }

    public Animation getIdleAnimation() {
        if(idleAnimationKey != null && (idleAnimation == null || !Animations.instance.contains(idleAnimation)) ){
            idleAnimation = Animations.instance.get(idleAnimationKey);
        }
        return idleAnimation;
    }

    public Animation getShootAnimation() {
        if(shootAnimationKey != null && (shootAnimation == null || !Animations.instance.contains(shootAnimation)) ){
            shootAnimation = Animations.instance.get(shootAnimationKey);
        }
        return shootAnimation;
    }

    public Animation getReloadAnimation() {
        if(reloadAnimationKey != null  && (reloadAnimation == null || !Animations.instance.contains(reloadAnimation)) ){
            reloadAnimation = Animations.instance.get(reloadAnimationKey);
        }
        return reloadAnimation;
    }

    public Animation getAimAnimation() {
        if(aimAnimationKey != null  && (aimAnimation == null || !Animations.instance.contains(aimAnimation)) ){
            aimAnimation = Animations.instance.get(aimAnimationKey);
        }
        return aimAnimation;
    }

    public Animation getAimShootAnimation() {
        if(aimShootAnimationKey != null  && (aimShootAnimation == null || !Animations.instance.contains(aimShootAnimation)) ){
            aimShootAnimation = Animations.instance.get(aimShootAnimationKey);
        }
        return aimShootAnimation;
    }

    public enum EnumGun {
        PISTOL, SHOTGUN, RIFLE, LAUNCHER, MACHINE, SUBMACHINE
    }

    public enum EnumWield {
        ONE_HAND, TWO_HAND, DUAL
    }

}
