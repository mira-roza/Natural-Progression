package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.Constants;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.network.chat.Component;

public class ToolNeutering {
    public static final TagKey<Item> ALLOWED_WOOD_TOOLS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "allowed_wooden_tools"));
    public static final TagKey<Item> ALLOWED_STONE_TOOLS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "allowed_stone_tools"));

    
    @SubscribeEvent
    public void onHover(ItemTooltipEvent evt) {
        var stack = evt.getItemStack();
        if (stack.isEmpty()) return;
        if (stack.is(ALLOWED_WOOD_TOOLS) || stack.is(ALLOWED_STONE_TOOLS)) return;

        if (!(stack.getItem() instanceof TieredItem tiered)) return;

        if ((tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get()) {
            evt.getToolTip().add(Component.translatable("natprog.too.brittle"));
        }

        if ((tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get()) {
            evt.getToolTip().add(Component.translatable("natprog.too.blunt"));
        }
    }


    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (!CommonConfig.TOOL_NEUTERING.get()) return;
        var stack = evt.getEntity().getMainHandItem();
        if (stack.isEmpty()) return;
        if (stack.is(ALLOWED_WOOD_TOOLS) || stack.is(ALLOWED_STONE_TOOLS)) return;

        if (evt.getState() == null || evt.getEntity() == null) return;
        if (!(stack.getItem() instanceof TieredItem tiered)) return;

        var cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        var cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            evt.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void registerEvent(AttackEntityEvent evt) {
        if (evt.getEntity() == null) return;
        var stack = evt.getEntity().getMainHandItem();
        if (stack.isEmpty()) return;
        if (stack.is(ALLOWED_WOOD_TOOLS) || stack.is(ALLOWED_STONE_TOOLS)) return;

        if (!(stack.getItem() instanceof TieredItem tiered)) return;

        var cancelWood = (tiered.getTier() == Tiers.WOOD) && CommonConfig.REMOVE_WOODEN_TOOL_FUNC.get();
        var cancelStone = (tiered.getTier() == Tiers.STONE) && CommonConfig.REMOVE_STONE_TOOL_FUNC.get();

        if (cancelWood || cancelStone) {
            evt.setCanceled(true);
        }
    }
}
