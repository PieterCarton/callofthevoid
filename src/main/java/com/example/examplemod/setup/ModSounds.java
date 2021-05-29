package com.example.examplemod.setup;

import com.example.examplemod.CallOfTheVoidMod;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static RegistryObject<SoundEvent> MUSIC_DISK_BACKSEAT = register("backseat",
            () -> new SoundEvent(new ResourceLocation(CallOfTheVoidMod.MOD_ID, "music_disk.backseat")));

    public static RegistryObject<SoundEvent> register(String name, Supplier<SoundEvent> supplier) {
        return Registration.SOUNDS.register(name, supplier);
    }

    static void init(){}
}
