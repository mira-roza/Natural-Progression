package com.oitsjustjose.natprog.common.data.damageitem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;

public class DamageItemRecipeSerializer
        implements RecipeSerializer<DamageItemRecipe> {

        private static final MapCodec<DamageItemRecipe> CODEC =
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Codec.STRING.optionalFieldOf("group", "")
                                .forGetter(DamageItemRecipe::getGroup),
                        Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients")
                                .forGetter(DamageItemRecipe::getIngredients),
                        ItemStack.CODEC.fieldOf("result")
                                .forGetter(DamageItemRecipe::getOutput)
                ).apply(instance, DamageItemRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, DamageItemRecipe>
                STREAM_CODEC = StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8,
                        DamageItemRecipe::getGroup,
                        ByteBufCodecs.collection(
                                ArrayList::new,
                                Ingredient.CONTENTS_STREAM_CODEC
                        ),
                        DamageItemRecipe::getIngredients,
                        ItemStack.STREAM_CODEC,
                        DamageItemRecipe::getOutput,
                        DamageItemRecipe::new
                );

        @Override
        public MapCodec<DamageItemRecipe> codec() {
                return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DamageItemRecipe> streamCodec() {
                return STREAM_CODEC;
        }
}