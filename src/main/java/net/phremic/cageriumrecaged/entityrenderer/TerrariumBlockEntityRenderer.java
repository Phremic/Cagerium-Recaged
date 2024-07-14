package net.phremic.cageriumrecaged.entityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.phremic.cageriumrecaged.blockentity.FarmBlockEntity;
import org.jetbrains.annotations.NotNull;

public class TerrariumBlockEntityRenderer extends FarmBlockEntityRenderer {

    public TerrariumBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(@NotNull FarmBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        renderEntity(pBlockEntity, 0.1875f, pPartialTick, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }
}
