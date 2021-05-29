package com.example.examplemod.setup;

import com.example.examplemod.CallOfTheVoidMod;
import com.example.examplemod.block.ReassemblerBlock;
import com.example.examplemod.tileentity.BatteryBlockTileEntity;
import com.example.examplemod.tileentity.ReassemblerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityTypes {
    public static final RegistryObject<TileEntityType<?>> REASSEMBLER = register(
            TileEntityType.Builder.create(ReassemblerTileEntity::new, ModBlocks.REASSEMBLER.get()).build(null), "reassemblerte"
    );

    public static final RegistryObject<TileEntityType<?>> BATTERY = register(
            TileEntityType.Builder.create(BatteryBlockTileEntity::new, ModBlocks.BATTERY_BLOCK.get()).build(null), "batteryblockte"
    );

    static void register() {}

    public static RegistryObject<TileEntityType<?>> register(TileEntityType<?> type, String name){
        return Registration.TILE_ENTITIES.register(name, () -> type);
    }
}
