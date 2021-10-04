package nuparu.sevendaystomine.integration.jei.chemistry;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeShapeless;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeShapeless;

public class ChemistryShapelessRecipeCategory extends AbstractChemistryRecipeCategory<ChemistryRecipeShapeless> {

    public static ResourceLocation ID = new ResourceLocation(SevenDaysToMine.MODID,"chemistry_shapeless");
    public ChemistryShapelessRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class getRecipeClass() {
        return ChemistryRecipeShapeless.class;
    }

}
