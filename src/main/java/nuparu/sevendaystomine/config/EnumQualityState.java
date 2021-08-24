package nuparu.sevendaystomine.config;

public enum EnumQualityState {
    OFF,
    SEVEN_DAYS_TO_MINE_ONLY,
    ALL;



    public static boolean isQualitySystemOn(){
        return CommonConfig.qualitySystem.get() != EnumQualityState.OFF;
    }
    public static boolean isVanillaOn(){
        return CommonConfig.qualitySystem.get() == EnumQualityState.ALL;
    }
}
