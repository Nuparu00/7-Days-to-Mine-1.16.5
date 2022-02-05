package nuparu.sevendaystomine.network;

import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.network.packets.*;

public class PacketManager {

	public static PacketManager INSTANCE;
	

    private static final String PROTOCOL_VERSION = Integer.toString(1);

	public static SimpleChannel playerCapabilitySync = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "player_cap_sync"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel blockBreakSync = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "block_break_sync"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel gunReload = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "gun_reload"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel openGuiClient = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "open_gui_client"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel syncTileEntity = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "sync_tile_entity"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel safeCodeUpdate = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "safe_code_reload"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel startProcess = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "start_process"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel syncIcon = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "sync_icon"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel photoToServer = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "photo_to_server"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel photoRequest = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "photo_request"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel photoToClient = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "photo_to_client"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel dialogueSelection = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "dialogue_selection"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel addSubtitle = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "add_subtitle"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel syncInventory = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "sync_inventory"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel syncThrottle = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "sync_throttle"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel killProcess = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "kill_process"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel syncProcess = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "sync_process"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel saveData = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "save_data"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	// This is packet for INetwork - does have nothing to do with actual
	// server-client packets!!!
	public static SimpleChannel sendPacket = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "send_packet"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel applyRecoil = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "apply_recoil"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel bulletImpact = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "bullet_impact"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel schedulePhoto = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "schedule_photo"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel cameraDimensions = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "camera_dimensions"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel honk = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "honk"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel redstoneSignal = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "redstone_signal"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel blockBreakSyncTracking = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "block_break_sync_tracking"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
	public static SimpleChannel turretShot = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(SevenDaysToMine.MODID, "turret_shot"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();
	public static SimpleChannel syncScrapsData = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(SevenDaysToMine.MODID, "sync_scraps_data"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	public static SimpleChannel synEntityRot = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(SevenDaysToMine.MODID, "sync_entity_rot"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();
	public static SimpleChannel spawnBlood = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(SevenDaysToMine.MODID, "spawn_blood"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();

	private static int discriminator = 0;

	public PacketManager() {
		INSTANCE = this;
	}
	
	public static void setup() {
		 PacketManager.playerCapabilitySync.registerMessage(PacketManager.discriminator++, PlayerCapabilitySyncMessage.class, PlayerCapabilitySyncMessage::encode, PlayerCapabilitySyncMessage::decode, PlayerCapabilitySyncMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.blockBreakSync.registerMessage(PacketManager.discriminator++, BreakSyncMessage.class, BreakSyncMessage::encode, BreakSyncMessage::decode, BreakSyncMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.gunReload.registerMessage(PacketManager.discriminator++, ReloadMessage.class, ReloadMessage::encode, ReloadMessage::decode, ReloadMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.openGuiClient.registerMessage(PacketManager.discriminator++, OpenGuiClientMessage.class, OpenGuiClientMessage::encode, OpenGuiClientMessage::decode, OpenGuiClientMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.syncTileEntity.registerMessage(PacketManager.discriminator++, SyncTileEntityMessage.class, SyncTileEntityMessage::encode, SyncTileEntityMessage::decode, SyncTileEntityMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.safeCodeUpdate.registerMessage(PacketManager.discriminator++, SafeCodeMessage.class, SafeCodeMessage::encode, SafeCodeMessage::decode, SafeCodeMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.safeCodeUpdate.registerMessage(PacketManager.discriminator++, StartProcessMessage.class, StartProcessMessage::encode, StartProcessMessage::decode, StartProcessMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.syncIcon.registerMessage(PacketManager.discriminator++, SyncIconMessage.class, SyncIconMessage::encode, SyncIconMessage::decode, SyncIconMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.photoToServer.registerMessage(PacketManager.discriminator++, PhotoToServerMessage.class, PhotoToServerMessage::encode, PhotoToServerMessage::decode, PhotoToServerMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.photoRequest.registerMessage(PacketManager.discriminator++, PhotoRequestMessage.class, PhotoRequestMessage::encode, PhotoRequestMessage::decode, PhotoRequestMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.photoToClient.registerMessage(PacketManager.discriminator++, PhotoToClientMessage.class, PhotoToClientMessage::encode, PhotoToClientMessage::decode, PhotoToClientMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.dialogueSelection.registerMessage(PacketManager.discriminator++, DialogueSelectionMessage.class, DialogueSelectionMessage::encode, DialogueSelectionMessage::decode, DialogueSelectionMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.addSubtitle.registerMessage(PacketManager.discriminator++, AddSubtitleMessage.class, AddSubtitleMessage::encode, AddSubtitleMessage::decode, AddSubtitleMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.syncInventory.registerMessage(PacketManager.discriminator++, SyncInventoryMessage.class, SyncInventoryMessage::encode, SyncInventoryMessage::decode, SyncInventoryMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.syncThrottle.registerMessage(PacketManager.discriminator++, SyncThrottleMessage.class, SyncThrottleMessage::encode, SyncThrottleMessage::decode, SyncThrottleMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.killProcess.registerMessage(PacketManager.discriminator++, KillProcessMessage.class, KillProcessMessage::encode, KillProcessMessage::decode, KillProcessMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.syncProcess.registerMessage(PacketManager.discriminator++, SyncProcessMessage.class, SyncProcessMessage::encode, SyncProcessMessage::decode, SyncProcessMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.saveData.registerMessage(PacketManager.discriminator++, SaveDataMessage.class, SaveDataMessage::encode, SaveDataMessage::decode, SaveDataMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.sendPacket.registerMessage(PacketManager.discriminator++, SendPacketMessage.class, SendPacketMessage::encode, SendPacketMessage::decode, SendPacketMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.applyRecoil.registerMessage(PacketManager.discriminator++, ApplyRecoilMessage.class, ApplyRecoilMessage::encode, ApplyRecoilMessage::decode, ApplyRecoilMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.bulletImpact.registerMessage(PacketManager.discriminator++, BulletImpactMessage.class, BulletImpactMessage::encode, BulletImpactMessage::decode, BulletImpactMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.schedulePhoto.registerMessage(PacketManager.discriminator++, SchedulePhotoMessage.class, SchedulePhotoMessage::encode, SchedulePhotoMessage::decode, SchedulePhotoMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.cameraDimensions.registerMessage(PacketManager.discriminator++, CameraDimensionsMessage.class, CameraDimensionsMessage::encode, CameraDimensionsMessage::decode, CameraDimensionsMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.honk.registerMessage(PacketManager.discriminator++, HonkMessage.class, HonkMessage::encode, HonkMessage::decode, HonkMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.redstoneSignal.registerMessage(PacketManager.discriminator++, SendRedstoneSignalMessage.class, SendRedstoneSignalMessage::encode, SendRedstoneSignalMessage::decode, SendRedstoneSignalMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.blockBreakSyncTracking.registerMessage(PacketManager.discriminator++, BreakSyncTrackingMessage.class, BreakSyncTrackingMessage::encode, BreakSyncTrackingMessage::decode, BreakSyncTrackingMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.startProcess.registerMessage(PacketManager.discriminator++, StartProcessMessage.class, StartProcessMessage::encode, StartProcessMessage::decode, StartProcessMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		 PacketManager.turretShot.registerMessage(PacketManager.discriminator++, TurretShotMessage.class, TurretShotMessage::encode, TurretShotMessage::decode, TurretShotMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.syncScrapsData.registerMessage(PacketManager.discriminator++, SyncScrapsMessage.class, SyncScrapsMessage::encode, SyncScrapsMessage::decode, SyncScrapsMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.synEntityRot.registerMessage(PacketManager.discriminator++, SyncEntityRotMesage.class, SyncEntityRotMesage::encode, SyncEntityRotMesage::decode, SyncEntityRotMesage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		 PacketManager.spawnBlood.registerMessage(PacketManager.discriminator++, SpawnBloodMessage.class, SpawnBloodMessage::encode, SpawnBloodMessage::decode, SpawnBloodMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

	}
	
	public static void sendTo(SimpleChannel channel, Object message, ServerPlayerEntity player) {
		channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToChunk(SimpleChannel channel, Object message, Supplier<Chunk> chunk) {
    	channel.send(PacketDistributor.TRACKING_CHUNK.with(chunk), message);
    }

    public static void sendToDimension(SimpleChannel channel, Object message, Supplier<RegistryKey<World>> world) {
    	channel.send(PacketDistributor.DIMENSION.with(world), message);
    }

    public static void sendToTrackingEntity(SimpleChannel channel, Object message, Supplier<Entity> entity) {
    	channel.send(PacketDistributor.TRACKING_ENTITY.with(entity), message);
    }

    public static void sendToAll(SimpleChannel channel, Object message) {
    	channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToServer(SimpleChannel channel, Object message) {
    	channel.sendToServer(message);
    }
}