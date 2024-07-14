package net.phremic.cageriumrecaged.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.phremic.cageriumrecaged.init.BlockEntityInit;

public class TerrariumBlockEntity extends FarmBlockEntity {

    // Initialize block entity
    public TerrariumBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.TERRARIUM_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}