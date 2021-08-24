package nuparu.sevendaystomine.computer.process;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ICharacterConsumer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.ni.CodeBlock;
import nuparu.ni.Interpreter;
import nuparu.ni.Token;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SaveDataMessage;
import nuparu.sevendaystomine.network.packets.SendPacketMessage;
import nuparu.sevendaystomine.network.packets.SendRedstoneSignalMessage;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Tree;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TransitProcess extends WindowedProcess {

    public ShellProcess shellProcess = null;
    int scrollProgress;
    int lines;
    @OnlyIn(Dist.CLIENT)
    List<Token> tokens;
    @OnlyIn(Dist.CLIENT)
    Button runButton;
    @OnlyIn(Dist.CLIENT)
    Button buildButton;
    private String text = "";
    private String prevText = "";
    private List<ITextComponent> output = new ArrayList<ITextComponent>();
    private int cursorPosition;
    private int selectionEnd;
    private int lineScrollOffset;
    @OnlyIn(Dist.CLIENT)
    private ColorRGBA cursorColor;
    @OnlyIn(Dist.CLIENT)
    private int cursorCounter;
    private int syncTimer = 150;

    public TransitProcess() {
        super();
    }

    public TransitProcess(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.application = ApplicationRegistry.INSTANCE.getByString("transit");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientInit() {
        super.clientInit();

        cursorColor = new ColorRGBA(255, 255, 255);
        tokens = new ArrayList<Token>();

        tokens = Interpreter.tokenize(text);

        runButton = new Button(x, y + height * (0.75) - 10, 40, 10, MonitorScreen.screen, "Run", 1) {
            @Override
            public boolean isVisible() {
                return this.tickingProcess != null
                        && ((WindowedProcess) this.tickingProcess).isNotHidden((int) (this.x + this.width), (int) (this.y + this.height));
            }
        };
        runButton.background = true;
        runButton.border = false;
        runButton.textNormal = 0xffffff;
        runButton.normal = new ColorRGBA(90, 90, 90);
        runButton.hovered = new ColorRGBA(120, 120, 120);
        runButton.setZLevel(20);
        runButton.setFontSize(0.7);
        runButton.setProcess(this);
        elements.add(runButton);

        buildButton = new Button(x + 45, y + height * (0.75) - 10, 40, 10, MonitorScreen.screen, "Build", 2) {
            @Override
            public boolean isVisible() {
                return this.tickingProcess != null
                        && ((WindowedProcess) this.tickingProcess).isNotHidden((int) (this.x + this.width), (int) (this.y + this.height));
            }
        };
        buildButton.background = true;
        buildButton.border = false;
        buildButton.textNormal = 0xffffff;
        buildButton.normal = new ColorRGBA(90, 90, 90);
        buildButton.hovered = new ColorRGBA(120, 120, 120);
        buildButton.setZLevel(20);
        buildButton.setFontSize(0.7);
        buildButton.setProcess(this);
        elements.add(buildButton);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        super.clientTick();
        ++cursorCounter;
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
    @OnlyIn(Dist.CLIENT)
    public void initWindow() {
        super.initWindow();

        if (runButton != null) {
            runButton.setX(x);
            runButton.setY(y + height * (0.75) - 10);
        }
        if (buildButton != null) {
            buildButton.setX(x + 45);
            buildButton.setY(y + height * (0.75) - 10);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onButtonPressed(Button button, int mouseButton) {
        super.onButtonPressed(button, mouseButton);
        if (isMinimized())
            return;
        int buttonId = button.ID;
        switch (buttonId) {
            case 1:
                run();
                break;
        }

    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putString("text", text);
        nbt.putInt("progress", scrollProgress);
        nbt.putInt("lines", lines);
        nbt.putInt("cursorPosition", cursorPosition);
        nbt.putInt("selectionEnd", cursorPosition);
        return nbt;
    }

    @Override
    public void readFromNBT(CompoundNBT nbt) {
        super.readFromNBT(nbt);
        text = nbt.getString("text");
        prevText = text;
        lines = nbt.getInt("lines");
        scrollProgress = nbt.getInt("progress");
        cursorPosition = nbt.getInt("cursorPosition");
        selectionEnd = nbt.getInt("selectionEnd");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, float partialTicks) {
        drawWindow(matrix, getTitle(), new ColorRGBA(47, 47, 47), new ColorRGBA(67, 68, 71));
        super.render(matrix, partialTicks);
        RenderUtils.drawColoredRect(matrix, new ColorRGBA(0, 0, 0), x, y + height * (0.75), width, height / 4, zLevel + 1);

        String s = text;

        // Splits text to fit the page
        //List<IReorderingProcessor> l = RenderComponentsUtil.wrapComponents(new StringTextComponent(s), (int) Math.floor(width - 35), MonitorScreen.mc.font);


        List<TextComponent> l = RenderUtils.splitText(new StringTextComponent(s),
                (int) Math.floor(width - 35), MonitorScreen.mc.font, true, true);
        int k1 = Math.min(8192 / MonitorScreen.mc.font.lineHeight, l.size());
        lines = k1;
        scrollProgress = MathUtils.clamp(scrollProgress, 0, k1);

        matrix.pushPose();
        // Cuts content outside the window
        RenderUtils.glScissor(MonitorScreen.mc, x, y + MonitorScreen.screen.ySize * title_bar_height + 2, width,
                (height * 0.75) - MonitorScreen.screen.ySize * title_bar_height - 2 - 10);
        RenderSystem.translated(0, -(scrollProgress * MonitorScreen.mc.font.lineHeight), 0);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        // Coordinates of top left corner
        double left = x + 2;
        double top = y + 2 + (MonitorScreen.screen.ySize * title_bar_height);

        // new line
        for (int l1 = 0; l1 < k1; ++l1) {
            TextComponent itextcomponent2 = l.get(l1);

            //itextcomponent2
            matrix.pushPose();
            matrix.translate(0, 0, offsetRelativeZ(2));
            // Renders line number
            RenderUtils.drawString(matrix, (l1 + 1) + "", left, top + l1 * MonitorScreen.mc.font.lineHeight, 0x939393);
            RenderUtils.drawString(matrix, "|", left + 20, top + l1 * MonitorScreen.mc.font.lineHeight, 0x939393);

            // Gets the text and separates it by empty spaces, commas, operators, etc. but
            // also keeps them in the array
            String[] array = itextcomponent2.getString()
                    .split(String.format(Interpreter.WITH_DELIMITER, Interpreter.REGEX));
            ArrayList<String> words = (ArrayList<String>) Arrays.stream(array).collect(Collectors.toList());

            int pointer = 0;
            double xx = left + 25;
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                int color = 0xbbbbbb;
                if (!word.trim().isEmpty()) {
                    for (int j = pointer; j < tokens.size(); j++) {
                        Token token = tokens.get(j);
                        //
                        if (token.value instanceof String && word.trim().equals(token.value)) {
                            color = token.type.getColor();
                            pointer = j;
                            break;
                        }
                    }
                }

                // System.out.println(l1 +" " + i + " " + word + " " + pointer + " " +
                // tokens.size());

                RenderUtils.drawString(matrix, word, xx, top + l1 * MonitorScreen.mc.font.lineHeight, color);
                xx += MonitorScreen.mc.font.width(words.get(i));
            }
            matrix.translate(0, 0, -offsetRelativeZ(2));
            matrix.popPose();
        }
        if (this.isFocused()) {
            if (this.cursorCounter / 6 % 2 == 0) {
                if ((cursorPosition) <= text.length()) {
                    String ss = text.substring(0, cursorPosition);
                    l = RenderUtils.splitText(new StringTextComponent(ss), (int) Math.floor(width - 35),
                            MonitorScreen.mc.font, true, true);
                    k1 = Math.min(8192 / MonitorScreen.mc.font.lineHeight, l.size());
                    int lineIndex = Math.max(0, k1 - 1);

                    // 25 due to the fact that the actual text input start more right because of the
                    // line counter
                    renderCursor(matrix,
                            left + 25 + MonitorScreen.mc.font.width(l.get(lineIndex).getString()),
                            top + lineIndex * MonitorScreen.mc.font.lineHeight, 1, MonitorScreen.mc.font.lineHeight,
                            zLevel + 2);
                }
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        matrix.popPose();

        matrix.pushPose();
        matrix.translate(0, 0, offsetRelativeZ(2));
        //Copy of the origina llist so we do not get ConcurrentModificationException
        List<ITextComponent> outputFull = new ArrayList<ITextComponent>(getOutput());
        List<ITextComponent> output = outputFull.subList(Math.max(0, outputFull.size() - 4), outputFull.size());
        for (int i = 0; i < output.size(); i++) {
            String out = output.get(i).getString();
            RenderUtils.drawString(matrix, out, left, y + height * (0.75) + MonitorScreen.mc.font.lineHeight * i + 2,
                    0xEBEBEB);
        }
        matrix.translate(0, 0, -offsetRelativeZ(2));

        matrix.popPose();
    }

    private void writeText(String newText) {
        if (text.length() >= Short.MAX_VALUE) {
            return;
        }
        String s = "";
        String s1 = newText;
        int i = this.cursorPosition;
        int k = text.length() - i;
        int l;
        if (i < text.length() && i >= 0) {
            String halfA = text.substring(0, i);
            String halfB = text.substring(i, text.length());
            s = halfA + s1 + halfB;
        } else {
            s = text + s1;
        }
        l = s1.length();
        text = s;
        setCursorPosition(i + l);
    }

    @OnlyIn(Dist.CLIENT)
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (!isFocused() || isMinimized())
            return;
        if (Screen.isPaste(keyCode)) {
            this.writeText(Minecraft.getInstance().keyboardHandler.getClipboard());
            tokens = Interpreter.tokenize(text);

        } else if (SharedConstants.isAllowedChatCharacter(typedChar)) {
            this.writeText(Character.toString(typedChar));
            tokens = Interpreter.tokenize(text);
        }


        //
    }

    @Override
    public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
        if (!isFocused() || isMinimized())
            return super.keyPressed(keyCode, p_231046_2_, p_231046_3_);
        switch (keyCode) {
            case 259: {
                if (text == null || text.length() == 0) {
                    break;
                }
                int i = this.cursorPosition;
                int l;
                String s = "";
                if (i < this.text.length()) {
                    String halfA = this.text.substring(0, i);
                    String halfB = this.text.substring(i, this.text.length());
                    if (halfA.length() > 0) {
                        s = halfA.substring(0, halfA.length() - 1) + halfB;
                        l = halfA.length() - 1;
                    } else {
                        s = halfA.substring(0, halfA.length()) + halfB;
                        l = halfA.length();
                    }

                } else {
                    if (text.length() > 0) {
                        s = text.substring(0, text.length() - 1);
                        l = s.length();
                    } else {
                        s = text.substring(0, text.length());
                        l = s.length();
                    }
                }
                text = s;
                setCursorPosition(l);
                break;
            }
            case 258: {
                run();
                break;
            }
            case 335:
            case 257:
                this.writeText("\n");
                break;
            case 266:
                scrollProgress = MathUtils.clamp(--scrollProgress, 0, lines);
                break;
            case 267:
                scrollProgress = MathUtils.clamp(++scrollProgress, 0, lines);
                break;
            case 262:
                if (cursorPosition > 0) {
                    setCursorPosition(--cursorPosition);
                }
                break;
            case 263:
                if (cursorPosition >= 0) {
                    setCursorPosition(++cursorPosition);
                }
                break;
        }
        tokens = Interpreter.tokenize(text);
        return super.keyPressed(keyCode, p_231046_2_, p_231046_3_);
    }

    // Runs the written code
    public void run() {
        Tree<Token> tree = Interpreter.parse(tokens);
        if (tree != null) {
            // tree.print("-", true);
            TransitProcess process = this;
            getOutput().clear();
            this.computerTE.codeBus = new Thread() {
                @Override
                public void run() {
                    try {
                        CodeBlock block = Interpreter.read(tree, null, process);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            this.computerTE.codeBus.start();

        }
    }

    public void setCursorPosition(int pos) {
        this.cursorPosition = pos;
        int i = this.text.length();
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
        this.setSelectionPos(this.cursorPosition);
    }

    public void setSelectionPos(int position) {
        int i = this.text.length();

        if (position > i) {
            position = i;
        }

        if (position < 0) {
            position = 0;
        }

        this.selectionEnd = position;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        double left = x + 2 + 25;
        double top = y + 2 + (MonitorScreen.screen.ySize * title_bar_height);

        if (this.isFocused()) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= y
                    && mouseY <= y + (height * 0.75) - MonitorScreen.screen.ySize * title_bar_height - 2 - 10) {
                this.isFocused = true;

                // position of Mouse from top left corner of the text area
                int i = (int) (mouseX - (int) Math.ceil(left));
                int j = (int) (mouseY - (int) Math.ceil(top));

                List<TextComponent> l = RenderUtils.splitText(new StringTextComponent(text),
                        (int) Math.floor(width - 35), MonitorScreen.mc.font, true, true);

                int line = (int) Math.ceil((double) j / (double) MonitorScreen.mc.font.lineHeight) - 1;

                if (line < l.size() && line >= 0) {
                    int newPosition = 0;
                    for (int k = 0; k < line; k++) {
                        String z = l.get(k).getString();
                        newPosition += l.get(k).getString().length();
                    }
                    String z = MonitorScreen.mc.font.plainSubstrByWidth(l.get(line).getString(), i);
                    this.setCursorPosition(z.length() + newPosition + Math.max(0, line));
                    this.cursorCounter = 0;
                }

            }
        }
    }

    public void renderCursor(MatrixStack matrixStack, double x, double y, double width, double height, double zLevel) {
        matrixStack.translate(0, 0, zLevel);
        AbstractGui.fill(matrixStack, (int)x, (int)y, (int)(x+width), (int)(y+height), this.cursorColor.toHex());
        matrixStack.translate(0, 0, -zLevel);
    }

    @OnlyIn(Dist.CLIENT)
    public void saveToDevice(String data) {
        PacketManager.saveData.sendToServer(new SaveDataMessage(data, computerTE.getPos()));
    }

    @OnlyIn(Dist.CLIENT)
    public void sendPacket(String packet, BlockPos to) {
        PacketManager.sendPacket.sendToServer(new SendPacketMessage(computerTE.getPos(), to, packet));
    }

    @OnlyIn(Dist.CLIENT)
    public void sendRedstoneSignal(Direction facing, int strength) {
        PacketManager.redstoneSignal.sendToServer(new SendRedstoneSignalMessage(strength, computerTE.getPos(), facing));
    }

    public List<ITextComponent> getOutput() {
        return output;
    }
}
