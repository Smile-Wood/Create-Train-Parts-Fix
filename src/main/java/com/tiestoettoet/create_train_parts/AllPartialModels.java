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

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
public class AllPartialModels {

//    public static final PartialModel;

    public static final Map<ResourceLocation, Couple<PartialModel>> TRAIN_STEP = new HashMap<>();

    static {
//        putTrainStep("train_step_train");
        putTrainStep("train_step_andesite");
//        putTrainStep("train_step_brass");
//        putTrainStep("train_step_copper");
    }

    private static void putTrainStep(String path) {
        TRAIN_STEP.put(CreateTrainParts.asResource(path),
                Couple.create(block(path + "/steps_closed"), block(path + "/steps_open")));
    }

    private static PartialModel block(String path) {
        return PartialModel.of(CreateTrainParts.asResource("block/" + path));
    }

    public static void init() {
        //
    }
}
