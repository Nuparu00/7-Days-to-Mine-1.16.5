package nuparu.sevendaystomine.integration.jei.forge;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeShapeless;

public class ForgeShapelessRecipeCategory extends AbstractForgeRecipeCategory<ForgeRecipeShapeless>{

    public static ResourceLocation ID = new ResourceLocation(SevenDaysToMine.MODID,"forge_shapeless");
    public ForgeShapelessRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class getRecipeClass() {
        return ForgeRecipeShapeless.class;
    }

}
