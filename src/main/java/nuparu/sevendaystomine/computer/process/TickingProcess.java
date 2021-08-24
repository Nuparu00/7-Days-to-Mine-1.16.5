package nuparu.sevendaystomine.computer.process;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.ShortNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncProcessMessage;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.util.ColorRGBA;

public abstract class TickingProcess {

	protected TileEntityComputer computerTE;

	public boolean noTimeLimit = false;
	public long existedFor = 0;
	public long duration = 0;

	protected ArrayList<IScreenElement> elements = new ArrayList<IScreenElement>();

	@OnlyIn(Dist.CLIENT)
	protected MonitorScreen screen;
	@OnlyIn(Dist.CLIENT)
	public boolean clientInit;

	protected UUID id;

	public TickingProcess() {
		id = UUID.randomUUID();
	}

	@OnlyIn(Dist.CLIENT)
	public void clientInit() {
		clientInit = true;
	}

	@OnlyIn(Dist.CLIENT)
	public void clientTick() {
		if (!clientInit)
			return;
		for (IScreenElement element : elements) {
			element.update();
		}
	}

	public void tick() {
		existedFor++;
	}

	public void onOpen() {

	}

	public void onClose() {

	}

	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {

	}

	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix, float partialTicks) {
		for (IScreenElement element : elements) {
			RenderSystem.pushMatrix();
			element.render(matrix, partialTicks);
			RenderSystem.popMatrix();
		}
	}

	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putString(ProcessRegistry.RES_KEY, ProcessRegistry.INSTANCE.getResByClass(this.getClass()).toString());
		nbt.putBoolean("noTimeLimit", noTimeLimit);
		nbt.putLong("existedFor", existedFor);
		nbt.putLong("duration", duration);
		nbt.putUUID("id", id);
		return nbt;
	}

	public void readFromNBT(CompoundNBT nbt) {
		if (nbt.contains("noTimeLimit")) {
			noTimeLimit = nbt.getBoolean("noTimeLimit");
		}
		if (nbt.contains("existedFor")) {
			existedFor = nbt.getLong("existedFor");
		}
		if (nbt.contains("duration")) {
			duration = nbt.getLong("duration");
		}
		id = nbt.getUUID("id");

	}

	public void setComputer(TileEntityComputer computerTE) {
		this.computerTE = computerTE;
	}

	@OnlyIn(Dist.CLIENT)
	public void addElement(IScreenElement element) {
		elements.add(element);
	}

	@OnlyIn(Dist.CLIENT)
	public void removeElement(IScreenElement element) {
		elements.remove(element);
	}

	public UUID getId() {
		return id;
	}

	@OnlyIn(Dist.CLIENT)
	public MonitorScreen getScreen() {
		return screen;
	}

	@OnlyIn(Dist.CLIENT)
	public void setScreen(MonitorScreen screen) {
		this.screen = screen;
	}

	@OnlyIn(Dist.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		for (IScreenElement element : elements) {
			element.keyTyped(typedChar, keyCode);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseReleased(int mouseX, int mouseY, int state) {
		for (IScreenElement element : elements) {
			element.mouseReleased(mouseX, mouseY, state);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double deltaX, double deltaY) {
		for (IScreenElement element : elements) {
			element.mouseClickMove(mouseX, mouseY, clickedMouseButton, deltaX, deltaY);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		for (IScreenElement element : elements) {
			element.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void initWindow() {

	}

	public TileEntityComputer getTE() {
		return this.computerTE;
	}

	@OnlyIn(Dist.CLIENT)
	public void sync() {
		
		CompoundNBT nbt = save(new CompoundNBT());
		PacketManager.syncProcess.sendToServer(new SyncProcessMessage(computerTE.getPos(), nbt));

	}

	@OnlyIn(Dist.CLIENT)
	public void sync(String... fields) {
		
		CompoundNBT nbt = new CompoundNBT();
		for (String s : fields) {
			Class<?> clazz = this.getClass();
			Field f = null;
			while (f == null && clazz != null) // stop when we got field or reached top of class hierarchy
			{
				try {
					f = clazz.getDeclaredField(s);
				} catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				}
			}

			if (f == null)
				continue;
			try {
				f.setAccessible(true);
				Object fieldValue;

				fieldValue = f.get(this);

				if (fieldValue == null)
					continue;
				if (fieldValue instanceof Integer) {
					nbt.putInt(s, (int) fieldValue);
				} else if (fieldValue instanceof Double) {
					nbt.putDouble(s, (double) fieldValue);
				} else if (fieldValue instanceof Float) {
					nbt.putFloat(s, (float) fieldValue);
				} else if (fieldValue instanceof Long) {
					nbt.putLong(s, (long) fieldValue);
				} else if (fieldValue instanceof String) {
					nbt.putString(s, (String) fieldValue);
				} else if (fieldValue instanceof Boolean) {
					nbt.putBoolean(s, (Boolean) fieldValue);
				} else if (fieldValue instanceof Byte) {
					nbt.putByte(s, (Byte) fieldValue);
				} else if (fieldValue instanceof Short) {
					nbt.putShort(s, (short) fieldValue);
				}

				else if (fieldValue instanceof List) {
					List<?> list = (List<?>) fieldValue;
					if (list.isEmpty())
						continue;
					Object first = list.get(0);

					ListNBT nbtList = new ListNBT();
					if (first instanceof Integer) {
						for (Object i : list) {
							nbtList.add(IntNBT.valueOf((int) i));
						}
					} else if (first instanceof Double) {
						for (Object i : list) {
							nbtList.add(DoubleNBT.valueOf((double) i));
						}
					} else if (first instanceof Float) {
						for (Object i : list) {
							nbtList.add(FloatNBT.valueOf((float) i));
						}
					} else if (first instanceof Long) {
						for (Object i : list) {
							nbtList.add(LongNBT.valueOf((long) i));
						}
					} else if (first instanceof String) {
						for (Object i : list) {
							nbtList.add(StringNBT.valueOf((String) i));
						}
					} else if (fieldValue instanceof Boolean) {
						for (Object i : list) {
							nbtList.add(ByteNBT.valueOf((byte) i));
						}
					} else if (first instanceof Byte) {
						for (Object i : list) {
							nbtList.add(ByteNBT.valueOf((byte) i));
						}
					} else if (first instanceof Short) {
						for (Object i : list) {
							nbtList.add(ShortNBT.valueOf((short) i));
						}
					}
					nbt.put(s, nbtList);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		nbt.putUUID("id", id);
		nbt.putString(ProcessRegistry.RES_KEY, ProcessRegistry.INSTANCE.getResByClass(this.getClass()).toString());
		if (computerTE != null && nbt != null) {
			PacketManager.syncProcess.sendToServer(new SyncProcessMessage(computerTE.getPos(), nbt));
			// 
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void renderTooltip(MatrixStack stack, int mouseX, int mouseY, FontRenderer font, Object...text) {
		int height = text.length * (font.lineHeight + 1);
		int width = 0;
		for (Object s : text) {
			int i = font.width(s.toString());
			if (i > width) {
				width = i;
			}
		}


		RenderUtils.drawColoredRect(stack, new ColorRGBA(1d, 1d, 1d), screen.mouseX, screen.mouseY, width, height, 1);
		GL11.glTranslatef(0, 0, 1);
		for (int i = 0; i < text.length; i++) {
			RenderUtils.drawString(stack, text[i].toString(), screen.mouseX, screen.mouseY + 1 + i * (font.lineHeight + 1),
					0x000000);
		}
		GL11.glTranslatef(0, 0, -1);
	}

	public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
		for (IScreenElement element : elements) {
			element.keyPressed(keyCode,p_231046_2_,p_231046_3_);
		}
		return false;
	}

	public boolean keyReleased(int keyCode, int p_223281_2_, int p_223281_3_){
		for (IScreenElement element : elements) {
			element.keyReleased(keyCode,p_223281_2_,p_223281_3_);
		}
		return false;
	}
}
