package com.infinityraider.ninjagear.api.v1;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Implement this interface in block classes which can have a rope attached to it.
 * Ropes can only attach to the bottom of a block, by default ropes are attachable to all blocks which have a solid bottom face.
 * However some blocks could be an exception to this, for example rope itself.
 */
public interface IRopeAttachable {
    /**
     * Checks if a rope can be attached to this block, ropes are always attacked below (BlockPos.down() )
     * @param world the world object
     * @param pos position of this block in the world
     * @param state block state for this block in the world
     * @return true if a rope can be attached to this block
     */
    boolean canAttachRope(LevelReader world, BlockPos pos, BlockState state);

    /**
     * This method is called when a rope is actually attached to the block,
     * can be used to change the block state if a rope is attached
     * @param world the world object
     * @param pos position of this block in the world
     * @param state block state for this block in the world
     */
    void onRopeAttached(Level world, BlockPos pos, BlockState state);
}
