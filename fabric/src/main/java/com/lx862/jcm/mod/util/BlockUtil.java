package com.lx862.jcm.mod.util;

import org.mtr.mapping.holder.*;

import javax.annotation.Nullable;

/**
 * Contains utilities method for block checking/manipulation
 */
public class BlockUtil {
    public static boolean canSurvive(Block instance, WorldAccess world, BlockPos pos, Direction facing, int part, int totalWidthHeight) {
        boolean checkLeftOrBottom = part != 0;
        boolean checkRightOrTop = part != totalWidthHeight - 1;
        boolean canSurvive = true;

        if (checkLeftOrBottom) {
            BlockPos posLeftOrDown = pos.offset(facing.getOpposite());
            BlockState blockLeftOrDown = world.getBlockState(posLeftOrDown);
            if (!blockLeftOrDown.getBlock().equals(instance)) {
                return false;
            }
        }

        if (checkRightOrTop) {
            BlockPos posRightOrUp = pos.offset(facing);
            BlockState blockRightOrUp = world.getBlockState(posRightOrUp);
            if (!blockRightOrUp.getBlock().equals(instance)) {
                return false;
            }
        }
        return canSurvive;
    }

    public static boolean isReplacable(World world, BlockPos startPos, Direction direction, ItemPlacementContext ctx, int distance) {
        for (int i = 0; i < distance; i++) {
            BlockPos posUp = startPos.offset(direction, i);
            BlockState blockState = world.getBlockState(posUp);
            if (!blockState.canReplace(ctx)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get block entity in world, but will return null if chunk is not loaded (And will not attempt to load the chunk).
     */
    public static @Nullable BlockEntity getBlockEntityOrNull(World world, BlockPos pos) {
        if(!world.isChunkLoaded(getChunkCoords(pos.getX()), getChunkCoords(pos.getZ()))) return null;
        return world.getBlockEntity(pos);
    }

    /**
     * Get block state in world, but will return null if chunk is not loaded (And will not attempt to load the chunk).
     */
    public static @Nullable BlockState getBlockState(World world, BlockPos pos) {
        if(!world.isChunkLoaded(getChunkCoords(pos.getX()), getChunkCoords(pos.getZ()))) return null;
        return world.getBlockState(pos);
    }

    private static int getChunkCoords(int pos) {
        return pos >> 4;
    }
}
