package com.oitsjustjose.natprog.common.event;

import com.oitsjustjose.natprog.common.data.WoodRecipeRewriter;
import net.minecraft.server.ReloadableServerResources;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class RecipeReloadEvent {

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        ReloadableServerResources resources = event.getServerResources();

        event.addListener(
                new WoodRecipeRewriter(
                        resources.getRecipeManager(),
                        resources.getRegistryLookup()
                )
        );
    }
}
