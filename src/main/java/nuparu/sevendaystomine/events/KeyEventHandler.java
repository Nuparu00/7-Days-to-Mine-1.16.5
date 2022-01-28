package nuparu.sevendaystomine.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.client.gui.GuiAnimationDebug;
import nuparu.sevendaystomine.client.gui.computer.GuiMonitor;
import nuparu.sevendaystomine.entity.CarEntity;
import nuparu.sevendaystomine.item.ItemAnalogCamera;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.CameraDimensionsMessage;
import nuparu.sevendaystomine.network.packets.HonkMessage;
import nuparu.sevendaystomine.proxy.ClientProxy;
import nuparu.sevendaystomine.util.ReloadHelper;

public class KeyEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onKeyPressed(KeyInputEvent event) {
		KeyBinding[] keyBindings = ClientProxy.keyBindings;
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		if (player != null) {
			if (keyBindings[0].isDown()) {
				ReloadHelper.tryToReload();
			} else {
				ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);
				if (!stack.isEmpty() && stack.getItem() instanceof ItemAnalogCamera) {
					if (keyBindings[1].isDown()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0.1,0,0));
					} else if (keyBindings[2].isDown()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(-0.1,0,0));
					} else if (keyBindings[3].isDown()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0.1,0));
					} else if (keyBindings[4].isDown()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,-0.1,0));
					} else if (keyBindings[5].isDown()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0,0.1));
					} else if (keyBindings[6].isDown()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0,-0.1));
					}
				}
			}
			if(keyBindings[7].isDown()) {
				Entity riding = player.getVehicle();
				if(riding != null && riding instanceof CarEntity) {
					if(riding.getPassengers().indexOf(player) == 0) {
						System.out.println("Beep beep");
						PacketManager.honk.sendToServer(new HonkMessage());
					}
				}
			}
			if(keyBindings[8].isDown()) {
				if(mc.screen == null || !(mc.screen instanceof GuiAnimationDebug)){
					mc.setScreen(new GuiAnimationDebug());
				}
			}

		}

		/*
		 * if (player != null) { Entity riding = player.getVehicle(); KeyBinding
		 * accelerate = keyBindings[1]; if (accelerate.isPressed() ||
		 * accelerate.isDown()) { if (riding != null && riding instanceof
		 * IControllable) { ((IControllable)riding).handleKey(0, (byte)1);
		 * PacketManager.controllableKeyUpdate.sendToServer(new
		 * ControllableKeyUpdateMessage(0, (byte) 1)); } } else if
		 * (!accelerate.isPressed() && !accelerate.isDown()) { if (riding != null &&
		 * riding instanceof IControllable) { ((IControllable)riding).handleKey(0,
		 * (byte)0); PacketManager.controllableKeyUpdate.sendToServer(new
		 * ControllableKeyUpdateMessage(0, (byte) 0)); } } }
		 */
	}
}
