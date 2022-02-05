package nuparu.sevendaystomine.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import nuparu.sevendaystomine.client.gui.EnumHudPosition;

public class ClientConfig {
    public static BooleanValue bloodmoonSky;
    public static DoubleValue blodmoonColorR;
    public static DoubleValue blodmoonColorG;
    public static DoubleValue blodmoonColorB;
    public static BooleanValue customMenu;
    public static BooleanValue customPlayerRenderer;
    public static BooleanValue customGunHands;
    public static BooleanValue minibikeCameraRoll;
    public static BooleanValue useVanillaCameraRendering;
    public static BooleanValue muzzleFlash;
    public static BooleanValue particles;
    public static BooleanValue burntForestParticles;
    public static BooleanValue wastelandFog;
    public static IntValue wastelandFogBlend;
    public static BooleanValue postprocessingShaders;
    public static BooleanValue bloodParticles;
    public static EnumValue<EnumHudPosition> hudPosition;
	

    public static void init(ForgeConfigSpec.Builder server) {
    	bloodmoonSky=server.comment("Should colorize sky during bloodmoons?").define("render.bloodmoonSky",true);
        blodmoonColorR=server.comment("The red channel of the bloodmoon color").defineInRange("render.blodmoonColorR",1d,0d,1);
        blodmoonColorG=server.comment("The green channel of the bloodmoon color").defineInRange("render.blodmoonColorG",0d,0d,1);
        blodmoonColorB=server.comment("The blue channel of the bloodmoon color").defineInRange("render.blodmoonColorB",0d,0d,1);
    	customMenu=server.comment("\"Should use the custom menu?").define("gui.custom_menu",true);
    	customPlayerRenderer=server.comment("Should use the custom player renderer (used for aiming in 3rd person, rotating the player while riding the minibike, etc..)?").define("render.player_renderer",true);
    	customGunHands=server.comment("Should use custom hand rendeing in first person (with guns)? Not recommended to turn off, as results might be questionable. Could be useful with mods that affect hand rendering (maybe something like Vivecraft)").define("render.custom_gun_hands",true);
    	minibikeCameraRoll=server.comment("Should the camera roll while turning a minibike?").define("render.minibike_camera_roll",true);
    	muzzleFlash=server.comment("Should draw the muzzle flash?").define("render.muzzle_flash",true);
    	useVanillaCameraRendering=server.comment("Should use vanilla code for rendering CCTV cameras instead of the code possibly injected by mods like Optfine").define("render.use_vanilla_camera_rendering",true);
    	particles=server.comment("Should use custom particles?").define("render.particles",true);
    	burntForestParticles=server.comment("Should use draw the void-fog-like particles in burnt biomes?").define("render.burnt_forest_particles",true);
    	wastelandFog=server.comment("Should render the fog in the wasteland/burnt biomes?").define("render.wasteland_fog",true);
    	postprocessingShaders=server.comment("Should use GLSL sahders for drunk/bleeding effects?").define("render.postprocessing_shaders",true);
    	hudPosition=server.comment("Should use custom sky (for bloodmons,etc..)?").defineEnum("gui.hud_position",EnumHudPosition.LEFT_BOTTOM,EnumHudPosition.values());
        wastelandFogBlend=server.comment("How far does the fog blend sample from").defineInRange("render.wastelandFogBlend",4,0,16);
        bloodParticles=server.comment("Should render blood particles?").define("render.bloodParticles",true);
    }
}
