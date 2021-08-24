package nuparu.sevendaystomine.events;

import java.util.HashMap;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.client.gui.GuiMainMenuEnhanced;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.*;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.util.VanillaManager.VanillaScrapableItem;

public class ClientEventHandler {

	public static boolean takingPhoto;

	public static void init() {

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void renderScopeOverlayPre(RenderGameOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
			// if (mc.options.thirdPersonView == 0) {
			if (Utils.getCrosshairSpread(mc.player) != -1) {
				event.setCanceled(true);
			}
			// }
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	@OnlyIn(Dist.CLIENT)
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
		if (event.isCancelable() && event.getType() == ElementType.ALL) {
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			if (player == null)
				return;
			ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);
			if (!stack.isEmpty() && stack.getItem() == ModItems.ANALOG_CAMERA.get()) {
				if (player.getUseItemRemainingTicks() > 0 || takingPhoto) {
					event.setCanceled(true);
				}
				if (takingPhoto)
					return;

				double dW = 1 - ItemAnalogCamera.getWidth(stack, player);
				double dH = 1 - ItemAnalogCamera.getHeight(stack, player);

				MainWindow res = event.getWindow();

				int xMin = (int) (0 + res.getGuiScaledWidth() * dW / 2);
				int yMin = (int) (0 + res.getGuiScaledHeight() * dH / 2);
				int xMax = (int) (res.getGuiScaledWidth() - 32 - res.getGuiScaledWidth() * dW / 2);
				int yMax = (int) (res.getGuiScaledHeight() - 32 - res.getGuiScaledHeight() * dH / 2);
				/*
				 * GL11.glPushMatrix(); GL11.glEnable(GL11.GL_BLEND);
				 * OpenGlHelper.glBlendFunc(770, 771, 1, 0); GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
				 * GL11.GL_ONE_MINUS_SRC_ALPHA);
				 * mc.getTextureManager().bind(GuiPlayerUI.UI_TEX);
				 * mc.gui.blit(xMin,yMin,0,39,32,32); mc.gui.blit(xMax,yMin,34,39,32,32);
				 * 
				 * mc.gui.blit(xMin,yMax,0,73,32,32); mc.gui.blit(xMax,yMax,34,73,32,32);
				 * 
				 * mc.font.draw(ItemAnalogCamera.getZoom(stack, player)+"x", xMin+5,
				 * yMax+25-mc.font.lineHeight, 0xffffff); GL11.glDisable(GL11.GL_BLEND);
				 * GL11.glPopMatrix();
				 */
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onGuiOpen(GuiOpenEvent e) {
		if (e.getGui() instanceof MainMenuScreen && !(e.getGui() instanceof GuiMainMenuEnhanced)) {
			e.setGui(new GuiMainMenuEnhanced());
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onPlaySoundEvent(PlaySoundEvent event) {
	}
	/*
	 * @SubscribeEvent public void onCLientConnect(ClientConnectedToServerEvent
	 * event) { SubtitleHelper.INSTANCE.clear(); ForgeIngameGui.renderArmor = true;
	 * }
	 */

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent()
	public void onFogColors(FogColors event) {

	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent()
	public void onFogDensity(FogDensity event) {

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void updateFOVEvent(FOVUpdateEvent event) {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		if (player == null)
			return;
		ItemStack stack = player.getMainHandItem();
		if (!stack.isEmpty() && stack.getItem() instanceof ItemAnalogCamera) {
			event.setNewfov((float) (event.getNewfov() / ItemAnalogCamera.getZoom(stack, player)));
			return;
		}
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
			stack = player.getOffhandItem();
			if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun))
				return;
		}
		ItemGun gun = (ItemGun) stack.getItem();
		float factor = gun.getFOVFactor(stack);
		if (factor == 1)
			return;
		if (mc.options.keyAttack.isDown()) {
			event.setNewfov(event.getNewfov() / factor);
		}

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onItmemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.isEmpty())
			return;
		Item item = stack.getItem();

		EnumMaterial mat = EnumMaterial.NONE;
		int weight = 0;
		if (item instanceof IScrapable) {
			IScrapable scrapable = (IScrapable) stack.getItem();
			mat = scrapable.getItemMaterial();
			weight = scrapable.getWeight();

		} else if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof IScrapable) {
			IScrapable scrapable = (IScrapable) ((BlockItem) item).getBlock();
			mat = scrapable.getItemMaterial();
			weight = scrapable.getWeight();
		} else if (VanillaManager.getVanillaScrapable(item) != null) {
			VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
			mat = scrapable.getMaterial();
			weight = scrapable.getWeight();
		}

		if(CommonConfig.qualitySystem.get() == EnumQualityState.ALL && PlayerUtils.isVanillaQualityItem(stack)){
			int quality = ItemQuality.getQualityFromStack(stack);
			EnumQuality tier = PlayerUtils.getQualityTierFromInt(quality);
			TranslationTextComponent qualityTitle = new TranslationTextComponent(
					"stat.quality." + tier.name().toLowerCase());
			IFormattableTextComponent qualityValue = new TranslationTextComponent("stat.quality",quality);

			Style style = qualityTitle.getStyle().withColor(tier.getColor());
			qualityTitle.setStyle(style);
			qualityValue.setStyle(style);
			event.getToolTip().add(1,qualityTitle);
			event.getToolTip().add(2,qualityValue);
		}

		if (mat != null && mat != EnumMaterial.NONE) {
			event.getToolTip().add(new StringTextComponent(weight + "x" + mat.getLocalizedName()));
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
		/*
		 * if(!ModConfig.client.minibikeCameraRoll) return; PlayerEntity player =
		 * Minecraft.getInstance().player; if(player == null) return; Entity riding =
		 * player.getVehicle();
		 * 
		 * if(riding != null && riding instanceof EntityMinibike) { EntityMinibike
		 * minibike = (EntityMinibike)riding;
		 * event.setRoll(event.getRoll()+(Utils.lerp(minibike.getTurningPrev(),
		 * minibike.getTurning(), (float)event.getRenderPartialTicks()))/8f); }
		 */
	}

	public static HashMap<BlockPos, CompoundNBT> cachedChunks = new HashMap<BlockPos, CompoundNBT>();

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {
		IWorld world = event.getWorld();
		if (world.isClientSide()) {
			IChunk ichunk = event.getChunk();

			if (ichunk instanceof Chunk) {
				Chunk chunk = (Chunk) ichunk;
				IChunkData data = CapabilityHelper.getChunkData(chunk);
				if (data != null) {
					CompoundNBT nbt = cachedChunks.get(chunk.getPos().getWorldPosition());
					if(nbt == null) return;
					data.readFromNBT(nbt);
					cachedChunks.remove(chunk.getPos().getWorldPosition());
				}
			}
		}
	}

}
