package nuparu.sevendaystomine.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface ILootTableProvider {

    ResourceLocation getLootTable();

    boolean tryLoadLootTable(CompoundNBT p_184283_1_);

    boolean trySaveLootTable(CompoundNBT p_184282_1_);

    void unpackLootTable(@Nullable PlayerEntity p_184281_1_);

    void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_);
    void setLootTable(ResourceLocation p_189404_1_);
}
