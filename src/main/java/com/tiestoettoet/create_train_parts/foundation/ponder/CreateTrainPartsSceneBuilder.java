package com.tiestoettoet.create_train_parts.foundation.ponder;

import com.tiestoettoet.create_train_parts.content.decoration.slidingWindow.SlidingWindowBlockEntity;
import com.tiestoettoet.create_train_parts.content.decoration.trainSlide.TrainSlideBlockEntity;
import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepBlock;
import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepBlockEntity;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.createmod.ponder.foundation.element.ElementLinkImpl;
import net.createmod.ponder.foundation.element.WorldSectionElementImpl;
import net.createmod.ponder.foundation.instruction.DisplayWorldSectionInstruction;
import net.createmod.ponder.foundation.instruction.FadeOutOfSceneInstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class CreateTrainPartsSceneBuilder extends PonderSceneBuilder {

    private final EffectInstructions effects;
    private final WorldInstructions world;
    private final SpecialInstructions special;

    public CreateTrainPartsSceneBuilder(SceneBuilder baseSceneBuilder) {
        this(baseSceneBuilder.getScene());
    }

    private CreateTrainPartsSceneBuilder(PonderScene ponderScene) {
        super(ponderScene);
        effects = new EffectInstructions();
        world = new WorldInstructions();
        special = new SpecialInstructions();
    }

    public EffectInstructions effects() {
        return effects;
    }

    public WorldInstructions world() {
        return world;
    }

    public SpecialInstructions special() {
        return special;
    }

    public class EffectInstructions extends PonderEffectInstructions {

    }

    public class WorldInstructions extends PonderWorldInstructions {

        public void showSectionImmediately(Selection selection) {
            addInstruction(
                    new DisplayWorldSectionInstruction(0, Direction.DOWN, selection, scene::getBaseWorldSection));
        }

        public void hideSectionImmediately(Selection selection) {
            addInstruction(scene -> {
                scene.getBaseWorldSection()
                        .erase(selection);
            });
        }

        public void hideIndependentSectionImmediately(ElementLink<WorldSectionElement> link) {
            addInstruction(new FadeOutOfSceneInstruction<>(0, Direction.NORTH, link));
        }

        public void showSectionAndMergeImmediately(Selection selection,
                ElementLink<WorldSectionElement> link) {
            addInstruction(new DisplayWorldSectionInstruction(0, Direction.DOWN, selection, () -> scene.resolve(link)));
        }

        public void animateTrainStep(BlockPos position, boolean open) {
            modifyBlockEntityNBT(getScene().getSceneBuildingUtil().select().position(position), TrainStepBlockEntity.class,
                    nbt -> nbt.putBoolean("ForceOpen", open));
        }

        public void animateTrainSlide(BlockPos position, boolean open) {
            modifyBlockEntityNBT(getScene().getSceneBuildingUtil().select().position(position), TrainSlideBlockEntity.class,
                    nbt -> nbt.putBoolean("ForceOpen", open));
        }

        public void animateSlidingWindow(BlockPos position, boolean open) {
            modifyBlockEntityNBT(getScene().getSceneBuildingUtil().select().position(position), SlidingWindowBlockEntity.class,
                    nbt -> nbt.putBoolean("ForceOpen", open));
        }
    }

    public class SpecialInstructions extends PonderSpecialInstructions {

    }

}
