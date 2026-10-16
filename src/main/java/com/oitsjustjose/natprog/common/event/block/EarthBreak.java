
package com.oitsjustjose.natprog.common.event.block;

import com.oitsjustjose.natprog.common.config.CommonConfig;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class EarthBreak {

    @SubscribeEvent
    public void registerEvent(PlayerEvent.BreakSpeed evt) {
        if (!CommonConfig.MAKE_GROUND_BLOCKS_HARDER.get()) {
            return;
        }

        BlockState state = evt.getState();

        boolean isEarthBlock =
                state.is(BlockTags.DIRT)
                || state.is(BlockTags.SAND)
                || state.is(BlockTags.SNOW)
                || state.is(Blocks.CLAY)
                || state.is(Blocks.SCULK)
                || state.is(Blocks.POWDER_SNOW)
                || state.is(Blocks.MOSS_BLOCK)
                || state.is(Blocks.MOSS_CARPET)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.TALL_GRASS);

        if (!isEarthBlock) {
            return;
        }

        if (!evt.getEntity().getMainHandItem().isCorrectToolForDrops(state)) {
            evt.setNewSpeed(evt.getOriginalSpeed() / 4.0F);
        }
    }
}
