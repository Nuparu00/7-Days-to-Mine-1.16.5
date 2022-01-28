package nuparu.sevendaystomine.client.gui.computer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import nuparu.sevendaystomine.inventory.block.ContainerMonitor;
import nuparu.sevendaystomine.proxy.ClientProxy;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;

public class GuiMonitor extends Screen implements IGuiEventListener, INestedGuiEventHandler {

    protected TileEntityMonitor monitor;
    VirtualScreen screen;
    Minecraft minecraft;

    public int fullScreenMessageTime = 150;
    public StringTextComponent fullsScreenMessage;
    public GuiMonitor(TileEntityMonitor monitor) {
        super(new StringTextComponent(""));
        this.monitor = monitor;
        minecraft = Minecraft.getInstance();
        int width = (int) (minecraft.getWindow().getGuiScaledWidth()*0.75);
        int height = (int) ((width/16f)*9);
        this.screen = new VirtualScreen(width,height,this);
        fullsScreenMessage = new StringTextComponent("Press " + /*ClientProxy.keyBindings[9].getKey().getDisplayName().getString()*/ "F9" + " to toggle fullscreen mode.");
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.render(matrix, mouseX, mouseY, partialTicks);
        MainWindow window = minecraft.getWindow();
        matrix.pushPose();
        matrix.translate((window.getGuiScaledWidth() - screen.getWidth())/2,(window.getGuiScaledHeight() - screen.getHeight())/2,0);
        screen.render(matrix, mouseX, mouseY, partialTicks);
        matrix.popPose();

        if(!screen.isFullscreen() && fullScreenMessageTime > 0) {
            matrix.pushPose();
            minecraft.font.draw(matrix, fullsScreenMessage, (window.getGuiScaledWidth() - font.width(fullsScreenMessage))/ 2, 10, 0xffffff);
            matrix.popPose();

            fullScreenMessageTime--;
        }
    }

    public boolean isFullscreen() {
        return this.screen.isFullscreen();
    }

    public void setFullscreen(boolean fullscreen) {
        this.screen.setFullscreen(fullscreen);
    }

    @Override
    public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
        if(keyCode == 298){
            this.setFullscreen(!this.isFullscreen());
        }

        return super.keyPressed(keyCode, p_231046_2_, p_231046_3_);
    }
}
