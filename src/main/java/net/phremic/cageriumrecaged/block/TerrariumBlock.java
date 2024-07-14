package net.phremic.cageriumrecaged.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.phremic.cageriumrecaged.blockentity.util.FarmEntityCategory;
import net.phremic.cageriumrecaged.init.BlockEntityInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TerrariumBlock extends FarmBlock {

    // Initialize block
    public TerrariumBlock(FarmEntityCategory pCategory, Properties pProperties) {
        super(pProperties, pCategory);
    }

    // Create a block entity corresponding to the block
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return BlockEntityInit.TERRARIUM_BLOCK_ENTITY.get().create(blockPos, blockState);
    }
}