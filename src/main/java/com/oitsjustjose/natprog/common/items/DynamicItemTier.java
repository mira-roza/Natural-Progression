
package com.oitsjustjose.natprog.common.items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DynamicItemTier implements Tier {
    private int uses;
    private float speed;
    private float attackDamageBonus;
    private int harvestLvl;
    private int enchantability;
    private Ingredient repairIngredient;

    public DynamicItemTier() {
        this.uses = 0;
        this.speed = 0;
        this.attackDamageBonus = 0;
        this.harvestLvl = 0;
        this.enchantability = 0;
        this.repairIngredient = Ingredient.of(Items.BARRIER);
    }

    public DynamicItemTier setMaxUses(int maxUses) {
        this.uses = maxUses;
        return this;
    }

    public DynamicItemTier setEfficiency(float efficiency) {
        this.speed = efficiency;
        return this;
    }

    public DynamicItemTier setAttackDamage(float damage) {
        this.attackDamageBonus = damage;
        return this;
    }

    public DynamicItemTier setHarvestLvl(int level) {
        this.harvestLvl = level;
        return this;
    }

    public DynamicItemTier setEnchantability(int enchantability) {
        this.enchantability = enchantability;
        return this;
    }

    public DynamicItemTier setRepairMat(@Nullable TagKey<Item> tag) {
        if (tag != null) {
            this.repairIngredient = Ingredient.of(tag);
        }
        return this;
    }

    public DynamicItemTier setRepairMats(Item... items) {
        this.repairIngredient = Ingredient.of(items);
        return this;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    public int getLevel() {
        return harvestLvl;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    @Nonnull
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }
    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
    }
}
