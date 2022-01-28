package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.tileentity.TileEntityCarMaster;
import nuparu.sevendaystomine.tileentity.TileEntityCarSlave;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BlockCar extends BlockHorizontalBase implements ISalvageable, IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    /*
     * Format: Height/Width/Length 0 = Empty; Any other value = block Width should
     * ideally be odd number
     */
    public byte[][][] shape;
    public ResourceLocation lootTable = ModLootTables.SEDAN;
    public ResourceLocation salvageLootTable = ModLootTables.CAR_SALVAGE;
    public boolean special = false;

    public BlockCar(AbstractBlock.Properties properties, byte[][][] shape) {
        super(properties.noOcclusion().isSuffocating(BlockCar::never).isViewBlocking(BlockCar::never).dynamicShape());
        this.shape = shape;
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(MASTER, true).setValue(WATERLOGGED, Boolean.FALSE));
    }

    private static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
        return false;
    }

    /*
     * For perfect cuboids
     */
    public BlockCar(AbstractBlock.Properties properties, int width, int length, int height) {
        super(properties);
        this.shape = new byte[height][width][length];
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(MASTER, true));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return state.getValue(MASTER) ? new TileEntityCarMaster() : new TileEntityCarSlave();
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
                            ItemStack stack) {
        TileEntity TE = worldIn.getBlockEntity(pos);
        if (!(TE instanceof TileEntityCarMaster)) {
            return;
        }
        TileEntityCarMaster masterTE = (TileEntityCarMaster) TE;
        if (state.getValue(MASTER)) {
            Direction facing = state.getValue(FACING);
            generate(worldIn, pos, facing, false, masterTE);

        }
    }

    /*
    Place version
     */
    public void generate(World worldIn, BlockPos pos, Direction facing, boolean placeMaster,
                         TileEntityCarMaster masterTE) {
        if (placeMaster) {
            FluidState fluidstate = worldIn.getFluidState(pos);
            BlockState state = defaultBlockState().setValue(FACING, facing).setValue(MASTER, true).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            worldIn.setBlockAndUpdate(pos, state);
            TileEntity TE = worldIn.getBlockEntity(pos);
            if (!(TE instanceof TileEntityCarMaster)) {
                return;
            }
            masterTE = (TileEntityCarMaster) TE;
        }

        masterTE.setLootTable(lootTable, worldIn.random.nextLong());

        int index = 1;

        for (int height = 0; height < shape.length; height++) {
            byte[][] shape2d = shape[height];
            for (int length = 0; length < shape2d.length; length++) {
                byte[] shape1d = shape2d[length];
                for (int width = 0; width < shape1d.length; width++) {
                    byte point = shape1d[width];
                    if (point == 0)
                        continue;
                    BlockPos pos2 = pos.relative(facing.getClockWise(), width - Math.round(shape2d.length / 2) + 1)
                            .relative(facing, length - Math.round(shape1d.length / 2) - 1).above(height);
                    if (pos2 == pos) {
                        continue;
                    }
                    FluidState fluidstate = worldIn.getFluidState(pos2);
                    BlockState state2 = defaultBlockState().setValue(FACING, facing).setValue(MASTER, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
                    worldIn.setBlockAndUpdate(pos2, state2);
                    TileEntity TE2 = worldIn.getBlockEntity(pos2);
                    if (TE2 instanceof TileEntityCarSlave) {
                        TileEntityCarSlave slave = (TileEntityCarSlave) TE2;
                        slave.setMaster(pos, masterTE,worldIn);
                        slave.setIndex(index);
                    }
                    index++;
                }
            }
        }
    }

    /*
    World Generator version
     */
    public void generate(IServerWorld worldIn, BlockPos pos, Direction facing, boolean placeMaster,
                         TileEntityCarMaster masterTE, Random random) {
        if (placeMaster) {
            FluidState fluidstate = worldIn.getFluidState(pos);
            BlockState state = defaultBlockState().setValue(FACING, facing).setValue(MASTER, true).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            worldIn.setBlock(pos, state,0);
            TileEntity TE = worldIn.getBlockEntity(pos);
            if (!(TE instanceof TileEntityCarMaster)) {
                return;
            }
            masterTE = (TileEntityCarMaster) TE;
        }

        masterTE.setLootTable(lootTable,random.nextLong());

        int index = 1;

        for (int height = 0; height < shape.length; height++) {
            byte[][] shape2d = shape[height];
            for (int length = 0; length < shape2d.length; length++) {
                byte[] shape1d = shape2d[length];
                for (int width = 0; width < shape1d.length; width++) {
                    byte point = shape1d[width];
                    if (point == 0)
                        continue;
                    BlockPos pos2 = pos.relative(facing.getClockWise(), width - Math.round(shape2d.length / 2) + 1)
                            .relative(facing, length - Math.round(shape1d.length / 2) - 1).above(height);
                    if (pos2 == pos) {
                        continue;
                    }
                    FluidState fluidstate = worldIn.getFluidState(pos2);
                    BlockState state2 = defaultBlockState().setValue(FACING, facing).setValue(MASTER, false).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
                    worldIn.setBlock(pos2, state2,0);
                    TileEntity TE2 = worldIn.getBlockEntity(pos2);
                    if (TE2 instanceof TileEntityCarSlave) {
                        TileEntityCarSlave slave = (TileEntityCarSlave) TE2;
                        slave.setMaster(pos, masterTE, worldIn);
                        slave.setIndex(index);
                    }
                    index++;
                }
            }
        }
    }

    public boolean canBePlaced(IServerWorld world, BlockPos pos, Direction facing) {
        for (int height = 0; height < shape.length; height++) {
            byte[][] shape2d = shape[height];
            for (int length = 0; length < shape2d.length; length++) {
                byte[] shape1d = shape2d[length];
                for (int width = 0; width < shape1d.length; width++) {
                    byte point = shape1d[width];
                    if (point == 0)
                        continue;
                    BlockPos pos2 = pos.relative(facing.getClockWise(), width - Math.round(shape2d.length / 2) + 1)
                            .relative(facing, length - Math.round(shape1d.length / 2) - 1).above(height);
                    BlockState state = world.getBlockState(pos2);
                    Block block2 = state.getBlock();
                    if (!state.getMaterial().isReplaceable()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                BlockRayTraceResult rayTraceResult) {
        if (player.isCrouching())
            return ActionResultType.PASS;

        TileEntity te = worldIn.getBlockEntity(pos);
        if (te != null) {
            if (te instanceof TileEntityCarMaster) {
                INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, te.getBlockPos());
                if (namedContainerProvider != null) {
                    TileEntityCarMaster tileEntity = (TileEntityCarMaster)namedContainerProvider;
                    tileEntity.unpackLootTable(player);
                    if (!(player instanceof ServerPlayerEntity))
                        return ActionResultType.FAIL;
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                    NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
                }
            } else if (te instanceof TileEntityCarSlave) {
                TileEntityCarSlave slave = (TileEntityCarSlave) te;
                TileEntityCarMaster master = slave.getMaster();
                if (master != null) {
                    master.unpackLootTable(player);
                    INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, master.getBlockPos());
                    if (namedContainerProvider != null) {
                        if (!(player instanceof ServerPlayerEntity))
                            return ActionResultType.FAIL;
                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                        NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(master.getBlockPos()));
                    }
                }
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getBlockEntity(pos);

            if (tileentity instanceof TileEntityCarMaster) {
                TileEntityCarMaster master = (TileEntityCarMaster) tileentity;
                for (Direction facing : Direction.values()) {
                    BlockPos pos2 = pos.relative(facing);
                    if (pos2.getY() < 0 || pos2.getY() > 255) {
                        continue;
                    }
                    TileEntity TE = world.getBlockEntity(pos2);
                    if (TE instanceof TileEntityCarSlave) {
                        TileEntityCarSlave slave2 = (TileEntityCarSlave) TE;
                        if (slave2.masterPos == master.getBlockPos()) {
                            world.destroyBlock(pos2, false);
                        }
                    }
                }
                InventoryHelper.dropContents(world, pos, master.getDrops());
            } else if (tileentity instanceof TileEntityCarSlave) {
                TileEntityCarSlave slave = (TileEntityCarSlave) tileentity;
                for (Direction facing : Direction.values()) {
                    BlockPos pos2 = pos.relative(facing);
                    if (pos2.getY() < 0 || pos2.getY() > 255) {
                        continue;
                    }
                    TileEntity TE = world.getBlockEntity(pos2);
                    if (TE instanceof TileEntityCarSlave) {
                        TileEntityCarSlave slave2 = (TileEntityCarSlave) TE;
                        System.out.println(slave2.masterPos + " " + slave.masterPos);
                        if (Utils.compareBlockPos(slave2.masterPos, slave.masterPos)) {
                            world.destroyBlock(pos2, false);
                        }
                    } else if (TE instanceof TileEntityCarMaster) {
                        TileEntityCarMaster master = (TileEntityCarMaster) TE;
                        if (Utils.compareBlockPos(master.getBlockPos(), slave.masterPos)) {
                            world.destroyBlock(pos2, false);
                        }
                    }
                }
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public ResourceLocation getSalvageLootTable(){
        return salvageLootTable;
    }

    @Override
    public void setSalvageLootTable(ResourceLocation resourceLocation){
        salvageLootTable = resourceLocation;
    }

    public List<ItemStack> getItems(World world, BlockPos pos, BlockState oldState, PlayerEntity player) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        if (world.random.nextDouble() < 0.05) {
            ItemStack engine = new ItemStack(ModItems.SMALL_ENGINE.get());
            ItemQuality.setQualityForPlayer(engine, player);
            items.add(engine);
        }
        if (world.random.nextDouble() < 0.05) {
            ItemStack battery = new ItemStack(ModItems.CAR_BATTERY.get());
            ItemQuality.setQualityForPlayer(battery, player);
            IBattery bat = (IBattery) battery.getItem();
            bat.setVoltage(battery, world, (long) (world.getRandom().nextDouble() * bat.getCapacity(battery, world)));
            items.add(battery);
        }
        if (world.random.nextDouble() < 0.25) {
            items.add(new ItemStack(ModItems.IRON_PIPE.get(), world.random.nextInt(3)));
        }
        items.add(new ItemStack(ModItems.IRON_SCRAP.get(), 1 + world.random.nextInt(5)));

        return items;
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.ANVIL_LAND;
    }

    @Override
    public float getUpgradeRate(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        return 1;
    }

    @Override
    public void onSalvage(World world, BlockPos pos, BlockState oldState) {
        world.destroyBlock(pos, false);
    }

    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
        TileEntity tileentity = p_220052_2_.getBlockEntity(p_220052_3_);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
        }

        return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, MASTER, WATERLOGGED);
    }

}
