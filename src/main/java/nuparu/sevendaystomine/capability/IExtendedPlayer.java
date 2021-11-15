package nuparu.sevendaystomine.capability;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface IExtendedPlayer {

	void setThirst(int thirst);

	void consumeThirst(int thirst);

	void addThirst(int thirst);

	int getThirst();

	int getMaximumThirst();

	void setStamina(int stamina);

	void consumeStamina(int stamina);

	void addStamina(int stamina);

	int getStamina();

	int getMaximumStamina();

	boolean getCrawling();

	void setCrawling(boolean state);

	boolean isInfected();

	int getInfectionTime();

	void setInfectionTime(int stage);
	
	void unlockRecipe(String rec);
	
	boolean hasRecipe(String rec);
	
	List<String> getRecipes();

	IExtendedPlayer setOwner(PlayerEntity player);

	PlayerEntity getOwner();

	void readNBT(CompoundNBT nbt);

	CompoundNBT writeNBT(CompoundNBT nbt);

	void copy(IExtendedPlayer iep);

	void onDataChanged();

	void onStartedTracking(PlayerEntity tracker);
	
	boolean hasHorde(World world);
	
	void setBloodmoon(int i);
	int getBloodmoon();
	
	void setWolfHorde(int i);
	int getWolfHorde();
	
	void setHorde(int i);
	int getHorde();
	
	void setDrinkCounter(int i);
	int getDrinkCounter();
	
	void setSurvivedBloodmoons(int i);
	int getSurvivedBloodmons();
	
	void setLastBloodmoonSurvivalCheck(int i);
	int getLastBloodmoonSurvivalCheck();

}