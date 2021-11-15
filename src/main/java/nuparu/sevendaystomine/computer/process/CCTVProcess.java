package nuparu.sevendaystomine.computer.process;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.tileentity.TileEntityCamera;
import nuparu.sevendaystomine.util.ColorRGBA;

public class CCTVProcess extends WindowedProcess {

	public int camera = 0;
	public List<TileEntityCamera> cameras = new ArrayList<TileEntityCamera>();

	@OnlyIn(Dist.CLIENT)
	Button button1;
	@OnlyIn(Dist.CLIENT)
	Button button2;

	public CCTVProcess() {
		this(0, 0, 0, 0);
	}

	public CCTVProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("cctv");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix, float partialTicks) {
		drawWindow(matrix,getTitle(), new ColorRGBA(0.1d, 0.1d, 0.1d), new ColorRGBA(0.8, 0.8, 0.8));
		MainWindow r = MonitorScreen.mc.getWindow();
		double scale = r.getGuiScale();
/*
		GL11.glPushMatrix();
		int yy = r.getScreenWidth() - (int) Math.round(y * scale) - (int) Math.round(height * scale);
		if (cameras.size() > camera && cameras.get(camera) != null) {
			EntityCameraView view = cameras.get(camera).getCameraView(screen.gui.player);
			if (view != null) {
				RenderUtils.renderView(MonitorScreen.mc, view, (int) Math.round(width * scale),
						(int) Math.round((height - (MonitorScreen.screen.ySize * title_bar_height)) * scale),
						r.getScreenWidth(), r.getScreenHeight(), (int) Math.round(x * scale), yy, -2000, partialTicks);
				GL11.glPopMatrix();
			}
			else if(isNotHidden((int) (x + width / 2), (int) (y + (MonitorScreen.screen.ySize * title_bar_height) + 2))) {
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glTranslated(0, 0, 10);
			GL11.glTranslated(x + width / 2, y + (MonitorScreen.screen.ySize * title_bar_height) + 2, 0);
			GL11.glScaled(0.75, 0.75, 1);
			RenderUtils.drawCenteredString(matrix,SevenDaysToMine.proxy.localize("computer.app.cctv.no.signal"), 0, 0,
					0xffffff, true);
			GL11.glPopMatrix();
			}
		}

		super.render(matrix,partialTicks);
		GL11.glPushMatrix();
		GL11.glTranslated(0, 0, 10);
		if (cameras.size() > camera
				&& isNotHidden((int) (x + 1), (int) (y + (MonitorScreen.screen.ySize * title_bar_height) + 2))) {
			RenderUtils.drawString(matrix,cameras.get(camera).getCustomName(), x + 1,
					y + (MonitorScreen.screen.ySize * title_bar_height) + 2, 0xffffff, true);
		} else if (cameras.isEmpty()
				&& isNotHidden((int) (x + width / 2), (int) (y + (MonitorScreen.screen.ySize * title_bar_height) + 2))) {
			GL11.glTranslated(x + width / 2, y + (MonitorScreen.screen.ySize * title_bar_height) + 2, 0);
			GL11.glScaled(0.75, 0.75, 1);
			RenderUtils.drawCenteredString(matrix,SevenDaysToMine.proxy.localize("computer.app.cctv.no.camera"), 0, 0,
					0xffffff, true);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();*/
	}

	@Override
	public void tick() {
		super.tick();
		if (camera < 0)
			camera = 0;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientTick() {
		super.clientTick();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientInit() {
		super.clientInit();
		for (BlockPos pos : getTE().getConnections()) {
			TileEntity te = getTE().getLevel().getBlockEntity(pos);
			if (te != null && te instanceof TileEntityCamera) {
				cameras.add((TileEntityCamera) te);
			}
		}

		button1 = new Button(x + 1, y + height - 16, 10, 10, MonitorScreen.screen, "<<", 1) {
			@Override
			public boolean isVisible() {
				return this.tickingProcess != null
						&& ((WindowedProcess) this.tickingProcess).isNotHidden((int) this.x, (int) this.y);
			}
		};
		button1.background = false;
		button1.border = false;
		button1.textNormal = 0xffffff;
		button1.setZLevel(20);
		button1.setFontSize(0.7);
		button1.setProcess(this);
		elements.add(button1);

		button2 = new Button(x + width - 11, y + height - 16, 10, 10, MonitorScreen.screen, ">>", 2) {
			@Override
			public boolean isVisible() {
				return this.tickingProcess != null
						&& ((WindowedProcess) this.tickingProcess).isNotHidden((int) this.x, (int) this.y);
			}
		};
		button2.background = false;
		button2.border = false;
		button2.textNormal = 0xffffff;
		button2.setZLevel(20);
		button2.setFontSize(0.7);
		button2.setProcess(this);
		elements.add(button2);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initWindow() {
		super.initWindow();
		if (elements.size() < 2) {
			return;
		}
		button1.setX(x + 1);
		button1.setY(y + height - 16);

		button2.setX(x + width - 11);
		button2.setY(y + height - 16);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onDragReleased() {
		super.onDragReleased();
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.putInt("cam", camera);
		return nbt;
	}

	@Override
	public void readFromNBT(CompoundNBT nbt) {
		super.readFromNBT(nbt);
		camera = nbt.getInt("cam");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		super.onButtonPressed(button, mouseButton);
		if (isMinimized())
			return;
		int buttonId = button.ID;
		if (buttonId == 1) {
			if (camera - 1 < 0) {
				camera = Math.max(0, cameras.size() - 1);
			} else {
				--camera;
			}
			sync();
		} else if (buttonId == 2) {
			if (camera + 1 > cameras.size() - 1) {
				camera = 0;
			} else {
				++camera;
			}
			sync();
		}

	}
}
