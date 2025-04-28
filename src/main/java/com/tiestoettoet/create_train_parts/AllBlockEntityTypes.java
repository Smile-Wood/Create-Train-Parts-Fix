package com.tiestoettoet.create_train_parts;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepBlockEntity;
import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class AllBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateTrainParts.registrate();

    public static final BlockEntityEntry<TrainStepBlockEntity> TRAIN_STEP =
            REGISTRATE.blockEntity("train_step", TrainStepBlockEntity::new)
                    .renderer(() -> TrainStepRenderer::new)
                    .validBlocks(AllBlocks.TRAIN_STEP_ANDESITE)
                    .register();

    public static void register() {

    }
}