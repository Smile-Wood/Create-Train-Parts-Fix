package com.tiestoettoet.create_train_parts.content.decoration.trainStep;

import net.minecraft.util.StringRepresentable;

public enum TrainStepType implements StringRepresentable {
    ANDESITE("andesite"),
    BRASS("brass"),
    COPPER("copper"),
    TRAIN("train");

    private final String name;

    private TrainStepType(String name) {
        this.name = name;
    }

    public String toString() {return this.name; }

    public String getSerializedName() {
        return this.name;
    }

}
