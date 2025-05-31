package com.tiestoettoet.create_train_parts;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.content.logistics.box.PackageStyles.PackageStyle;

import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
public class AllPartialModels {

//    public static final PartialModel
//
//        STEPS = new
//
//
//    ;

//    public static final Map<ResourceLocation, Couple<PartialModel>> TRAIN_STEP = new HashMap<>();

    public static final Map<ResourceLocation, PartialModel> TRAIN_STEP = new HashMap<>();
    public static final Map<ResourceLocation, PartialModel> TRAIN_STEP_SLIDE = new HashMap<>();
    public static final Map<ResourceLocation, PartialModel> TRAIN_STEP_PIVOT = new HashMap<>();
    public static final Map<ResourceLocation, PartialModel> TRAIN_STEP_MOVE = new HashMap<>();
    public static final Map<ResourceLocation, PartialModel> TRAIN_STEP_FLAP= new HashMap<>();

    static {
        putTrainStep("train_step_train");
        putTrainStep("train_step_andesite");
        putTrainStep("train_step_brass");
        putTrainStep("train_step_copper");
    }

    private static void putTrainStep(String path) {
        for (Direction facing : Iterate.directions) {
            if (facing == Direction.UP || facing == Direction.DOWN)
                continue;
            for (Direction face : Iterate.directions) {
                for (TrainStepBlock.ConnectedState state : TrainStepBlock.ConnectedState.values()) {

                    TRAIN_STEP.put(CreateTrainParts.asResource(path + "/" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()), block(path + "/steps_" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()));
                    TRAIN_STEP_SLIDE.put(CreateTrainParts.asResource(path + "/" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()), block(path + "/slide_" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()));
                    System.out.println("Model path: " + path + "/slide");
                    TRAIN_STEP_PIVOT.put(CreateTrainParts.asResource(path + "/" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()), block(path + "/pivot_" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()));
                    TRAIN_STEP_MOVE.put(CreateTrainParts.asResource(path + "/" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()), block(path + "/move_" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()));
                    TRAIN_STEP_FLAP.put(CreateTrainParts.asResource(path + "/" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()), block(path + "/flap_" + facing.getSerializedName() + "_" + state.getSerializedName() + "_" + face.getSerializedName()));
                }
            }
        }

    }

    private static PartialModel block(String path) {
        return PartialModel.of(CreateTrainParts.asResource("block/" + path));
    }

    public static void init() {
        //
    }
}
