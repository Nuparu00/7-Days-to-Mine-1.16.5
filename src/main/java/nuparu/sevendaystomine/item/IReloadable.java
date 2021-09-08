package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IReloadable {

	Item getReloadItem(ItemStack stack);
	int getReloadTime(ItemStack stack);
	SoundEvent getReloadSound();
	void onReloadStart(World world, PlayerEntity player, ItemStack stack, int reloadTime);
	void onReloadEnd(World world,PlayerEntity player, ItemStack stack, ItemStack bullet);
	int getAmmo(ItemStack stack, PlayerEntity player);
	void setAmmo(ItemStack stack, @Nullable  PlayerEntity player, int ammo);
	int getCapacity(ItemStack stack, @Nullable PlayerEntity player);
}
