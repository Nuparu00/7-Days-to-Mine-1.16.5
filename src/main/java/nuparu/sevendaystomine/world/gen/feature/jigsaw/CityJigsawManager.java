package nuparu.sevendaystomine.world.gen.feature.jigsaw;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.JigsawBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.mixin.JigsawPatternAccessor;
import nuparu.sevendaystomine.mixin.SingleJigsawPieceAccessor;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CityJigsawManager {
    private static final Logger LOGGER = LogManager.getLogger();

    public CityJigsawManager() {
    }

    public static void addPieces(DynamicRegistries p_242837_0_, VillageConfig p_242837_1_, JigsawManager.IPieceFactory p_242837_2_, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos p_242837_5_, List<? super StructurePiece> components, Random random, boolean p_242837_8_, boolean p_242837_9_,int maxY,
                                 int minY) {
        Structure.bootstrap();
        MutableRegistry<JigsawPattern> mutableregistry = p_242837_0_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(random);
        JigsawPattern jigsawpattern = (JigsawPattern)p_242837_1_.startPool().get();
        JigsawPiece jigsawpiece = jigsawpattern.getRandomTemplate(random);

        AbstractVillagePiece abstractvillagepiece = p_242837_2_.create(templateManager, jigsawpiece, p_242837_5_, jigsawpiece.getGroundLevelDelta(), rotation, jigsawpiece.getBoundingBox(templateManager, p_242837_5_, rotation));
        MutableBoundingBox mutableboundingbox = abstractvillagepiece.getBoundingBox();
        int pieceCenterX = (mutableboundingbox.x1 + mutableboundingbox.x0) / 2;
        int pieceCenterZ = (mutableboundingbox.z1 + mutableboundingbox.z0) / 2;
        int pieceCenterY;
        if (p_242837_9_) {
            pieceCenterY  = p_242837_5_.getY() + chunkGenerator.getFirstFreeHeight(pieceCenterX, pieceCenterZ, Heightmap.Type.WORLD_SURFACE_WG);
        } else {
            pieceCenterY  = p_242837_5_.getY();
        }

        int l = mutableboundingbox.y0 + abstractvillagepiece.getGroundLevelDelta();
        abstractvillagepiece.move(0, pieceCenterY  - l, 0);
        components.add(abstractvillagepiece);
        if (p_242837_1_.maxDepth() > 0) {
            AxisAlignedBB axisalignedbb = new AxisAlignedBB((double)(pieceCenterX - 80), (double)(pieceCenterY  - 80), (double)(pieceCenterZ - 80), (double)(pieceCenterX + 80 + 1), (double)(pieceCenterY  + 80 + 1), (double)(pieceCenterZ + 80 + 1));
            BoxOctree boxOctree = new BoxOctree(axisalignedbb);
            Entry startPieceEntry = new Entry(abstractvillagepiece, new MutableObject<>(boxOctree), pieceCenterY + 80, 0);

            Assembler assembler = new Assembler(mutableregistry, p_242837_1_.maxDepth(),chunkGenerator, templateManager, components, random,maxY, minY);
            assembler.availablePieces.addLast(startPieceEntry);

            while(!assembler.availablePieces.isEmpty()) {


                Entry entry = assembler.availablePieces.removeFirst();
                assembler.generatePiece(entry.piece, entry.parentBoxOctree, entry.boundsTop, entry.depth, p_242837_8_);

            }
        }

    }

    /*public static void addPieces(DynamicRegistries p_242838_0_, AbstractVillagePiece p_242838_1_, int p_242838_2_, JigsawManager.IPieceFactory p_242838_3_, ChunkGenerator p_242838_4_, TemplateManager p_242838_5_, List<? super AbstractVillagePiece> p_242838_6_, Random p_242838_7_) {
        MutableRegistry<JigsawPattern> mutableregistry = p_242838_0_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Assembler jigsawmanager$assembler =  new Assembler(mutableregistry, p_242837_1_.maxDepth(),chunkGenerator, templateManager, components, random,maxY, minY);
        jigsawmanager$assembler.availablePieces.addLast(new CityJigsawManager.Entry(p_242838_1_, new MutableObject(VoxelShapes.INFINITY), 0, 0));

        while(!jigsawmanager$assembler.availablePieces.isEmpty()) {
            CityJigsawManager.Entry jigsawmanager$entry = (CityJigsawManager.Entry)jigsawmanager$assembler.availablePieces.removeFirst();
            jigsawmanager$assembler.generatePiece(jigsawmanager$entry.piece, jigsawmanager$entry.parentBoxOctree, jigsawmanager$entry.boundsTop, jigsawmanager$entry.depth, false);
        }

    }*/

    public interface IPieceFactory {
        AbstractVillagePiece create(TemplateManager var1, JigsawPiece var2, BlockPos var3, int var4, Rotation var5, MutableBoundingBox var6);
    }

    static final class Entry {
        private final AbstractVillagePiece piece;
        public final MutableObject<BoxOctree> parentBoxOctree;
        private final int boundsTop;
        private final int depth;

        private Entry(AbstractVillagePiece p_i232042_1_, MutableObject<BoxOctree> parentBoxOctree, int p_i232042_3_, int p_i232042_4_) {
            this.piece = p_i232042_1_;
            this.parentBoxOctree = parentBoxOctree;
            this.boundsTop = p_i232042_3_;
            this.depth = p_i232042_4_;
        }
    }

    static final class Assembler {
        private final Registry<JigsawPattern> poolRegistry;
        private final int maxDepth;
        private final ChunkGenerator chunkGenerator;
        private final TemplateManager templateManager;
        private final List<? super AbstractVillagePiece> structurePieces;
        private final Random rand;
        public final Deque<Entry> availablePieces = Queues.newArrayDeque();
        private final int maxY;
        private final int minY;

        public Assembler(Registry<JigsawPattern> poolRegistry, int maxDepth, ChunkGenerator chunkGenerator, TemplateManager templateManager, List<? super AbstractVillagePiece> structurePieces, Random rand, int maxY, int minY) {
            this.poolRegistry = poolRegistry;
            this.maxDepth = maxDepth;
            this.chunkGenerator = chunkGenerator;
            this.templateManager = templateManager;
            this.structurePieces = structurePieces;
            this.rand = rand;
            this.maxY = maxY;
            this.minY = minY;
        }

        public void generatePiece(AbstractVillagePiece piece, MutableObject<BoxOctree> boxOctree, int minY, int depth, boolean doBoundaryAdjustments) {
            // Collect data from params regarding piece to process
            JigsawPiece pieceBlueprint = piece.getElement();
            BlockPos piecePos = piece.getPosition();
            Rotation pieceRotation = piece.getRotation();
            MutableBoundingBox pieceBoundingBox = piece.getBoundingBox();
            MutableObject<BoxOctree> parentOctree = new MutableObject<>();
            int pieceMinY = pieceBoundingBox.y0;

            // Get list of all jigsaw blocks in this piece
            List<Template.BlockInfo> pieceJigsawBlocks = pieceBlueprint.getShuffledJigsawBlocks(this.templateManager, piecePos, pieceRotation, this.rand);

            for (Template.BlockInfo jigsawBlock : pieceJigsawBlocks) {
                // Gather jigsaw block information
                Direction direction = JigsawBlock.getFrontFacing(jigsawBlock.state);
                BlockPos jigsawBlockPos = jigsawBlock.pos;
                BlockPos jigsawBlockTargetPos = jigsawBlockPos.relative(direction);

                // Get the jigsaw block's piece pool
                ResourceLocation jigsawBlockPool = new ResourceLocation(jigsawBlock.nbt.getString("pool"));
                Optional<JigsawPattern> poolOptional = this.poolRegistry.getOptional(jigsawBlockPool);

                // Only continue if we are using the jigsaw pattern registry and if it is not empty
                if (!(poolOptional.isPresent() && (poolOptional.get().size() != 0 || Objects.equals(jigsawBlockPool, JigsawPatternRegistry.EMPTY.location())))) {
                    SevenDaysToMine.LOGGER.warn("Repurposed Structures: Empty or nonexistent pool: {} which is being called from {}", jigsawBlockPool, pieceBlueprint instanceof SingleJigsawPiece ? ((SingleJigsawPieceAccessor) pieceBlueprint).sevendaystomine_getTemplate().left().get() : "not a SingleJigsawPiece class");
                    continue;
                }

                // Get the jigsaw block's fallback pool (which is a part of the pool's JSON)
                ResourceLocation jigsawBlockFallback = poolOptional.get().getFallback();
                Optional<JigsawPattern> fallbackOptional = this.poolRegistry.getOptional(jigsawBlockFallback);

                // Only continue if the fallback pool is present and valid
                if (!(fallbackOptional.isPresent() && (fallbackOptional.get().size() != 0 || Objects.equals(jigsawBlockFallback, JigsawPatternRegistry.EMPTY.location())))) {
                    SevenDaysToMine.LOGGER.warn("Repurposed Structures: Empty or nonexistent pool: {} which is being called from {}", jigsawBlockFallback, pieceBlueprint instanceof SingleJigsawPiece ? ((SingleJigsawPieceAccessor) pieceBlueprint).sevendaystomine_getTemplate().left().get() : "not a SingleJigsawPiece class");
                    continue;
                }

                // Adjustments for if the target block position is inside the current piece
                // Sets which octree to use for bounds checking
                boolean isTargetInsideCurrentPiece = pieceBoundingBox.isInside(jigsawBlockTargetPos);
                int targetPieceBoundsTop;
                MutableObject<BoxOctree> octreeToUse;
                if (isTargetInsideCurrentPiece) {
                    targetPieceBoundsTop = pieceMinY;
                    octreeToUse = parentOctree;
                    if(parentOctree.getValue() == null) {
                        parentOctree.setValue(new BoxOctree(AxisAlignedBB.of(pieceBoundingBox)));
                    }
                }
                else {
                    targetPieceBoundsTop = minY;
                    octreeToUse = boxOctree;
                }

                // Process the pool pieces, randomly choosing different pieces from the pool to spawn
                if (depth != this.maxDepth) {
                    JigsawPiece generatedPiece = this.processList(new ArrayList<>(((JigsawPatternAccessor)poolOptional.get()).sevendaystomine_getRawTemplates()), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, octreeToUse, piece, depth, targetPieceBoundsTop);
                    if (generatedPiece != null) continue; // Stop here since we've already generated the piece
                }

                // Process the fallback pieces in the event none of the pool pieces work
                this.processList(new ArrayList<>(((JigsawPatternAccessor)fallbackOptional.get()).sevendaystomine_getRawTemplates()), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, octreeToUse, piece, depth, targetPieceBoundsTop);
            }
        }

        /**
         * Helper function. Searches candidatePieces for a suitable piece to spawn.
         * All other params are intended to be passed directly from {@link Assembler#generatePiece}
         * @return The piece genereated, or null if no suitable pieces were found.
         */
        private JigsawPiece processList(
                List<Pair<JigsawPiece, Integer>> candidatePieces,
                boolean doBoundaryAdjustments,
                Template.BlockInfo jigsawBlock,
                BlockPos jigsawBlockTargetPos,
                int pieceMinY,
                BlockPos jigsawBlockPos,
                MutableObject<BoxOctree> mutableObjectBoxOctree,
                AbstractVillagePiece piece,
                int depth,
                int targetPieceBoundsTop
        ) {
            JigsawPattern.PlacementBehaviour piecePlacementBehavior = piece.getElement().getProjection();
            boolean isPieceRigid = piecePlacementBehavior == JigsawPattern.PlacementBehaviour.RIGID;
            int jigsawBlockRelativeY = jigsawBlockPos.getY() - pieceMinY;
            int surfaceHeight = -1; // The y-coordinate of the surface. Only used if isPieceRigid is false.

            int totalCount = candidatePieces.stream().mapToInt(Pair::getSecond).reduce(0, Integer::sum);

            while (candidatePieces.size() > 0) {
                // Prioritize required piece if the following conditions are met:
                // 1. It's a potential candidate for this pool
                // 2. It hasn't already been placed
                // 3. We are at least certain amount of pieces away from the starting piece.
                Pair<JigsawPiece, Integer> chosenPiecePair = null;
                // Condition 2

                // Choose piece if required piece wasn't selected
                if (chosenPiecePair == null) {
                    int chosenWeight = rand.nextInt(totalCount) + 1;

                    for (Pair<JigsawPiece, Integer> candidate : candidatePieces) {
                        chosenWeight -= candidate.getSecond();
                        if (chosenWeight <= 0) {
                            chosenPiecePair = candidate;
                            break;
                        }
                    }
                }

                JigsawPiece candidatePiece = chosenPiecePair.getFirst();

                // Vanilla check. Not sure on the implications of this.
                if (candidatePiece == EmptyJigsawPiece.INSTANCE) {
                    return null;
                }

                // Before performing any logic, check to ensure we haven't reached the max number of instances of this piece.
                // This logic is my own additional logic - vanilla does not offer this behavior.
                ResourceLocation pieceName = null;


                // Try different rotations to see which sides of the piece are fit to be the receiving end
                for (Rotation rotation : Rotation.getShuffled(this.rand)) {
                    List<Template.BlockInfo> candidateJigsawBlocks = candidatePiece.getShuffledJigsawBlocks(this.templateManager, BlockPos.ZERO, rotation, this.rand);
                    MutableBoundingBox tempCandidateBoundingBox = candidatePiece.getBoundingBox(this.templateManager, BlockPos.ZERO, rotation);

                    // Some sort of logic for setting the candidateHeightAdjustments var if doBoundaryAdjustments.
                    // Not sure on this - personally, I never enable doBoundaryAdjustments.
                    int candidateHeightAdjustments;
                    if (doBoundaryAdjustments && tempCandidateBoundingBox.getYSpan() <= 16) {
                        candidateHeightAdjustments = candidateJigsawBlocks.stream().mapToInt((pieceCandidateJigsawBlock) -> {
                            if (!tempCandidateBoundingBox.isInside(pieceCandidateJigsawBlock.pos.relative(JigsawBlock.getFrontFacing(pieceCandidateJigsawBlock.state)))) {
                                return 0;
                            } else {
                                ResourceLocation candidateTargetPool = new ResourceLocation(pieceCandidateJigsawBlock.nbt.getString("pool"));
                                Optional<JigsawPattern> candidateTargetPoolOptional = this.poolRegistry.getOptional(candidateTargetPool);
                                Optional<JigsawPattern> candidateTargetFallbackOptional = candidateTargetPoolOptional.flatMap((p_242843_1_) -> this.poolRegistry.getOptional(p_242843_1_.getFallback()));
                                int tallestCandidateTargetPoolPieceHeight = candidateTargetPoolOptional.map((p_242842_1_) -> p_242842_1_.getMaxSize(this.templateManager)).orElse(0);
                                int tallestCandidateTargetFallbackPieceHeight = candidateTargetFallbackOptional.map((p_242840_1_) -> p_242840_1_.getMaxSize(this.templateManager)).orElse(0);
                                return Math.max(tallestCandidateTargetPoolPieceHeight, tallestCandidateTargetFallbackPieceHeight);
                            }
                        }).max().orElse(0);
                    } else {
                        candidateHeightAdjustments = 0;
                    }

                    // Check for each of the candidate's jigsaw blocks for a match
                    for (Template.BlockInfo candidateJigsawBlock : candidateJigsawBlocks) {
                        if (JigsawBlock.canAttach(jigsawBlock, candidateJigsawBlock)) {
                            BlockPos candidateJigsawBlockPos = candidateJigsawBlock.pos;
                            BlockPos candidateJigsawBlockRelativePos = new BlockPos(jigsawBlockTargetPos.getX() - candidateJigsawBlockPos.getX(), jigsawBlockTargetPos.getY() - candidateJigsawBlockPos.getY(), jigsawBlockTargetPos.getZ() - candidateJigsawBlockPos.getZ());

                            // Get the bounding box for the piece, offset by the relative position difference
                            MutableBoundingBox candidateBoundingBox = candidatePiece.getBoundingBox(this.templateManager, candidateJigsawBlockRelativePos, rotation);

                            // Determine if candidate is rigid
                            JigsawPattern.PlacementBehaviour candidatePlacementBehavior = candidatePiece.getProjection();
                            boolean isCandidateRigid = candidatePlacementBehavior == JigsawPattern.PlacementBehaviour.RIGID;

                            // Determine how much the candidate jigsaw block is off in the y direction.
                            // This will be needed to offset the candidate piece so that the jigsaw blocks line up properly.
                            int candidateJigsawBlockRelativeY = candidateJigsawBlockPos.getY();
                            int candidateJigsawYOffsetNeeded = jigsawBlockRelativeY - candidateJigsawBlockRelativeY + JigsawBlock.getFrontFacing(jigsawBlock.state).getStepY();

                            // Determine how much we need to offset the candidate piece itself in order to have the jigsaw blocks aligned.
                            // Depends on if the placement of both pieces is rigid or not
                            int adjustedCandidatePieceMinY;
                            if (isPieceRigid && isCandidateRigid) {
                                adjustedCandidatePieceMinY = pieceMinY + candidateJigsawYOffsetNeeded;
                            } else {
                                if (surfaceHeight == -1) {
                                    surfaceHeight = this.chunkGenerator.getFirstFreeHeight(jigsawBlockPos.getX(), jigsawBlockPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                }

                                adjustedCandidatePieceMinY = surfaceHeight - candidateJigsawBlockRelativeY;
                            }
                            int candidatePieceYOffsetNeeded = adjustedCandidatePieceMinY - candidateBoundingBox.y0;

                            // Offset the candidate's bounding box by the necessary amount
                            MutableBoundingBox adjustedCandidateBoundingBox = candidateBoundingBox.moved(0, candidatePieceYOffsetNeeded, 0);

                            // Add this offset to the relative jigsaw block position as well
                            BlockPos adjustedCandidateJigsawBlockRelativePos = candidateJigsawBlockRelativePos.offset(0, candidatePieceYOffsetNeeded, 0);

                            // Final adjustments to the bounding box.
                            if (candidateHeightAdjustments > 0) {
                                int heightAdjustement = Math.max(candidateHeightAdjustments + 1, adjustedCandidateBoundingBox.y1 - adjustedCandidateBoundingBox.y0);
                                adjustedCandidateBoundingBox.y1 = adjustedCandidateBoundingBox.y0 + heightAdjustement;
                            }

                            // Prevent pieces from spawning above max Y or below min Y
                            if (adjustedCandidateBoundingBox.y1 > this.maxY || adjustedCandidateBoundingBox.y0 < this.minY) {
                                continue;
                            }

                            AxisAlignedBB axisAlignedBB = AxisAlignedBB.of(adjustedCandidateBoundingBox);
                            AxisAlignedBB axisAlignedBBDeflated = axisAlignedBB.deflate(0.25D); // Avoid any edge case weirdness if size is exact with bounding boxes.

                            // debugging
//                            if(!(mutableObjectBoxOctree.getValue().boundaryContains(axisAlignedBBDeflated) && !mutableObjectBoxOctree.getValue().intersectsAnyBox(axisAlignedBBDeflated))){
//                                if(piece.toString().contains("bastion")){
//                                    sevendaystomine.LOGGER.warn(" Failed to spawn pieces. Parent: {} - General info: {} - Child: {} - General info: {} - Octree: {}", piece.getBoundingBox().toString(), piece.toString(), axisAlignedBBDeflated.toString(), candidatePiece.toString(), mutableObjectBoxOctree.toString());
//                                }
//                            }

                            // Make sure new piece fits within the chosen octree without intersecting any other piece.
                            if (mutableObjectBoxOctree.getValue().boundaryContains(axisAlignedBBDeflated) && !mutableObjectBoxOctree.getValue().intersectsAnyBox(axisAlignedBBDeflated)) {
                                mutableObjectBoxOctree.getValue().addBox(axisAlignedBB);

                                // Determine ground level delta for this new piece
                                int newPieceGroundLevelDelta = piece.getGroundLevelDelta();
                                int groundLevelDelta;
                                if (isCandidateRigid) {
                                    groundLevelDelta = newPieceGroundLevelDelta - candidateJigsawYOffsetNeeded;
                                } else {
                                    groundLevelDelta = candidatePiece.getGroundLevelDelta();
                                }

                                // Create new piece
                                AbstractVillagePiece newPiece = new AbstractVillagePiece(
                                        this.templateManager,
                                        candidatePiece,
                                        adjustedCandidateJigsawBlockRelativePos,
                                        groundLevelDelta,
                                        rotation,
                                        adjustedCandidateBoundingBox
                                );

                                // Determine actual y-value for the new jigsaw block
                                int candidateJigsawBlockY;
                                if (isPieceRigid) {
                                    candidateJigsawBlockY = pieceMinY + jigsawBlockRelativeY;
                                } else if (isCandidateRigid) {
                                    candidateJigsawBlockY = adjustedCandidatePieceMinY + candidateJigsawBlockRelativeY;
                                } else {
                                    if (surfaceHeight == -1) {
                                        surfaceHeight = this.chunkGenerator.getFirstFreeHeight(jigsawBlockPos.getX(), jigsawBlockPos.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                    }

                                    candidateJigsawBlockY = surfaceHeight + candidateJigsawYOffsetNeeded / 2;
                                }

                                // Add the junction to the existing piece
                                piece.addJunction(
                                        new JigsawJunction(
                                                jigsawBlockTargetPos.getX(),
                                                candidateJigsawBlockY - jigsawBlockRelativeY + newPieceGroundLevelDelta,
                                                jigsawBlockTargetPos.getZ(),
                                                candidateJigsawYOffsetNeeded,
                                                candidatePlacementBehavior)
                                );

                                // Add the junction to the new piece
                                newPiece.addJunction(
                                        new JigsawJunction(
                                                jigsawBlockPos.getX(),
                                                candidateJigsawBlockY - candidateJigsawBlockRelativeY + groundLevelDelta,
                                                jigsawBlockPos.getZ(),
                                                -candidateJigsawYOffsetNeeded,
                                                piecePlacementBehavior)
                                );

                                // Add the piece
                                this.structurePieces.add(newPiece);
                                if (depth + 1 <= this.maxDepth) {
                                    this.availablePieces.addLast(new Entry(newPiece, mutableObjectBoxOctree, targetPieceBoundsTop, depth + 1));
                                }
                                return candidatePiece;
                            }
                        }
                    }
                }
                totalCount -= chosenPiecePair.getSecond();
                candidatePieces.remove(chosenPiecePair);
            }
            return null;
        }
    }

}
