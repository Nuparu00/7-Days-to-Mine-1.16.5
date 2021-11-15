package nuparu.sevendaystomine.inventory.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModContainers;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;

public class ContainerMonitor extends Container {

    private final World world;
    TileEntityMonitor tileEntity;

    protected ContainerMonitor(int windowID, PlayerInventory invPlayer, TileEntityMonitor tileEntity) {
        super(ModContainers.MONITOR.get(), windowID);
        this.world = invPlayer.player.level;
        this.tileEntity = tileEntity;
    }

    public static ContainerMonitor createContainerClientSide(int windowID, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        return new ContainerMonitor(windowID,playerInventory, (TileEntityMonitor) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos()));
    }

    public static ContainerMonitor createContainerServerSide(int windowID, PlayerInventory playerInventory, TileEntityMonitor tileEntity){
        return new ContainerMonitor(windowID, playerInventory, tileEntity);
    }


    @Override
    public boolean stillValid(PlayerEntity player) {
        return tileEntity.isUsableByPlayer(player);
    }

    @Override
    public void removed(PlayerEntity playerIn)
    {
        super.removed(playerIn);
        if(tileEntity != null) {
            tileEntity.removePlayer(playerIn);
        }
    }

    public TileEntityMonitor getTileEntity() {
        return this.tileEntity;
    }
}
