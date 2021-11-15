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

public class WindowsCreateAccountProcess extends CreateAccountProcess {

	public int page = 0;

	public WindowsCreateAccountProcess() {
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
		TextField field = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.3), 100, 10, screen) {
			@Override
			public boolean isDisabled() {
				return ((WindowsCreateAccountProcess) process).page != 0;
			}
		};
		field.setDefaultText("XXX-XXX-XXX");
		field.setProcess(this);
		elements.add(field);

		Button button1 = new Button(screen.localXToGlobal(20), screen.getRelativeY(0.8), 50, 10, screen,
				"computer.win10.later", 0) {

			@Override
			public boolean isDisabled() {
				return ((WindowsCreateAccountProcess) tickingProcess).page > 1;
			}

		};
		button1.background = false;
		button1.border = false;
		button1.textNormal = 0xffffff;
		button1.setFontSize(0.7);
		button1.setProcess(this);
		elements.add(button1);

		Button button2 = new Button(screen.localXToGlobal(screen.xSize - 50), screen.getRelativeY(0.8), 30, 8, screen,
				"computer.next", 1) {
			/*
			 * @Override public boolean isDisabled() { return
			 * ((CreateAccountProcess)process).page != 0; }
			 */
		};
		button2.normal = new ColorRGBA(0, 0.328125, 0.63671875);
		button2.textNormal = 0xffffff;
		button2.setProcess(this);
		elements.add(button2);

		TextField field2 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4), 60, 10, screen) {
			@Override
			public boolean isDisabled() {
				return ((WindowsCreateAccountProcess) process).page != 1;
			}
		};
		field2.setDefaultText("E-mail");
		field2.setProcess(this);
		elements.add(field2);

		TextField field3 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4) + 15, 60, 10, screen) {
			@Override
			public boolean isDisabled() {
				return ((WindowsCreateAccountProcess) process).page != 1;
			}
		};
		field3.setDefaultText("Password");
		field3.setProcess(this);
		elements.add(field3);

		TextField field4 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4), 60, 10, screen) {
			@Override
			public boolean isDisabled() {
				return ((WindowsCreateAccountProcess) process).page != 2;
			}
		};
		field4.setDefaultText("User name");
		field4.setProcess(this);
		elements.add(field4);

		TextField field5 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4) + 15, 60, 10, screen) {
			@Override
			public boolean isDisabled() {
				return ((WindowsCreateAccountProcess) process).page != 2;
			}
		};
		field5.setDefaultText("Password");
		field5.setProcess(this);
		elements.add(field5);

		TextField field6 = new TextField(screen.localXToGlobal(20), screen.getRelativeY(0.4) + 30, 60, 10, screen) {
			@Override
			public boolean isDisabled() {
				return ((WindowsCreateAccountProcess) process).page != 2;
			}
		};
		field6.setDefaultText("Hint");
		field6.setProcess(this);
		elements.add(field6);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack stack, float partialTicks) {

		MainWindow sr = MonitorScreen.mc.getWindow();

		RenderUtils.drawColoredRect(stack, new ColorRGBA(0, 0.328125, 0.63671875),
				(sr.getGuiScaledWidth() / 2) - (screen.xSize / 2), (sr.getGuiScaledHeight() / 2) - (screen.ySize / 2),
				screen.xSize, screen.ySize, 0);

		super.render(stack, partialTicks);
		if (page == 0) {
			GL11.glPushMatrix();
			RenderUtils.drawString(stack, SevenDaysToMine.proxy.localize("computer.win10.product_key"),
					(int) screen.localXToGlobal(20), (int) screen.getRelativeY(0.2), 0xffffff);
			GL11.glPopMatrix();
		} else if (page == 1) {
			GL11.glPushMatrix();
			RenderUtils.drawString(stack, SevenDaysToMine.proxy.localize("computer.win10.make_it_yours"),
					(int) screen.localXToGlobal(20), (int) screen.getRelativeY(0.2), 0xffffff);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScaled(0.8, 0.8, 1);
			String s = SevenDaysToMine.proxy.localize("computer.win10.make_it_yours_desc");
			RenderUtils.drawString(stack, s, (float) screen.localXToGlobal(20) + MonitorScreen.mc.font.width(s) * 0.2f,
					(float) screen.getRelativeY(0.2) + 40, 0xffffff);
			GL11.glPopMatrix();
		} else if (page == 2) {
			RenderUtils.drawString(stack, SevenDaysToMine.proxy.localize("computer.win10.create_account"),
					(int) screen.localXToGlobal(20), (int) screen.getRelativeY(0.2), 0xffffff);

			GL11.glPushMatrix();
			GL11.glScaled(0.8, 0.8, 1);
			String s = SevenDaysToMine.proxy.localize("computer.win10.create_account_desc");
			RenderUtils.drawString(stack, s, (float) screen.localXToGlobal(20) + MonitorScreen.mc.font.width(s) * 0.2f,
					(float) screen.getRelativeY(0.2) + 40, 0xffffff);
			GL11.glPopMatrix();

		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initWindow() {
		if (elements.size() == 0) {
			return;
		}

		IScreenElement element0 = elements.get(0);

		element0.setX(screen.localXToGlobal(20));
		element0.setY(screen.getRelativeY(0.3));
		element0.setWidth(100);
		element0.setHeight(10);

		IScreenElement element1 = elements.get(1);

		element1.setX(screen.localXToGlobal(20));
		element1.setY(screen.getRelativeY(0.8));
		element1.setWidth(50);
		element1.setHeight(8);

		IScreenElement element2 = elements.get(2);

		element2.setX(screen.localXToGlobal(screen.xSize - 50));
		element2.setY(screen.getRelativeY(0.8));
		element2.setWidth(30);
		element2.setHeight(8);

		IScreenElement element3 = elements.get(3);

		element3.setX(screen.localXToGlobal(20));
		element3.setY(screen.getRelativeY(0.4));
		element3.setWidth(60);
		element3.setHeight(10);

		IScreenElement element4 = elements.get(4);

		element4.setX(screen.localXToGlobal(20));
		element4.setY(screen.getRelativeY(0.4) + 15);
		element4.setWidth(60);
		element4.setHeight(10);

		IScreenElement element5 = elements.get(5);

		element5.setX(screen.localXToGlobal(20));
		element5.setY(screen.getRelativeY(0.4));
		element5.setWidth(60);
		element5.setHeight(10);

		IScreenElement element6 = elements.get(6);

		element6.setX(screen.localXToGlobal(20));
		element6.setY(screen.getRelativeY(0.4) + 15);
		element6.setWidth(60);
		element6.setHeight(10);

		IScreenElement element7 = elements.get(7);

		element7.setX(screen.localXToGlobal(20));
		element7.setY(screen.getRelativeY(0.4) + 30);
		element7.setWidth(60);
		element7.setHeight(10);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		int buttonId = button.ID;
		if (buttonId == 0) {
			if (page == 0) {
				page = 1;
				sync("page");
			} else {
				page = 2;
				sync("page");
			}
		}
		if (buttonId == 1) {
			if (page == 2 && !completed && !computerTE.isRegistered()) {
				this.completed = true;
				TextField un = (TextField) elements.get(5);
				TextField p = (TextField) elements.get(6);
				TextField h = (TextField) elements.get(7);

				this.username = un.getContentText();
				this.password = p.getContentText();
				this.hint = h.getContentText();

				sync("completed", "username", "password", "hint");
			}
		}
	}

}
