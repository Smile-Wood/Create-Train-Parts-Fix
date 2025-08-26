package com.tiestoettoet.create_train_parts.content.trains.crossing;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.tiestoettoet.create_train_parts.AllContraptionTypes;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.AllBlocks.MECHANICAL_PISTON_HEAD;
import static com.simibubi.create.AllBlocks.PISTON_EXTENSION_POLE;
import static com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock.*;
import static com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock.isPistonHead;
import static com.tiestoettoet.create_train_parts.content.trains.crossing.ArmExtenderBlock.isArm;
import static com.tiestoettoet.create_train_parts.content.trains.crossing.CrossingBlock.isCrossing;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class CrossingContraption extends Contraption {
    protected Direction facing;

    public CrossingContraption() {
    }

    public CrossingContraption(Direction direction) {
        facing = direction;
    }

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        anchor = pos;
//        System.out.println(collectArms( world, pos, facing) ? "Arms collected" : "No arms found");
//        System.out.println(blocks);
        if (!collectArms(world, pos, facing))
            return false;
        if (!searchMovedStructure(world, pos.relative(facing), null))
            return false;
        expandBoundsAroundAxis(facing.getClockWise().getAxis());
        if (blocks.isEmpty())
            return false;

        // Implement logic to collect all blocks (crossing + arms) into this contraption
        // Use BFS/DFS to find connected ArmExtenderBlocks
        // Add them with addBlock(world, pos, capture(world, pos));
        return true;
    }

    @Override
    public ContraptionType getType() {
        // Register and return your custom ContraptionType
        return AllContraptionTypes.CROSSING.value();
    }

    @Override
    protected boolean isAnchoringBlockAt(BlockPos pos) {
        return pos.equals(anchor.relative(facing.getOpposite()));
    }

    @Override
    public void addBlock(Level level, BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture) {
        super.addBlock(level, pos, capture);
    }

    @Override
    public CompoundTag writeNBT(HolderLookup.Provider registries, boolean spawnPacket) {
        CompoundTag tag = super.writeNBT(registries, spawnPacket);
        tag.putInt("Facing", facing.get3DDataValue());
        return tag;
    }

    @Override
    public void readNBT(Level world, CompoundTag tag, boolean spawnData) {
        facing = Direction.from3DDataValue(tag.getInt("Facing"));
        super.readNBT(world, tag, spawnData);
    }

    public Direction getFacing() {
        return facing;
    }

    private boolean collectArms(Level world, BlockPos pos, Direction direction) {
        if (bounds == null) {
            bounds = new AABB(BlockPos.ZERO);
        }
        List<StructureTemplate.StructureBlockInfo> arms = new ArrayList<>();
        BlockState state = world.getBlockState(pos);
        boolean flipped = state.getValue(CrossingBlock.FLIPPED);
        BlockPos currentPos = pos.relative(flipped ? direction.getCounterClockWise() : direction.getClockWise());
        BlockState currentState = world.getBlockState(currentPos);

        System.out.println(currentPos);

        if (!isArm(currentState)) {
            return false; // No arm found
        }

//        addBlock(world, pos, capture(world, pos));


        while (isArm(currentState)) {
            arms.add(new StructureTemplate.StructureBlockInfo(
                    currentPos.subtract(anchor), currentState, null
            ));
//            addBlock(world, currentPos, capture(world, currentPos));
            currentPos = flipped ? currentPos.relative(direction.getCounterClockWise())
                    : currentPos.relative(direction.getClockWise());
            currentState = world.getBlockState(currentPos);
            System.out.println(currentPos);
        }

        for (StructureTemplate.StructureBlockInfo arm : arms) {
            BlockPos relPos = arm.pos();
            BlockPos localPos = relPos.subtract(anchor);
            getBlocks().put(localPos, new StructureTemplate.StructureBlockInfo(localPos, arm.state(), null));
            //pistonExtensionCollisionBox = pistonExtensionCollisionBox.union(new AABB(localPos));
        }


        return true;

    }



    @Override
    public boolean canBeStabilized(Direction facing, BlockPos localPos) {
        return false;
    }


}
