package nuparu.sevendaystomine.client.gui.computer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;
import nuparu.sevendaystomine.util.ColorRGBA;

public class VirtualScreen {

    Minecraft minecraft;
    private int width;
    private int height;
    private boolean fullscreen = false;
    private GuiMonitor gui;
    private TileEntityMonitor monitor;
    private ColorRGBA screenColor = new ColorRGBA(0, 0, 0);
    private ColorRGBA frameColor = new ColorRGBA(0.056, 0.056, 0.056);

    public VirtualScreen(int width, int height, GuiMonitor gui) {
        this.width = width;
        this.height = height;
        this.gui = gui;
        this.monitor = gui.monitor;
        minecraft = Minecraft.getInstance();
    }

    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderMonitor(matrix, mouseX, mouseY, partialTicks);
        if(monitor.getComputer() != null && monitor.isOn() && monitor.getComputer().isOn()){

        }
    }

    public void renderMonitor(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        MainWindow window = minecraft.getWindow();
        if(!isFullscreen()){
            RenderUtils.drawColoredRect(matrix, frameColor, -5, -5, width+10, height+10, 0);
        }
        RenderUtils.drawColoredRect(matrix, screenColor, 0, 0, width, height, 0);
    }

    public boolean isFullscreen() {
        return this.fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        boolean wasFullscreen = this.fullscreen;

        this.fullscreen = fullscreen;
        if (fullscreen != wasFullscreen) {
            MainWindow window = minecraft.getWindow();
            if (fullscreen) {
                width = window.getGuiScaledWidth();
                height = window.getGuiScaledHeight();
            } else {
                width = (int) (minecraft.getWindow().getGuiScaledWidth() * 0.75);
                height = (int) ((width / 16f) * 9);
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
