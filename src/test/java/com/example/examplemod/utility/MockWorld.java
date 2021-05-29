package com.example.examplemod.utility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.storage.ISpawnWorldInfo;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Supplier;

public class MockWorld extends World {
    public static MockWorld createMock() {
        ISpawnWorldInfo info = new ISpawnWorldInfo() {
            @Override
            public void setSpawnX(int x) {

            }

            @Override
            public void setSpawnY(int y) {

            }

            @Override
            public void setSpawnZ(int z) {

            }

            @Override
            public void setSpawnAngle(float angle) {

            }

            @Override
            public int getSpawnX() {
                return 0;
            }

            @Override
            public int getSpawnY() {
                return 0;
            }

            @Override
            public int getSpawnZ() {
                return 0;
            }

            @Override
            public float getSpawnAngle() {
                return 0;
            }

            @Override
            public long getGameTime() {
                return 0;
            }

            @Override
            public long getDayTime() {
                return 0;
            }

            @Override
            public boolean isThundering() {
                return false;
            }

            @Override
            public boolean isRaining() {
                return false;
            }

            @Override
            public void setRaining(boolean isRaining) {

            }

            @Override
            public boolean isHardcore() {
                return false;
            }

            @Override
            public GameRules getGameRulesInstance() {
                return null;
            }

            @Override
            public Difficulty getDifficulty() {
                return null;
            }

            @Override
            public boolean isDifficultyLocked() {
                return false;
            }
        };

        return new MockWorld(info, World.OVERWORLD, MockDimensionType.createMock(), null, false, true, 1);
    }

    protected MockWorld(ISpawnWorldInfo worldInfo, RegistryKey<World> dimension, DimensionType dimensionType, Supplier<IProfiler> profiler, boolean isRemote, boolean isDebug, long seed) {
        super(worldInfo, dimension, dimensionType, profiler, isRemote, isDebug, seed);
    }

    @Override
    public void notifyBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags) {

    }

    @Override
    public void playSound(PlayerEntity player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playMovingSound(PlayerEntity playerIn, Entity entityIn, SoundEvent eventIn, SoundCategory categoryIn, float volume, float pitch) {

    }


    @Override
    public Entity getEntityByID(int id) {
        return null;
    }


    @Override
    public MapData getMapData(String mapName) {
        return null;
    }

    @Override
    public void registerMapData(MapData mapDataIn) {

    }

    @Override
    public int getNextMapId() {
        return 0;
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public RecipeManager getRecipeManager() {
        return null;
    }

    @Override
    public ITagCollectionSupplier getTags() {
        return null;
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return null;
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return null;
    }

    @Override
    public AbstractChunkProvider getChunkProvider() {
        return null;
    }

    @Override
    public void playEvent(PlayerEntity player, int type, BlockPos pos, int data) {

    }

    @Override
    public DynamicRegistries func_241828_r() {
        return null;
    }

    @Override
    public float func_230487_a_(Direction p_230487_1_, boolean p_230487_2_) {
        return 0;
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return null;
    }

    @Override
    public Biome getNoiseBiomeRaw(int x, int y, int z) {
        return null;
    }
}
