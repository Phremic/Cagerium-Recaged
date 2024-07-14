package net.phremic.cageriumrecaged.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.phremic.cageriumrecaged.CageriumRecaged;
import net.phremic.cageriumrecaged.blockentity.CageBlockEntity;
import net.phremic.cageriumrecaged.blockentity.PlateBlockEntity;
import net.phremic.cageriumrecaged.blockentity.TerrariumBlockEntity;
import com.mojang.datafixers.types.Type;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CageriumRecaged.MODID);


    public static final RegistryObject<BlockEntityType<TerrariumBlockEntity>> TERRARIUM_BLOCK_ENTITY = BLOCK_ENTITIES.register("terrarium_block_entity",
            () -> BlockEntityType.Builder.of(TerrariumBlockEntity::new, BlockInit.TERRARIUM_BLOCK.get()).build((Type<?>)null));

    public static final RegistryObject<BlockEntityType<CageBlockEntity>> CAGE_BLOCK_ENTITY = BLOCK_ENTITIES.register("cage_block_entity",
            () -> BlockEntityType.Builder.of(CageBlockEntity::new, BlockInit.CAGE_BLOCK.get()).build((Type<?>)null));

    public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE_BLOCK_ENTITY = BLOCK_ENTITIES.register("plate_block_entity",
            () -> BlockEntityType.Builder.of(PlateBlockEntity::new, BlockInit.PLATE_BLOCK.get()).build((Type<?>)null));
}