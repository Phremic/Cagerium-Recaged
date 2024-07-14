package net.phremic.cageriumrecaged.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.phremic.cageriumrecaged.CageriumRecaged;
import net.phremic.cageriumrecaged.block.CageBlock;
import net.phremic.cageriumrecaged.block.PlateBlock;
import net.phremic.cageriumrecaged.block.TerrariumBlock;
import net.phremic.cageriumrecaged.blockentity.util.FarmEntityCategory;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CageriumRecaged.MODID);

    // Register blocks and set their properties
    public static final RegistryObject<Block> TERRARIUM_BLOCK = BLOCKS.register("terrarium", () -> new TerrariumBlock(
            FarmEntityCategory.PASSIVE,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.5F, 3.0F)
                    .sound(SoundType.WOOD)
                    .isRedstoneConductor(BlockInit::never)
                    .noOcclusion()));
    public static final RegistryObject<Block> CAGE_BLOCK = BLOCKS.register("cage", () -> new CageBlock(
            FarmEntityCategory.HOSTILE,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(0.5F, 3.0F)
                    .sound(SoundType.METAL)
                    .isRedstoneConductor(BlockInit::never)
                    .noOcclusion()));
    public static final RegistryObject<Block> PLATE_BLOCK = BLOCKS.register("plate", () -> new PlateBlock(
            FarmEntityCategory.BOSS,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.5F, 3.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .isRedstoneConductor(BlockInit::never)
                    .noOcclusion()));

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
}
