package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

public class ContainerCodeSafeLocked extends Container {

    private final World world;
    TileEntityCodeSafe tileEntity;

    protected ContainerCodeSafeLocked(int windowID, PlayerInventory invPlayer, TileEntityCodeSafe tileEntity) {
        super(ModContainers.CODE_SAFE_LOCKED.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;
    }

    public static ContainerCodeSafeLocked createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerCodeSafeLocked(windowID,playerInventory, (TileEntityCodeSafe) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()));
    }

    public static ContainerCodeSafeLocked createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityCodeSafe tileEntity){
        return new ContainerCodeSafeLocked(windowID, playerInventory, tileEntity);
    }


    @Override
    public boolean stillValid(PlayerEntity player) {
        return tileEntity.isUsableByPlayer(player);
    }


    public TileEntityCodeSafe getTileEntity(){
        return tileEntity;
    }
}
