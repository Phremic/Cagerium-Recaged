package net.phremic.cageriumrecaged.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CageriumRecagedCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> GENERATE_LOOT_PERIOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> GENERATE_LOOT_INSTANCES;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ONLY_ACCEPT_VANILLA;

    static {
        BUILDER.push("Cagerium Recaged - Common Config");
        BUILDER.comment("When a farm block (Terrarium, Cage, or Plate) contains a Soul Shard with a captured soul, the farm block will periodically generate loot.");

        BUILDER.comment("How long (in ticks) a farm block will wait between generating loot. (Default = 600)");
        GENERATE_LOOT_PERIOD = BUILDER.define("Generate Loot Period", 600);

        BUILDER.comment("How many times to spawn drops when generating loot every period. This can also be interpreted as how many mobs are slain and generate drops every period. (Default = 1)");
        GENERATE_LOOT_INSTANCES = BUILDER.define("Generate Loot Instances", 1);

        BUILDER.comment("Whether Soul Shards should only capture souls from slain vanilla mobs. (Default = false)");
        ONLY_ACCEPT_VANILLA = BUILDER.define("Only Accept Vanilla Mobs", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
