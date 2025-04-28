package com.tiestoettoet.create_train_parts.content.contraptions.behaviour;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction;
import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class StepMovingInteraction extends SimpleBlockMovingInteraction {

    @Override
    protected BlockState handle(Player player, Contraption contraption, BlockPos pos, BlockState currentState) {
        boolean trainStep = currentState.getBlock() instanceof TrainStepBlock;
        SoundEvent sound = currentState.getValue(TrainStepBlock.OPEN) ? trainStep ? null : SoundEvents.WOODEN_DOOR_CLOSE
            : trainStep ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.WOODEN_DOOR_OPEN;
        currentState = currentState.cycle(TrainStepBlock.OPEN);
        if (player != null) {
            if (trainStep) {
                Direction facing = currentState.getValue(TrainStepBlock.FACING);

            }
            float pitch = player.level().random.nextFloat() * 0.1F + 0.9F;
            if (sound != null)
                playSound(player, sound, pitch);

        }
        return currentState;
    }

    @Override
    protected boolean updateColliders() {
        return true;
    }

}
