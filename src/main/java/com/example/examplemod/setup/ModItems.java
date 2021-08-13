package com.example.examplemod.setup;

import com.example.examplemod.item.*;
import com.example.examplemod.itemgroup.ModItemGroups;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashSet;

public class ModItems {
    public static final RegistryObject<Item> CUSTOM_INGOT = Registration.ITEMS.register("custom_ingot", () ->
            new Item(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID)));

    public static final RegistryObject<Item> ORDER_ESSENCE = Registration.ITEMS.register("order_essence", () ->
            new Item(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID)));

    public static final RegistryObject<Item> ENTROPIC_ESSENCE = Registration.ITEMS.register("entropic_essence", () ->
            new Item(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID)));

    public static final RegistryObject<Item> BATTERY_BLOCK_ITEM = Registration.ITEMS.register("battery_block", () ->
            new BatteryBlockItem(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1)));

    public static final RegistryObject<Item> ESSENCE_ANALYSER_ITEM = Registration.ITEMS.register("essence_analyser", () ->
            new EssenceAnalyserItem(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1)));

    public static final RegistryObject<Item> PIPE_WRENCH_ITEM = Registration.ITEMS.register("pipe_wrench", () ->
            new PipeWrenchItem(5.0f, 0.8f, ItemTier.IRON, new HashSet<>(), new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1)));

    public static final RegistryObject<Item> WALKING_CANE_ITEM = Registration.ITEMS.register("walking_cane", () ->
            new CaneItem(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1)));

    public static final RegistryObject<Item> SHEATHED_SWORD_CANE_ITEM = Registration.ITEMS.register("sheathed_sword_cane", () ->
            new SheathedSwordCaneItem(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1)));

    public static final RegistryObject<Item> SWORD_CANE_ITEM = Registration.ITEMS.register("sword_cane", () ->
            new SwordCaneItem(5.0f, 0.8f, ItemTier.IRON, new HashSet<>(),new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1)));

    public static final RegistryObject<Item> SWORD_CANE_SHEATH_ITEM = Registration.ITEMS.register("sword_cane_sheath", () ->
            new Item(new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1)));

    public static final RegistryObject<Item> MUSIC_DISK_BACKSEAT = Registration.ITEMS.register("music_disk_backseat", () ->
            new MusicDiscItem(12, ModSounds.MUSIC_DISK_BACKSEAT, new Item.Properties().group(ModItemGroups.CALL_OF_THE_VOID).maxStackSize(1).rarity(Rarity.RARE)));

    static void register(){}
}
