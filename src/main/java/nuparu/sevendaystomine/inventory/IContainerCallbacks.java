package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.player.PlayerEntity;

public interface IContainerCallbacks {

	void onContainerOpened(PlayerEntity player);

	void onContainerClosed(PlayerEntity player);

	boolean isUsableByPlayer(PlayerEntity player);
}