package nuparu.sevendaystomine.util;

import java.util.HashMap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncInventoryMessage;
import nuparu.sevendaystomine.util.item.InventoryCache;
import nuparu.sevendaystomine.util.item.ItemCache;

public class PlayerInventorySyncHelper {

	public static HashMap<String, ItemCache> itemsCache = new HashMap<String, ItemCache>();

	// Map of players and their inventories
	public static HashMap<String, InventoryCache> inventoryCache = new HashMap<String, InventoryCache>();

	@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
	public static class InventoryDetectionHandler {
		@SubscribeEvent
		public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
			if (!event.getEntityLiving().level.isClientSide()) {
				if (ServerConfig.renderPlayerInventory.get()) {
					if (event.getEntityLiving() instanceof PlayerEntity) {
						PlayerEntity player = (PlayerEntity) event.getEntityLiving();
						ItemStack[] inventory = new ItemStack[player.inventory.items.size()];
						inventory = player.inventory.items.toArray(inventory);
						ItemStack selected = player.inventory.getSelected();
						ItemStack backpack = ItemStack.EMPTY;
						ExtendedInventory inv = (ExtendedInventory) CapabilityHelper.getExtendedInventory(player);

						if (inv != null) {
							backpack = inv.getStackInSlot(0);
						}

						if (inventoryCache.containsKey(player.getName().getString())) {
							InventoryCache cache = inventoryCache.get(player.getName().getString());
							if (areInventoriesEqual(inventory, cache.inventory)
									&& ItemStack.isSame(selected, cache.selected)
									&& ItemStack.isSame(backpack, cache.backpack)
									&& player.inventory.selected == cache.index) {
								return;
							}

							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), selected, player.inventory.selected);
							items.backpack = backpack;

							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName().getString());
							
							
							PacketManager.sendToTrackingEntity(PacketManager.syncInventory, message, () -> player);
							PacketManager.sendTo(PacketManager.syncInventory, message, (ServerPlayerEntity) player);

							inventoryCache.put(player.getName().getString(), new InventoryCache(inventory.clone(), selected,
									backpack, player.inventory.selected));

						} else {
							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), selected, player.inventory.selected);
							items.backpack = backpack;

							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName().getString());
							PacketManager.sendToTrackingEntity(PacketManager.syncInventory, message, () -> player);
							PacketManager.sendTo(PacketManager.syncInventory, message, (ServerPlayerEntity) player);

							inventoryCache.put(player.getName().getString(), new InventoryCache(inventory.clone(), selected,
									backpack, player.inventory.selected));
						}
					}
				}
			}
		}

		@SubscribeEvent
		public static void onEntityJoin(EntityJoinWorldEvent event) {
			if (!event.getEntity().level.isClientSide()) {
				if (ServerConfig.renderPlayerInventory.get()) {
					if (event.getEntity() instanceof PlayerEntity) {
						PlayerEntity player = (PlayerEntity) event.getEntity();

						ItemStack[] inventory = new ItemStack[player.inventory.items.size()];
						inventory = player.inventory.items.toArray(inventory);
						ItemStack selected = player.inventory.getSelected();
						ItemStack backpack = ItemStack.EMPTY;
						ExtendedInventory inv = (ExtendedInventory) CapabilityHelper.getExtendedInventory(player);

						if (inv != null) {
							backpack = inv.getStackInSlot(0);
						}

						if (inventoryCache.containsKey(player.getName().getString())) {

							InventoryCache cache = inventoryCache.get(player.getName().getString());

							if (areInventoriesEqual(inventory, cache.inventory)
									&& ItemStack.matches(selected, cache.selected)
									&& ItemStack.matches(backpack, cache.backpack)
									&& player.inventory.selected == cache.index) {
								return;
							}

							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), selected, player.inventory.selected);
							items.backpack = backpack;

							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName().getString());
	
							PacketManager.sendToTrackingEntity(PacketManager.syncInventory, message, () -> player);
							PacketManager.sendTo(PacketManager.syncInventory, message, (ServerPlayerEntity) player);

							inventoryCache.put(player.getName().getString(), new InventoryCache(inventory.clone(), selected,
									backpack, player.inventory.selected));

						} else {

							ItemCache items = new ItemCache();

							items.selectCorrectItems(inventory.clone(), selected, player.inventory.selected);
							items.backpack = backpack;
							SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName().getString());
							PacketManager.sendToTrackingEntity(PacketManager.syncInventory, message, () -> player);
							PacketManager.sendTo(PacketManager.syncInventory, message, (ServerPlayerEntity) player);

							inventoryCache.put(player.getName().getString(), new InventoryCache(inventory.clone(), selected,
									backpack, player.inventory.selected));
						}
					}
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerStartedTracking(PlayerEvent.StartTracking event) {
			if (!event.getEntity().level.isClientSide()) {
				if (ServerConfig.renderPlayerInventory.get()) {
					if (event.getEntity() instanceof PlayerEntity && event.getTarget() instanceof PlayerEntity) {
						PlayerEntity player = (PlayerEntity) event.getTarget();
						PlayerEntity trackingPlayer = (PlayerEntity) event.getEntity();
						ItemStack[] inventory = new ItemStack[player.inventory.items.size()];
						inventory = player.inventory.items.toArray(inventory);
						ItemStack selected = player.inventory.getSelected();
						ItemCache items = new ItemCache();
						ExtendedInventory inv = (ExtendedInventory) CapabilityHelper.getExtendedInventory(player);

						items.selectCorrectItems(inventory.clone(), selected, player.inventory.selected);
						if (inv != null) {
							items.backpack = inv.getStackInSlot(0);
						}
						SyncInventoryMessage message = new SyncInventoryMessage(items, player.getName().getString());
						PacketManager.sendTo(PacketManager.syncInventory, message, (ServerPlayerEntity) trackingPlayer);

					}
				}
			}
		}

		public static boolean areInventoriesEqual(ItemStack[] a, ItemStack[] b) {
			if (a == null || b == null) {
				return false;
			}
			for (int i = 0; i < a.length; i++) {
				if (a[i] == null && b[i] != null) {
					return false;
				}
				if (a[i] != null && b[i] == null) {
					return false;
				}
				if (a[i] == null && b[i] == null) {
					return true;
				}
				if (!ItemStack.matches(a[i], b[i])) {
					return false;
				}
			}
			return true;
		}

		protected float handleRotationFloat(LivingEntity livingBase, float partialTicks) {
			return (float) livingBase.tickCount + partialTicks;
		}

		protected float interpolateRotation(float par1, float par2, float par3) {
			float f;

			for (f = par2 - par1; f < -180.0F; f += 360.0F) {
			}

			while (f >= 180.0F) {
				f -= 360.0F;
			}

			return par1 + par3 * f;
		}
	}
}
