package nuparu.sevendaystomine.integration.jei;

import net.minecraftforge.fml.ModList;

//Adds a layer between my normal code and the JEI plugin. Might not actually be needed but it is better to be safe
public class JeiBridge {

    public static void registerScrapRecipes(){
        ModList.get().getMods().stream().forEach(m -> System.out.println(m.getModId() + " " + m.getNamespace()));
        if(ModList.get().isLoaded("jei")) {
            JeiPlugin.instance.registerScrapRecipes();
        }
    }
}
