package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IItemHolsterTransform {

	@OnlyIn(Dist.CLIENT)
	public double getRotationX(ItemStack stack, PlayerEntity player);
	
	@OnlyIn(Dist.CLIENT)
	public double getRotationY(ItemStack stack, PlayerEntity player);
	
	@OnlyIn(Dist.CLIENT)
	public double getRotationZ(ItemStack stack, PlayerEntity player);
	
    public void setRotationX(double x);
	
	public void setRotationY(double y);
	
	public void setRotationZ(double z);
}
