package nuparu.sevendaystomine.electricity.network;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface INetwork {
	
	
	List<BlockPos> getConnections();
	void connectTo(INetwork toConnect);
	void disconnect(INetwork toDisconnect);
	boolean isConnectedTo(INetwork net);
	void disconnectAll();
	BlockPos getPosition();
	void sendPacket(String packet, INetwork from, PlayerEntity playerFrom);
	
}
