package net.phremic.cageriumrecaged.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.phremic.cageriumrecaged.CageriumRecaged;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FilledSoulShardItem extends Item {

    public FilledSoulShardItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.cageriumrecaged.filled_soul_shard"));
        pTooltipComponents.add(Component.literal("Soul: " + getEntityName(pStack, pLevel)).withStyle(Style.EMPTY.withColor(11184810)));
    }

    public String getEntityName(ItemStack pStack, Level pLevel) {

        String default_name = "None";

        // Get the MODID tag from the soul shard
        CompoundTag mod_id_tag = pStack.getTagElement(CageriumRecaged.MODID);
        if (mod_id_tag == null) return default_name;

        // Get the entities ID tag from the MODID tag
        String mob_id = mod_id_tag.getString("EntityID");
        Optional<EntityType<?>> entity_type = EntityType.byString(mob_id);
        if (entity_type.isEmpty()) return default_name;

        // Create a new instance of the entity and return its name
        Entity entity = entity_type.get().create(pLevel);
        return entity != null ? entity.getName().getString() : "Unknown";
    }
}
