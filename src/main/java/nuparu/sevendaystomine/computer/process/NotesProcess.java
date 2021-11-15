package nuparu.sevendaystomine.computer.process;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class NotesProcess extends WindowedProcess {

    int scrollProgress;
    int lines;
    private String text = "";
    private String prevText = "";
    @OnlyIn(Dist.CLIENT)
    private int updateCount;
    private int syncTimer = 150;

    public NotesProcess() {
        super();
    }

    public NotesProcess(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.application = ApplicationRegistry.INSTANCE.getByString("notes");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientInit() {
        super.clientInit();
    }

    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        super.clientTick();
        ++updateCount;
        if (syncTimer > 0) {
            syncTimer--;
        }
        if (syncTimer == 0) {
            if (!prevText.equals(text)) {
                prevText = text;
                syncTimer = 150;
                sync();
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putString("text", text);
        nbt.putInt("progress", scrollProgress);
        nbt.putInt("lines", lines);
        return nbt;
    }

    @Override
    public void readFromNBT(CompoundNBT nbt) {
        super.readFromNBT(nbt);
        text = nbt.getString("text");
        prevText = text;
        lines = nbt.getInt("lines");
        scrollProgress = nbt.getInt("progress");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, float partialTicks) {
        drawWindow(matrix, getTitle(), new ColorRGBA(1, 0.949, 0.671), new ColorRGBA(1, 0.921, 0.505));
        super.render(matrix, partialTicks);
        String s = text;
        if (this.isFocused()) {
            if (this.updateCount / 6 % 2 == 0) {
                s = s + "" + TextFormatting.BLACK + "_";
            } else {
                s = s + "" + TextFormatting.GRAY + "_";
            }
        }

        List<IReorderingProcessor> list = RenderComponentsUtil.wrapComponents(new StringTextComponent(s), (int) Math.floor(width), MonitorScreen.mc.font);

        int k1 = Math.min(8192 / MonitorScreen.mc.font.lineHeight, list.size());
        lines = k1;
        scrollProgress = MathUtils.clamp(scrollProgress, 0, k1);

        matrix.pushPose();
        RenderUtils.glScissor(MonitorScreen.mc, x, y + MonitorScreen.screen.ySize * title_bar_height + 2, width,
                height - MonitorScreen.screen.ySize * title_bar_height - 2);
        RenderSystem.translated(0, -(scrollProgress * MonitorScreen.mc.font.lineHeight), 0);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        for (int l1 = 0; l1 < k1; ++l1) {
            IReorderingProcessor processor = list.get(l1);
            matrix.pushPose();
            matrix.translate(0, 0, offsetRelativeZ(2));
            RenderUtils.drawString(matrix, processor, x + 2,
                    y + 2 + (MonitorScreen.screen.ySize * title_bar_height) + l1 * MonitorScreen.mc.font.lineHeight,
                    0x000000);

            matrix.translate(0, 0, -offsetRelativeZ(2));
            matrix.popPose();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        matrix.popPose();
    }

    private void pageInsertIntoCurrent(String p_146459_1_) {
        String s1 = text + p_146459_1_;
        int i = MonitorScreen.mc.font.wordWrapHeight(s1 + "" + TextFormatting.BLACK + "_",
                (int) Math.floor(width));

        if (s1.length() < 8192 && !text.equals(s1)) {
            text = s1;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (!isFocused() || isMinimized())
            return;
        if (Screen.isPaste(keyCode)) {
            this.pageInsertIntoCurrent(Minecraft.getInstance().keyboardHandler.getClipboard());
        }
        if (SharedConstants.isAllowedChatCharacter(typedChar)) {
            this.pageInsertIntoCurrent(Character.toString(typedChar));
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
        if (keyCode == 265) {
            scrollProgress = MathUtils.clamp(--scrollProgress, 0, lines);
        } else if (keyCode == 264) {
            scrollProgress = MathUtils.clamp(++scrollProgress, 0, lines);
        } else {
            switch (keyCode) {
                case 259:
                    String s = text;
                    if (!s.isEmpty()) {
                        text = (s.substring(0, s.length() - 1));
                    }
                    break;
                case 335:
                case 257:
                    this.pageInsertIntoCurrent("\n");
                    break;
                default:
            }
        }
        return super.keyPressed(keyCode, p_231046_2_, p_231046_3_);
    }
}
