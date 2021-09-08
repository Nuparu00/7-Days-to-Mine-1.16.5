package nuparu.sevendaystomine.proxy;

import java.io.StringWriter;
import java.util.function.Supplier;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.computer.process.ProcessRegistry;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.crafting.campfire.CampfireRecipeManager;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeManager;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import nuparu.sevendaystomine.crafting.separator.SeparatorRecipeManager;
import nuparu.sevendaystomine.entity.human.EntityHuman;
import nuparu.sevendaystomine.events.LoudSoundEvent;
import nuparu.sevendaystomine.events.TickHandler;
import nuparu.sevendaystomine.util.EnumModParticleType;
import nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

@SuppressWarnings("deprecation")
public class CommonProxy {

	public static ScriptEngineManager mgr;
	public static ScriptEngine engine;
	public static StringWriter sw;



	public void preInit() {
		TickHandler.init(Dist.DEDICATED_SERVER);

		mgr = new ScriptEngineManager(null);
		engine = mgr.getEngineByName("javascript");
		sw = new StringWriter();
		engine.getContext().setWriter(sw);
	}

	public void init() {

		ProcessRegistry.INSTANCE.register();
		ApplicationRegistry.INSTANCE.register();
		DialoguesRegistry.INSTANCE.register();
		

		new CampfireRecipeManager();
		new ForgeRecipeManager();
		new ChemistryRecipeManager();
		new SeparatorRecipeManager();

	}

	public void postInit() {

		if (CommonConfig.removeVanillaZommbies.get()) {
		}

	}

	public void serverStarting(FMLServerStartingEvent event) {

	}

	public PlayerEntity getPlayerEntityFromContext(Supplier<NetworkEvent.Context> ctx) {
		return ctx.get().getSender();
	}

	public String localize(String unlocalized, Object... args) {
		return I18n.get(unlocalized, args);
	}


	public void openClientSideGui(int id, int x, int y, int z) {

	}

	public void openClientOnlyGui(int id, ItemStack stack) {

	}

	public void openClientOnlyGui(int id, TileEntity te) {

	}

	public void openPhoto(String path){

	}

	public void startDialogue(EntityHuman human) {

	}

	public void schedulePhoto() {

	}

	public void addRecoil(float recoil, PlayerEntity shooter) {

	}

	public void onGunStop(int useCount) {

	}

	public void addParticle(World world, EnumModParticleType type, double x, double y, double z, double xMotion,
			double yMotion, double zMotion) {

	}

	public int getParticleLevel() {
		return -1;
	}

	public void setSkyRenderer(World world) {

	}

	public void setCloudRenderer(World world) {

	}

	public void playLoudSound(World world, SoundEvent resource, float volume, BlockPos blockPosIn,
			SoundCategory category) {
		MinecraftForge.EVENT_BUS.post(new LoudSoundEvent(world, blockPosIn, resource, volume, category));
	}

	public void stopLoudSound(BlockPos blockPosIn) {

	}

	public boolean isHittingBlock(PlayerEntity player) {
		/*if (player instanceof ServerPlayerEntity) {
			PlayerInteractionManager manager = ((ServerPlayerEntity) player).interactionManager;
			return ObfuscationReflectionHelper.getPrivateValue(PlayerInteractionManager.class, manager,
					"field_73088_d");
		}*/
		return false;
	}

	public void playMovingSound(int id, Entity enttiy) {

	}
	
	public void onConstruct(FMLConstructModEvent event) {
	}

	public int getQualityForCurrentPlayer(){
		return 0;
	}
}
