package nuparu.sevendaystomine.capability;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface IExtendedPlayer {

	public void setThirst(int thirst);

	public void consumeThirst(int thirst);

	public void addThirst(int thirst);

	public int getThirst();

	public int getMaximumThirst();

	public void setStamina(int stamina);

	public void consumeStamina(int stamina);

	public void addStamina(int stamina);

	public int getStamina();

	public int getMaximumStamina();

	public boolean getCrawling();

	public void setCrawling(boolean state);

	public boolean isInfected();

	public int getInfectionTime();

	public void setInfectionTime(int stage);
	
	public void unlockRecipe(String rec);
	
	public boolean hasRecipe(String rec);
	
	public List<String> getRecipes();

	public IExtendedPlayer setOwner(PlayerEntity player);

	public PlayerEntity getOwner();

	public void readNBT(CompoundNBT nbt);

	public CompoundNBT writeNBT(CompoundNBT nbt);

	public void copy(IExtendedPlayer iep);

	public void onDataChanged();

	public void onStartedTracking(PlayerEntity tracker);
	
	public boolean hasHorde(World world);
	
	public void setBloodmoon(int i);
	public int getBloodmoon();
	
	public void setWolfHorde(int i);
	public int getWolfHorde();
	
	public void setHorde(int i);
	public int getHorde();
	
	public void setDrinkCounter(int i);
	public int getDrinkCounter();
	
	public void setSurvivedBloodmoons(int i);
	public int getSurvivedBloodmons();
	
	public void setLastBloodmoonSurvivalCheck(int i);
	public int getLastBloodmoonSurvivalCheck();

}