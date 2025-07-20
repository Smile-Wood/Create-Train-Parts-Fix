package com.tiestoettoet.create_train_parts.infrastructure.ponder.scenes;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tiestoettoet.create_train_parts.content.decoration.slidingWindow.SlidingWindowBlockEntity;
import com.tiestoettoet.create_train_parts.content.decoration.trainStep.TrainStepBlockEntity;
import com.tiestoettoet.create_train_parts.content.foundation.gui.AllIcons;
import com.tiestoettoet.create_train_parts.foundation.ponder.CreateTrainPartsSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class SlidingWindowScenes {
    public static void modes(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        CreateTrainPartsSceneBuilder createScene = new CreateTrainPartsSceneBuilder(scene);
        scene.title("window_modes", "Configuring Modes");
        scene.configureBasePlate(1, 0, 5);
        scene.setSceneOffsetY(-1);
        scene.showBasePlate();

        Selection window = util.select().position(3, 2, 2);
        for (int i = 4; i >= 2; i--) {
            scene.world().showSection(util.select().position(i, 1, 2), Direction.DOWN);
            scene.idle(1);
        }

        scene.idle(9);

        ElementLink<WorldSectionElement> windowElement = scene.world().showIndependentSection(window, Direction.DOWN);

        scene.idle(10);

        Vec3 target = util.vector().centerOf(2, 3, 2);

        scene.overlay().showText(50)
                .pointAt(target)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Sliding Windows normally open upwards");

        scene.idle(60);

        createScene.world().animateSlidingWindow(util.grid().at(3, 2, 2), true);

        scene.idle(10);

        scene.overlay().showText(50)
                .pointAt(target)
                .placeNearTarget()
                .attachKeyFrame()
                .text("But you can configure it to open another Direction");

        scene.idle(60);

        createScene.world().animateSlidingWindow(util.grid().at(3, 2, 2), false);

        scene.idle(10);

        scene.overlay().showControls(util.vector().centerOf(3, 3, 2), Pointing.DOWN, 75)
                .rightClick()
                .withItem(AllItems.WRENCH.asStack());
        scene.overlay().showControls(util.vector().centerOf(4, 2, 2), Pointing.RIGHT, 30).showing(AllIcons.I_SLIDING_WINDOW_UP);
        scene.world().modifyBlockEntity(util.grid().at(3, 2, 2), SlidingWindowBlockEntity.class, be -> be.setMode(SlidingWindowBlockEntity.SelectionMode.RIGHT));

        scene.idle(35);

        scene.overlay().showControls(util.vector().centerOf(4, 2, 2), Pointing.RIGHT, 40).showing(AllIcons.I_SLIDING_WINDOW_RIGHT);

        scene.idle(50);

        createScene.world().animateSlidingWindow(util.grid().at(3, 2, 2), true);

        scene.idle(20);

        scene.world().showSection(util.select().position(3, 3, 2), Direction.DOWN);

        scene.idle(20);

        scene.overlay().showText(50)
                .pointAt(util.vector().centerOf(3, 3, 2))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Connected Windows with the same Mode will open together");

        scene.idle(60);

        createScene.world().animateSlidingWindow(util.grid().at(3, 2, 2), false);

        scene.idle(10);


        scene.world().modifyBlockEntity(util.grid().at(3, 3, 2), SlidingWindowBlockEntity.class, be -> be.setMode(SlidingWindowBlockEntity.SelectionMode.RIGHT));
        createScene.world().hideIndependentSectionImmediately(windowElement);
        window = util.select().fromTo(3, 2, 2, 3, 3, 2);
        windowElement = scene.world().showIndependentSectionImmediately(window);

        scene.idle(10);

        createScene.world().animateSlidingWindow(util.grid().at(3, 2, 2), true);
        createScene.world().animateSlidingWindow(util.grid().at(3, 3, 2), true);

        scene.idle(50);

        scene.world().showIndependentSection(util.select().fromTo(4, 2, 2, 4, 3, 2), Direction.DOWN);
        createScene.world().animateSlidingWindow(util.grid().at(3, 2, 2), false);
        createScene.world().animateSlidingWindow(util.grid().at(3, 3, 2), false);

        scene.idle(14);

        createScene.world().hideIndependentSectionImmediately(windowElement);
        window = util.select().fromTo(3, 2, 2, 4, 3, 2);
        windowElement = scene.world().showIndependentSectionImmediately(window);
        scene.overlay().showControls(util.vector().centerOf(4, 4, 2), Pointing.DOWN, 75)
                .rightClick()
                .withItem(AllItems.WRENCH.asStack());
        scene.overlay().showControls(util.vector().centerOf(5, 3, 2), Pointing.RIGHT, 30).showing(AllIcons.I_SLIDING_WINDOW_RIGHT);

        scene.overlay().showText(50)
                .pointAt(util.vector().centerOf(2, 3, 2))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Holding Control changes all Windows");

        scene.idle(35);

        scene.world().modifyBlockEntity(util.grid().at(3, 2, 2), SlidingWindowBlockEntity.class, be -> be.setMode(SlidingWindowBlockEntity.SelectionMode.LEFT));
        scene.world().modifyBlockEntity(util.grid().at(3, 3, 2), SlidingWindowBlockEntity.class, be -> be.setMode(SlidingWindowBlockEntity.SelectionMode.LEFT));
        scene.world().modifyBlockEntity(util.grid().at(4, 2, 2), SlidingWindowBlockEntity.class, be -> be.setMode(SlidingWindowBlockEntity.SelectionMode.LEFT));
        scene.world().modifyBlockEntity(util.grid().at(4, 3, 2), SlidingWindowBlockEntity.class, be -> be.setMode(SlidingWindowBlockEntity.SelectionMode.LEFT));
        scene.overlay().showControls(util.vector().centerOf(5, 3, 2), Pointing.RIGHT, 40).showing(AllIcons.I_SLIDING_WINDOW_LEFT);

        scene.idle(50);

        createScene.world().animateSlidingWindow(util.grid().at(3, 2, 2), true);
        createScene.world().animateSlidingWindow(util.grid().at(3, 3, 2), true);
        createScene.world().animateSlidingWindow(util.grid().at(4, 2, 2), true);
        createScene.world().animateSlidingWindow(util.grid().at(4, 3, 2), true);

        // TODO: Show that ctrl changes them all
    }
}
