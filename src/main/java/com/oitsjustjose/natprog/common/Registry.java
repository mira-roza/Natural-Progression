
package com.oitsjustjose.natprog.common;

import com.google.common.collect.Lists;
import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.common.blocks.PebbleBlock;
import com.oitsjustjose.natprog.common.blocks.TwigBlock;
import com.oitsjustjose.natprog.common.data.damageitem.DamageItemRecipeSerializer;
import com.oitsjustjose.natprog.common.data.damageitem.DamageItemRecipeType;
import com.oitsjustjose.natprog.common.items.DynamicItemTier;
import com.oitsjustjose.natprog.common.items.PebbleItem;
import com.oitsjustjose.natprog.common.items.SawItem;
import com.oitsjustjose.natprog.common.world.feature.PebbleFeature;
import com.oitsjustjose.natprog.common.world.feature.TwigFeature;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;

public class Registry {

    // --------------------------------------------------
    // Registries
    // --------------------------------------------------

        public final DeferredRegister<Block> BlockRegistry =
                DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MOD_ID);

        public final DeferredRegister<Item> ItemRegistry =
                DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MOD_ID);

        public final DeferredRegister<RecipeType<?>> RecipeTypeRegistry =
                DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);

        public final DeferredRegister<RecipeSerializer<?>> SerializerRegistry =
                DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

        public final DeferredRegister<Feature<?>> FeatureRegistry =
                DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);

        public final DeferredRegister<CreativeModeTab> CreativeTabRegistry =
                DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    // --------------------------------------------------
    // Creative tab
    // --------------------------------------------------

        public final DeferredHolder<CreativeModeTab, CreativeModeTab> NATPROG_TAB =
                CreativeTabRegistry.register("main", () ->
                        CreativeModeTab.builder()
                                .title(Component.translatable(Constants.MOD_ID + ".name"))
                                .icon(() -> Items.IRON_INGOT.getDefaultInstance())
                                .displayItems((parameters, output) -> {
                                ItemRegistry.getEntries().forEach(holder -> {
                                        Item item = holder.get();

                                        if (item instanceof PebbleItem pebbleItem) {
                                                if (pebbleItem.getBlock() instanceof PebbleBlock pebbleBlock
                                                        && pebbleBlock.getParentBlock() != null) {
                                                output.accept(item);
                                                }
                                        } else {
                                                output.accept(item);
                                        }
                                        });
                                })
                                .build()
                );

    // --------------------------------------------------
    // Tool tiers
    // --------------------------------------------------

        public static final Tier flintTier =
                new DynamicItemTier()
                        .setMaxUses(16)
                        .setEfficiency(1.5F)
                        .setAttackDamage(1.0F)
                        .setHarvestLvl(0)
                        .setEnchantability(0)
                        .setRepairMats(Items.FLINT);

        public static final Tier boneTier =
                new DynamicItemTier()
                        .setMaxUses(128)
                        .setEfficiency(2.0F)
                        .setAttackDamage(2.0F)
                        .setHarvestLvl(1)
                        .setEnchantability(0)
                        .setRepairMats(Items.BONE);

        public static final Tier copperTier =
                new DynamicItemTier()
                        .setMaxUses(192)
                        .setEfficiency(1.65F)
                        .setAttackDamage(1.5F)
                        .setHarvestLvl(0)
                        .setEnchantability(0)
                        .setRepairMat(ItemTags.create(
                                ResourceLocation.fromNamespaceAndPath("forge", "ingots/copper")
                        ));

        public static final Tier bronzeTier =
                new DynamicItemTier()
                        .setMaxUses(442)
                        .setEfficiency(2.5F)
                        .setAttackDamage(2.5F)
                        .setHarvestLvl(2)
                        .setEnchantability(0)
                        .setRepairMat(ItemTags.create(
                                ResourceLocation.fromNamespaceAndPath("forge", "ingots/bronze")
                        ));

        public static final Tier steelTier =
                new DynamicItemTier()
                        .setMaxUses(914)
                        .setEfficiency(3.5F)
                        .setAttackDamage(3.5F)
                        .setHarvestLvl(3)
                        .setEnchantability(0)
                        .setRepairMat(ItemTags.create(
                                ResourceLocation.fromNamespaceAndPath("forge", "ingots/steel")
                        ));

    // --------------------------------------------------
    // Items
    // --------------------------------------------------

        public DeferredHolder<Item, Item> flintHatchet;
        public DeferredHolder<Item, Item> bonePickaxe;
        public DeferredHolder<Item, Item> boneKnife;
        public DeferredHolder<Item, Item> boneShard;

        public DeferredHolder<Item, Item> ironSaw;
        public DeferredHolder<Item, Item> flintSaw;
        public DeferredHolder<Item, Item> goldSaw;
        public DeferredHolder<Item, Item> diamondSaw;
        public DeferredHolder<Item, Item> netheriteSaw;
        public DeferredHolder<Item, Item> copperSaw;
        public DeferredHolder<Item, Item> bronzeSaw;
        public DeferredHolder<Item, Item> steelSaw;

    // --------------------------------------------------
    // Blocks
    // --------------------------------------------------

        public DeferredHolder<Block, Block> twigBlock;

        private final List<DeferredHolder<Block, ? extends Block>> NeedItemBlocks =
                Lists.newArrayList();

        public final HashMap<ResourceLocation, DeferredHolder<Block, ? extends Block>> Mapper =
                new HashMap<>();
        private Block createCobbledBlock() {
                return new Block(
                        Block.Properties.of()
                                .mapColor(MapColor.STONE)
                                .strength(2.0F, 6.0F)
                                .sound(SoundType.STONE)
                );
        }
    // --------------------------------------------------
    // Initialization
    // --------------------------------------------------

        public Registry() {
                registerBlocks();
                registerItems();
                registerRecipeStuff();
                registerFeatures();
        }

        public void RegisterAll(IEventBus modEventBus) {
                BlockRegistry.register(modEventBus);
                ItemRegistry.register(modEventBus);
                RecipeTypeRegistry.register(modEventBus);
                SerializerRegistry.register(modEventBus);
                FeatureRegistry.register(modEventBus);
                CreativeTabRegistry.register(modEventBus);
        }

    // --------------------------------------------------
    // Blocks
    // --------------------------------------------------

        private void registerBlocks() {
                twigBlock = BlockRegistry.register("twigs", TwigBlock::new);

                for (ResourceLocation parentId : Constants.PebbleMaterials) {
                        String name =
                                (parentId.getNamespace().equals("minecraft")
                                ? ""
                                : parentId.getNamespace() + "_")
                                + parentId.getPath()
                                + "_pebble";

                        DeferredHolder<Block, PebbleBlock> pebble =
                                BlockRegistry.register(name, () -> new PebbleBlock(parentId));

                        Mapper.put(parentId, pebble);
                        NeedItemBlocks.add(pebble);
                }

                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_andesite", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_diorite", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_granite", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_sandstone", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_red_sandstone", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_tuff", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_dripstone_block", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_netherrack", this::createCobbledBlock));
                NeedItemBlocks.add(BlockRegistry.register(
                        "cobbled_end_stone", this::createCobbledBlock));
                }
    // --------------------------------------------------
    // Items and block items
    // --------------------------------------------------

        private void registerItems() {
                for (DeferredHolder<Block, ? extends Block> blockHolder : NeedItemBlocks) {
                        ResourceLocation id = blockHolder.getId();

                        ItemRegistry.register(id.getPath(), () -> {
                                Block block = blockHolder.get();

                                if (block instanceof PebbleBlock pebbleBlock) {
                                        return new PebbleItem(block, new Item.Properties());
                                }

                                return new BlockItem(block, new Item.Properties());
                        });
                }

                flintHatchet = ItemRegistry.register(
                        "flint_hatchet",
                        () -> new AxeItem(
                                flintTier,
                                new Item.Properties().attributes(
                                        AxeItem.createAttributes(flintTier, 1.8F, 0.0F)
                                )
                        )
                );

                bonePickaxe = ItemRegistry.register(
                        "bone_pickaxe",
                        () -> new PickaxeItem(
                                boneTier,
                                new Item.Properties().attributes(
                                        PickaxeItem.createAttributes(boneTier, 1.0F, -2.8F)
                                )
                        )
                );

                boneKnife = ItemRegistry.register(
                        "bone_knife",
                        () -> new SwordItem(
                                boneTier,
                                new Item.Properties().attributes(
                                        SwordItem.createAttributes(boneTier, 1.0F, -1.4F)
                                )
                        )
                );

                boneShard = ItemRegistry.register(
                        "bone_shard",
                        () -> new Item(new Item.Properties())
                );

                flintSaw = ItemRegistry.register(
                        "flint_saw", () -> new SawItem(flintTier)
                );
                ironSaw = ItemRegistry.register(
                        "iron_saw", () -> new SawItem(Tiers.IRON)
                );
                goldSaw = ItemRegistry.register(
                        "gold_saw", () -> new SawItem(Tiers.GOLD)
                );
                diamondSaw = ItemRegistry.register(
                        "diamond_saw", () -> new SawItem(Tiers.DIAMOND)
                );
                netheriteSaw = ItemRegistry.register(
                        "netherite_saw", () -> new SawItem(Tiers.NETHERITE, true)
                );
                copperSaw = ItemRegistry.register(
                        "copper_saw", () -> new SawItem(copperTier)
                );
                bronzeSaw = ItemRegistry.register(
                        "bronze_saw", () -> new SawItem(bronzeTier)
                );
                steelSaw = ItemRegistry.register(
                        "steel_saw", () -> new SawItem(steelTier)
                );
        }

    // --------------------------------------------------
    // Recipes
    // --------------------------------------------------

        private void registerRecipeStuff() {
                SerializerRegistry.register(
                        "damage_tools", DamageItemRecipeSerializer::new
                );

                RecipeTypeRegistry.register(
                        "damage_tools", DamageItemRecipeType::new
                );
        }

    // --------------------------------------------------
    // Features
    // --------------------------------------------------

        private void registerFeatures() {
                FeatureRegistry.register(
                        "pebbles",
                        () -> new PebbleFeature(NoneFeatureConfiguration.CODEC)
                );

                FeatureRegistry.register(
                        "twigs",
                        () -> new TwigFeature(NoneFeatureConfiguration.CODEC)
                );
        }
}
