package net.phremic.cageriumrecaged.blockentity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.phremic.cageriumrecaged.CageriumRecaged;
import net.phremic.cageriumrecaged.blockentity.util.TickableBlockEntity;
import net.phremic.cageriumrecaged.config.CageriumRecagedCommonConfigs;
import net.phremic.cageriumrecaged.init.ItemInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class FarmBlockEntity extends BlockEntity implements TickableBlockEntity {

    private Entity entity;
    private int tickCount;
    private static final GameProfile DUMMY_PROFILE = new GameProfile(UUID.fromString("5b580441-d54a-38f1-1120-e3842a80632b"), "[cageriumrecaged]");

    private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);
    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            FarmBlockEntity.this.setChanged();
            if (level != null) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };


    // Initialize block entity
    public FarmBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    // -------------------------------- OVERRIDDEN FUNCTIONS --------------------------------

    // Load entity data
    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        // Get the compound tag for this mod
        CompoundTag cageriumRecagedModData = pTag.getCompound(CageriumRecaged.MODID);
        // Load the corresponding tag data into the respective variables
        this.inventory.deserializeNBT(cageriumRecagedModData.getCompound("Inventory"));
        this.tickCount = cageriumRecagedModData.getInt("TickCount");
        // Update entity data (Required for entity renderer)
        this.entity = getEntityFromStack(getStack());
    }

    // Save entity data
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        // Create a new compound tag to hold the mod data
        var cageriumRecagedModData = new CompoundTag();
        // Store the corresponding variable data in the respective tag data
        cageriumRecagedModData.put("Inventory", this.inventory.serializeNBT());
        cageriumRecagedModData.putInt("TickCount", this.tickCount);
        // Save the tag data in the compound tag for this mod
        pTag.put(CageriumRecaged.MODID, cageriumRecagedModData);
        // Update entity data (Required for loot generation)
        this.entity = getEntityFromStack(getStack());
    }

    // Update client NBT data on block update
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    // Update client NBT data on chunk update
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // Initialize capabilities
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.optional.cast() : super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
    }

    @Override
    public void tick() {
        if (this.entity != null) {
            this.tickCount++;
            if (this.tickCount > CageriumRecagedCommonConfigs.GENERATE_LOOT_PERIOD.get()) {
                this.tickCount = 0;
                for (int i = 0; i < CageriumRecagedCommonConfigs.GENERATE_LOOT_INSTANCES.get(); i++) {
                    this.generateLoot();
                }
            }
        }
    }

    // -------------------------------- PUBLIC FUNCTIONS --------------------------------

    public ItemStack getStack() { return this.inventory.getStackInSlot(0); }

    public Entity getEntity() { return this.entity; }

    public Entity getEntityFromStack(ItemStack pStack) {

        if (this.level == null) return null;
        if (pStack == null) return null;
        CompoundTag mod_id_tag = pStack.getTagElement(CageriumRecaged.MODID);
        if (mod_id_tag == null) return null;
        String entity_id = mod_id_tag.getString("EntityID");
        Optional<EntityType<?>> optional_entity_type = EntityType.byString(entity_id);
        return optional_entity_type.<Entity>map(entityType -> entityType.create(this.level)).orElse(null);
    }

    public void addSoulShard(ItemStack pStack) {

        this.tickCount = 0;
        this.entity = getEntityFromStack(pStack);
        this.inventory.insertItem(0, pStack, false);
    }

    public ItemStack removeSoulShard() {

        this.entity = null;
        return this.inventory.extractItem(0, this.getStack().getCount(), false);
    }

    // -------------------------------- PRIVATE FUNCTIONS --------------------------------

    private void generateLoot() {

        System.out.println("generateLoot");

        if (this.level == null) return;
        if (this.level.isClientSide()) return;
        if (getStack().isEmpty()) return;
        if (getStack().getItem() != ItemInit.FILLED_SOUL_SHARD.get()) return;
        CompoundTag mod_id_tag = getStack().getTagElement(CageriumRecaged.MODID);
        if (mod_id_tag == null) return;
        if (mod_id_tag.getString("EntityID").isEmpty()) return;

        // Get the block entity below the farm block
        BlockEntity below_block_entity = this.level.getExistingBlockEntity(this.getBlockPos().below());
        if (below_block_entity == null) return;

        // Create an item handler for the block entity if it is capable of it
        IItemHandler item_handler = below_block_entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP).orElse((IItemHandler) null);

        // Generated a list of dropped loot for the entity
        NonNullList<ItemStack> drops_list = generateDropsList();

        // Insert each item from the drops list into the below inventory
        drops_list.forEach((pItem) -> {
            ItemHandlerHelper.insertItem(item_handler, pItem, false);
        });
    }

    private NonNullList<ItemStack> generateDropsList() {

        // Create a list to hold the generated loot
        NonNullList<ItemStack> drops_list = NonNullList.create();

        // Add loot to the drops list manually if entity has a non-functioning loot table
        if (this.entity instanceof EnderDragon) {
            // Ender dragons drop 1 dragon egg
            drops_list.add(Items.DRAGON_EGG.getDefaultInstance());

        } else if (this.entity instanceof MagmaCube) {
            // Magma cubes drop 0-1 magma cream
            if (new Random().nextBoolean()) drops_list.add(Items.MAGMA_CREAM.getDefaultInstance());

        } else if (this.entity instanceof Piglin) {
            // Piglins have a 50% chance of spawning with either a sword or a crossbow
            // Piglins have an 8.5% chance of dropping the held item (a golden sword or crossbow with random durability)
            if (new Random().nextFloat() < 0.5f * 0.085f) {
                ItemStack item = new Random().nextBoolean() ? Items.GOLDEN_SWORD.getDefaultInstance() : Items.CROSSBOW.getDefaultInstance();
                item.setDamageValue(new Random().nextInt(item.getMaxDamage()));
                drops_list.add(item);
            }
            // Piglins have a 10% chance per armor slot of spawning with golden armor
            // Piglins have an 8.5% chance of dropping the armor piece with random durability
            NonNullList<ItemStack> armor_list = NonNullList.create();
            armor_list.add(Items.GOLDEN_HELMET.getDefaultInstance());
            armor_list.add(Items.GOLDEN_CHESTPLATE.getDefaultInstance());
            armor_list.add(Items.GOLDEN_LEGGINGS.getDefaultInstance());
            armor_list.add(Items.GOLDEN_BOOTS.getDefaultInstance());
            armor_list.forEach((pItem) -> {
                if (new Random().nextFloat() < 0.1f * 0.085f) {
                    pItem.setDamageValue(new Random().nextInt(pItem.getMaxDamage()));
                    drops_list.add(pItem);
                }
            });

        } else if (this.entity instanceof PiglinBrute) {
            // Piglin brutes have an 8.5% chance of dropping a golden axe with a random durability
            if (new Random().nextFloat() < 0.085f) {
                ItemStack item = Items.GOLDEN_AXE.getDefaultInstance();
                item.setDamageValue(new Random().nextInt(item.getMaxDamage()));
                drops_list.add(item);
            }

        } else if (this.entity instanceof WanderingTrader) {
            // Wandering traders have an 8.5% chance of dropping a milk bucket
            if (new Random().nextFloat() < 0.085f) drops_list.add(Items.MILK_BUCKET.getDefaultInstance());
            // Wandering traders have an 8.5% chance of dropping an invisibility potion
            if (new Random().nextFloat() < 0.085f) drops_list.add(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Potions.INVISIBILITY));

        } else if (this.entity instanceof WitherBoss) {
            // Withers drop 1 nether star
            drops_list.add(Items.NETHER_STAR.getDefaultInstance());

        } else if (this.entity instanceof LivingEntity living_entity) {
            if (this.level == null) return drops_list;
            MinecraftServer server = this.level.getServer();
            if (server == null) return drops_list;

            LootTable loot_table = server.getLootData().getLootTable(living_entity.getLootTable());
            FakePlayer player = FakePlayerFactory.get((ServerLevel)this.level, DUMMY_PROFILE);

            LootParams.Builder loot_params_builder = new LootParams.Builder((ServerLevel) this.level);
            loot_params_builder.withParameter(LootContextParams.THIS_ENTITY, living_entity);
            loot_params_builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
            loot_params_builder.withParameter(LootContextParams.DAMAGE_SOURCE, this.level.damageSources().generic());
            loot_params_builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player);
            LootParams loot_params = loot_params_builder.create(LootContextParamSets.ENTITY);

            if (living_entity instanceof Drowned) {
                // Drowned have a 6.25% chance of spawning with a trident
                // Drowned have an 8.5% chance of dropping the held item
                if (new Random().nextFloat() < 0.0625f * 0.085f) drops_list.add(Items.TRIDENT.getDefaultInstance());

                // Drowned have a 3% chance of spawning with a nautilus shell (always drop)
                if (new Random().nextFloat() < 0.03f) drops_list.add(Items.NAUTILUS_SHELL.getDefaultInstance());

            } else if (living_entity instanceof ZombifiedPiglin) {
                // All zombified piglins spawn with a golden sword
                // Zombified piglins also have an 8.5% chance of dropping their original weapon
                if (new Random().nextFloat() < 0.085f) {
                    ItemStack item = Items.GOLDEN_SWORD.getDefaultInstance();
                    item.setDamageValue(new Random().nextInt(item.getMaxDamage()));
                    drops_list.add(item);
                }
            }

            // Generate random item drops and add them to the drops list
            drops_list.addAll(loot_table.getRandomItems(loot_params));
        }

        return drops_list;
    }
}
