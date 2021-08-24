package nuparu.sevendaystomine.crafting.forge;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

public interface IForgeRecipe {
	 ForgeResult matches(TileEntityForge inv, World worldIn);
	 ItemStack getResult();
	 public ItemStack getOutput(TileEntityForge tileEntity);
	 public ItemStack getMold();
	 public List<ItemStack> getIngredients();
	 //Returns the leftovers
	 public List<ItemStack> consumeInput(TileEntityForge tileEntity);
	 public int intGetXP(PlayerEntity player);
	 
}
