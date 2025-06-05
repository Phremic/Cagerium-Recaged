package net.phremic.cageriumrecaged.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.phremic.cageriumrecaged.CageriumRecaged;
import net.phremic.cageriumrecaged.config.CageriumRecagedCommonConfigs;
import net.phremic.cageriumrecaged.init.ItemInit;

import java.util.Objects;


public class ModEvents {

    @Mod.EventBusSubscriber(modid = CageriumRecaged.MODID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void harvestSoul(LivingDeathEvent event) {
            if (event.getSource().getEntity() instanceof Player player) {

                // Get the slain entity's ID
                String entity_id = Objects.requireNonNull(event.getEntity().getEncodeId());

                // Return if the config settings don't allow the entity to be captured
                if (CageriumRecagedCommonConfigs.ONLY_ACCEPT_VANILLA.get()) {
                    if (!entity_id.startsWith("minecraft:")) {
                        return;
                    }
                }

                // Get the player's inventory
                Inventory inventory = player.getInventory();

                // Create a generic Empty Soul Shard to compare with player's inventory
                ItemStack empty_soul_shard = ItemInit.EMPTY_SOUL_SHARD.get().getDefaultInstance();

                // Check if the player has an Empty Soul Shard
                if (inventory.contains(empty_soul_shard)) {

                    // Create a new Filled Soul Shard item
                    ItemStack filled_soul_shard = ItemInit.FILLED_SOUL_SHARD.get().getDefaultInstance();

                    // Save the entity as a tag in the Filled Soul Shard
                    // Soul Shard -> MODID -> "EntityID" -> Entity ID
                    CompoundTag tag = new CompoundTag();
                    tag.putString("EntityID", entity_id);
                    filled_soul_shard.addTagElement(CageriumRecaged.MODID, tag);

                    // Get the stack of Empty Soul Shards from the player's inventory
                    int soul_shard_slot = inventory.findSlotMatchingItem(empty_soul_shard);
                    ItemStack held_empty_soul_shard = inventory.getItem(soul_shard_slot);

                    // Take one Empty Soul Shard from the players inventory and give the player the Filled Soul Shard
                    held_empty_soul_shard.setCount(held_empty_soul_shard.getCount() - 1);
                    inventory.placeItemBackInInventory(filled_soul_shard);
                }
            }
        }
    }
}
