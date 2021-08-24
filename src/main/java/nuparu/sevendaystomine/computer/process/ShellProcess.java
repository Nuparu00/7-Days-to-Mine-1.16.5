package nuparu.sevendaystomine.computer.process;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.events.HandleCommandEvent;
import nuparu.sevendaystomine.proxy.CommonProxy;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;
import org.lwjgl.opengl.GL11;

import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ShellProcess extends WindowedProcess {

    public LinkedList<String> log = new LinkedList<String>();
    public LinkedList<String> history = new LinkedList<String>();
    @OnlyIn(Dist.CLIENT)
    TextField field;
    @OnlyIn(Dist.CLIENT)
    int scrollProgress;
    @OnlyIn(Dist.CLIENT)
    int logLines;
    private String input = "";
    private int historyPointer = 0;

    public ShellProcess() {
        this(0, 0, 0, 0);
    }

    public ShellProcess(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.application = ApplicationRegistry.INSTANCE.getByString("shell");
    }

    @Override
    public String getTitle() {
        return SevenDaysToMine.proxy.localize("computer.app.command_line");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, float partialTicks) {
        drawWindow(matrix, getTitle(), new ColorRGBA(0d, 0d, 0d), new ColorRGBA(0.8, 0.8, 0.8));
        super.render(matrix, partialTicks);
        matrix.pushPose();
        matrix.translate(0, 0, offsetRelativeZ(2));
        ArrayList<String> strings = new ArrayList<String>();
        String branding = "Microsoft Windows [Version 10]";
        while (branding.length() > 0) {

            strings.add(branding.substring(0, Math.min(branding.length(), (int) (width / 6))));
            branding = branding.substring(Math.min(branding.length(), (int) (width / 6)));
        }
        for (int i = 0; i < log.size(); i++) {
            if (log.get(i) != null) {
                String s = log.get(i);
                while (s.length() > 0) {

                    strings.add(s.substring(0, Math.min(s.length(), (int) (width / 6))));
                    s = s.substring(Math.min(s.length(), (int) (width / 6)));
                }
            }
        }
        logLines = strings.size() - (int) ((height - 10 - MonitorScreen.screen.ySize * title_bar_height - 4) / 10);
        if (logLines < 0) {
            logLines = 0;
        }
        scrollProgress = MathUtils.clamp(scrollProgress, 0, logLines);

        RenderUtils.glScissor(MonitorScreen.mc, x, y + MonitorScreen.screen.ySize * title_bar_height + 4, width,
                height - 10 - MonitorScreen.screen.ySize * title_bar_height - 4);
        matrix.translate(0, (-logLines * 10) + (scrollProgress * 10 + 4), 0);
        // RenderSystem.translated(0, -10, 0);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        for (int i = 0; i < strings.size(); i++) {
            RenderUtils.drawString(matrix, strings.get(i), x,
                    (y + 2 + MonitorScreen.screen.ySize * title_bar_height) + (10 * i), 14737632);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        matrix.translate(0, 0, -offsetRelativeZ(2));
        matrix.popPose();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        super.clientTick();
        if (field != null) {
            input = field.getContentText();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientInit() {
        super.clientInit();
        if (field == null) {
            field = new TextField(x, y + height - 9, this.width, 9, screen);
            field.setProcess(this);
            field.enabledColor = 0xffffff;
            field.backgroundColor = new ColorRGBA(0, 0, 0, 0);
            field.cursorColor = new ColorRGBA(1d, 1d, 1d);
            field.setZLevel(zLevel + 1);
            field.setContentText(input);
            elements.add(field);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initWindow() {
        super.initWindow();
        if (elements.size() < 2) {
            return;
        }

        field.setX(x);
        field.setY(y + height - 9);
        field.setWidth(this.width);
        field.setHeight(9);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onDragReleased() {
        if (field != null) {
            field.setContentText(input);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putString("input", input);
        ListNBT logTag = new ListNBT();
        for (String str : log) {
            logTag.add(StringNBT.valueOf(str));
        }
        nbt.put("log", logTag);

        ListNBT historyTag = new ListNBT();
        for (String str : history) {
            historyTag.add(StringNBT.valueOf(str));
        }
        nbt.put("history", historyTag);

        return nbt;
    }

    @Override
    public void readFromNBT(CompoundNBT nbt) {
        super.readFromNBT(nbt);
        if (nbt.contains("input")) {
            input = nbt.getString("input");
        }
        log = new LinkedList<String>();
        if (nbt.contains("log")) {
            ListNBT logTag = nbt.getList("log", Constants.NBT.TAG_STRING);
            Iterator<INBT> it = logTag.iterator();
            while (it.hasNext()) {
                INBT base = it.next();
                if (base instanceof StringNBT) {
                    log.add(((StringNBT) base).getAsString());
                }
            }
        }
        if (nbt.contains("history")) {
            ListNBT historyTag = nbt.getList("history", Constants.NBT.TAG_STRING);
            Iterator<INBT> it2 = historyTag.iterator();
            while (it2.hasNext()) {
                INBT base = it2.next();
                if (base instanceof StringNBT) {
                    history.add(((StringNBT) base).getAsString());
                }
            }
        }
    }

    public void addTextToLog(String s) {
        if (s != null) {
            while (log.size() > 10) {
                log.removeFirst();
            }
            log.addLast(s);
            sync("log");
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
        boolean res = super.keyPressed(keyCode, p_231046_2_, p_231046_3_);

        if (!isFocused() || isMinimized())
            return res;

        if (field.isFocused()) {
            switch (keyCode) {
                case 259: {
                    break;
                }
                case 335:
                case 257:
                    scrollProgress = 0;
                    String t = field.getContentText();
                    if (t.isEmpty()) {
                        break;
                    }
                    addTextToLog(t);
                    String s = handleCommand(t);
                    sync("history");
                    sync("log");
                    historyPointer = 0;
                    if (!s.isEmpty()) {
                        addTextToLog(s);
                    }
                    field.setContentText("");
                    input = "";
                    break;
                case 267:
                    scrollProgress = MathUtils.clamp(--scrollProgress, 0, logLines);
                    break;
                case 266:
                    scrollProgress = MathUtils.clamp(++scrollProgress, 0, logLines);
                    break;
            }
        } else {
			switch (keyCode) {
				case 262:
					if (history.size() > 0) {
						historyPointer = MathUtils.clamp(historyPointer + 1, 0, history.size() - 1);
						this.field.setContentText(history.get(history.size() - 1 - historyPointer));
					}
					break;
				case 263:
					if (history.size() > 0) {
						historyPointer = MathUtils.clamp(historyPointer - 1, 0, history.size() - 1);
						this.field.setContentText(history.get(history.size() - 1 - historyPointer));
					}
					break;
			}
		}

            return res;
        }

        @OnlyIn(Dist.CLIENT)
        public String handleCommand (String command){
            while (history.size() > 10) {
                history.removeFirst();
            }
            history.add(command);
            HandleCommandEvent event = new HandleCommandEvent(this.computerTE, this, command);
            MinecraftForge.EVENT_BUS.post(event);
            history.add(event.command);
            if (event.override) {
                return event.output;
            }
            String[] words = input.split(" ");
            switch (words[0].toLowerCase()) {
                case "help":
                    help();
                    return "";
                case "connections":
                    connections();
                    return "";
                case "ipconfig":
                    ipconfig();
                    return "";
                case "compute":
                    return compute(command.substring(command.indexOf(' ') + 1));
                case "run":
                    run(words);
                    return "";
            }

            return "\"" + words[0] + "\" is not recognized as a command.";

        }

        @OnlyIn(Dist.CLIENT)
        public void help () {
            addTextToLog("HELP Provides Help information for Windows commands.");
            addTextToLog("IPCONFIG Displays all current TCP/IP network configuration values.");
            addTextToLog("COMPUTE Computes a JavaScript code.");
            addTextToLog("RUN Runs a program.");
            addTextToLog("CONNECTIONS Prints all connected devices.");
            addTextToLog("DISCONNECTALL Disconnets all connected devices.");
        }

        @OnlyIn(Dist.CLIENT)
        public void ipconfig () {
            addTextToLog("IPv4 Address: " + computerTE.getPos().getX() + "." + computerTE.getPos().getY() + "."
                    + computerTE.getPos().getZ() + "." + Minecraft.getInstance().level.dimension().hashCode());
        }

        @OnlyIn(Dist.CLIENT)
        public String compute (String input){
            try {
                CommonProxy.sw.getBuffer().setLength(0);
                Object object = CommonProxy.engine.eval(input);
                if (object != null) {
                    return object.toString();
                } else {
                    String s = CommonProxy.sw.toString();
                    return s.substring(0, Math.max(0, s.length() - 3));
                }
            } catch (ScriptException exception) {
                exception.printStackTrace();
                return "An error occured while trying to perform the command:" + exception.getMessage();
            }
        }

        @OnlyIn(Dist.CLIENT)
        public void run (String[]words){
            if (words.length == 2 && words[1].equals("cbburner.exe")) {
                addTextToLog("F");
            } else {
                addTextToLog("\"" + words[1] + "\" is not recognized as a program.");
            }
        }

        @OnlyIn(Dist.CLIENT)
        public void connections () {
            List<BlockPos> connections = this.computerTE.getConnections();
            for (BlockPos connection : connections) {
                addTextToLog(connection.getX() + "/" + connection.getY() + "/" + connection.getZ() + " "
                        + new BigDecimal(Math.sqrt(connection.distSqr(this.computerTE.getPos())))
                        .setScale(3, BigDecimal.ROUND_HALF_UP).toString()
                        + " m");
            }
        }
    }
