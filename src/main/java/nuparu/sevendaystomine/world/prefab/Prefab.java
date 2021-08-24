package nuparu.sevendaystomine.world.prefab;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.prefab.buffered.BufferedBlock;
import nuparu.sevendaystomine.world.prefab.buffered.BufferedEntity;

import java.util.List;

public class Prefab {
    String name;

    int width;
    int height;
    int length;

    int offsetX;
    int offsetY;
    int offsetZ;

    int pedestalLayer = 0;

    boolean underground = false;
    double requiredRoom;
    double requiredFlatness;
    int weight;

    EnumPrefabType type = EnumPrefabType.NONE;
    List<EnumStructureBiomeType> biomeTypes;

    List<BufferedBlock> blocks;
    List<BufferedEntity> entities;

    public Prefab(String name, int width, int height, int length, int offsetX, int offsetY, int offsetZ,
                  boolean underground, double requiredRoom, double requiredFlatness, int weight, EnumPrefabType type,
                  List<EnumStructureBiomeType> biomeTypes, List<BufferedBlock> blocks, List<BufferedEntity> entities,
                  int pedestalLayer) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        this.pedestalLayer = pedestalLayer;

        this.underground = underground;
        this.requiredRoom = requiredRoom;
        this.requiredFlatness = requiredFlatness;
        this.weight = weight;

        this.type = type;
        this.biomeTypes = biomeTypes;

        this.blocks = blocks;
        this.entities = entities;
    }

    public void generate(World world, BlockPos pos, int rotation) {
        this.generate(world, pos, rotation, false);
    }

    @SuppressWarnings("deprecation")
    public void generate(World world, BlockPos pos, int rotation, boolean base) {
        for (BufferedBlock buffered : blocks) {
            BufferedBlock bufferedBlock = buffered.rotate(rotation);
            BlockState state = bufferedBlock.getBlockState();
            if(buffered.getState() != null && !buffered.getState().isEmpty()){
                try {
                    state = NBTUtil.readBlockState(JsonToNBT.parseTag(buffered.getState()));
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }
            if (state == null)
                continue;
            BlockPos pos2 = bufferedBlock.getPos(pos);
            if (pos2 == null)
                continue;

            /*if (base) {
                BlockState state2 = world.getBlockState(pos2);
                if (!state2.getBlock().isReplaceable(world, pos2)
                        && state.getBlock() instanceof VineBlock) {
                    continue;
                }
            }*/

            Block block = state.getBlock();
            state = block.rotate(state, Utils.intToRotation(rotation));

            if(block instanceof LeavesBlock){
                state = state.setValue(LeavesBlock.PERSISTENT,true);
            }

            world.setBlockAndUpdate(pos2, state);
           if (state.getBlock().hasTileEntity(state)) {
                TileEntity TE = world.getBlockEntity(pos2);
                if (TE != null) {
                    CompoundNBT oldNBT = TE.save(new CompoundNBT());
                    CompoundNBT newNBT = bufferedBlock.getNBT().copy();
                    newNBT.putString("id", oldNBT.getString("id"));
                    newNBT.putInt("x", oldNBT.getInt("x"));
                    newNBT.putInt("y", oldNBT.getInt("y"));
                    newNBT.putInt("z", oldNBT.getInt("z"));
                    if (TE != null) {
                        TE.load(state,newNBT);
                    }
                }
            }
            /*if (base) {
                if (bufferedBlock.getY() == pedestalLayer && !state.getBlock().isReplaceable(world, pos2)) {

                    BlockPos pos3 = pos2.down();
                    BlockState state2 = world.getBlockState(pos3);
                    Block block2 = state2.getBlock();
                    while (block2.isReplaceable(world, pos3) || block2 instanceof BlockLog
                            || block2 instanceof BlockLeaves) {
                        world.setBlockState(pos3, state);
                        pos3 = pos3.down();
                        state2 = world.getBlockState(pos3);
                        block2 = state2.getBlock();
                    }
                }
            }*/
        }
        for (BufferedEntity bufferedEntity : entities) {
            bufferedEntity.rotate(rotation).spawn(world, pos);
        }
    }

    public void generate(World world, BlockPos pos, Direction facing) {
        this.generate(world, pos, facing, false);
    }

    public void generate(World world, BlockPos pos, Direction facing, boolean base) {
        this.generate(world, pos, Utils.facingToInt(facing), base);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLength() {
        return this.length;
    }
}