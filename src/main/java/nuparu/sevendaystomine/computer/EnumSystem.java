package nuparu.sevendaystomine.computer;

import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public enum EnumSystem {
    NONE("none"), MAC("mac"), LINUX("linux"), WIN98("win98"),
    WINXP("winxp"), WIN7("win7"), WIN8("win8"),
    WIN10("win10"), TERMINAL("terminal");

    private String name;

    EnumSystem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getReadeable() {
        return SevenDaysToMine.proxy.localize("os."+name+".name");
    }

    public static EnumSystem getEnum(String value) {
        for (EnumSystem st : EnumSystem.values()) {
            if (st.name.equalsIgnoreCase(value)) {
                return st;
            }
        }
        return EnumSystem.NONE;
    }
}
