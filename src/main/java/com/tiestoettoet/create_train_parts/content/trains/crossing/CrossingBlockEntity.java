package com.tiestoettoet.create_train_parts.content.trains.crossing;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.*;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static com.tiestoettoet.create_train_parts.content.trains.crossing.CrossingBlock.OPEN;

public class CrossingBlockEntity extends KineticBlockEntity implements IControlContraption {
    LerpedFloat animation;
    int bridgeTicks;
    public boolean running;
    protected float angle;
    protected double sequencedAngleLimit;
    protected boolean assembleNextTick;

    public ControlledContraptionEntity movedContraption;
//    boolean deferUpdate;
//    Map<String, BlockState> neighborStates = new HashMap<>();

    protected AssemblyException lastException;
    Object openObj = null;

    public CrossingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        animation = LerpedFloat.linear()
                .startWithValue(isOpen(getBlockState()) ? 1 : 0);
        sequencedAngleLimit = -1;
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        lastException = AssemblyException.read(tag, registries);
        super.read(tag, registries, clientPacket);
        invalidateRenderBoundingBox();

        if (tag.contains("ForceOpen"))
            openObj = tag.getBoolean("ForceOpen");
    }

    public void assemble() throws AssemblyException {
        if (!(level.getBlockState(worldPosition)
                .getBlock() instanceof CrossingBlock))
            return;

        Direction direction = getBlockState().getValue(HORIZONTAL_FACING);
        CrossingContraption contraption = new CrossingContraption(direction);
//        System.out.println(contraption.assemble(level, worldPosition));
        if (!contraption.assemble(level, worldPosition))
            return;



        contraption.removeBlocksFromWorld(level, BlockPos.ZERO);
        movedContraption = ControlledContraptionEntity.create(level, this, contraption);
        BlockPos anchor = worldPosition;
        movedContraption.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
        movedContraption.setRotationAxis(direction.getAxis());
        level.addFreshEntity(movedContraption);

//        System.out.println(movedContraption);

        AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(level, worldPosition);

        if (contraption.containsBlockBreakers())
            award(AllAdvancements.CONTRAPTION_ACTORS);

        running = true;
        angle = 90;
        sendData();
    }

    public void disassemble() {
        if (!running && movedContraption == null)
            return;
        angle = 0;
        sequencedAngleLimit = -1;
        if (movedContraption != null) {
            movedContraption.disassemble();
            AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(level, worldPosition);
        }

        movedContraption = null;
        running = false;
        assembleNextTick = false;
        sendData();
    }

    @Override
    public void tick() {
        super.tick();
        BlockState block = getBlockState();
        CrossingBlock crossingBlock = (CrossingBlock) block.getBlock();
        boolean open = isOpen(getBlockState());
//        if (open != isOpen(getBlockState())) {
//            block = block.setValue(OPEN, !open);
//            level.setBlock(worldPosition, block, 10);
//            level.gameEvent(null, block.getValue(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, worldPosition);
//            level.sendBlockUpdated(worldPosition, block, block, 3);
//        }
        boolean wasSettled = animation.settled();
        BlockEntity below = level.getBlockEntity(worldPosition.below());
        float speed = 0;
        if (below instanceof KineticBlockEntity kbe) {
            speed = getSpeed();
        }

//        System.out.println("Speed: " + speed);
        boolean shouldOpen = speed < 0;
        if (speed < 0) {
            speed = -speed;
        }
        speed = speed / 50f * 0.05f;
        animation.chase(shouldOpen ? 0 : 1, speed, LerpedFloat.Chaser.LINEAR);
        animation.tickChaser();

        if (level.isClientSide()) {
            if (bridgeTicks < 2 && open)
                bridgeTicks++;
            else if (bridgeTicks > 0 && !open)
                bridgeTicks--;
        }

        if (animation.settled() && open == (animation.getValue() != 0)) {
//            disassemble();
            return;
        }

        block = block.setValue(OPEN, animation.getValue() != 0);
        level.setBlock(worldPosition, block, 10);

        try {
            assemble();
        } catch (AssemblyException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(blocks);
        movedContraption.setAngle((float) (1.56 * animation.getValue()));
//
        // Update connected ArmExtenderBlocks
//        updateConnectedArmExtenders(animation.getChaseTarget(), speed);
        // System.out.println("Animation value: " + animation.getValue());
        // System.out.println("Bridge ticks: " + bridgeTicks);

    }




    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(1);
    }

    protected boolean shouldRenderSpecial(BlockState state) {
        return true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public static boolean isOpen(BlockState state) {
        return state.getOptionalValue(OPEN)
                .orElse(false);
    }

    @Override
    public boolean isAttachedTo(AbstractContraptionEntity contraption) {
        return movedContraption == contraption;
    }

    @Override
    public void attach(ControlledContraptionEntity contraption) {
        this.movedContraption = contraption;
        if (!level.isClientSide) {
            this.running = true;
            sendData();
        }
    }

    @Override
    public void onStall() {
        if (!level.isClientSide) {
            sendData();
        }
    }

    @Override
    public boolean isValid() {
        return !isRemoved();
    }

    @Override
    public BlockPos getBlockPosition() {
        return worldPosition;
    }
}
