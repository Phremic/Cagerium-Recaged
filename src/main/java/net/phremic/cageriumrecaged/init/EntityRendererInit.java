package net.phremic.cageriumrecaged.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.phremic.cageriumrecaged.CageriumRecaged;
import net.phremic.cageriumrecaged.entityrenderer.CageBlockEntityRenderer;
import net.phremic.cageriumrecaged.entityrenderer.PlateBlockEntityRenderer;
import net.phremic.cageriumrecaged.entityrenderer.TerrariumBlockEntityRenderer;

@Mod.EventBusSubscriber(modid = CageriumRecaged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EntityRendererInit {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.TERRARIUM_BLOCK_ENTITY.get(), TerrariumBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.CAGE_BLOCK_ENTITY.get(), CageBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.PLATE_BLOCK_ENTITY.get(), PlateBlockEntityRenderer::new);
    }
}
