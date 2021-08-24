package nuparu.sevendaystomine.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemAnalogCamera;

public class CapabilityHandler {

	public static final ResourceLocation EXTENDED_PLAYER_CAP = new ResourceLocation(SevenDaysToMine.MODID,
			"extended_player");
	public static final ResourceLocation EXTENDED_INV_CAP = new ResourceLocation(SevenDaysToMine.MODID, "extended_inv");
	public static final ResourceLocation CHUNK_DATA_CAP = new ResourceLocation(SevenDaysToMine.MODID,
			"chunk_data");

	@SubscribeEvent
	public void attachCapabilityToEntity(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof PlayerEntity))
			return;
		PlayerEntity player = (PlayerEntity) event.getObject();
		event.addCapability(EXTENDED_PLAYER_CAP, new ExtendedPlayerProvider().setOwner(player));
		event.addCapability(EXTENDED_INV_CAP, new ExtendedInventoryProvider().setSize(3));
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		PlayerEntity player = event.getPlayer();
		// EXTENDED PLAYER
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		IExtendedPlayer oldExtendedPlayer = CapabilityHelper.getExtendedPlayer(event.getOriginal());

		for (String s : oldExtendedPlayer.getRecipes()) {
			extendedPlayer.unlockRecipe(s);
		}
		extendedPlayer.setHorde(oldExtendedPlayer.getHorde());
		extendedPlayer.setWolfHorde(oldExtendedPlayer.getWolfHorde());
		extendedPlayer.setBloodmoon(oldExtendedPlayer.getBloodmoon());

		if (!event.isWasDeath()) {
			extendedPlayer.copy(oldExtendedPlayer);
			
			IItemHandlerExtended extendedInv = CapabilityHelper.getExtendedInventory(player);
			IItemHandlerExtended oldExtendedInv = CapabilityHelper.getExtendedInventory(event.getOriginal());
			extendedInv.copy(oldExtendedInv);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		if(!event.getPlayer().level.isClientSide()) {
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(event.getPlayer());
			extendedPlayer.onDataChanged();
		}
	}
	
	@SubscribeEvent
	public void attachCapabilityToStack(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() instanceof ItemAnalogCamera){
			event.addCapability(EXTENDED_INV_CAP, new ExtendedInventoryProvider().setSize(1));
		}
	}
	
	@SubscribeEvent
    public void onAttachCapabilitiesChunk(AttachCapabilitiesEvent<Chunk> event) {
        ChunkDataProvider provider = new ChunkDataProvider().setOwner(event.getObject());
        event.addCapability(CHUNK_DATA_CAP, provider);
        event.addListener(() -> provider.instance.invalidate());
    }
}
