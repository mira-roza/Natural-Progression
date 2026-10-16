package com.oitsjustjose.natprog.common;

import com.oitsjustjose.natprog.Constants;
import com.oitsjustjose.natprog.NatProg;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

public class Utils {

    public static final TagKey<Block> GROUND =
            BlockTags.create(
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "ground")
            );

    public static Block getPebbleForPos(WorldGenLevel level, BlockPos pos) {
        var mapper = NatProg.getInstance().REGISTRY.Mapper;
        var search = new BlockPos(pos.getX(), level.getHeight(), pos.getZ());

        for (var y = level.getHeight() / 2; y < search.getY(); y++) {
            var at = level.getBlockState(search.below(y));

            if (at.is(Blocks.STONE) || at.is(Blocks.DEEPSLATE) || at.isAir()) {
                continue;
            }

            var id = BuiltInRegistries.BLOCK.getKey(at.getBlock());
            if (mapper.containsKey(id)) {
                return mapper.get(id).get();
            }
        }

        // Fallback : stone ou deepslate.
        var choice = level.getRandom().nextBoolean()
                ? Blocks.STONE
                : Blocks.DEEPSLATE;

        var id = BuiltInRegistries.BLOCK.getKey(choice);
        return mapper.get(id).get();
    }

    public static boolean isInWater(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).getFluidState().isSourceOfType(
                net.minecraft.world.level.material.Fluids.WATER
        );
    }

    public static boolean inNonWaterFluid(WorldGenLevel level, BlockPos pos) {
        FluidState fluid = level.getBlockState(pos).getFluidState();

        return !fluid.isEmpty() && !fluid.isSourceOfType(
                net.minecraft.world.level.material.Fluids.WATER
        );
    }

    @Nullable
    public static BlockPos getTopLevelPlacePos(
            WorldGenLevel level,
            ChunkPos chunkPos
    ) {
        return getTopLevelPlacePos(level, chunkPos, -1);
    }

    @Nullable
    public static BlockPos getTopLevelPlacePos(
            WorldGenLevel level,
            ChunkPos chunkPos,
            int spread
    ) {
        int usedSpread = Math.max(8, spread);

        int xCenter = (chunkPos.getMinBlockX() + chunkPos.getMaxBlockX()) / 2;
        int zCenter = (chunkPos.getMinBlockZ() + chunkPos.getMaxBlockZ()) / 2;

        int x = xCenter + level.getRandom().nextInt(usedSpread)
                * (level.getRandom().nextBoolean() ? 1 : -1);
        int z = zCenter + level.getRandom().nextInt(usedSpread)
                * (level.getRandom().nextBoolean() ? 1 : -1);

        int startY = level.getMaxBuildHeight() - 1;

        for (int y = startY; y > level.getMinBuildHeight(); y--) {
            BlockPos groundPos = new BlockPos(x, y, z);

            if (!canPlaceOn(level, groundPos)) {
                continue;
            }

            BlockPos placePos = groundPos.above();

            if (canReplace(level, placePos)) {
                return placePos;
            }
        }

        return null;
    }

    public static boolean canPlaceOn(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        return Block.isShapeFullBlock(state.getShape(level, pos))
                && state.is(GROUND);
    }

    public static boolean canReplace(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        return state.isAir()
                || state.is(BlockTags.LEAVES)
                || state.canBeReplaced()
                || !state.getFluidState().isEmpty();
    }

    public static void fixSnowyBlock(
            WorldGenLevel level,
            BlockPos posPlaced
    ) {
        var below = level.getBlockState(posPlaced.below());

        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            level.setBlock(
                    posPlaced.below(),
                    below.setValue(BlockStateProperties.SNOWY, Boolean.FALSE),
                    2 | 16
            );
        }
    }
}