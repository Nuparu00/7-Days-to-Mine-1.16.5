package nuparu.sevendaystomine.computer.process;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.ColorRGBA;

public class MacCreateAccountProcess extends CreateAccountProcess {

	public int page = 0;

	public MacCreateAccountProcess() {
		super();
	}

	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT nbt2 = super.save(nbt);
		nbt2.putInt("page", page);
		return nbt2;
	}

	public void readFromNBT(CompoundNBT nbt) {
		super.readFromNBT(nbt);
		this.page = nbt.getInt("page");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientInit() {
		super.clientInit();
		TextField field4 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4), 60, 9, screen);
		field4.setDefaultText("User name");
		field4.setProcess(this);
		elements.add(field4);

		TextField field5 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4) + 15, 60, 9, screen);
		field5.setDefaultText("Password");
		field5.setProcess(this);
		elements.add(field5);

		TextField field6 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4) + 30, 60, 9, screen);
		field6.setDefaultText("Hint");
		field6.setProcess(this);
		elements.add(field6);

		Button button2 = new Button(screen.localXToGlobal(screen.xSize - 50), screen.getRelativeY(0.8), 30, 8, screen,
				"computer.next", 0) {
			/*
			 * @Override public boolean isDisabled() { return
			 * ((CreateAccountProcess)process).page != 0; }
			 */
		};
		button2.normal = new ColorRGBA(200, 200, 200);
		button2.textNormal = 0x0a0a0a;
		button2.setProcess(this);
		elements.add(button2);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack stack,float partialTicks) {

		MainWindow sr = MonitorScreen.mc.getWindow();

		RenderUtils.drawColoredRect(stack,new ColorRGBA(200, 200, 200), (sr.getGuiScaledWidth() / 2) - (screen.xSize / 2),
				(sr.getGuiScaledHeight() / 2) - (screen.ySize / 2), screen.xSize, screen.ySize, 0);

		super.render(stack,partialTicks);

		RenderUtils.drawCenteredString(stack,SevenDaysToMine.proxy.localize("computer.mac.create_account"),
				(int) screen.getRelativeX(0.5), (int) screen.getRelativeY(0.2), 0x0a0a0a);

		GL11.glPushMatrix();
		GL11.glScaled(0.8, 0.8, 1);
		String s = SevenDaysToMine.proxy.localize("computer.mac.create_account_desc");
		RenderUtils.drawString(stack,s, (float) screen.localXToGlobal(40),
				(float) screen.getRelativeY(0.2) + 40, 0x0a0a0a);
		GL11.glPopMatrix();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initWindow() {
		if (elements.size() == 0) {
			return;
		}

		IScreenElement element0 = elements.get(0);

		element0.setX(screen.localXToGlobal(20));
		element0.setY(screen.getRelativeY(0.4));
		element0.setWidth(60);
		element0.setHeight(9);

		IScreenElement element1 = elements.get(1);

		element1.setX(screen.localXToGlobal(20));
		element1.setY(screen.getRelativeY(0.4) + 15);
		element1.setWidth(60);
		element1.setHeight(9);

		IScreenElement element2 = elements.get(2);

		element2.setX(screen.localXToGlobal(20));
		element2.setY(screen.getRelativeY(0.4) + 30);
		element2.setWidth(60);
		element2.setHeight(9);

		IScreenElement element3 = elements.get(3);

		element3.setX(screen.localXToGlobal(screen.xSize - 50));
		element3.setY(screen.getRelativeY(0.8));
		element3.setWidth(30);
		element3.setHeight(8);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		int buttonId = button.ID;
		if (buttonId == 0) {
			
			if (page == 0 && !completed && !computerTE.isRegistered()) {
				this.completed = true;
				TextField un = (TextField) elements.get(0);
				TextField p = (TextField) elements.get(1);
				TextField h = (TextField) elements.get(2);

				this.username = un.getContentText();
				this.password = p.getContentText();
				this.hint = h.getContentText();

				sync("completed", "username", "password", "hint");
			}
		}
	}

}
