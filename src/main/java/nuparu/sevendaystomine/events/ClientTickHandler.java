package nuparu.sevendaystomine.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.client.util.CameraHelper;
import nuparu.sevendaystomine.config.ClientConfig;

import java.lang.reflect.Method;


@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTickHandler {

    public static float recoil = 0;
    public static float antiRecoil = 0;
    public static int time = 0;
    public static int useCount = 0;

    public static final ResourceLocation THIRST_SHADER = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/thirst.json");


    public static int windCounter = 10;

    
    public static int beat = 999999;
    
    public static ResourceLocation drunkShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/drunk.json");
    
    public static Method f_loadShader;
    
    public static boolean bloodmoon;
    
    private static ResourceLocation bleedShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/blur_bleed.json");
    
    private static ResourceLocation nightShaderRes = new ResourceLocation(SevenDaysToMine.MODID + ":shaders/post/night.json");
    private static Method f_setSize;
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getInstance();
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = mc.player;

            if (player == null)
                return;
            World world = player.level;

            if (recoil > 0) {
                recoil *= 0.8F;

                player.xRot -= recoil / 2;
                if (useCount < 25) {
                    antiRecoil += recoil / 2;
                    player.xRot += antiRecoil * 0.1F;
                }
                antiRecoil *= 0.8F;
            }

            IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
            if (iep != null && iep.getStamina() <= 0) {
                KeyBinding.set(mc.options.keySprint.getKey(), false);
                player.setSprinting(false);
            }
            if (ClientConfig.burntForestParticles.get()) {
                for (int l = 0; l < 500 * SevenDaysToMine.proxy.getParticleLevel(); ++l) {
                    int i1 = MathHelper.floor(player.getX()) + world.random.nextInt(16) - world.random.nextInt(16);
                    int j1 = MathHelper.floor(player.getY()) + world.random.nextInt(16) - world.random.nextInt(16);
                    int k1 = MathHelper.floor(player.getZ()) + world.random.nextInt(16) - world.random.nextInt(16);
                    BlockPos pos = new BlockPos(i1, j1, k1);
                    Biome biome = world.getBiome(pos);

					/*if ((biome instanceof BiomeWastelandBase) && ((BiomeWastelandBase) biome).floatingParticles()) {
						if (world.random.nextInt(8) > Math.abs(world.getHeight(pos).getY() - j1)) {
							BlockState block = world.getBlockState(pos);

							if (block.getMaterial() == Material.AIR) {

								world.addParticle(ParticleTypes.ASH,
										(double) ((float) i1 + world.random.nextFloat()),
										(double) ((float) j1 + world.random.nextFloat()),
										(double) ((float) k1 + world.random.nextFloat()), 0.0D, -1D, 0.0D);
							}
						}
					}*/
                }
            }
            if (ClientEventHandler.takingPhoto) {
                CameraHelper.INSTANCE.saveScreenshot(mc.getWindow().getScreenWidth(), mc.getWindow().getScreenHeight(), mc.getMainRenderTarget(), player);
                ClientEventHandler.takingPhoto = false;
            }

        }

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (!ClientConfig.postprocessingShaders.get())
            return;

        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null)
            return;
        GameRenderer gameRenderer = mc.gameRenderer;

        if(gameRenderer.currentEffect() == null || !gameRenderer.currentEffect().getName().equals(THIRST_SHADER.toString())) {
            //gameRenderer.loadEffect(THIRST_SHADER);
        }


    }
}
