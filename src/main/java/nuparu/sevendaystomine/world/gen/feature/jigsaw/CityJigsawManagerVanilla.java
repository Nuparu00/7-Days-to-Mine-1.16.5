package nuparu.sevendaystomine.world.gen.feature.jigsaw;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.block.JigsawBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CityJigsawManagerVanilla {
    private static final Logger LOGGER = LogManager.getLogger();

    static List<String> overridesList = new ArrayList<String>();

    public CityJigsawManagerVanilla() {
        overridesList.add("sevendaystomine:supermarket_right");
    }

    public static void addPieces(DynamicRegistries p_242837_0_, VillageConfig p_242837_1_, JigsawManager.IPieceFactory p_242837_2_, ChunkGenerator p_242837_3_, TemplateManager p_242837_4_, BlockPos p_242837_5_, List<? super AbstractVillagePiece> p_242837_6_, Random p_242837_7_, boolean p_242837_8_, boolean p_242837_9_) {
        Structure.bootstrap();
        MutableRegistry<JigsawPattern> mutableregistry = p_242837_0_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(p_242837_7_);
        JigsawPattern jigsawpattern = (JigsawPattern) p_242837_1_.startPool().get();
        JigsawPiece jigsawpiece = jigsawpattern.getRandomTemplate(p_242837_7_);
        AbstractVillagePiece abstractvillagepiece = p_242837_2_.create(p_242837_4_, jigsawpiece, p_242837_5_, jigsawpiece.getGroundLevelDelta(), rotation, jigsawpiece.getBoundingBox(p_242837_4_, p_242837_5_, rotation));
        MutableBoundingBox mutableboundingbox = abstractvillagepiece.getBoundingBox();
        int i = (mutableboundingbox.x1 + mutableboundingbox.x0) / 2;
        int j = (mutableboundingbox.z1 + mutableboundingbox.z0) / 2;
        int k;
        if (p_242837_9_) {
            k = p_242837_5_.getY() + p_242837_3_.getFirstFreeHeight(i, j, Heightmap.Type.WORLD_SURFACE_WG);
        } else {
            k = p_242837_5_.getY();
        }

        int l = mutableboundingbox.y0 + abstractvillagepiece.getGroundLevelDelta();
        abstractvillagepiece.move(0, k - l, 0);
        p_242837_6_.add(abstractvillagepiece);
        if (p_242837_1_.maxDepth() > 0) {
            AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) (i - 80), (double) (k - 80), (double) (j - 80), (double) (i + 80 + 1), (double) (k + 80 + 1), (double) (j + 80 + 1));
            CityJigsawManagerVanilla.Assembler jigsawmanager$assembler = new CityJigsawManagerVanilla.Assembler(mutableregistry, p_242837_1_.maxDepth(), p_242837_2_, p_242837_3_, p_242837_4_, p_242837_6_, p_242837_7_);
            jigsawmanager$assembler.placing.addLast(new CityJigsawManagerVanilla.Entry(abstractvillagepiece, new MutableObject(VoxelShapes.join(VoxelShapes.create(axisalignedbb), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)), IBooleanFunction.ONLY_FIRST)), k + 80, 0));

            while (!jigsawmanager$assembler.placing.isEmpty()) {
                CityJigsawManagerVanilla.Entry jigsawmanager$entry = (CityJigsawManagerVanilla.Entry) jigsawmanager$assembler.placing.removeFirst();
                List<SingleJigsawPiece> pieces = jigsawmanager$assembler.tryPlacingChildren(jigsawmanager$entry.piece, jigsawmanager$entry.free, jigsawmanager$entry.boundsTop, jigsawmanager$entry.depth, p_242837_8_);
            }
        }

    }

    public static void addPieces(DynamicRegistries p_242838_0_, AbstractVillagePiece p_242838_1_, int p_242838_2_, JigsawManager.IPieceFactory p_242838_3_, ChunkGenerator p_242838_4_, TemplateManager p_242838_5_, List<? super AbstractVillagePiece> p_242838_6_, Random p_242838_7_) {
        MutableRegistry<JigsawPattern> mutableregistry = p_242838_0_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        CityJigsawManagerVanilla.Assembler jigsawmanager$assembler = new CityJigsawManagerVanilla.Assembler(mutableregistry, p_242838_2_, p_242838_3_, p_242838_4_, p_242838_5_, p_242838_6_, p_242838_7_);
        jigsawmanager$assembler.placing.addLast(new CityJigsawManagerVanilla.Entry(p_242838_1_, new MutableObject(VoxelShapes.INFINITY), 0, 0));

        while (!jigsawmanager$assembler.placing.isEmpty()) {
            CityJigsawManagerVanilla.Entry jigsawmanager$entry = (CityJigsawManagerVanilla.Entry) jigsawmanager$assembler.placing.removeFirst();
            jigsawmanager$assembler.tryPlacingChildren(jigsawmanager$entry.piece, jigsawmanager$entry.free, jigsawmanager$entry.boundsTop, jigsawmanager$entry.depth, false);
        }

    }

    public interface IPieceFactory {
        AbstractVillagePiece create(TemplateManager var1, JigsawPiece var2, BlockPos var3, int var4, Rotation var5, MutableBoundingBox var6);
    }


    public static boolean depthOverride(Entry entry){
        JigsawPiece jigsawPiece = entry.piece.getElement();
        if(jigsawPiece instanceof SingleJigsawPiece) {
            SingleJigsawPiece singleJigsawPiece = (SingleJigsawPiece)jigsawPiece;
            if (singleJigsawPiece.template.left().isPresent()){
                ResourceLocation resourceLocation = singleJigsawPiece.template.left().get();
                return overridesList.contains(resourceLocation.toString());
            }
        }
        return false;
    }

    public static boolean depthOverride(AbstractVillagePiece villagePiece){
        JigsawPiece jigsawPiece = villagePiece.getElement();
        System.out.println("PHUJKY");
        if(jigsawPiece instanceof SingleJigsawPiece) {
            SingleJigsawPiece singleJigsawPiece = (SingleJigsawPiece)jigsawPiece;
            System.out.println("FUJKY");
            if (singleJigsawPiece.template.left().isPresent()){
                ResourceLocation resourceLocation = singleJigsawPiece.template.left().get();
                System.out.println("JUDGE " + resourceLocation.toString() + " " + overridesList.toString() + " " + overridesList.contains(resourceLocation.toString()));
                return overridesList.contains(resourceLocation.toString());
            }
        }
        return false;
    }

    public static ResourceLocation getJigsawPieceLocation(JigsawPiece jigsawPiece){
        if(jigsawPiece instanceof SingleJigsawPiece) {
            SingleJigsawPiece singleJigsawPiece = (SingleJigsawPiece)jigsawPiece;
            if (singleJigsawPiece.template.left().isPresent()){
                return singleJigsawPiece.template.left().get();
            }
        }
        return null;
    }

    static final class Entry {
        private final AbstractVillagePiece piece;
        private final MutableObject<VoxelShape> free;
        private final int boundsTop;
        private final int depth;

        private Entry(AbstractVillagePiece p_i232042_1_, MutableObject<VoxelShape> p_i232042_2_, int p_i232042_3_, int p_i232042_4_) {
            this.piece = p_i232042_1_;
            this.free = p_i232042_2_;
            this.boundsTop = p_i232042_3_;
            this.depth = p_i232042_4_;
        }
    }

    static final class Assembler {
        private final Registry<JigsawPattern> pools;
        private final int maxDepth;
        private final JigsawManager.IPieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final TemplateManager structureManager;
        private final List<? super AbstractVillagePiece> pieces;
        private final Random random;
        private final Deque<CityJigsawManagerVanilla.Entry> placing;

        private Assembler(Registry<JigsawPattern> p_i242005_1_, int p_i242005_2_, JigsawManager.IPieceFactory p_i242005_3_, ChunkGenerator p_i242005_4_, TemplateManager p_i242005_5_, List<? super AbstractVillagePiece> p_i242005_6_, Random p_i242005_7_) {
            this.placing = Queues.newArrayDeque();
            this.pools = p_i242005_1_;
            this.maxDepth = p_i242005_2_;
            this.factory = p_i242005_3_;
            this.chunkGenerator = p_i242005_4_;
            this.structureManager = p_i242005_5_;
            this.pieces = p_i242005_6_;
            this.random = p_i242005_7_;
        }

        private List<SingleJigsawPiece> tryPlacingChildren(AbstractVillagePiece oldPiece, MutableObject<VoxelShape> p_236831_2_, int p_236831_3_, int p_236831_4_, boolean p_236831_5_) {
            JigsawPiece jigsawpiece = oldPiece.getElement();
            BlockPos blockpos = oldPiece.getPosition();
            Rotation rotation = oldPiece.getRotation();
            JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = jigsawpiece.getProjection();
            boolean flag = jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID;
            MutableObject<VoxelShape> mutableobject = new MutableObject<>();
            MutableBoundingBox mutableboundingbox = oldPiece.getBoundingBox();
            int i = mutableboundingbox.y0;

            List<SingleJigsawPiece> generatedPieces = new ArrayList<SingleJigsawPiece>();
            label139:
            //Goes thourgh all jigsaw blocks of the old piece
            for (Template.BlockInfo template$blockinfo : jigsawpiece.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
                Direction direction = JigsawBlock.getFrontFacing(template$blockinfo.state);
                BlockPos blockpos1 = template$blockinfo.pos;
                BlockPos blockpos2 = blockpos1.relative(direction);
                int j = blockpos1.getY() - i;
                int k = -1;
                ResourceLocation resourcelocation = new ResourceLocation(template$blockinfo.nbt.getString("pool"));
                Optional<JigsawPattern> optional = this.pools.getOptional(resourcelocation);
                if (optional.isPresent() && (optional.get().size() != 0 || Objects.equals(resourcelocation, JigsawPatternRegistry.EMPTY.location()))) {
                    ResourceLocation resourcelocation1 = optional.get().getFallback();
                    Optional<JigsawPattern> optional1 = this.pools.getOptional(resourcelocation1);
                    if (optional1.isPresent() && (optional1.get().size() != 0 || Objects.equals(resourcelocation1, JigsawPatternRegistry.EMPTY.location()))) {
                        boolean flag1 = mutableboundingbox.isInside(blockpos2);
                        MutableObject<VoxelShape> mutableobject1;
                        int l;
                        if (flag1) {
                            mutableobject1 = mutableobject;
                            l = i;
                            if (mutableobject.getValue() == null) {
                                mutableobject.setValue(VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)));
                            }
                        } else {
                            mutableobject1 = p_236831_2_;
                            l = p_236831_3_;
                        }

                        List<JigsawPiece> list = Lists.newArrayList();
                        if (p_236831_4_ != this.maxDepth || depthOverride(oldPiece)) {
                            list.addAll(optional.get().getShuffledTemplates(this.random));
                        }

                        //depthOverride(oldPiece);

                        list.addAll(optional1.get().getShuffledTemplates(this.random));

                        //Goes through all possible new pieces
                        for (JigsawPiece jigsawpiece1 : list) {
                            if (jigsawpiece1 == EmptyJigsawPiece.INSTANCE) {
                                break;
                            }
                            //System.out.println("GLAZZ "  + jigsawpiece1.getClass().toString());

                            //Tries all rotations
                            for (Rotation rotation1 : Rotation.getShuffled(this.random)) {
                                List<Template.BlockInfo> list1 = jigsawpiece1.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, rotation1, this.random);
                                MutableBoundingBox mutableboundingbox1 = jigsawpiece1.getBoundingBox(this.structureManager, BlockPos.ZERO, rotation1);

                                int i1;
                                if (p_236831_5_ && mutableboundingbox1.getYSpan() <= 16) {
                                    i1 = list1.stream().mapToInt((p_242841_2_) -> {
                                        if (!mutableboundingbox1.isInside(p_242841_2_.pos.relative(JigsawBlock.getFrontFacing(p_242841_2_.state)))) {
                                            return 0;
                                        } else {
                                            ResourceLocation resourcelocation2 = new ResourceLocation(p_242841_2_.nbt.getString("pool"));
                                            Optional<JigsawPattern> optional2 = this.pools.getOptional(resourcelocation2);
                                            Optional<JigsawPattern> optional3 = optional2.flatMap((p_242843_1_) -> {
                                                return this.pools.getOptional(p_242843_1_.getFallback());
                                            });
                                            int k3 = optional2.map((p_242842_1_) -> {
                                                return p_242842_1_.getMaxSize(this.structureManager);
                                            }).orElse(0);
                                            int l3 = optional3.map((p_242840_1_) -> {
                                                return p_242840_1_.getMaxSize(this.structureManager);
                                            }).orElse(0);
                                            return Math.max(k3, l3);
                                        }
                                    }).max().orElse(0);
                                } else {
                                    i1 = 0;
                                }

                                for (Template.BlockInfo template$blockinfo1 : list1) {
                                    if (JigsawBlock.canAttach(template$blockinfo, template$blockinfo1)) {
                                        BlockPos blockpos3 = template$blockinfo1.pos;
                                        BlockPos blockpos4 = new BlockPos(blockpos2.getX() - blockpos3.getX(), blockpos2.getY() - blockpos3.getY(), blockpos2.getZ() - blockpos3.getZ());
                                        MutableBoundingBox mutableboundingbox2 = jigsawpiece1.getBoundingBox(this.structureManager, blockpos4, rotation1);
                                        int j1 = mutableboundingbox2.y0;
                                        JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour1 = jigsawpiece1.getProjection();
                                        boolean flag2 = jigsawpattern$placementbehaviour1 == JigsawPattern.PlacementBehaviour.RIGID;
                                        int k1 = blockpos3.getY();
                                        int l1 = j - k1 + JigsawBlock.getFrontFacing(template$blockinfo.state).getStepY();
                                        int i2;
                                        if (flag && flag2) {
                                            i2 = i + l1;
                                        } else {
                                            if (k == -1) {
                                                k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                            }

                                            i2 = k - k1;
                                        }

                                        int j2 = i2 - j1;
                                        MutableBoundingBox mutableboundingbox3 = mutableboundingbox2.moved(0, j2, 0);
                                        BlockPos blockpos5 = blockpos4.offset(0, j2, 0);
                                        if (i1 > 0) {
                                            int k2 = Math.max(i1 + 1, mutableboundingbox3.y1 - mutableboundingbox3.y0);
                                            mutableboundingbox3.y1 = mutableboundingbox3.y0 + k2;
                                        }

                                        //Collision check
                                        if (!VoxelShapes.joinIsNotEmpty(mutableobject1.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox3).deflate(0.25D)), IBooleanFunction.ONLY_SECOND) || depthOverride(oldPiece)) {
                                            mutableobject1.setValue(VoxelShapes.joinUnoptimized(mutableobject1.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox3)), IBooleanFunction.ONLY_FIRST));
                                            int j3 = oldPiece.getGroundLevelDelta();
                                            int l2;
                                            if (flag2) {
                                                l2 = j3 - l1;
                                            } else {
                                                l2 = jigsawpiece1.getGroundLevelDelta();
                                            }

                                            AbstractVillagePiece newPiece = this.factory.create(this.structureManager, jigsawpiece1, blockpos5, l2, rotation1, mutableboundingbox3);
                                            int i3;
                                            if (flag) {
                                                i3 = i + j;
                                            } else if (flag2) {
                                                i3 = i2 + k1;
                                            } else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                                }

                                                i3 = k + l1 / 2;
                                            }

                                            oldPiece.addJunction(new JigsawJunction(blockpos2.getX(), i3 - j + j3, blockpos2.getZ(), l1, jigsawpattern$placementbehaviour1));
                                            newPiece.addJunction(new JigsawJunction(blockpos1.getX(), i3 - k1 + l2, blockpos1.getZ(), -l1, jigsawpattern$placementbehaviour));
                                            this.pieces.add(newPiece);
                                            if(jigsawpiece1 instanceof SingleJigsawPiece){
                                                SingleJigsawPiece singleJigsawPiece = (SingleJigsawPiece)jigsawpiece1;
                                                System.out.println(singleJigsawPiece.template.left().get().toString() +  " " + p_236831_4_ + " " + maxDepth + " "  + ((p_236831_4_ + 1 <= maxDepth)));
                                            }
                                            if (p_236831_4_ + 1 <= this.maxDepth || depthOverride(oldPiece)) {
                                                //Adds the new piece to the list of future parents
                                                this.placing.addLast(new CityJigsawManagerVanilla.Entry(newPiece, mutableobject1, l, p_236831_4_ + 1));
                                            }

                                            if(jigsawpiece1 instanceof SingleJigsawPiece){
                                                generatedPieces.add((SingleJigsawPiece)jigsawpiece1);
                                            }
                                            continue label139;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        CityJigsawManagerVanilla.LOGGER.warn("Empty or none existent fallback pool: {}", (Object) resourcelocation1);
                    }
                } else {
                    CityJigsawManagerVanilla.LOGGER.warn("Empty or none existent pool: {}", (Object) resourcelocation);
                }
            }
            return generatedPieces;
        }
    }


}
