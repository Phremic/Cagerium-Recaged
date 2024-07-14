package net.phremic.cageriumrecaged.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.phremic.cageriumrecaged.blockentity.FarmBlockEntity;
import net.phremic.cageriumrecaged.blockentity.util.FarmEntityCategory;
import net.phremic.cageriumrecaged.blockentity.util.TickableBlockEntity;
import net.phremic.cageriumrecaged.init.ItemInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FarmBlock extends HorizontalDirectionalBlock implements EntityBlock {

    private final FarmEntityCategory category;

    // Initialize block
    public FarmBlock(Properties pProperties, FarmEntityCategory category) {
        super(pProperties);
        this.category = category;
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    // Implement block states and add facing state
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    // Set block rotation when placed
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    // -------------------------------- BLOCK ENTITY --------------------------------

    // Register block entity (Handled in child classes)
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return null;
    }

    // Implement block ticker for the block entity
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTickerHelper(pLevel);
    }

    // -------------------------------- BLOCK INTERACTION --------------------------------

    // When the block is destroyed
    @Override
    public void onRemove(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (!pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof FarmBlockEntity blockEntity) {
                // Drop inventory contents when the block is destroyed
                var item_entity = new ItemEntity(pLevel, pPos.getX() + 0.5D, pPos.getY() + 0.5D, pPos.getZ() + 0.5D, blockEntity.getStack());
                pLevel.addFreshEntity(item_entity);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    // When a player interacts with the block
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {

        // Check that the interaction is not on the client side
        if (!pLevel.isClientSide()) {

            // Check that the player interacted with their main hand
            if (pHand == InteractionHand.MAIN_HAND) {

                // Check that the interacted entity is the correct type
                if (pLevel.getBlockEntity(pPos) instanceof FarmBlockEntity blockEntity) {

                    // Check if the block contains an item
                    if (blockEntity.getStack().isEmpty()) {
                        // Attempt to insert an item into the block
                        if (insertItem(pPlayer, pHand, blockEntity)) return InteractionResult.SUCCESS;
                    } else {
                        // Attempt to remove the item from the block
                        if (removeItem(pPlayer, pHand, blockEntity)) return InteractionResult.SUCCESS;
                    }
                }
            }
        }

        return InteractionResult.FAIL;
    }

    private boolean removeItem(Player pPlayer, InteractionHand pHand, FarmBlockEntity pBlockEntity) {

        // Check that the player's hand is empty
        if (pPlayer.getItemInHand(pHand).isEmpty()) {

            // Remove the soul shard from the block
            ItemStack extracted_item = pBlockEntity.removeSoulShard();

            // Add the soul shard to the player's hand
            pPlayer.setItemInHand(pHand, extracted_item);

            return true;
        }

        return false;
    }

    private boolean insertItem(Player pPlayer, InteractionHand pHand, FarmBlockEntity pBlockEntity) {

        // Check that the player is holding a soul shard
        if (pPlayer.getItemInHand(pHand).getItem() == ItemInit.FILLED_SOUL_SHARD.get()) {

            // Check that the farm accepts the entity in the soul shard
            Entity entity = pBlockEntity.getEntityFromStack(pPlayer.getItemInHand(pHand));
            if (this.category.acceptsEntity(entity)) {

                // Get the player's held item and a copy of it
                ItemStack player_item_stack = pPlayer.getItemInHand(pHand);

                // Create a single copy of the player's held item
                ItemStack insert_item_stack = player_item_stack.copy();
                insert_item_stack.setCount(1);

                // Attempt to put the copied item into the block entity inventory
                pBlockEntity.addSoulShard(insert_item_stack);

                // Remove the copied item from player's item stack
                player_item_stack.setCount(player_item_stack.getCount() - 1);
                pPlayer.setItemInHand(pHand, player_item_stack);
                return true;
            }
        }

        return false;
    }
}
