package com.tiestoettoet.create_train_parts;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepBlock;
import com.tiestoettoet.create_train_parts.content.foundation.data.BuilderTransformers;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import com.simibubi.create.AllCreativeModeTabs;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class AllBlocks {
    private static final CreateRegistrate REGISTRATE = CreateTrainParts.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }
    public static final BlockEntry<TrainStepBlock> TRAIN_STEP_ANDESITE = REGISTRATE.block("train_step_andesite", TrainStepBlock::new)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .transform(BuilderTransformers.trainStep("andesite", () -> AllSpriteShifts.TRAIN_STEP_ANDESITE))
            .register();
    public static final BlockEntry<TrainStepBlock> TRAIN_STEP_BRASS = REGISTRATE.block("train_step_brass", TrainStepBlock::new)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .transform(BuilderTransformers.trainStep("brass", () -> AllSpriteShifts.TRAIN_STEP_BRASS))
            .register();
    public static final BlockEntry<TrainStepBlock> TRAIN_STEP_COPPER = REGISTRATE.block("train_step_copper", TrainStepBlock::new)
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .transform(BuilderTransformers.trainStep("copper", () -> AllSpriteShifts.TRAIN_STEP_COPPER))
            .register();

    public static final BlockEntry<TrainStepBlock> TRAIN_STEP_TRAIN = REGISTRATE.block("train_step_train", TrainStepBlock::new)
            .properties(p -> p.mapColor(MapColor.PODZOL)
                    .sound(SoundType.NETHERITE_BLOCK))
            .transform(BuilderTransformers.trainStep("train",
//                    () -> AllSpriteShifts.TRAIN_STEP_SIDE,
                    () -> com.simibubi.create.AllSpriteShifts.RAILWAY_CASING))
            .register();
//
//    public static final BlockEntry<TrainStepBlock> TRAIN_STEP_BRASS = REGISTRATE.block("train_step_brass", TrainStepBlock::new)
//            .transform(BuilderTransformers.trainStep("brass", () -> AllSpriteShifts.TRAIN_STEP_BRASS))
//            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN))
//            .lang("Brass Step")
//            .register();
//
//    public static final BlockEntry<TrainStepBlock> TRAIN_STEP_COPPER = REGISTRATE.block("train_step_copper", TrainStepBlock::new)
//            .transform(BuilderTransformers.trainStep("copper", () -> AllSpriteShifts.TRAIN_STEP_COPPER))
//            .properties(p -> p.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
//            .lang("Copper Step")
//            .register();

//    public static final BlockEntry<TrainStepBlock> TRAIN_STEP_TRAIN = REGISTRATE.block("train_step_train", TrainStepBlock::new)
//            .transform(BuilderTransformers.trainStep("train", () -> AllSpriteShifts.TRAIN_STEP_SIDE,
//                    () -> AllSpriteShifts.TRAIN_STEP_TRAIN))
//            .properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN)
//                    .sound(SoundType.NETHERITE_BLOCK))
//            .lang("Train Step")
//            .register();

    public static void register() {
    }
}
