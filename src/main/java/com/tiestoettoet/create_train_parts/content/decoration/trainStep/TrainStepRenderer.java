package com.tiestoettoet.create_train_parts.content.decoration.trainStep;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.tiestoettoet.create_train_parts.AllPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.createmod.catnip.render.CachedBuffers;


public class TrainStepRenderer extends SafeBlockEntityRenderer<TrainStepBlockEntity> {

    public TrainStepRenderer(Context context) {}

    @Override
    protected void renderSafe(TrainStepBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!be.shouldRenderSpecial(blockState))
            return;

        Direction facing = blockState.getValue(TrainStepBlock.FACING);
        Direction movementDirection = facing.getClockWise();

        float value = be.animation.getValue(partialTicks);
        float value2 = Mth.clamp(value * 10, 0, 1);

        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        Vec3 offset = Vec3.atLowerCornerOf(movementDirection.getNormal())
                .scale(value * value * 13 / 16f)
                .add(Vec3.atLowerCornerOf(facing.getNormal())
                        .scale(value2 * 1 / 32f));

        Couple<PartialModel> partials = AllPartialModels.TRAIN_STEP.get(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()));
        SuperByteBuffer partial = CachedBuffers.partial(partials.get(false), blockState);

        float f = 1;

        partial.translate(0, -1 / 512f, 0)
                .translate(Vec3.atLowerCornerOf(facing.getNormal())
                        .scale(value2 * 1 / 32f));
        partial.rotateCentered(Mth.DEG_TO_RAD * AngleHelper.horizontalAngle(facing.getClockWise()), Direction.UP);

        partial.rotateYDegrees(91 * f * value * value);

        partial.light(light)
                .renderInto(ms, vb);






        // Add your rendering logic here
        // For example, you can use CachedBufferer to create a buffer for the TrainStepBlock
        // and render it using the provided PoseStack and MultiBufferSource.
    }
}
