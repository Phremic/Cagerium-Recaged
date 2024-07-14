package net.phremic.cageriumrecaged.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.phremic.cageriumrecaged.blockentity.util.FarmEntityCategory;
import net.phremic.cageriumrecaged.init.BlockEntityInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlateBlock extends FarmBlock {

    // Initialize block
    public PlateBlock(FarmEntityCategory pCategory, Properties pProperties) {
        super(pProperties, pCategory);
    }

    // Create a block entity corresponding to the block
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return BlockEntityInit.PLATE_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    // Set block hitbox shape
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Block.box(0,0,0,16,5,16);
    }
}