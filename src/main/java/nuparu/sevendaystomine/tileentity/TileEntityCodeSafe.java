package nuparu.sevendaystomine.tileentity;

import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.block.BlockCodeSafe;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.item.ItemStethoscope;
import nuparu.sevendaystomine.util.Utils;

public class TileEntityCodeSafe extends TileEntitySafe {

	private int correctCode = 000;
	private int selectedCode = 000;

	public TileEntityCodeSafe() {
		super(ModTileEntities.CODE_SAFE.get());
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		correctCode = compound.getInt("CorrectCode");
		selectedCode = compound.getInt("SelectedCode");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		 super.save(compound);
		compound.putInt("CorrectCode", correctCode);
		compound.putInt("SelectedCode", selectedCode);
		return compound;
	}

	@Override
	public void tick() {
		if (!level.isClientSide()) {
			super.tick();
			if (!init) {
				while (correctCode == selectedCode && locked) {
					Random random = level.random;
					correctCode = random.nextInt(1000);
					selectedCode = random.nextInt(1000);
				}
				init = !init;
				setChanged();
			}
			tryToUnlock();
		}
	}

	public boolean tryToUnlock() {
		if (correctCode == selectedCode && locked) {
			unlock();
			return true;
		} else if (correctCode != selectedCode && !locked) {
			lock();
			return false;
		}
		return false;

	}

	public void unlock() {
		super.unlock();
		level.playSound(null, worldPosition, ModSounds.SAFE_UNLOCK.get(), SoundCategory.BLOCKS, 0.9f + level.random.nextFloat() / 4f,
				0.9f + level.random.nextFloat() / 4f);
		locked = false;
		setChanged();
		BlockCodeSafe.setState(locked, level, worldPosition);
	}

	public void lock() {
		locked = true;
		setChanged();
		BlockCodeSafe.setState(locked, level, worldPosition);

	}

	public void setInit(boolean init) {
		this.init = init;
		setChanged();
	}

	public int getSelectedCode() {
		return this.selectedCode;
	}

	public int superSecretMethod() {
		return this.correctCode;
	}

	public void setSelectedCode(int code, @Nullable ServerPlayerEntity player ) {
		this.selectedCode = code;
		boolean prevState = locked;
		tryToUnlock();
		System.out.println(this.superSecretMethod());
		if(player != null && prevState && !locked) {
			ModTriggers.SAFE_UNLOCK.trigger(player, new Predicate() {
				@Override
				public boolean test(Object o) {
					return true;
				}
			});
		}
		setChanged();
	}

	public void setCorrectCode(int code) {
		this.correctCode = code;
		setChanged();
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
		nbtTag.remove("CorrectCode");
		return new SUpdateTileEntityPacket(this.worldPosition, 0, nbtTag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = save(new CompoundNBT());
		nbt.remove("CorrectCode");
		return nbt;
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

	/*
	 * 0 = units, 2 = hundreds,...
	 */
	private int tryToGetDigit(PlayerEntity player, int digit) {
		if (!isUsableByPlayer(player))
			return -1;
		if (!(player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemStethoscope))
			return -1;

		return (int) ((correctCode / Math.pow(10, digit)) % 10);
	}

	public int testDigit(PlayerEntity player, int guess, int numPos) {
		int digit = tryToGetDigit(player, numPos);
		return Integer.compare(digit, guess);
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public net.minecraft.inventory.container.Container createMenu(int windowID, PlayerInventory playerInventory,
			PlayerEntity playerEntity) {
		System.out.println(locked);
		return (this.locked || playerEntity.isCrouching()) ? null : ContainerSmall.createContainerServerSide(windowID, playerInventory, this);
	}

}
