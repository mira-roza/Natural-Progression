package com.oitsjustjose.natprog.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;


public class CommonConfig {
    public static final ModConfigSpec COMMON_CONFIG;
    private static final Builder COMMON_BUILDER = new Builder();

    public static ModConfigSpec.IntValue MAX_PEBBLES_PER_CHUNK;
    public static ModConfigSpec.IntValue MAX_TWIGS_PER_CHUNK;
    public static ModConfigSpec.BooleanValue TOOL_NEUTERING;
    public static ModConfigSpec.BooleanValue REMOVE_WOODEN_TOOL_FUNC;
    public static ModConfigSpec.BooleanValue REMOVE_STONE_TOOL_FUNC;
    public static ModConfigSpec.BooleanValue MAKE_GROUND_BLOCKS_HARDER;
    public static ModConfigSpec.BooleanValue ARE_PEBBLES_REPLACEABLE;
    public static ModConfigSpec.BooleanValue ARE_TWIGS_PLACEABLE;
    public static ModConfigSpec.IntValue FLINT_CHANCE;
    public static ModConfigSpec.IntValue BONE_SHARD_CHANCE;
    public static ModConfigSpec.IntValue BONE_DROP_CHANCE;
    public static ModConfigSpec.BooleanValue ALL_ENTITIES_DROP_BONES;
    public static ModConfigSpec.BooleanValue SHOW_BREAKING_HELP;
    public static ModConfigSpec.BooleanValue INCORRECT_TOOL_DAMAGE;
    public static ModConfigSpec.BooleanValue ENABLE_KNAPPING;

    static {
        init();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void init() {
        String CATEGORY_GENERAL = "general";
        COMMON_BUILDER.comment("Miscellaneous").push(CATEGORY_GENERAL);

        MAX_PEBBLES_PER_CHUNK = COMMON_BUILDER.comment("The maximum number of pebbles that can be found in each chunk").defineInRange("maxPebblesPerChunk", 5, 0, 256);
        MAX_TWIGS_PER_CHUNK = COMMON_BUILDER.comment("The maximum number of twigs that can be found in each chunk").defineInRange("maxTwigsPerChunk", 3, 0, 256);
        TOOL_NEUTERING = COMMON_BUILDER.comment("Make disabled tools completely useless - can't even break grass.").define("toolNeutering", false);
        REMOVE_WOODEN_TOOL_FUNC = COMMON_BUILDER.comment("Setting this to true prevents the ability to use wooden tools, though you can still craft them for compatibility.").define("removeWoodenToolFunctionality", true);
        REMOVE_STONE_TOOL_FUNC = COMMON_BUILDER.comment("Setting this to true prevents the ability to use stone tools, though you can still craft them for compatibility.").define("removeStoneToolFunctionality", true);
        MAKE_GROUND_BLOCKS_HARDER = COMMON_BUILDER.comment("Setting this to true will make ground blocks (e.g. sand, dirt, gravel) harder to break without the correct tool.").define("makeGroundBlocksHarder", true);
        ARE_PEBBLES_REPLACEABLE = COMMON_BUILDER.comment("Setting this to true will allow you to replace pebbles like tall grass (more convenient for building, but loses the block forever").define("arePebblesReplaceable", true);
        ARE_TWIGS_PLACEABLE = COMMON_BUILDER.comment("Setting this to true will allow players to place Twigs by Right-Clicking a Stick on a solid surface").define("areTwigsPlaceable", true);
        FLINT_CHANCE = COMMON_BUILDER.comment("The chance (out of 100) for flint to be created via knapping.\n" + "e.g.: Setting to 75 means there is a 75% chance knapping will provide flint.").defineInRange("flintKnappingChance", 75, 1, 100);
        BONE_SHARD_CHANCE = COMMON_BUILDER.comment("The chance (out of 100) for bone to be created via knapping.\n" + "e.g.: Setting to 75 means there is a 75% chance knapping will provide a bone shard.").defineInRange("boneShardKnappingChance", 75, 1, 100);
        BONE_DROP_CHANCE = COMMON_BUILDER.comment("The chance (out of 100) that a bone can drop from the entities in 'boneDropMobs'.\nSetting this to 0 disables this feature").defineInRange("boneDropFromMobsChance", 50, 0, 100);
        ALL_ENTITIES_DROP_BONES = COMMON_BUILDER.comment("Enabling this causes all entities to drop additional bones when killed").define("allEntitiesDropBones", false);
        SHOW_BREAKING_HELP = COMMON_BUILDER.comment("Setting this to true will let players know that they can't break certain blocks without a certain tool").define("showToolHelp", true);
        INCORRECT_TOOL_DAMAGE = COMMON_BUILDER.comment("Setting this to true will damage players that attempt to break blocks with the incorrect tool").define("incorrectToolDamage", true);
        ENABLE_KNAPPING = COMMON_BUILDER.comment("Setting this to false will disable the knapping mechanic entirely - a substitute mechanic for obtaining tools or flint will need to be added by the pack developer").define("enableKnapping", true);
        COMMON_BUILDER.pop();
    }
}
