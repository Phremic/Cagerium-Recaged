package net.phremic.cageriumrecaged.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.phremic.cageriumrecaged.CageriumRecaged;

public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CageriumRecaged.MODID);

    public static final RegistryObject<CreativeModeTab> CAGERIUMRECAGED_TAB = CREATIVE_MODE_TABS.register("cageriumrecaged_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.cageriumrecaged_tab"))
            .icon(() -> new ItemStack(BlockInit.CAGE_BLOCK.get()))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ItemInit.EMPTY_SOUL_SHARD.get());
                output.accept(BlockInit.TERRARIUM_BLOCK.get());
                output.accept(BlockInit.CAGE_BLOCK.get());
                output.accept(BlockInit.PLATE_BLOCK.get());
            })
            .build());
}