package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IItemHolsterTransform {

	@OnlyIn(Dist.CLIENT)
    double getRotationX(ItemStack stack, PlayerEntity player);
	
	@OnlyIn(Dist.CLIENT)
    double getRotationY(ItemStack stack, PlayerEntity player);
	
	@OnlyIn(Dist.CLIENT)
    double getRotationZ(ItemStack stack, PlayerEntity player);
	
    void setRotationX(double x);
	
	void setRotationY(double y);
	
	void setRotationZ(double z);
}
