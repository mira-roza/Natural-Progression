package com.oitsjustjose.natprog;

import com.oitsjustjose.natprog.common.Registry;
import com.oitsjustjose.natprog.common.config.CommonConfig;
import com.oitsjustjose.natprog.common.event.*;
import com.oitsjustjose.natprog.common.event.block.EarthBreak;
import com.oitsjustjose.natprog.common.event.block.StoneBreak;
import com.oitsjustjose.natprog.common.event.block.WoodBreak;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MOD_ID)
public class NatProg {
    private static NatProg instance;

    public final Logger LOGGER = LogManager.getLogger();

    public final Registry REGISTRY;

    public NatProg(IEventBus modEventBus, ModContainer modContainer) {
        instance = this;

        // Enregistrement des registres du mod
        REGISTRY = new Registry();
        REGISTRY.RegisterAll(modEventBus);

        // Enregistrement des événements communs
        NeoForge.EVENT_BUS.register(new WoodBreak());
        NeoForge.EVENT_BUS.register(new StoneBreak());
        NeoForge.EVENT_BUS.register(new EarthBreak());
        NeoForge.EVENT_BUS.register(new ToolNeutering());
        NeoForge.EVENT_BUS.register(new BoneEvent());
        NeoForge.EVENT_BUS.register(new TwigPlacement());
        NeoForge.EVENT_BUS.register(new RecipeReloadEvent());

        // Enregistrement de la configuration commune
        modContainer.registerConfig(
                ModConfig.Type.COMMON,
                CommonConfig.COMMON_CONFIG
        );
    }

    public static NatProg getInstance() {
        return instance;
    }
}