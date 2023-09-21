package com.lx862.jcm.blocks.base;

import com.lx862.jcm.blocks.behavior.HorizontalMultiBlock;
import com.lx862.jcm.data.BlockProperties;
import com.lx862.jcm.util.BlockUtil;
import net.minecraft.world.WorldView;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import java.util.List;

public abstract class VerticallyAttachedDirectional2Block extends VerticallyAttachedDirectionalBlock implements HorizontalMultiBlock {
    public static final int width = 2;
    public static final IntegerProperty PART = BlockProperties.HORIZONTAL_PART;

    public VerticallyAttachedDirectional2Block(BlockSettings settings, boolean canAttachTop, boolean canAttachBottom) {
        super(settings, canAttachTop, canAttachBottom);
    }

    @Override
    public boolean canPlaceAt2(BlockState state, WorldView world, BlockPos pos) {
        boolean canPlaceMultiBlock = HorizontalMultiBlock.canBePlaced(state, world, pos, width);
        boolean canBeAttached = super.canPlaceAt2(state, world, pos) && super.canPlaceAt2(state, world, pos.offset(BlockUtil.getProperty(state, FACING).rotateYClockwise()));
        return canPlaceMultiBlock && canBeAttached;
    }

    public void onPlaced2(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        HorizontalMultiBlock.placeBlock(world, pos, state, new Property<>(PART.data), Direction.convert(state.get(new Property<>(FACING.data)).rotateYClockwise()), width);
    }

    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!HorizontalMultiBlock.blockNotValid(pos, state, world, new Property<>(PART.data), width)) {
            return Blocks.getAirMapped().getDefaultState();
        }

        return super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        super.addBlockProperties(properties);
        properties.add(PART);
    }
}