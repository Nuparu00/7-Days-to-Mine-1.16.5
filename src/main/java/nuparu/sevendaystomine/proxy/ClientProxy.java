package nuparu.sevendaystomine.proxy;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.client.animation.Animations;
import nuparu.sevendaystomine.client.gui.*;
import nuparu.sevendaystomine.client.sound.PositionedLoudSound;
import nuparu.sevendaystomine.client.toast.NotificationToast;
import nuparu.sevendaystomine.client.util.MP3Helper;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.entity.human.EntityHuman;
import nuparu.sevendaystomine.events.*;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemGuide;
import nuparu.sevendaystomine.item.ItemNote;
import nuparu.sevendaystomine.item.ItemRecipeBook;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;
import nuparu.sevendaystomine.util.EnumModParticleType;
import nuparu.sevendaystomine.util.MathUtils;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

public class ClientProxy extends CommonProxy {

    private static final Map<BlockPos, ISound> mapSoundPositions = Maps.<BlockPos, ISound>newHashMap();
    public static KeyBinding[] keyBindings;
    private Field f_skinMap;

    @Override
    public void preInit() {
        super.preInit();
        //OBJLoader.INSTANCE.loadMaterialLibrary(new ResourceLocation(SevenDaysToMine.MODID,""));
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        TickHandler.init(Dist.CLIENT);

        MinecraftForge.EVENT_BUS.register(new GuiPlayerUI());
        MinecraftForge.EVENT_BUS.register(new GuiSubtitles());
        MinecraftForge.EVENT_BUS.register(new GuiUpgradeOverlay());
        MinecraftForge.EVENT_BUS.register(new GuiGun());

        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        bus.register(new TextureStitcherEventHandler());

        MinecraftForge.EVENT_BUS.register(new KeyEventHandler());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void init() {
        super.init();
        initKeybindings();
        ClientEventHandler.init();
        MP3Helper.init();

        ItemColors colors = Minecraft.getInstance().getItemColors();
        Item[] clothes = new Item[]{ModItems.SHORTS.get(), ModItems.SKIRT.get(), ModItems.SHORTS_LONG.get(), ModItems.JEANS.get(),
                ModItems.SHIRT.get(), ModItems.SHORT_SLEEVED_SHIRT.get(), ModItems.JACKET.get(), ModItems.JUMPER.get(), ModItems.COAT.get(),
                ModItems.T_SHIRT_0.get(), ModItems.T_SHIRT_1.get()};

        colors.register(new IItemColor() {
            public int getColor(ItemStack stack, int tintIndex) {
                return tintIndex > 0 ? -1 : ((IDyeableArmorItem) stack.getItem()).getColor(stack);
            }
        }, clothes);
    }

    @Override
    public PlayerEntity getPlayerEntityFromContext(Supplier<NetworkEvent.Context> ctx) {
        return (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT ? Minecraft.getInstance().player
                : super.getPlayerEntityFromContext(ctx));
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.get(unlocalized, args);
    }

    void initKeybindings() {
        keyBindings = new KeyBinding[9];
        keyBindings[0] = new KeyBinding("key.reload.desc", GLFW.GLFW_KEY_R, "key.sevendaystomine.category");
        keyBindings[1] = new KeyBinding("key.camera.width.increase.desc", GLFW.GLFW_KEY_KP_6,
                "key.sevendaystomine.category");
        keyBindings[2] = new KeyBinding("key.camera.width.decrease.desc", GLFW.GLFW_KEY_KP_4,
                "key.sevendaystomine.category");
        keyBindings[3] = new KeyBinding("key.camera.height.increase.desc", GLFW.GLFW_KEY_KP_8,
                "key.sevendaystomine.category");
        keyBindings[4] = new KeyBinding("key.camera.height.decrease.desc", GLFW.GLFW_KEY_KP_2,
                "key.sevendaystomine.category");
        keyBindings[5] = new KeyBinding("key.camera.zoom.desc", GLFW.GLFW_KEY_KP_ADD, "key.sevendaystomine.category");
        keyBindings[6] = new KeyBinding("key.camera.unzoom.desc", GLFW.GLFW_KEY_KP_SUBTRACT,
                "key.sevendaystomine.category");
        keyBindings[7] = new KeyBinding("key.honk.desc", GLFW.GLFW_KEY_SPACE, "key.sevendaystomine.category");

        keyBindings[8] = new KeyBinding("key.animation_debug.desc", GLFW.GLFW_KEY_SEMICOLON, "key.sevendaystomine.category");

        for (int i = 0; i < keyBindings.length; ++i) {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    @Override
    public void openClientSideGui(int id, int x, int y, int z) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        TileEntity te = player.level.getBlockEntity(new BlockPos(x, y, z));
        switch (id) {
            case 0:
                mc.setScreen(new GuiCodeSafeLocked(te, new BlockPos(x, y, z)));
                return;
        }
    }

    @Override
    public void openClientOnlyGui(int id, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        switch (id) {
            case 0:
                mc.setScreen(new GuiMp3());
                return;
            case 1:
                mc.setScreen(new GuiPhoto(stack.getOrCreateTag().getString("path")));
                return;
            case 2:
                mc.getToasts()
                        .addToast(new NotificationToast(stack, new TranslationTextComponent("unlocked.toast"),
                                new TranslationTextComponent(!stack.isEmpty() && stack.getItem() instanceof ItemRecipeBook
                                        ? stack.getItem().getRegistryName().getPath() + ".title"
                                        : "THIS IS NOT BOOK!")));
                return;
            case 3:
                mc.setScreen(new GuiBook(((ItemGuide) stack.getItem()).data));
                return;
            case 4:
                mc.setScreen(new GuiNote(((ItemNote) stack.getItem()).getData(stack)));
                return;
        }

    }

    @Override
    public void openClientOnlyGui(int id, TileEntity te) {
        Minecraft mc = Minecraft.getInstance();
        if (te != null && te instanceof TileEntityPhoto && id == 0) {
            mc.setScreen(new GuiPhoto(((TileEntityPhoto) te).getPath()));
        }
    }

    @Override
    public void openPhoto(String path){
        Minecraft.getInstance().setScreen(new GuiPhoto(path));
    }

    @Override
    public void startDialogue(EntityHuman human) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new GuiDialogue(human));
    }

