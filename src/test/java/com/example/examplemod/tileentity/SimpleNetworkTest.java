package com.example.examplemod.tileentity;

import com.example.examplemod.block.BatteryBlock;
import com.example.examplemod.block.ReassemblerBlock;
import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.utility.MockWorld;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.GameConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class SimpleNetworkTest {

    ReassemblerBlock reassemblerBlock;

    ReassemblerTileEntity reassembler;
    BatteryBlockTileEntity orderBattery;
    BatteryBlockTileEntity entropyBattery;

    World world;

    @BeforeEach
    void setup() {
        world = Mockito.mock(World.class);
        reassemblerBlock = new ReassemblerBlock(AbstractBlock.Properties.create(Material.ROCK));

        reassembler = new ReassemblerTileEntity(TileEntityType.BANNER);
        orderBattery = new BatteryBlockTileEntity(TileEntityType.BANNER, EssenceType.ORDER,
                1000, 10000, 20);
        entropyBattery = new BatteryBlockTileEntity(TileEntityType.BANNER, EssenceType.ORDER,
                1000, 10000, 20);

        //all blocks are air and contain no tile entity unless specified otherwise
        when(world.getBlockState(any())).thenReturn(Blocks.AIR.getDefaultState());
        when(world.isRemote).thenReturn(false);
    }

    @Test
    void validPowerSourceTest() {
        when(world.getBlockState(eq(new BlockPos(1.0d, 0.0d, 0.0d)))).thenReturn(reassemblerBlock.getDefaultState());
        when(world.getTileEntity(eq(new BlockPos(1.0d, 0.0d, 0.0d)))).thenReturn(reassembler);

        reassembler.setWorldAndPos(world, new BlockPos(0.0d, 0.0d, 0.0d));
        orderBattery.setWorldAndPos(world, new BlockPos(1.0d, 0.0d, 0.0d));
        orderBattery.tick();

        assertEquals(980, orderBattery.essenceStored(EssenceType.ORDER));
        assertEquals(20, reassembler.essenceStored(EssenceType.ORDER));
    }

}