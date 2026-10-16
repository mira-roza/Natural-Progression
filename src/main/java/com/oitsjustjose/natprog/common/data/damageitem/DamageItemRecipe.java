package com.oitsjustjose.natprog.common.data.damageitem;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;
import java.util.Random;

public class DamageItemRecipe extends ShapelessRecipe {

    private static final net.minecraft.tags.TagKey<net.minecraft.world.item.Item> SAWS =
            net.minecraft.tags.TagKey.create(
                    net.minecraft.core.registries.Registries.ITEM,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("natprog", "saw"));
    private static final net.minecraft.tags.TagKey<net.minecraft.world.item.Item> AXES =
            net.minecraft.tags.TagKey.create(
                    net.minecraft.core.registries.Registries.ITEM,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("natprog", "axe"));

    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;

    public DamageItemRecipe(
            String group,
            java.util.List<Ingredient> ingredients,
            ItemStack output
    ) {
        super(
                group,
                CraftingBookCategory.MISC,
                output,
                toIngredientList(ingredients)
        );
    
        this.inputs = super.getIngredients();
        this.output = output.copy();
    }
    
    private static NonNullList<Ingredient> toIngredientList(
            java.util.List<Ingredient> ingredients
    ) {
        NonNullList<Ingredient> result = NonNullList.create();
        result.addAll(ingredients);
        return result;
    }

    public ItemStack getOutput() {
        return this.output;
    }


    @Override
    public net.minecraft.world.item.crafting.RecipeSerializer<?> getSerializer() {
        return net.minecraft.core.registries.BuiltInRegistries.RECIPE_SERIALIZER.get(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                        "natprog", "damage_tools"));
    }


    @Override
    public ItemStack assemble(
            CraftingInput input,
            net.minecraft.core.HolderLookup.Provider registries
    ) {
        return this.output.copy();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        StackedContents contents = new StackedContents();
        int occupiedSlots = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (!stack.isEmpty()) {
                occupiedSlots++;
                contents.accountStack(stack, 1);
            }
        }

        return occupiedSlots == this.inputs.size()
                && contents.canCraft(this, null);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining =
                NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.isEmpty()
                    || (!stack.is(SAWS) && !stack.is(AXES))
                    || !stack.isDamageableItem()) {
                continue;
            }

            ItemStack damaged = stack.copy();
            int damage = damaged.getDamageValue() + 1;

            if (damage < damaged.getMaxDamage()) {
                damaged.setDamageValue(damage);
                remaining.set(i, damaged);
            }
        }

        return remaining;
    }
}