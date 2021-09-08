package nuparu.sevendaystomine.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.client.gui.GuiMainMenuEnhanced;
import nuparu.sevendaystomine.client.gui.GuiPlayerUI;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.*;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.VanillaManager;
import nuparu.sevendaystomine.util.VanillaManager.VanillaScrapableItem;

import java.util.HashMap;

public class ClientEventHandler {

    public static boolean takingPhoto;
    public static HashMap<BlockPos, CompoundNBT> cachedChunks = new HashMap<BlockPos, CompoundNBT>();

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

                MatrixStack matrixStack = event.getMatrixStack();
                ;

                matrixStack.pushPose();
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                mc.getTextureManager().bind(GuiPlayerUI.UI_TEX);
                mc.gui.blit(matrixStack, xMin, yMin, 0, 39, 32, 32);
                mc.gui.blit(matrixStack, xMax, yMin, 34, 39, 32, 32);

                mc.gui.blit(matrixStack, xMin, yMax, 0, 72, 32, 32);
                mc.gui.blit(matrixStack, xMax, yMax, 34, 72, 32, 32);

                mc.font.draw(matrixStack, ItemAnalogCamera.getZoom(stack, player) + "x", xMin + 5,
                        yMax + 25 - mc.font.lineHeight, 0xffffff);
                RenderSystem.disableBlend();
                matrixStack.popPose();

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
    /*
     * @SubscribeEvent public void onCLientConnect(ClientConnectedToServerEvent
     * event) { SubtitleHelper.INSTANCE.clear(); ForgeIngameGui.renderArmor = true;
     * }
     */

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onPlaySoundEvent(PlaySoundEvent event) {
    }

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

        if (CommonConfig.qualitySystem.get() == EnumQualityState.ALL && PlayerUtils.isVanillaQualityItem(stack)) {
            int quality = ItemQuality.getQualityFromStack(stack);
            EnumQuality tier = PlayerUtils.getQualityTierFromInt(quality);
            TranslationTextComponent qualityTitle = new TranslationTextComponent(
                    "stat.quality." + tier.name().toLowerCase());
            IFormattableTextComponent qualityValue = new TranslationTextComponent("stat.quality", quality);

            Style style = qualityTitle.getStyle().withColor(tier.getColor());
            qualityTitle.setStyle(style);
            qualityValue.setStyle(style);
            event.getToolTip().add(1, qualityTitle);
            event.getToolTip().add(2, qualityValue);
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
                    if (nbt == null) return;
                    data.readFromNBT(nbt);
                    cachedChunks.remove(chunk.getPos().getWorldPosition());
                }
            }
        }
    }

}
