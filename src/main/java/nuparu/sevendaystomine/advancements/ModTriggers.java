package nuparu.sevendaystomine.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModTriggers {

    public static final CustomTrigger AIRDROP_INTERACT = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "airdrop_interact"));
    public static final CustomTrigger SAFE_UNLOCK = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "safe_unlock"));
    public static final CustomTrigger FLARE = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "flare"));
    public static final CustomTrigger MOSCO = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "mosco"));
    public static final CustomTrigger CURE = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "cure"));
    public static final CustomTrigger BLOOD_BOND = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "blood_bond"));
    public static final CustomTrigger GUN_TRIGGER = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "gun_trigger"));
    public static final CustomTrigger ROAD_TRIP = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "road_trip"));
    public static final CustomTrigger KNOW_IT_ALL = new CustomTrigger(new ResourceLocation(SevenDaysToMine.MODID, "know_it_all"));
    public static final BloodmoonSurvivalTrigger BLOODMOON_SURVIVAL = new BloodmoonSurvivalTrigger(new ResourceLocation(SevenDaysToMine.MODID, "bloodmoon_survival"));
    public static final UpgradeBlockTrigger BLOCK_UPGRADE = new UpgradeBlockTrigger(new ResourceLocation(SevenDaysToMine.MODID, "upgrade_block"));

    public static final ICriterionTrigger[] TRIGGER_ARRAY = new ICriterionTrigger[]{AIRDROP_INTERACT, SAFE_UNLOCK, FLARE, MOSCO, CURE,
            BLOOD_BOND, GUN_TRIGGER, ROAD_TRIP, KNOW_IT_ALL,BLOODMOON_SURVIVAL,BLOCK_UPGRADE};


    public static void register() {
        for (ICriterionTrigger trigger : TRIGGER_ARRAY) {
            CriteriaTriggers.register(trigger);
        }
    }
}
