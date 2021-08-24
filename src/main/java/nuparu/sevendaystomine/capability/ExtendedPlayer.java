package nuparu.sevendaystomine.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PlayerCapabilitySyncMessage;
import nuparu.sevendaystomine.util.Utils;

public class ExtendedPlayer implements IExtendedPlayer {
	public static final int MAX_THIRST = 780;
	public static final int MAX_STAMINA = 780;

	List<String> recipes = new ArrayList<String>();
	PlayerEntity player;
	int survivedBloodmoons = 0;
	int lastBloodmoonSurvivalCheck = 0;

	int thirst = 780;
	int stamina = 780;

	boolean isCrawling;

	int infectionTime = -1;

	// Last bloodmoon
	int bloodmoon;
	// Last wolf horde
	int wolfHorde;
	// Last generic horde
	int horde;
	//block drinking counter
	int drinkCounter;

	public ExtendedPlayer() {
		thirst = MAX_THIRST;
		stamina = MAX_STAMINA;
	}

	public void setThirst(int thirst) {
		this.thirst = MathHelper.clamp(thirst, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void consumeThirst(int thirst) {
		this.thirst = MathHelper.clamp(this.thirst - thirst, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void addThirst(int thirst) {
		this.thirst = MathHelper.clamp(this.thirst + thirst, 0, MAX_STAMINA);
		onDataChanged();
	}

	public int getThirst() {
		return this.thirst;
	}

	public int getMaximumThirst() {
		return this.MAX_THIRST;
	}

	public void setStamina(int stamina) {
		this.stamina = MathHelper.clamp(stamina, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void consumeStamina(int stamina) {
		this.stamina = MathHelper.clamp(this.stamina - stamina, 0, MAX_STAMINA);
		onDataChanged();
	}

	public void addStamina(int stamina) {
		this.stamina = MathHelper.clamp(this.stamina + stamina, 0, MAX_STAMINA);
		onDataChanged();
	}

	public int getStamina() {
		return this.stamina;
	}

	public int getMaximumStamina() {
		return this.MAX_STAMINA;
	}

	public boolean getCrawling() {
		return this.isCrawling;
	}

	public void setCrawling(boolean state) {
		this.isCrawling = state;
		onDataChanged();
	}

	public int getInfectionTime() {
		return this.infectionTime;
	}

	public void setInfectionTime(int time) {
		this.infectionTime = time;
		onDataChanged();
	}

	public IExtendedPlayer setOwner(PlayerEntity player) {
		this.player = player;
		return this;
	}

	public PlayerEntity getOwner() {
		return this.player;
	}

	public void readNBT(CompoundNBT nbt) {

		thirst = nbt.getInt("thirst");
		stamina = nbt.getInt("stamina");
		isCrawling = nbt.getBoolean("isCrawling");
		infectionTime = nbt.getInt("infectionTime");
		bloodmoon = nbt.getInt("bloodmoon");
		wolfHorde = nbt.getInt("wolfHorde");
		horde = nbt.getInt("horde");
		drinkCounter = nbt.getInt("drinkCounter");
		survivedBloodmoons = nbt.getInt("survivedBloodmoons");
		lastBloodmoonSurvivalCheck = nbt.getInt("lastBloodmoonSurvivalCheck");

		recipes.clear();
		ListNBT list = nbt.getList("recipes", Constants.NBT.TAG_STRING);
		for (int i = 0; i < list.size(); i++) {
			recipes.add(list.getString(i));
		}
	}
	
	public CompoundNBT writeNBT(CompoundNBT nbt) {
		nbt.putInt("thirst", thirst);
		nbt.putInt("stamina", stamina);

		nbt.putBoolean("isCrawling", isCrawling);

		nbt.putInt("infectionTime", infectionTime);

		nbt.putInt("bloodmoon", bloodmoon);
		nbt.putInt("wolfHorde", wolfHorde);
		nbt.putInt("horde", horde);
		nbt.putInt("drinkCounter", drinkCounter);
		nbt.putInt("survivedBloodmoons", survivedBloodmoons);
		nbt.putInt("lastBloodmoonSurvivalCheck", lastBloodmoonSurvivalCheck);

		ListNBT list = new ListNBT();
		for (String rec : recipes) {
			list.add(StringNBT.valueOf(rec));
		}
		nbt.put("recipes", list);

		return nbt;
	}

	public void copy(IExtendedPlayer iep) {
		readNBT(iep.writeNBT(new CompoundNBT()));
	}

	public void onDataChanged() {
		if (!player.level.isClientSide()) {
			ServerWorld world = (ServerWorld) player.level;
			
			/*Class<?> clazz = Class.forName(" net.minecraft.world.server.ChunkManager$EntityTracker");
			
			Int2ObjectMap<Object> entityMap = ObfuscationReflectionHelper.getPrivateValue(ChunkManager.class, world.getChunkSource().chunkMap, "entityMap");
			for(Object obj : entityMap.values()) {
				Entity entity = ObfuscationReflectionHelper.getPrivateValue(clazz, obj, "entity");
			}*/

			PlayerCapabilitySyncMessage message = new PlayerCapabilitySyncMessage(this, player);
			
			PacketManager.sendToTrackingEntity(PacketManager.playerCapabilitySync, message, () -> player);
			PacketManager.sendTo(PacketManager.playerCapabilitySync, message, (ServerPlayerEntity) player);
		}
	}

	public void onStartedTracking(PlayerEntity tracker) {

		if (!player.level.isClientSide()) {
			PlayerCapabilitySyncMessage message = new PlayerCapabilitySyncMessage(this, player);
			PacketManager.sendTo(PacketManager.playerCapabilitySync, message, (ServerPlayerEntity) tracker);
		}
	}

	public static class Factory implements Callable<IExtendedPlayer> {

		@Override
		public IExtendedPlayer call() throws Exception {
			return new ExtendedPlayer();
		}
	}

	@Override
	public void unlockRecipe(String rec) {
		if (hasRecipe(rec))
			return;
		recipes.add(rec);
		onDataChanged();
	}

	@Override
	public boolean hasRecipe(String rec) {
		return recipes.contains(rec);
	}

	@Override
	public List<String> getRecipes() {
		return recipes;
	}

	@Override
	public boolean hasHorde(World world) {
		int day = Utils.getDay(world);

		return bloodmoon == day || wolfHorde == day || horde == day;
	}

	@Override
	public void setBloodmoon(int i) {
		bloodmoon = i;
		onDataChanged();
	}

	@Override
	public int getBloodmoon() {
		return bloodmoon;
	}

	@Override
	public void setWolfHorde(int i) {
		wolfHorde = i;
		onDataChanged();
	}

	@Override
	public int getWolfHorde() {
		return wolfHorde;
	}

	@Override
	public void setHorde(int i) {
		horde = i;
		onDataChanged();
	}

	@Override
	public int getHorde() {
		return horde;
	}

	@Override
	public boolean isInfected() {
		return infectionTime != -1;
	}

	@Override
	public void setDrinkCounter(int i) {
		drinkCounter = i;
		onDataChanged();
	}

	@Override
	public int getDrinkCounter() {
		return drinkCounter;
	}

	@Override
	public void setSurvivedBloodmoons(int i) {
		survivedBloodmoons = i;
		onDataChanged();
	}

	@Override
	public int getSurvivedBloodmons() {
		return survivedBloodmoons;
	}

	@Override
	public void setLastBloodmoonSurvivalCheck(int i) {
		lastBloodmoonSurvivalCheck = i;
		onDataChanged();
	}

	@Override
	public int getLastBloodmoonSurvivalCheck() {
		return lastBloodmoonSurvivalCheck;
	}

}