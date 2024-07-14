package net.phremic.cageriumrecaged.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.phremic.cageriumrecaged.init.BlockEntityInit;

public class PlateBlockEntity extends FarmBlockEntity {

    // Initialize block entity
    public PlateBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PLATE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