    public void schedulePhoto() {
        /*
         * Minecraft mc = Minecraft.getInstance(); if (mc != null && mc.player != null)
         * { CameraHelper.INSTANCE.saveScreenshot(mc.displayWidth, mc.displayHeight,
         * mc.getFramebuffer(), mc.player); }
         */
        if (CommonConfig.allowPhotos.get()) {
            ClientEventHandler.takingPhoto = true;
        }
    }

    @Override
    public void addRecoil(float recoil, PlayerEntity shooter) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.player != null) {
            if (mc.player == shooter && mc.getCameraEntity() == shooter) {
                TickHandler.recoil += recoil;
            }
        }
    }

    @Override
    public void onGunStop(int useCount) {

    }

    /*
     * Maybe should check ParticleManager.registerParticle in the future
     */
    @Override
    public void addParticle(World world, EnumModParticleType type, double x, double y, double z, double xMotion,
                            double yMotion, double zMotion) {
        if (!ClientConfig.particles.get()) return;
        IParticleFactory factory = getParticleFactory(type);
		/*if (factory != null) {
			Particle particle = factory.createParticle(0, world, x, y, z, xMotion, yMotion, zMotion, 0);
			if (particle != null) {
				Minecraft.getInstance().effectRenderer.addEffect(particle);
			}
		}*/
    }

    private IParticleFactory getParticleFactory(EnumModParticleType type) {
        switch (type) {
		/*case BLOOD:
			return new ParticleBlood.Factory();*/
            default:
                break;
        }
        return null;
    }

    // 2==max,1==decreased,0==minimal
    @Override
    public int getParticleLevel() {
        return Math.abs(Minecraft.getInstance().options.particles.getId() - 2);
    }

    @Override
    public void setSkyRenderer(World world) {
        //world.provider.setSkyRenderer(new SkyRenderer(Minecraft.getInstance().renderGlobal));
    }

    @Override
    public void setCloudRenderer(World world) {
        //world.provider.setCloudRenderer(new CloudRenderer(Minecraft.getInstance().renderGlobal));
    }

    @Override
    public void playLoudSound(World world, SoundEvent soundEvent, float volume, BlockPos blockPosIn,
                              SoundCategory category) {
        super.playLoudSound(world, soundEvent, volume, blockPosIn, category);
        if (Minecraft.getInstance().level == null || Minecraft.getInstance().level != world)
            return;
        ISound isound = (ISound) mapSoundPositions.get(blockPosIn);
        if (isound != null) {
            Minecraft.getInstance().getSoundManager().stop(isound);
            mapSoundPositions.remove(blockPosIn);
        }

        if (soundEvent != null && soundEvent.getLocation() != null) {
            PositionedLoudSound positionedsoundrecord = new PositionedLoudSound(soundEvent.getLocation(), volume, 1.0F,
                    false, 0, ISound.AttenuationType.LINEAR, (float) blockPosIn.getX(), (float) blockPosIn.getY(),
                    (float) blockPosIn.getZ(), category);
            mapSoundPositions.put(blockPosIn, positionedsoundrecord);
            Minecraft.getInstance().getSoundManager().play(positionedsoundrecord);
        }
    }

    @Override
    public void stopLoudSound(BlockPos blockPosIn) {
        ISound is = mapSoundPositions.get(blockPosIn);
        if (is != null) {
            Minecraft.getInstance().getSoundManager().stop(is);
            mapSoundPositions.remove(is);
        }

    }

    @Override
    public boolean isHittingBlock(PlayerEntity player) {
        if (player instanceof ClientPlayerEntity) {
			/*PlayerControllerMP controller = Minecraft.getInstance().playerController;
			return ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class, controller, "field_78778_j");*/
        }
        return super.isHittingBlock(player);
    }

    @Override
    public void playMovingSound(int id, Entity entity) {
/*
		switch (id) {
		case 0:
			if (entity instanceof EntityMinibike) {
				Minecraft.getInstance().getSoundManager().play(new MovingSoundMinibikeIdle((EntityMinibike) entity));
			}
			break;
		case 1:
			if (entity instanceof EntityCar) {
				Minecraft.getInstance().getSoundManager().play(new MovingSoundCarIdle((EntityCar) entity));
			}
			break;
		}*/
    }

    @Override
    public void onConstruct(FMLConstructModEvent event) {
        if (Minecraft.getInstance() != null) {
            IReloadableResourceManager manager = (IReloadableResourceManager) Minecraft.getInstance().getResourceManager();
            manager.registerReloadListener(BookDataManager.instance);
            manager.registerReloadListener(Animations.instance);
        }
    }

    @Override
    public int getQualityForCurrentPlayer() {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return 0;
        return (int) MathUtils.clamp(player.totalExperience / CommonConfig.xpPerQuality.get(), 1,
                CommonConfig.maxQuality.get());
    }
}
