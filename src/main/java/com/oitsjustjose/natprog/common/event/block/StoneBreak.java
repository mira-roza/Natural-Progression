
package com.oitsjustjose.natprog.common.event.block;

import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class StoneBreak {
    public static final TagKey<Item> CONSIDERED_AS_PICKAXE =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(
                    Constants.MOD_ID, "considered_as_pickaxe"));

    public static final TagKey<Block> IGNORED_STONE_BLOCKS =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(
                    Constants.MOD_ID, "ignored_stone_blocks"));

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (evt.getState() == null || evt.getEntity() == null) return;

        var state = evt.getState();

        boolean heavyBlock =
                state.is(BlockTags.MINEABLE_WITH_PICKAXE)
                || state.is(BlockTags.NEEDS_STONE_TOOL)
                || state.is(BlockTags.NEEDS_IRON_TOOL);

        if (!heavyBlock || state.is(IGNORED_STONE_BLOCKS)) return;

        var player = evt.getEntity();
        var heldItem = player.getMainHandItem();

        if (heldItem.is(CONSIDERED_AS_PICKAXE)
                || heldItem.is(ItemTags.PICKAXES)) {
            return;
        }

        evt.setCanceled(true);

        if (heldItem.isEmpty()
                && player.getRandom().nextInt(25) == 1
                && CommonConfig.INCORRECT_TOOL_DAMAGE.get()) {
            if (player.getRandom().nextBoolean()) {
                player.hurt(player.level().damageSources().generic(), 1.0F);
            }
        }

        if (CommonConfig.SHOW_BREAKING_HELP.get()) {
            player.displayClientMessage(
                    Component.translatable("natprog.stone.warning"),
                    true
            );
        }
    }
}
