package com.example.examplemod.setup;

import com.example.examplemod.block.BatteryBlock;
import com.example.examplemod.block.PipeBlock;
import com.example.examplemod.block.ReassemblerBlock;
import com.example.examplemod.itemgroup.ModItemGroups;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<Block> TEST_BLOCK = register("test_block", () ->
            new RotatedPillarBlock(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(5.0f).sound(SoundType.SOUL_SAND)));
    public static final RegistryObject<Block> REASSEMBLER = register("reassembler", () ->
            new ReassemblerBlock(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(5.0f).sound(SoundType.SOUL_SAND)));
    public static final RegistryObject<Block> BATTERY_BLOCK = registerNoItem("battery_block", () ->
            new BatteryBlock(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(5.0f).sound(SoundType.SOUL_SAND)));
    public static final RegistryObject<Block> PIPE_BLOCK = register("pipe_block", () ->
            new PipeBlock(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(5.0f).sound(SoundType.SOUL_SAND)));

    static void register(){};

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> supplier){
        return Registration.BLOCKS.register(name, supplier);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier){
        RegistryObject<T> ret = registerNoItem(name, supplier);
        Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID)));
        return ret;
    }
}
