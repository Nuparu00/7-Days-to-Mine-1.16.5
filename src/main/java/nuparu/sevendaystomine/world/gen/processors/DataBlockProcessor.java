package nuparu.sevendaystomine.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import nuparu.sevendaystomine.block.*;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModStructureProcessors;
import nuparu.sevendaystomine.util.Utils;

import java.util.Random;

//Based on https://github.com/TelepathicGrunt/RepurposedStructures/blob/312ef7f73499fe49081596f41d9a336b36ecab02/src/main/java/com/telepathicgrunt/repurposedstructures/world/processors/DataBlockProcessor.java#L27 by TelepathicGrunt
public class DataBlockProcessor extends StructureProcessor {


    public static final Codec<DataBlockProcessor> CODEC = Codec.unit(DataBlockProcessor::new);

    @Override
    public Template.BlockInfo processBlock(IWorldReader worldView, BlockPos pos, BlockPos blockPos, Template.BlockInfo structureBlockInfoLocal, Template.BlockInfo structureBlockInfoWorld, PlacementSettings structurePlacementData) {
        BlockState blockState = structureBlockInfoWorld.state;
        BlockPos worldPos = structureBlockInfoWorld.pos;
        if (blockState.is(Blocks.STRUCTURE_BLOCK)) {
            String metadata = structureBlockInfoWorld.nbt.getString("metadata");
            Random random = structurePlacementData.getRandom(worldPos);
            /*if(metadata.length() >= 9 && metadata.substring(0,9).equals("code_safe")){

                    Direction direction = Direction.byName(metadata.substring(10,metadata.length()-1));
                    BlockState state = ModBlocks.CODE_SAFE.get().defaultBlockState().setValue(BlockCodeSafe.FACING, direction);
                    state = state.getBlock().rotate(state, structurePlacementData.getRotation());

                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/garbage/garbage");
                    return new Template.BlockInfo(worldPos, state, nbt);

            }*/
            switch (metadata) {
                case "cobweb": {
                    if (random.nextInt(4) == 0) {
                        return new Template.BlockInfo(worldPos, Blocks.COBWEB.defaultBlockState(), null);
                    }
                    return new Template.BlockInfo(worldPos, Blocks.AIR.defaultBlockState(), null);
                }

                case "garbage": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/garbage/garbage");
                    return new Template.BlockInfo(worldPos, ModBlocks.GARBAGE.get().defaultBlockState().setValue(BlockGarbage.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "beaker": {
                    if (random.nextInt(10) == 0) {
                        return new Template.BlockInfo(worldPos, ModBlocks.BEAKER.get().defaultBlockState().setValue(BlockCookware.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), null);
                    }
                    return new Template.BlockInfo(worldPos, Blocks.AIR.defaultBlockState(), null);
                }
                case "backpack": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/backpack/backpack");
                    return new Template.BlockInfo(worldPos, ModBlocks.BACKPACK_NORMAL.get().defaultBlockState().setValue(BlockBackpack.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "medical_backpack": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/medical_cabinet/medical_cabinet");
                    return new Template.BlockInfo(worldPos, ModBlocks.BACKPACK_MEDICAL.get().defaultBlockState().setValue(BlockBackpack.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "cardboard": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/cardboard/cardboard");
                    return new Template.BlockInfo(worldPos, ModBlocks.CARDBOARD_BOX.get().defaultBlockState().setValue(BlockCardboardBox.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "nest": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/nest/nest");
                    return new Template.BlockInfo(worldPos, ModBlocks.BIRD_NEST.get().defaultBlockState().setValue(BlockCardboardBox.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "sedan_v": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("car", "sedan_red");
                    return new Template.BlockInfo(worldPos, ModBlocks.CAR_PLACER.get().defaultBlockState().setValue(BlockCarPlacer.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "sedan_h": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("car", "sedan_red");
                    return new Template.BlockInfo(worldPos, ModBlocks.CAR_PLACER.get().defaultBlockState().setValue(BlockCarPlacer.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "corpse": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/nest/nest");
                    BlockState state = ModBlocks.CORPSE_00.get().defaultBlockState();
                    if (random.nextBoolean()) {
                        state = ModBlocks.CORPSE_01.get().defaultBlockState();
                    }
                    return new Template.BlockInfo(worldPos, state.setValue(BlockCardboardBox.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "plagued_nurse": {
                    return null;
                }
                case "skeleton": {
                    BlockState state = Blocks.AIR.defaultBlockState();
                    if (random.nextInt(5) == 0) {
                        state = ModBlocks.SKELETON.get().defaultBlockState().setValue(BlockSkeleton.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]);
                    } else if (random.nextInt(5) == 0) {
                        state = ModBlocks.SKELETON_TORSO.get().defaultBlockState().setValue(BlockSkeleton.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]);
                    }
                    return new Template.BlockInfo(worldPos, state, null);
                }
                case "trash_can": {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("LootTable", "sevendaystomine:containers/trash_can/trash_can");
                    return new Template.BlockInfo(worldPos, ModBlocks.TRASH_CAN.get().defaultBlockState().setValue(BlockTrashCan.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]), nbt);
                }
                case "street_decoration": {
                    BlockState state = Blocks.AIR.defaultBlockState();
                    CompoundNBT nbt = null;
                    if (random.nextInt(2) == 0) {
                        int i = random.nextInt(10);
                        if (i <= 3) {
                            state = ModBlocks.PAPER.get().defaultBlockState().setValue(BlockPaper.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]);
                        } else if (i <= 7) {
                            nbt = new CompoundNBT();
                            nbt.putString("LootTable", "sevendaystomine:containers/garbage/garbage");
                            state = ModBlocks.GARBAGE.get().defaultBlockState().setValue(BlockGarbage.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]);
                        } else {
                            if (random.nextInt(10) <= 6) {
                                state = ModBlocks.SKELETON.get().defaultBlockState().setValue(BlockSkeleton.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]);
                            } else {
                                state = ModBlocks.SKELETON_TORSO.get().defaultBlockState().setValue(BlockSkeleton.FACING, Utils.HORIZONTALS[random.nextInt(Utils.HORIZONTALS.length)]);
                            }
                        }
                    }
                    return new Template.BlockInfo(worldPos, state, nbt);
                }
            }
        }
        if (blockState.getBlock() instanceof BlockCodeSafePlacer) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("LootTable", "sevendaystomine:containers/code_safe/code_safe");
            nbt.putBoolean("Locked", true);
            nbt.putBoolean("Init", false);
            return new Template.BlockInfo(worldPos, ModBlocks.CODE_SAFE.get().defaultBlockState().setValue(BlockCodeSafe.FACING, blockState.getValue(BlockCodeSafePlacer.FACING)).setValue(BlockCodeSafe.WATERLOGGED, blockState.getValue(BlockCodeSafePlacer.WATERLOGGED)), nbt);
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return ModStructureProcessors.DATA_BLOCK_PROCESSOR;
    }
}
