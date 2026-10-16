
package com.oitsjustjose.natprog.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class SawItem extends Item {

    public SawItem(Tier tier) {
        this(tier, false);
    }

    public SawItem(Tier tier, boolean fireResistant) {
        super(createProperties(tier, fireResistant));
    }

    private static Item.Properties createProperties(
            Tier tier,
            boolean fireResistant
    ) {
        Item.Properties properties = new Item.Properties()
                .stacksTo(1)
                .durability(tier.getUses());

        return fireResistant
                ? properties.fireResistant()
                : properties;
    }
}