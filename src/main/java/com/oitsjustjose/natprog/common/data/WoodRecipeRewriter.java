package com.oitsjustjose.natprog.common.data;

import com.oitsjustjose.natprog.common.data.damageitem.DamageItemRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class WoodRecipeRewriter implements PreparableReloadListener {
    private static final TagKey<Item> SAWS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("natprog", "saw"));
    private static final TagKey<Item> AXES = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("natprog", "axe"));

    private static final List<String> WOOD_SUFFIXES =
            List.of("_log", "_wood", "_stem");

    private final RecipeManager recipeManager;
    private final HolderLookup.Provider registries;

    public WoodRecipeRewriter(
            RecipeManager recipeManager,
            HolderLookup.Provider registries
    ) {
        this.recipeManager = recipeManager;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<Void> reload(
            PreparationBarrier barrier,
            ResourceManager resourceManager,
            ProfilerFiller preparationProfiler,
            ProfilerFiller reloadProfiler,
            Executor backgroundExecutor,
            Executor gameExecutor
    ) {
        return barrier.wait(null).thenRunAsync(this::rewriteRecipes, gameExecutor);
    }

    private void rewriteRecipes() {
        Collection<RecipeHolder<?>> existing = recipeManager.getRecipes();
        List<RecipeHolder<?>> result = new ArrayList<>();
        int removed = 0;

        for (RecipeHolder<?> holder : existing) {
            if (isStandardWoodRecipe(holder)) {
                removed++;
            } else {
                result.add(holder);
            }
        }

        int generated = 0;

        for (ResourceLocation plankId : BuiltInRegistries.ITEM.keySet()) {
            String path = plankId.getPath();
            if (!path.endsWith("_planks")) {
                continue;
            }

            String woodName =
                    path.substring(0, path.length() - "_planks".length());
            Item plank = findItem(plankId);

            if (plank == null) {
                continue;
            }

            for (String suffix : WOOD_SUFFIXES) {
                ResourceLocation sourceId = ResourceLocation.fromNamespaceAndPath(
                        plankId.getNamespace(), woodName + suffix);
                Item source = findItem(sourceId);

                if (source == null) {
                    continue;
                }

                Item stripped = findStripped(
                        plankId.getNamespace(), woodName, suffix);

                if (stripped != null) {
                    ResourceLocation strippedId =
                            BuiltInRegistries.ITEM.getKey(stripped);

                    add(result, id(sourceId, "strip_saw"),
                            source, stripped, 1, SAWS);
                    add(result, id(sourceId, "strip_axe"),
                            source, stripped, 1, AXES);

                    add(result, id(strippedId, "saw_planks"),
                            stripped, plank, 4, SAWS);
                    add(result, id(strippedId, "axe_plank"),
                            stripped, plank, 1, AXES);

                    generated += 4;
                } else {
                    add(result, id(sourceId, "saw_planks"),
                            source, plank, 4, SAWS);
                    add(result, id(sourceId, "axe_plank"),
                            source, plank, 1, AXES);

                    generated += 2;
                }
            }
        }

        recipeManager.replaceRecipes(result);
        System.out.println("[NatProg] Removed " + removed
                + " standard wood recipes; generated " + generated
                + " replacements. Non-standard recipes were preserved.");
    }

    private boolean isStandardWoodRecipe(RecipeHolder<?> holder) {
        if (!(holder.value() instanceof CraftingRecipe recipe)) {
            return false;
        }

        ItemStack output = holder.value().getResultItem(registries);
        if (output.isEmpty()) {
            return false;
        }

        ResourceLocation outputId =
                BuiltInRegistries.ITEM.getKey(output.getItem());
        String path = outputId.getPath();
        String namespace = outputId.getNamespace();

        // Recipes producing planks from their corresponding log/wood/stem.
        if (path.endsWith("_planks")) {
            String woodName =
                    path.substring(0, path.length() - "_planks".length());

            for (String suffix : WOOD_SUFFIXES) {
                ResourceLocation source = ResourceLocation.fromNamespaceAndPath(
                        namespace, woodName + suffix);
                ResourceLocation stripped = ResourceLocation.fromNamespaceAndPath(
                        namespace, "stripped_" + woodName + suffix);
                ResourceLocation strippedAlt = ResourceLocation.fromNamespaceAndPath(
                        namespace, woodName + suffix + "_stripped");

                if (hasIngredient(recipe, source)
                        || hasIngredient(recipe, stripped)
                        || hasIngredient(recipe, strippedAlt)) {
                    return true;
                }
            }
        }

        // Recipes producing stripped wood from the matching unstripped item.
        String sourcePath = null;
        if (path.startsWith("stripped_")) {
            sourcePath = path.substring("stripped_".length());
        } else if (path.endsWith("_stripped")) {
            sourcePath = path.substring(0, path.length() - "_stripped".length());
        }

        if (sourcePath != null) {
            for (String suffix : WOOD_SUFFIXES) {
                if (sourcePath.endsWith(suffix)) {
                    ResourceLocation source = ResourceLocation.fromNamespaceAndPath(
                            namespace, sourcePath);
                    return hasIngredient(recipe, source);
                }
            }
        }

        return false;
    }

    private boolean hasIngredient(
            CraftingRecipe recipe,
            ResourceLocation itemId
    ) {
        Item item = findItem(itemId);
        if (item == null) {
            return false;
        }

        for (Ingredient ingredient : recipe.getIngredients()) {
            for (ItemStack stack : ingredient.getItems()) {
                if (stack.is(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Item findStripped(
            String namespace,
            String woodName,
            String suffix
    ) {
        Item item = findItem(ResourceLocation.fromNamespaceAndPath(
                namespace, "stripped_" + woodName + suffix));

        if (item != null) {
            return item;
        }

        return findItem(ResourceLocation.fromNamespaceAndPath(
                namespace, woodName + suffix + "_stripped"));
    }

    private static Item findItem(ResourceLocation id) {
        Item item = BuiltInRegistries.ITEM.get(id);
        return item == net.minecraft.world.item.Items.AIR ? null : item;
    }

    private static ResourceLocation id(ResourceLocation source, String action) {
        return ResourceLocation.fromNamespaceAndPath(
                "natprog",
                "generated/wood/" + source.getNamespace()
                        + "/" + source.getPath() + "_" + action);
    }

    private static void add(
            List<RecipeHolder<?>> recipes,
            ResourceLocation id,
            Item input,
            Item output,
            int count,
            TagKey<Item> toolTag
    ) {
        List<Ingredient> ingredients = List.of(
                Ingredient.of(input),
                Ingredient.of(toolTag));

        DamageItemRecipe recipe = new DamageItemRecipe(
                "planks", ingredients, new ItemStack(output, count));

        recipes.add(new RecipeHolder<>(id, recipe));
    }
}
