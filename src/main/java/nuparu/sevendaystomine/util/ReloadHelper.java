package nuparu.sevendaystomine.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.item.IReloadable;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.ReloadMessage;

@OnlyIn(Dist.CLIENT)
public class ReloadHelper {
	
	private static final Minecraft mc = Minecraft.getInstance();
	
	@OnlyIn(Dist.CLIENT)
	public static void tryToReload() {
		PlayerEntity player = mc.player;
		if(player == null) return;
		
		ItemStack mainStack = player.getMainHandItem();
		ItemStack secStack = player.getOffhandItem();
		if(mainStack.isEmpty() && secStack.isEmpty()) return;
		
		Item mainItem = mainStack.getItem();
		Item secItem = secStack.getItem();
		if(mainItem instanceof IReloadable || secItem instanceof IReloadable) {
			PacketManager.gunReload.sendToServer(new ReloadMessage());
		}
	}
}
