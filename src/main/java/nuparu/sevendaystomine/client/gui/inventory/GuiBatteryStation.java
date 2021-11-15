package nuparu.sevendaystomine.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.inventory.block.ContainerBatteryStation;
import nuparu.sevendaystomine.tileentity.TileEntityBatteryStation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiBatteryStation extends ContainerScreen<ContainerBatteryStation> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/container/battery_station.png");
    ContainerBatteryStation container;
    TileEntityBatteryStation tileEntityBatteryStation;

    public GuiBatteryStation(ContainerBatteryStation container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.container = container;
        this.tileEntityBatteryStation = container.getTileEntity();
    }

    // Returns true if the given x,y coordinates are within the given rectangle
    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
        return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        int marginHorizontal = (this.width - this.imageWidth) / 2;
        int marginVertical = (this.height - this.imageHeight) / 2;
        if (this.tileEntityBatteryStation != null && (isInRect(marginHorizontal+7, marginVertical+4, 40, 80, mouseX, mouseY) || isInRect(marginHorizontal+135, marginVertical+4, 40, 80, mouseX, mouseY))) {
            List<ITextComponent> tooltip = new ArrayList<ITextComponent>();
            tooltip.add(new StringTextComponent(tileEntityBatteryStation.getVoltageStored() + "/" + tileEntityBatteryStation.getCapacity() + "J"));
            tooltip.add(new StringTextComponent(String.format("%.2f", ((double) tileEntityBatteryStation.getVoltageStored() / tileEntityBatteryStation.getCapacity()) * 100) + "%"));
            this.renderComponentTooltip(matrixStack, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);

        int marginHorizontal = (this.width - this.imageWidth) / 2;
        int marginVertical = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

        double percentage = ((double) tileEntityBatteryStation.getVoltageStored() / tileEntityBatteryStation.getCapacity());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                blit(matrixStack, marginHorizontal+7 + (128 * j), marginVertical+4 + (27 * i) + 23 - (int) Math.round((23 * percentage)), 176,
                        14 + 23 - (int) Math.round((23 * percentage)), 34, (int) Math.round((23 * percentage)));
            }
        }

    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, (this.imageWidth-this.font.width(this.title))/2, 5, Color.darkGray.getRGB()); /// this.font.draw
        // draw the label for the player inventory slots
        this.font.draw(matrixStack, this.inventory.getDisplayName(), /// this.font.draw
                (this.imageWidth-this.font.width(this.inventory.getDisplayName()))/2, 70, Color.darkGray.getRGB());
    }


}
