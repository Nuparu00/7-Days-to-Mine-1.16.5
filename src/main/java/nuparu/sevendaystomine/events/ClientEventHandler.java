package nuparu.sevendaystomine.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.client.gui.GuiMainMenuEnhanced;
import nuparu.sevendaystomine.client.gui.GuiPlayerUI;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.crafting.scrap.ScrapDataManager;
import nuparu.sevendaystomine.entity.MinibikeEntity;
import nuparu.sevendaystomine.init.ModBiomes;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.*;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.util.Utils;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    public static boolean takingPhoto;
    public static HashMap<BlockPos, CompoundNBT> cachedChunks = new HashMap<BlockPos, CompoundNBT>();
    static double biomeBlendPrev = 0;

    /*@SubscribeEvent
    public static void onPlayerConnected(ClientPlayerNetworkEvent.LoggedInEvent event) {
        if(event.getPlayer()==Minecraft.getInstance().player) {
            BookDataManager.instance.reloadRecipes();
        }
    }*/

    @SubscribeEvent
    public static void renderScopeOverlayPre(RenderGameOverlayEvent.Pre event) {
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
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
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
    /*
     * @SubscribeEvent public void onCLientConnect(ClientConnectedToServerEvent
     * event) { SubtitleHelper.INSTANCE.clear(); ForgeIngameGui.renderArmor = true;
     * }
     */

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGuiOpen(GuiOpenEvent e) {
        if (e.getGui() instanceof MainMenuScreen && !(e.getGui() instanceof GuiMainMenuEnhanced)) {
            e.setGui(new GuiMainMenuEnhanced());
        }
    }

    @SubscribeEvent
    public static void onPlaySoundEvent(PlaySoundEvent event) {
    }

    @SubscribeEvent
    public static void onFogColors(FogColors event) {

        if (ClientConfig.bloodmoonSky.get()) {
            int sunsetStart = 12610;
            int sunsestEnd = 13702;
            int sunsetDarkEnd = 13000;
            int sunsetRedStart = 13000;

            World world = event.getInfo().getEntity().level;
            float partialTicks = (float) event.getRenderPartialTicks();
            double angle = world.getTimeOfDay(partialTicks);
            long time = world.getDayTime() - 24000 * (Utils.getDay(world) - 1);
            //12610 = sunset start
            if (time > sunsetStart) {
                double mult = MathUtils.clamp(Math.abs(angle - 0.54), 0, 0.30);
                double lightMult = 0;

                //22300 = sunrise start
                if (time > 22300) {
                    lightMult = (time - 22300) / 1700d;
                }

                float rNew = (float) (mult * 0.1f);

                float r = rNew;

                if (time > 22300) {
                    r = (float) MathUtils.lerp(rNew, event.getRed(), (float) ((time - 22300) / 1700d));
                } else if (time < sunsestEnd) {
                    double rOld = MathUtils.lerp(event.getRed(), event.getRed() - 0.2f, (float) ((MathUtils.clamp(time, 0, sunsetDarkEnd) - sunsetStart) / 390));
                    mult *= ((time - sunsetStart) / (sunsestEnd - sunsetStart));
                    lightMult = 1 - ((time - sunsetStart) / (sunsestEnd - sunsetStart));
                    rNew = (float) (mult * 0.1f);
                    if (time > sunsetRedStart) {
                        r = (float) MathUtils.lerp(rOld, rNew, (float) ((time - 12610) / 1092d));
                    }
                    r = event.getRed();
                }
                event.setRed(r);
                event.setGreen((float) (lightMult * event.getGreen()));
                event.setBlue((float) (lightMult * event.getBlue()));
            }
        }
    }

    @SubscribeEvent
    public static void onFogDensity(FogDensity event) {
        if (!ClientConfig.wastelandFog.get()) {
            return;
        }

        BlockState blockstate = event.getInfo().getBlockAtCamera();
        Material mat = blockstate.getMaterial();
        if (mat != Material.WATER && mat != Material.LAVA) {
            double biomeBlend = getFogBiomeBlend(Minecraft.getInstance().level, event.getInfo().getBlockPosition());
            //System.out.println(biomeBlend);
            if (biomeBlend > 0) {
                double biomeBlendToUse = MathUtils.lerp(biomeBlendPrev, biomeBlend, event.getRenderPartialTicks());
                event.setDensity((float) (event.getDensity() / (30f * (1 - (biomeBlendToUse * biomeBlendToUse) / 1.2d))));
                RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
                /*
                 * GlStateManager.setFogStart(1); GlStateManager.setFogEnd(10);
                 */
                event.setCanceled(true);
            } else {
                event.setCanceled(false);
            }
            biomeBlendPrev = biomeBlend;
        } else {
            event.setCanceled(false);

            biomeBlendPrev = 0;
        }
    }

    public static boolean isWastelandBiome(Biome biome) {
        if (biome == null || biome.getRegistryName() == null) return false;
        if (biome.getRegistryName().equals(ModBiomes.WASTELAND_PLAINS.getId()) ||
                biome.getRegistryName().equals(ModBiomes.WASTELAND_FOREST.getId())) return true;
        return false;
    }

    public static double getFogBiomeBlend(World world, BlockPos center) {
        int dst = ClientConfig.wastelandFogBlend.get();
        if (dst == 0) {
            Biome biome = world.getBiome(center);
            if (isWastelandBiome(biome)) {
                return 1;
            }
            return 0;
        }

        int sum = 0;
        int max = 0;
        for (int i = -dst; i < dst; i++) {
            for (int j = -dst; j < dst; j++) {
                BlockPos pos = center.offset(i, 0, j);
                Biome biome = world.getBiome(pos);
                if (isWastelandBiome(biome)) {
                    sum++;
                }
                max++;
            }
        }
        return (double) sum / max;
    }

    @SubscribeEvent
    public static void updateFOVEvent(FOVUpdateEvent event) {
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
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty())
            return;
        Item item = stack.getItem();

        EnumMaterial mat = EnumMaterial.NONE;
        int weight = 0;
        if (ScrapDataManager.instance.hasEntry(item)) {
            ScrapDataManager.ScrapEntry entry = ScrapDataManager.instance.getEntry(item);

            mat = entry.material;
            weight = entry.weight;
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
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {

        if (!ClientConfig.minibikeCameraRoll.get()) return;
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        Entity riding = player.getVehicle();

        if (riding instanceof MinibikeEntity) {
            MinibikeEntity
                    minibike = (MinibikeEntity) riding;
            Vector3d forward = minibike.getForward();
            Vector3d right = forward.yRot(90);

            float turning = Utils.lerp(minibike.getTurningPrev(),
                    minibike.getTurning(), (float) event.getRenderPartialTicks());
            event.setRoll(event.getRoll() - turning / 8f);
            //event.getInfo().move(right.z*0.1,0,-right.x*0.1);

        }

    }

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        MatrixStack matrixStack = event.getMatrixStack();
        float partialTicks = event.getPartialRenderTick();


        if (!ClientConfig.minibikeCameraRoll.get()) return;
        ActiveRenderInfo camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        Entity riding = player.getVehicle();

        if (riding instanceof MinibikeEntity) {
            MinibikeEntity minibike = (MinibikeEntity) riding;
            Vector3d forward = minibike.getForward();
            Vector3d vec3d = (new Vector3d((double) 0.2, 0.0D, 0.0D))
                    .yRot(-Utils.lerp(minibike.yRotO, minibike.yRot, partialTicks) * 0.017453292F - ((float) Math.PI / 2F));
            //matrixStack.translate(vec3d.x,3*minibike.getPassengersRidingOffset(),vec3d.z);
            //matrixStack.translate(event.getPlayer().getX()-camera.getPosition().x,event.getPlayer().getY()-camera.getPosition().y,event.getPlayer().getZ()-camera.getPosition().z);

            matrixStack.mulPose(Vector3f.XN.rotationDegrees((float) (forward.x * Utils.lerp(minibike.getTurningPrev(), minibike.getTurning(), partialTicks))));
            matrixStack.mulPose(Vector3f.ZN.rotationDegrees((float) (forward.z * Utils.lerp(minibike.getTurningPrev(), minibike.getTurning(), partialTicks))));

            //matrixStack.translate(-vec3d.x,-3*minibike.getPassengersRidingOffset(),-vec3d.z);
            //matrixStack.translate(-(event.getPlayer().getX()-camera.getPosition().x),-(event.getPlayer().getY()-camera.getPosition().y),-(event.getPlayer().getZ()-camera.getPosition().z));

        }

    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
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
