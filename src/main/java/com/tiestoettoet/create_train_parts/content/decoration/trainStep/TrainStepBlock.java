package com.tiestoettoet.create_train_parts.content.decoration.trainStep;

import com.mojang.serialization.MapCodec;
import com.tiestoettoet.create_train_parts.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import com.simibubi.create.foundation.block.IBE;

public class TrainStepBlock extends HorizontalDirectionalBlock implements IBE<TrainStepBlockEntity> {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public static final EnumProperty<ConnectedState> CONNECTED = EnumProperty.create("connected", ConnectedState.class);

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    protected static final VoxelShape NORTH_OPEN;
    protected static final VoxelShape SOUTH_OPEN;
    protected static final VoxelShape WEST_OPEN;
    protected static final VoxelShape EAST_OPEN;
    protected static final VoxelShape CLOSED;

    private final BlockSetType type = BlockSetType.OAK;

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    public TrainStepBlock(Properties properties) {
        super(properties);
    }




    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (!(Boolean) state.getValue(OPEN)) {
            return CLOSED;
        } else {
            return switch ((Direction) state.getValue(FACING)) {
                default -> NORTH_OPEN;
                case SOUTH -> SOUTH_OPEN;
                case WEST -> WEST_OPEN;
                case EAST -> EAST_OPEN;
            };
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        if (stateForPlacement != null && stateForPlacement.getValue(OPEN))
            return stateForPlacement.setValue(OPEN, false)
                    .setValue(POWERED, false)
                    .setValue(FACING, facing)
                    .setValue(CONNECTED, ConnectedState.NONE);

        return stateForPlacement;
    }



    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return switch (pathComputationType) {
            case LAND, AIR -> (Boolean) state.getValue(OPEN);
            default -> false;
        };
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!this.type.canOpenByHand()) {
            return InteractionResult.PASS;
        } else {
            this.toggle(state, level, pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks() && this.type.canOpenByWindCharge() && !(Boolean)state.getValue(POWERED)) {
            this.toggle(state, level, pos, (Player)null);
        }

        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    private void toggle(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
        BlockState blockstate = (BlockState)state.cycle(OPEN);
        level.setBlock(pos, blockstate, 2);
    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
//        boolean isPowered = isStepPowered(level, pos, state);
//        if (defaultBlockState().is(block))
//            return;
//        if (isPowered == state.getValue(POWERED)) {
//            return;
//        }
//
//        TrainStepBlockEntity be = getBlockEntity(level, pos);
//        if (be != null && be.deferUpdate) {
//            return;
//        }
//
//        BlockState changedState = state.setValue(POWERED, Boolean.valueOf(isPowered))
//                .setValue(OPEN, Boolean.valueOf(isPowered));
//        if (isPowered)
//            changedState = changedState.setValue(VISIBLE, false);
//
//        if (isPowered != state.getValue(OPEN)) {
//            level.gameEvent(null, isPowered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
//            Direction facing = changedState.getValue(FACING);
//            BlockPos otherPos = pos.relative(facing);
//            BlockState otherStep = level.getBlockState(otherPos);
//        }
//
//        level.setBlock(pos, changedState, 2);
    }

    public static boolean isStepPowered(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BlockPos otherPos = pos.relative(facing);

        return level.hasNeighborSignal(pos) || level.hasNeighborSignal(otherPos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, POWERED, VISIBLE, CONNECTED);
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(VISIBLE) ? RenderShape.MODEL : RenderShape.ENTITYBLOCK_ANIMATED;
    }

    protected BlockSetType getType() {
        return this.type;
    }

    static {
        CLOSED = Stream.of(
                Stream.of(
                        Block.box(1, 11, 11, 15, 16, 16),
                        Stream.of(
                                Block.box(1, 15, 6, 15, 16, 11),
                                Block.box(1, 11, 0, 15, 16, 6),
                                Block.box(1, 6, 0, 15, 11, 1)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
        Shapes.join(Block.box(1, 1, 0, 15, 6, 5), Block.box(1, 0, 0, 15, 1, 5), BooleanOp.OR),
                Block.box(1, 0, 5, 15, 1, 16),
                Block.box(1, 1, 15, 15, 11, 16)
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
        Block.box(0, 0, 0, 1, 16, 16),
                Block.box(15, 0, 0, 16, 16, 16)
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        NORTH_OPEN = Stream.of(
                Stream.of(
                        Block.box(1, 11, 11, 15, 16, 16),
                        Stream.of(
                                Block.box(1, 11, 10, 15, 16, 11),
                                Block.box(1, 6, 5, 15, 11, 11),
                                Block.box(1, 5, 5, 15, 6, 10)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                        Shapes.join(Block.box(1, 1, 0, 15, 6, 5), Block.box(1, 0, -5, 15, 1, 0), BooleanOp.OR),
                        Block.box(1, 0, 5, 15, 1, 16),
                        Block.box(1, 1, 15, 15, 11, 16)
                ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                Block.box(0, 0, 0, 1, 16, 16),
                Block.box(15, 0, 0, 16, 16, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        EAST_OPEN = Stream.of(
                Stream.of(
                        Block.box(0, 11, 1, 5, 16, 15),
                        Stream.of(
                                Block.box(5, 11, 1, 6, 16, 15),
                                Block.box(5, 6, 1, 11, 11, 15),
                                Block.box(6, 5, 1, 11, 6, 15)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                        Shapes.join(Block.box(11, 1, 1, 16, 6, 15), Block.box(16, 0, 1, 21, 1, 15), BooleanOp.OR),
                        Block.box(0, 0, 1, 11, 1, 15),
                        Block.box(0, 1, 1, 1, 11, 15)
                ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                Block.box(0, 0, 0, 16, 16, 1),
                Block.box(0, 0, 15, 16, 16, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        SOUTH_OPEN = Stream.of(
                Stream.of(
                        Block.box(1, 11, 0, 15, 16, 5),
                        Stream.of(
                                Block.box(1, 11, 5, 15, 16, 6),
                                Block.box(1, 6, 5, 15, 11, 11),
                                Block.box(1, 5, 6, 15, 6, 11)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                        Shapes.join(Block.box(1, 1, 11, 15, 6, 16), Block.box(1, 0, 16, 15, 1, 21), BooleanOp.OR),
                        Block.box(1, 0, 0, 15, 1, 11),
                        Block.box(1, 1, 0, 15, 11, 1)
                ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                Block.box(15, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 1, 16, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        WEST_OPEN = Stream.of(
                Stream.of(
                        Block.box(11, 11, 1, 16, 16, 15),
                        Stream.of(
                                Block.box(10, 11, 1, 11, 16, 15),
                                Block.box(5, 6, 1, 11, 11, 15),
                                Block.box(5, 5, 1, 10, 6, 15)
                        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                        Shapes.join(Block.box(0, 1, 1, 5, 6, 15), Block.box(-5, 0, 1, 0, 1, 15), BooleanOp.OR),
                        Block.box(5, 0, 1, 16, 1, 15),
                        Block.box(15, 1, 1, 16, 11, 15)
                ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                Block.box(0, 0, 15, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 1)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return IBE.super.newBlockEntity(pos, state);
    }

    @Override
    public Class<TrainStepBlockEntity> getBlockEntityClass() {
        return TrainStepBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TrainStepBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.TRAIN_STEP.get();
    }

    public enum ConnectedState implements StringRepresentable {
        NONE, RIGHT, LEFT, BOTH;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}


