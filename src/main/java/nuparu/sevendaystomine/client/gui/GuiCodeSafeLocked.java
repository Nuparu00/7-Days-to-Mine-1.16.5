package nuparu.sevendaystomine.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SafeCodeMessage;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

@OnlyIn(Dist.CLIENT)
public class GuiCodeSafeLocked extends Screen {

	private static final ResourceLocation resourceLocation = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/code_safe_locked.png");
	
	TileEntityCodeSafe safe;
	BlockPos pos;

	int guiLeft;
	int guiTop;
	int xSize = 110;
    int ySize = 100;

	public GuiCodeSafeLocked(TileEntity tileEntity, BlockPos pos) {
		super(new StringTextComponent("screen.code_safe.locked"));
		if (!(tileEntity instanceof TileEntityCodeSafe)) {
			throw new IllegalArgumentException("Passed TileEntity is not isntance of TileEntityCodeSafe!");
		}
		this.safe = (TileEntityCodeSafe) tileEntity;
		this.pos = pos;
	}

	public void init() {
		super.init();
		guiLeft = (this.width - xSize) / 2;
		guiTop = (this.height - ySize) / 2;
		this.buttons.clear();

		int x1 = guiLeft + xSize/2 - 26;
		int x2 = guiLeft + xSize/2 - 6;
		int x3 = guiLeft + xSize/2 + 14;
		
		int y1 = guiTop + 23;
		int y2 = guiTop + 53;
		
		this.buttons.add(new CodeButton(4, x1, y1, 12, 20, new StringTextComponent("+"), (button) -> {
			actionPerformed((CodeButton) button);
        }));
		
		this.buttons.add(new CodeButton(5, x1, y2, 12, 20, new StringTextComponent("-"), (button) -> {
			actionPerformed((CodeButton) button);
        }));

		this.buttons.add(new CodeButton(2, x2, y1, 12, 20, new StringTextComponent("+"), (button) -> {
			actionPerformed((CodeButton) button);
        }));
		this.buttons.add(new CodeButton(3, x2, y2, 12, 20, new StringTextComponent("-"), (button) -> {
			actionPerformed((CodeButton) button);
        }));

		this.buttons.add(new CodeButton(0, x3, y1, 12, 20, new StringTextComponent("+"), (button) -> {
			actionPerformed((CodeButton) button);
        }));
		this.buttons.add(new CodeButton(1, x3, y2, 12, 20, new StringTextComponent("-"), (button) -> {
			actionPerformed((CodeButton) button);
        }));

	}

	public boolean isPauseScreen() {
		return false;
	}

	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		this.drawGuiContainerBackgroundLayer(matrix,partialTicks, mouseX, mouseY);
		super.render(matrix,mouseX, mouseY, partialTicks);
		this.drawGuiContainerForegroundLayer(matrix,mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(MatrixStack matrix,float particalTicks, int mouseX, int mouseY) {
		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bind(resourceLocation);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		blit(matrix,marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		RenderSystem.popMatrix();
	}

	protected void drawGuiContainerForegroundLayer(MatrixStack matrix,int mouseX, int mouseY) {
		int selectedCode = safe.getSelectedCode();

		int h = (selectedCode / 100) % 10;
		int d = (selectedCode / 10) % 10;
		int u = selectedCode % 10;
		
		int x1 = guiLeft + xSize/2 - 23;
		int x2 = guiLeft + xSize/2 - 3;
		int x3 = guiLeft + xSize/2 + 17;
		
		int y1 = guiTop + 15;
		int y2 = guiTop + 45;
		int y3 = guiTop + 75;
		
		this.font.draw(matrix,String.valueOf(h), x1, y2, 0xffffff);
		this.font.draw(matrix,String.valueOf(d), x2, y2, 0xffffff);
		this.font.draw(matrix,String.valueOf(u), x3, y2, 0xffffff);
		
		this.font.draw(matrix,String.valueOf(h==9?0:h+1), x1, y1, 0x555555);
		this.font.draw(matrix,String.valueOf(d==9?0:d+1), x2, y1, 0x555555);
		this.font.draw(matrix,String.valueOf(u==9?0:u+1), x3, y1, 0x555555);
		
		this.font.draw(matrix,String.valueOf(h==0?9:h-1), x1, y3, 0x555555);
		this.font.draw(matrix,String.valueOf(d==0?9:d-1), x2, y3, 0x555555);
		this.font.draw(matrix,String.valueOf(u==0?9:u-1), x3, y3, 0x555555);
	}
	
	protected void actionPerformed(CodeButton button)
	{
		int buttonID = button.id;
		if(buttonID >= 0 && buttonID <= 5) {
			
			int multpleOfTwo = buttonID/2;
			
			int toAdd = (int) Math.pow(10, multpleOfTwo+1);
			if(buttonID%2==0) {
				
			}else {
				toAdd = -toAdd;
			}
			PacketManager.safeCodeUpdate.sendToServer(new SafeCodeMessage(pos,toAdd));
			TileEntity te = Minecraft.getInstance().level.getBlockEntity(pos);
			if(te != null && te instanceof TileEntityCodeSafe) {
				safe = (TileEntityCodeSafe)te;
			}
		}
	}
	
	public static class CodeButton extends Button{

		public int id;
		
		public CodeButton(int id, int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_,
				ITextComponent p_i232255_5_, IPressable p_i232255_6_) {
			super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
			this.id = id;
		}
		
	}

}
