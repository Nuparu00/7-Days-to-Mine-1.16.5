package nuparu.sevendaystomine.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumQuality;
import nuparu.sevendaystomine.item.IQuality;
import nuparu.sevendaystomine.item.ItemQuality;

public class PlayerUtils {
	
	public static int getInfectionStageStart(int stage) {
		if(stage >= ServerConfig.infectionStagesDuration.get().size()) return Integer.MIN_VALUE;
		
		int start = 0;
		
		if(stage == 0) return start;
		
		for(int i = 0; i < stage; i++) {
			start+= ServerConfig.infectionStagesDuration.get().get(i);
		}
		return start;
	}
	
	// Returns the time until the next infection stage, takes the infection time
	// from IExtendedPlayer
	public static int getCurrentInectionStageRemainingTime(int time) {
		int stage = getInfectionStage(time);
		if(stage >= ServerConfig.infectionStagesDuration.get().size()) return 24000;
	
		int nextStageStart = getInfectionStageStart(stage+1);
		return nextStageStart-time;

	}

	// Reutns the current infection staged based on the infection time from
	// IExtendedPlayer
	public static int getInfectionStage(int time) {
		if (time < 0)
			return -1;
		for(int i = 0; i < getNumberOfstages()-1;i++) {
			int start = getInfectionStageStart(i);
			int end = getInfectionStageStart(i+1);
			if(start <= time && time < end) {
				
				return i;
			}
			
		}
		return ServerConfig.infectionStagesDuration.get().size()-1;
	}
	
	public static int getNumberOfstages() {
		return ServerConfig.infectionStagesDuration.get().size();
	}

	/*
	Can see the direction in which energy flows?
	 */
	public static boolean canSeeEnergyFlow(PlayerEntity player){
		ItemStack mainHand = player.getMainHandItem();
		ItemStack offHand = player.getOffhandItem();

		return canItemShowEneergyFlow(mainHand) || canItemShowEneergyFlow(offHand);
	}
	public static boolean canItemShowEneergyFlow(ItemStack stack){
		return stack.getItem() == ModItems.VOLTMETER.get() || stack.getItem() == ModItems.WIRE.get();
	}

	public static EnumQuality getQualityTierFromStack(ItemStack stack) {
		return getQualityTierFromInt(ItemQuality.getQualityFromStack(stack));
	}

	public static EnumQuality getQualityTierFromInt(int quality) {
		return EnumQuality.getFromQuality(quality);
	}
	public static boolean isQualityItem(ItemStack stack){
		return stack.getItem() instanceof IQuality || (ServerConfig.qualitySystem.get() == EnumQualityState.ALL && stack.getTag() != null && stack.getTag().contains("Quality", Constants.NBT.TAG_INT));
	}

	public static boolean isVanillaQualityItem(ItemStack stack){
		return !(stack.getItem() instanceof IQuality) && (ServerConfig.qualitySystem.get() == EnumQualityState.ALL && stack.getTag() != null && stack.getTag().contains("Quality", Constants.NBT.TAG_INT));
	}

	public static boolean isVanillaItemSuitableForQuality(Object object){
		return object instanceof ToolItem || object instanceof SwordItem || object instanceof ArmorItem || object instanceof BowItem || object instanceof CrossbowItem;
	}

}
