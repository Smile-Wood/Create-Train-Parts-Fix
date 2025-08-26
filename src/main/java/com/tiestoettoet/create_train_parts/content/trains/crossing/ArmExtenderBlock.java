package com.tiestoettoet.create_train_parts.content.trains.crossing;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import com.tiestoettoet.create_train_parts.AllBlockEntityTypes;
import com.tiestoettoet.create_train_parts.AllBlocks;
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.placement.PoleHelper;
import com.tiestoettoet.create_train_parts.content.foundation.placement.ArmHelper;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class ArmExtenderBlock extends HorizontalDirectionalBlock implements IWrenchable {
    private static final int placementHelperId = PlacementHelpers.register(PlacementHelper.get());

    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public ArmExtenderBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FLIPPED, false).setValue(OPEN, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        }

        Direction facing = context.getHorizontalDirection(); // Get the horizontal direction the player is facing
        boolean flipped = false; // Default value for flipped

        return state.setValue(HORIZONTAL_FACING, facing).setValue(FLIPPED, flipped).setValue(OPEN, true);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (placementHelper.matchesItem(stack) && !player.isShiftKeyDown())
            return placementHelper.getOffset(player, level, state, pos, hitResult).placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static boolean isArm(BlockState state) {
        return AllBlocks.ARM_EXTENDER.has(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(HORIZONTAL_FACING).add(FLIPPED).add(OPEN));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        return state;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
        // RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

//    @Override
//    public Class<ArmExtenderBlockEntity> getBlockEntityClass() {
//        return ArmExtenderBlockEntity.class;
//    }
//
//    @Override
//    public BlockEntityType<? extends ArmExtenderBlockEntity> getBlockEntityType() {
//        return AllBlockEntityTypes.ARM_EXTENDER.get();
//    }

    @MethodsReturnNonnullByDefault
    public static class PlacementHelper extends ArmHelper<Direction> {

        private static final PlacementHelper instance = new PlacementHelper();

        public static PlacementHelper get() {
            return instance;
        }

        private PlacementHelper() {
            super(
                    AllBlocks.ARM_EXTENDER::has,
                    state -> state.getValue(HORIZONTAL_FACING).getClockWise().getAxis(),
                    HORIZONTAL_FACING
            );
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.ARM_EXTENDER::isIn;
        }
    }

}
