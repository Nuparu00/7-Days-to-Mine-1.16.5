package nuparu.sevendaystomine.entity.human;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;
import nuparu.sevendaystomine.entity.human.dialogue.DialogueDataManager;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.util.dialogue.Dialogues;

import javax.annotation.Nullable;

public class SurvivorEntity extends EntityHuman implements IMerchant {
    @Nullable
    private PlayerEntity tradingPlayer;
    @Nullable
    protected MerchantOffers offers;
    private final Inventory inventory = new Inventory(8);

    public SurvivorEntity(EntityType<? extends EntityHuman> type, World world) {
        super(type, world);
    }

    public SurvivorEntity(World world) {
        super(ModEntities.SURVIVOR.get(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        Dialogues dialogues = DialogueDataManager.instance.get(new ResourceLocation(SevenDaysToMine.MODID, "survivor.miner"));
        this.setDialogues(dialogues);
        this.setCurrentDialogue("survivor.miner.a");
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                (living) -> living instanceof ZombieBaseEntity));
    }

    @Override
    protected int getExperienceReward(PlayerEntity p_70693_1_) {
        return 10;
    }

    public static AttributeModifierMap createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D).add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 20).build();
    }

    public void setTradingPlayer(@Nullable PlayerEntity player) {
        boolean flag = this.getTradingPlayer() != null && player == null;
        this.tradingPlayer = player;
        if (flag) {
            this.stopTrading();
            return;
        }
        if (player != null) {
            this.openTradingScreen(player, this.getDisplayName(), 0);
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerWorld p_241206_1_, net.minecraftforge.common.util.ITeleporter teleporter) {
        this.stopTrading();
        return super.changeDimension(p_241206_1_, teleporter);
    }

    protected void stopTrading() {
        this.setTradingPlayer(null);
    }

    @Override
    public void die(DamageSource p_70645_1_) {
        super.die(p_70645_1_);
        this.stopTrading();
    }


    @Nullable
    public PlayerEntity getTradingPlayer() {
        return this.tradingPlayer;
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.updateTrades();
        }

        return this.offers;
    }

    @Override
    public void overrideOffers(@Nullable MerchantOffers p_213703_1_) {

    }

    @Override
    public void notifyTrade(MerchantOffer p_213704_1_) {

    }

    @Override
    public void notifyTradeUpdated(ItemStack p_110297_1_) {

    }

    @Override
    public World getLevel() {
        return level;
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int p_213702_1_) {

    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    protected void updateTrades() {

    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.isTrading() && !this.isSleeping() && !p_230254_1_.isSecondaryUseActive()) {
            if (this.isBaby()) {
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            } else {
                SevenDaysToMine.proxy.startDialogue(this);
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }
        } else {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
    }

    @Override
    public void onDialogue(String dialogue, PlayerEntity player) {
        super.onDialogue(dialogue, player);
        System.out.println("dialogueFFFF");
        if (dialogue.equals("survivor.trade")) {
            System.out.println("FFFF");
            setTradingPlayer(player);
        }
    }
}
