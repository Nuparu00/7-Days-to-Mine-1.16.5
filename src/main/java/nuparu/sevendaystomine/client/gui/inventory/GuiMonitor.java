package nuparu.sevendaystomine.client.gui.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.inventory.block.ContainerMonitor;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;

@OnlyIn(Dist.CLIENT)
public class GuiMonitor extends ContainerScreen<ContainerMonitor> implements IGuiEventListener {

	private MonitorScreen screen = new MonitorScreen(this);
	private ContainerMonitor container;
	private TileEntityMonitor monitorTE;
	public PlayerEntity player;

	public GuiMonitor(ContainerMonitor container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.container = container;
		this.monitorTE = container.getTileEntity();
		this.player = playerInventory.player;
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		super.render(matrix,mouseX,mouseY,partialTicks);
		screen.mouseX = mouseX;
		screen.mouseY = mouseY;


		screen.render(matrix,partialTicks);
		/*
		if (TileEntityCamera.TEST != null) {
			RenderUtils.renderView(Minecraft.getInstance(), TileEntityCamera.TEST.getCameraView(player));
		}
		else {

		}*/
	}

	@Override
	protected void renderLabels(MatrixStack matrix, int p_230451_2_, int p_230451_3_) {
		/*for some unknown reason the screen does not render if there is not some text rendered, so for now we just render a dot outside
		 *of the physical screen, though it might be a good idea to examine why is this the case
		 */
		this.font.draw(matrix,".",-Integer.MAX_VALUE,-Integer.MAX_VALUE,0);
	}

	@Override
	protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

	}

	@Override
	public void tick() {
		super.tick();
		screen.update();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void init() {
		screen.setScale(minecraft.getWindow());
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		screen.mouseClicked(mouseX, mouseY, mouseButton);
		return true;
	}

	public boolean charTyped(char typedChar, int keyCode){
		screen.keyTyped(typedChar, keyCode);
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
		screen.keyPressed(keyCode,p_231046_2_,p_231046_3_);
		if (keyCode == 256 && this.shouldCloseOnEsc()) {
			this.onClose();
			return true;
		} else if (keyCode == 258) {
			boolean flag = !hasShiftDown();
			if (!this.changeFocus(flag)) {
				this.changeFocus(flag);
			}

			return false;
		}
		return true;
	}

	@Override
	public boolean keyReleased(int keyCode, int p_223281_2_, int p_223281_3_){
		screen.keyReleased(keyCode,p_223281_2_,p_223281_3_);
		return super.keyReleased(keyCode,p_223281_2_,p_223281_3_);
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		screen.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY) {
		screen.mouseClickMove(mouseX, mouseY, clickedMouseButton,deltaX,deltaY);
		return true;
	}

	public TileEntityMonitor getMonitor() {
		return monitorTE;
	}

	public TileEntityComputer getComputer() {
		return monitorTE != null ? monitorTE.getComputer() : null;
	}

	public boolean shouldDisplayAnything() {
		return getComputer() != null && getComputer().isOn() && monitorTE != null && monitorTE.getState()
				&& getComputer().isCompleted();
	}

	/*
	 *Please, please never use this!!! This has to be in here so JEI does not render its stuff over the GUI!!!!!
	 */
	@Override
	public Minecraft getMinecraft() {
		return null;
	}
}
