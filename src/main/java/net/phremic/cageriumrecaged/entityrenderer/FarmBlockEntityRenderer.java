package net.phremic.cageriumrecaged.entityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.phremic.cageriumrecaged.block.FarmBlock;
import net.phremic.cageriumrecaged.blockentity.FarmBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FarmBlockEntityRenderer implements BlockEntityRenderer<FarmBlockEntity> {

    private Entity entity;
    private float entity_offset;
    private float entity_scale;
    private float entity_rotation;

    private final BlockEntityRendererProvider.Context context;

    public FarmBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.context = pContext;
    }

    @Override
    public void render(@NotNull FarmBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

    }

    public void renderEntity(@NotNull FarmBlockEntity pBlockEntity, float pY, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        // Check if the level is loaded
        Level level = pBlockEntity.getLevel();
        if (level == null) return;

        // Check if there is an item in the block's inventory
        ItemStack stack = pBlockEntity.getStack();
        if (stack.isEmpty()) return;

        Entity entity = pBlockEntity.getEntity();
        if (entity != null) {
            renderLivingEntity(pBlockEntity, entity, pY, pPoseStack, pBuffer, pPackedLight);
        } else {
            renderItemEntity(pBlockEntity, stack, level, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }
}

    private void renderLivingEntity(@NotNull FarmBlockEntity pBlockEntity, Entity pEntity, float pY, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {

        if (!Objects.equals(this.entity, pEntity)) updateEntityData(pBlockEntity, pEntity);

        // Set position, scale, and rotation
        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, pY + this.entity_offset, 0.5f);
        pPoseStack.scale(this.entity_scale, this.entity_scale, this.entity_scale);
        pPoseStack.mulPose(Axis.YN.rotationDegrees(this.entity_rotation));

        // Render the entity
        this.context.getEntityRenderer().render(this.entity, 0.0, 0.0, 0.0, 0.0f, 0.0f, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.popPose();
    }

    private void renderItemEntity(@NotNull FarmBlockEntity pBlockEntity, ItemStack pStack, Level pLevel, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        // Set position and scale
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 0.5, 0.5);
        pPoseStack.scale(0.4f, 0.4f, 0.4f);
        pPoseStack.mulPose(Axis.YN.rotationDegrees(pBlockEntity.getBlockState().getValue(FarmBlock.FACING).toYRot()));

        // Render the item
        this.context.getItemRenderer().renderStatic(pStack, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, pLevel, 0);
        pPoseStack.popPose();
    }

    private void updateEntityData(FarmBlockEntity pBlockEntity, Entity pEntity) {

        if (pEntity instanceof ZombieVillager zombie_villager) {
            zombie_villager.setVillagerData(zombie_villager.getVillagerData().setProfession(VillagerProfession.NONE));
        }

        float offset = 0.0f;
        float scale = Math.min(0.85f / (pEntity.getBbWidth() + pEntity.getBbHeight()), 0.4f);
        float rotation = pBlockEntity.getBlockState().getValue(FarmBlock.FACING).toYRot();

        if (pEntity instanceof EnderDragon) offset = 2.5f / 16.0f;
        if (pEntity instanceof Ghast) offset = 4.5f / 16.0f;
        if (pEntity instanceof Squid) offset = 5.5f / 16.0f;

        if (pEntity instanceof Bat) scale = 0.7f;
        if (pEntity instanceof EnderDragon) scale = 0.07f;
        if (pEntity instanceof HoglinBase) scale = 0.23f;
        if (pEntity instanceof IronGolem) scale = 0.26f;
        if (pEntity instanceof Rabbit) scale = 0.6f;
        if (pEntity instanceof Turtle) scale = 0.3f;

        if (pEntity instanceof EnderDragon) rotation += 180.0f;

        this.entity = pEntity;
        this.entity_offset = offset;
        this.entity_scale = scale;
        this.entity_rotation = rotation;
    }
}