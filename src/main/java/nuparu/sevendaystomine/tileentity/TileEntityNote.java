package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.client.util.ResourcesHelper.Image;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.util.book.BookData;

public class TileEntityNote extends TileEntity {

	//Permanent data
	public ResourceLocation data;

	//Cached bookData instance
	private BookData bookData;

	public TileEntityNote() {
		super(ModTileEntities.NOTE.get());
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains("data", Constants.NBT.TAG_STRING)) {
			this.data = new ResourceLocation(compound.getString("data"));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (this.data != null) {
			compound.putString("data", this.data.toString());
		}
		return compound;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return super.getRenderBoundingBox().inflate(2);
	}

	public ResourceLocation getData() {
		return this.data;
	}

	public void setData(ResourceLocation data) {
		this.data = data;
	}

	public void markForUpdate() {
		level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
				level.getBlockState(worldPosition), 3);
		setChanged();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		this.save(nbtTag);
		return new SUpdateTileEntityPacket(this.worldPosition, 0, nbtTag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT tag = pkt.getTag();
		load(level.getBlockState(pkt.getPos()), tag);
		if (hasLevel()) {
			level.sendBlockUpdated(pkt.getPos(), level.getBlockState(this.worldPosition),
					level.getBlockState(pkt.getPos()), 2);
		}
	}

	public BookData getBookData(){
		if(bookData == null){
			if(data == null) return null;

			bookData = BookDataManager.instance.get(data);
		}
		return bookData;
	}
}
