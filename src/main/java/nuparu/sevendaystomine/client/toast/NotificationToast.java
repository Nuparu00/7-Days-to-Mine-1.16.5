package nuparu.sevendaystomine.client.toast;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModSounds;

@OnlyIn(Dist.CLIENT)
public class NotificationToast implements IToast {

	private boolean playedSound = false;
	private ItemStack stack;
	private ITextComponent title;
	private ITextComponent desc;

	public NotificationToast(ItemStack stack, ITextComponent title, ITextComponent desc) {
		this.stack = stack;
		this.title = title;
		this.desc = desc;
	}

	public IToast.Visibility render(MatrixStack p_230444_1_, ToastGui p_230444_2_, long p_230444_3_) {
		Minecraft mc = p_230444_2_.getMinecraft();
		p_230444_2_.getMinecraft().getTextureManager().bind(TEXTURE);
		RenderSystem.color3f(1.0F, 1.0F, 1.0F);
		p_230444_2_.blit(p_230444_1_, 0, 0, 0, 0, this.width(), this.height());
		List<IReorderingProcessor> list = mc.font
				.split(desc, 125);
		int i = 16776960;
		if (list.size() == 1) {
			mc.font.draw(p_230444_1_, title, 30.0F, 7.0F, i | -16777216);
			mc.font.draw(p_230444_1_, list.get(0), 30.0F, 18.0F, -1);
		} else {
			int j = 1500;
			float f = 300.0F;
			if (p_230444_3_ < 1500L) {
				int k = MathHelper
						.floor(MathHelper.clamp((float) (1500L - p_230444_3_) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24
						| 67108864;
				mc.font.draw(p_230444_1_, desc, 30.0F, 11.0F, i | k);
			} else {
				int i1 = MathHelper
						.floor(MathHelper.clamp((float) (p_230444_3_ - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24
						| 67108864;
				int l = this.height() / 2 - list.size() * 9 / 2;

				for (IReorderingProcessor ireorderingprocessor : list) {
					mc.font.draw(p_230444_1_, ireorderingprocessor, 30.0F, (float) l,
							16777215 | i1);
					l += 9;
				}
			}
		}

		if (!this.playedSound && p_230444_3_ > 0L) {
			this.playedSound = true;
			p_230444_2_.getMinecraft().getSoundManager()
					.play(SimpleSound.forUI(ModSounds.RECIPE_UNLOCKED.get(), 1.0F, 1.0F));
		}

		p_230444_2_.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(stack, 8, 8);
		return p_230444_3_ >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;

	}
}