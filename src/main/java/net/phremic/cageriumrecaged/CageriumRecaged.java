package net.phremic.cageriumrecaged;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.phremic.cageriumrecaged.config.CageriumRecagedCommonConfigs;
import net.phremic.cageriumrecaged.init.BlockEntityInit;
import net.phremic.cageriumrecaged.init.BlockInit;
import net.phremic.cageriumrecaged.init.CreativeTabInit;
import net.phremic.cageriumrecaged.init.ItemInit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CageriumRecaged.MODID)
public class CageriumRecaged {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cageriumrecaged";

    public CageriumRecaged()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register creative mode tabs
        CreativeTabInit.CREATIVE_MODE_TABS.register(modEventBus);

        // Register mod items
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);

        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

        // Register the common config file
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CageriumRecagedCommonConfigs.SPEC, "cageriumrecaged-common.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {}
    }
}
