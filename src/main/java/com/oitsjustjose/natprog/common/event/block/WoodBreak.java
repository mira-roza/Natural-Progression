
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

public class WoodBreak {
    public static final TagKey<Item> CONSIDERED_AS_AXE =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(
                    Constants.MOD_ID, "considered_as_axe"));

    public static final TagKey<Block> IGNORED_WOOD_BLOCKS =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath(
                    Constants.MOD_ID, "ignored_wood_blocks"));

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (evt.getState() == null || evt.getEntity() == null) return;

        var state = evt.getState();

        if (!state.is(BlockTags.MINEABLE_WITH_AXE)
                || state.is(IGNORED_WOOD_BLOCKS)) {
            return;
        }

        var player = evt.getEntity();
        var heldItem = player.getMainHandItem();

        if (heldItem.is(CONSIDERED_AS_AXE)
                || heldItem.is(ItemTags.AXES)) {
            return;
        }

        evt.setCanceled(true);

        if (heldItem.isEmpty()
                && player.getRandom().nextInt(25) == 1
                && CommonConfig.INCORRECT_TOOL_DAMAGE.get()) {
            if (player.getRandom().nextInt(10) == 1) {
                player.hurt(player.level().damageSources().generic(), 1.0F);
            }
        }

        if (CommonConfig.SHOW_BREAKING_HELP.get()) {
            player.displayClientMessage(
                    Component.translatable("natprog.wood.warning"),
                    true
            );
        }
    }
}
