package com.tiestoettoet.create_train_parts.content.decoration.trainStep;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.tiestoettoet.create_train_parts.content.foundation.gui.AllIcons;
import com.tiestoettoet.create_train_parts.content.foundation.utility.CreateTrainPartsLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainStepBlockEntity extends SmartBlockEntity {
    LerpedFloat animation;
    int bridgeTicks;
    boolean deferUpdate;
    Map<String, BlockState> neighborStates = new HashMap<>();
    TrainStepType trainStepType;
    protected ScrollOptionBehaviour<SlideMode> slideMode;

    public TrainStepBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        animation = LerpedFloat.linear()
                .startWithValue(isOpen(getBlockState()) ? 1 : 0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(slideMode = new ScrollOptionBehaviour<>(SlideMode.class,
                CreateTrainPartsLang.translateDirect("train_step.mode"), this, new TrainStepModeSlot()));

        slideMode.onlyActiveWhen(this::isVisible);
        slideMode.requiresWrench();


    }

    public SlideMode getMode() {
        return slideMode.get();
    }

    public boolean isVisible() {
        return getBlockState().getValue(TrainStepBlock.VISIBLE);
    }

    @Override
    public void tick() {
        if (deferUpdate && !level.isClientSide()) {
            deferUpdate = false;
            BlockState blockState = getBlockState();
            blockState.handleNeighborChanged(level, worldPosition, Blocks.AIR, worldPosition, false);
        }
        super.tick();
        boolean open = isOpen(getBlockState());
        boolean wasSettled = animation.settled();
//        System.out.println("TrainStepBlockEn/**/tity ticking: " + worldPosition + ", open: " + open + ", wasSettled: " + wasSettled);
        animation.chase(open ? 1 : 0, .15f, LerpedFloat.Chaser.LINEAR);
        animation.tickChaser();

        if (level.isClientSide()) {
            if (bridgeTicks < 2 && open)
                bridgeTicks++;
            else if (bridgeTicks > 0 && !open && isVisible(getBlockState()))
                bridgeTicks--;
            return;
        }

        if (!open && !wasSettled && animation.settled() && !isVisible(getBlockState()))
            showBlockModel();
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(1);
    }

    protected boolean isVisible(BlockState state) {

        return state.getOptionalValue(TrainStepBlock.VISIBLE)
                .orElse(true);
    }

    public void setNeighborState(BlockState state) {
        if (level == null)
            return; // Ensure the level is not null

        Direction facing = state.getValue(TrainStepBlock.FACING); // Get the block's facing direction
        BlockPos leftPos = worldPosition.relative(facing.getCounterClockWise()); // Calculate left neighbor position
        BlockPos rightPos = worldPosition.relative(facing.getClockWise()); // Calculate right neighbor position

        BlockState leftState = level.getBlockState(leftPos); // Get the left neighbor's state
        BlockState rightState = level.getBlockState(rightPos); // Get the right neighbor's state

        neighborStates.put("left", leftState); // Store the left state
        neighborStates.put("right", rightState); // Store the right state
    }

    public Map<String, BlockState> getNeighborStates() {
        return neighborStates; // Return the map of neighbor states
    }

    public void setTrainStepType(TrainStepType trainStepType) {
        this.trainStepType = trainStepType;
    }

    public TrainStepType getTrainStepType() {
        return trainStepType;
    }

    protected boolean shouldRenderSpecial(BlockState state) {
        return !isVisible(state) || bridgeTicks != 0;
    }

    protected void showBlockModel() {
        level.setBlock(worldPosition, getBlockState().setValue(TrainStepBlock.VISIBLE, true), 3);
        level.playSound(null, worldPosition, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, .5f, 1);
    }

    public enum SlideMode implements INamedIconOptions, StringRepresentable {
        SLIDE(AllIcons.I_OPEN_SLIDE),
        NO_SLIDE(AllIcons.I_CLOSE_SLIDE)

        ;

        private final String translationKey;
        private final AllIcons icon;

        SlideMode(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "step.mode." + Lang.asId(name());
        }

        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }


    public static boolean isOpen(BlockState state) {
        return state.getOptionalValue(TrainStepBlock.OPEN)
                .orElse(false);
    }
}
