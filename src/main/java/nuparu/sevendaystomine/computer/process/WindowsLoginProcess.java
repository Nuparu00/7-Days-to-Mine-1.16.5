package nuparu.sevendaystomine.computer.process;

import net.minecraft.client.MainWindow;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumState;
import nuparu.sevendaystomine.util.ColorRGBA;

public class WindowsLoginProcess extends TickingProcess {


	public final ResourceLocation DEFAULT_PROFILE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/computer/default_profile.png");

	public String password = "";
	public boolean completed = false;

	public WindowsLoginProcess() {

	}

	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT nbt2 = super.save(nbt);
		nbt2.putString("password", password);
		nbt2.putBoolean("completed", completed);
		return nbt2;
	}

	public void readFromNBT(CompoundNBT nbt) {
		super.readFromNBT(nbt);
		if (nbt.contains("password")) {
			this.password = nbt.getString("password");
		}
		if (nbt.contains("completed")) {
			this.completed = nbt.getBoolean("completed");
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (computerTE.getState() != EnumState.LOGIN) {
			computerTE.killProcess(this);
			return;
		}
		if (completed) {

			if (computerTE != null && computerTE.verifyPassword(this.password)) {
				computerTE.onLogin(this);
			} else {
				completed = false;
				password = "";
			}
			computerTE.setChanged();
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientInit() {
		super.clientInit();
		double fieldWidth = screen.xSize / 3d;
		double buttonWidth = screen.xSize / 4d;
		double iconSize = screen.ySize / 3d;
		TextField field = new TextField((float) screen.getRelativeX(0.5) - (fieldWidth / 2d),
				(float) (screen.getRelativeY(0) + 25 + iconSize), fieldWidth, 9, screen);
		field.setDefaultText("Password");
		field.setProcess(this);
		elements.add(field);

		Button button = new Button((float) screen.getRelativeX(0.5) - (buttonWidth / 2d),
				(float) (screen.getRelativeY(0) + 60 + iconSize), buttonWidth, 9, screen, "computer.win10.login", 0);
		button.textNormal = 0xffffff;
		button.normal = new ColorRGBA(0, 0.328125, 0.63671875);
		button.setFontSize(0.7);
		button.setProcess(this);
		elements.add(button);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix, float partialTicks) {

		MainWindow sr = MonitorScreen.mc.getWindow();

		RenderUtils.drawColoredRect(matrix, new ColorRGBA(0, 0.328125, 0.63671875),
				(sr.getGuiScaledWidth() / 2) - (screen.xSize / 2), (sr.getGuiScaledHeight() / 2) - (screen.ySize / 2),
				screen.xSize, screen.ySize, 0);
		super.render(matrix, partialTicks);
		RenderUtils.drawString(matrix, SevenDaysToMine.proxy.localize("computer.win10.welcome_back"),
				(int) screen.localXToGlobal(20), (int) screen.getRelativeY(0.15), 0xffffff);

		double iconSize = screen.ySize / 3d;
		RenderUtils.drawCenteredString(matrix, computerTE.getUsername(), (float) screen.getRelativeX(0.5),
				(float) (screen.getRelativeY(0) + 10 + iconSize), 0xffffff);
		RenderUtils.drawCenteredString(matrix, "Hint: " + computerTE.getHint(), (float) screen.getRelativeX(0.5),
				(float) (screen.getRelativeY(0) + 45 + iconSize), 0xffffff);

		RenderSystem.pushMatrix();
		RenderSystem.enableAlphaTest();
		RenderUtils.drawTexturedRect(matrix,DEFAULT_PROFILE, (float) screen.getRelativeX(0.5) - (iconSize / 2d),
				(float) screen.getRelativeY(0) + 10, 0, 0, iconSize, iconSize, iconSize, iconSize, 1, 1);
		RenderSystem.disableAlphaTest();
		RenderSystem.popMatrix();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initWindow() {
		if (elements.size() == 0) {
			return;
		}
		double fieldWidth = screen.xSize / 3d;
		double buttonWidth = screen.xSize / 4d;
		double iconSize = screen.ySize / 3d;

		IScreenElement element0 = elements.get(0);

		element0.setX((float) screen.getRelativeX(0.5) - (fieldWidth / 2d));
		element0.setY((float) (screen.getRelativeY(0) + 25 + iconSize));
		element0.setWidth(fieldWidth);
		element0.setHeight(9);

		IScreenElement element1 = elements.get(1);

		element1.setX((float) screen.getRelativeX(0.5) - (buttonWidth / 2d));
		element1.setY((float) (screen.getRelativeY(0) + 60 + iconSize));
		element1.setWidth(buttonWidth);
		element1.setHeight(9);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		int buttonId = button.ID;
		if (buttonId == 0) {
			if (computerTE.isRegistered() == true && computerTE.getState() == EnumState.LOGIN && completed == false) {
				TextField p = (TextField) elements.get(0);
				this.password = p.getContentText();
				this.completed = true;
				sync("password", "completed");
				p.setContentText("");
				this.completed = false;

			}
		}
	}
}
