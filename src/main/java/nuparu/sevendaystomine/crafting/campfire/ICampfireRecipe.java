package nuparu.sevendaystomine.crafting.campfire;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityCampfire;

public interface ICampfireRecipe {
	 boolean matches(TileEntityCampfire inv, World worldIn);
	 ItemStack getResult();
	 ItemStack getOutput(TileEntityCampfire tileEntity);
	 ItemStack getPot();
	 List<ItemStack> getIngredients();
	 void consumeInput(TileEntityCampfire tileEntity);
	 int intGetXP(PlayerEntity player);
	 
}
