package nuparu.sevendaystomine.integration.jei.locked;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class LockedIcon {

    private final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/jei/locked_icon.png");

    public LockedIcon() {

    }

    public void draw(MatrixStack matrixStack, int recipeWidth) {

        int width = 8;
        matrixStack.pushPose();
        RenderUtils.drawTexturedRect(matrixStack,TEXTURE,recipeWidth-width-10,0,0,0,width,width,width,width,1,0);
        matrixStack.popPose();
    }

    @Nullable
    public List<ITextComponent> getTooltipStrings(int mouseX, int mouseY, int recipeWidth, int recipeHeight, String recipe) {

        int width = 8;
        boolean inArea = Utils.isInArea(mouseX,mouseY,recipeWidth-width-10,0,width,width);

        TranslationTextComponent textComponent = new TranslationTextComponent("jei.tooltip.locked.recipe",new TranslationTextComponent("book_"+recipe+".title"));

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null) {
            IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
            if(iep != null){
                 textComponent.setStyle(textComponent.getStyle().withColor(iep.hasRecipe(recipe) ? TextFormatting.GREEN : TextFormatting.RED));

            }
        }
        if (inArea) {
            return Collections.singletonList(textComponent);
        }
        return null;
    }

}
