package nuparu.sevendaystomine.network.packets;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.item.IReloadable;

public class ReloadMessage {

	public ReloadMessage() {

	}

	public static void encode(ReloadMessage msg, PacketBuffer buf) {
	}

	public static ReloadMessage decode(PacketBuffer buf) {
		return new ReloadMessage();
	}

	public static class Handler {

		private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(16);

		public static void handle(ReloadMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				ctx.get().setPacketHandled(true);
				final ServerPlayerEntity player = ctx.get().getSender();
				final World world = player.level;
				final ItemStack mainStack = player.getMainHandItem();
				final ItemStack secStack = player.getOffhandItem();

				ItemStack mainBullet = null;
				ItemStack secBullet = null;

				IReloadable reloadableMain = null;
				IReloadable reloadableSec = null;

				Item main = mainStack.getItem();
				int reloadTime = 0;

				if (main != null && main instanceof IReloadable) {
					reloadableMain = (IReloadable) main;
					mainBullet = getReloadItem(player.inventory, reloadableMain.getReloadItem(mainStack));
					if (!mainBullet.isEmpty()) {
						SoundEvent reloadSound = reloadableMain.getReloadSound();
						world.playSound(null, player.blockPosition(), reloadSound, SoundCategory.PLAYERS, 1F, 1F);
						reloadTime = reloadableMain.getReloadTime(mainStack);
						reloadableMain.onReloadStart(world, player, mainStack, reloadTime);
					}
				}
				Item sec = secStack.getItem();
				if (sec != null && sec instanceof IReloadable) {
					reloadableSec = (IReloadable) sec;
					secBullet = getReloadItem(player.inventory, reloadableSec.getReloadItem(secStack));
					if (!secBullet.isEmpty()) {
						SoundEvent reloadSound = reloadableSec.getReloadSound();
						world.playSound(null, player.blockPosition(), reloadSound, SoundCategory.PLAYERS, 1F, 1F);
						reloadTime = Math.max(reloadTime, reloadableSec.getReloadTime(secStack));
						reloadableSec.onReloadStart(world, player, secStack, reloadTime);

					}
				}

				final ItemStack mainBulletFinal = mainBullet;
				final ItemStack secBulletFinal = secBullet;
				final IReloadable reloadableMainFinal = reloadableMain;
				final IReloadable reloadableSecFinal = reloadableSec;

				ScheduledFuture<?> countdown = scheduler.schedule(() -> reload(reloadableMainFinal, reloadableSecFinal, world, player, mainStack, mainBulletFinal,
						secStack, secBulletFinal), reloadTime, TimeUnit.MILLISECONDS);
			});
		}

		public static ItemStack getReloadItem(PlayerInventory inventory, Item reloadItem) {
			ItemStack itemstack = ItemStack.EMPTY;
			for (ItemStack s : inventory.items) {
				if (s != null && s.getItem() == reloadItem) {
					itemstack = s;
					break;
				}
			}

			return itemstack;
		}

		public static void reload(IReloadable reloadableMain, IReloadable reloadableSec, World world,
				ServerPlayerEntity player, ItemStack mainStack, ItemStack mainBullet, ItemStack secStack,
				ItemStack secBullet) {
			if (reloadableMain != null) {
				reloadableMain.onReloadEnd(world, player, mainStack, mainBullet);
			}
			if (reloadableSec != null) {
				reloadableSec.onReloadEnd(world, player, secStack, secBullet);
			}
		}
	}
}
