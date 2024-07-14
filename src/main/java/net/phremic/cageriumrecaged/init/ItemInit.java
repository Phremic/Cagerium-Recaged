package net.phremic.cageriumrecaged.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.phremic.cageriumrecaged.CageriumRecaged;
import net.phremic.cageriumrecaged.item.CageItem;
import net.phremic.cageriumrecaged.item.EmptySoulShardItem;
import net.phremic.cageriumrecaged.item.FilledSoulShardItem;
import net.phremic.cageriumrecaged.item.PlateItem;
import net.phremic.cageriumrecaged.item.TerrariumItem;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CageriumRecaged.MODID);

    // Register items and set their properties
    public static final RegistryObject<Item> EMPTY_SOUL_SHARD = ITEMS.register("empty_soul_shard", () -> new EmptySoulShardItem(new Item.Properties()));
    public static final RegistryObject<Item> FILLED_SOUL_SHARD = ITEMS.register("filled_soul_shard", () -> new FilledSoulShardItem(new Item.Properties()));
    public static final RegistryObject<Item> TERRARIUM_ITEM = ITEMS.register("terrarium", () -> new TerrariumItem(BlockInit.TERRARIUM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> CAGE_ITEM = ITEMS.register("cage", () -> new CageItem(BlockInit.CAGE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> PLATE_ITEM = ITEMS.register("plate", () -> new PlateItem(BlockInit.PLATE_BLOCK.get(), new Item.Properties()));
}
