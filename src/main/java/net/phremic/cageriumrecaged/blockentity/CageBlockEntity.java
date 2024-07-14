package net.phremic.cageriumrecaged.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.phremic.cageriumrecaged.init.BlockEntityInit;

public class CageBlockEntity extends FarmBlockEntity {

    // Initialize block entity
    public CageBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.CAGE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
