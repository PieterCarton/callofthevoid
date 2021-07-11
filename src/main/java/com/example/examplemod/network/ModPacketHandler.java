package com.example.examplemod.network;

import com.example.examplemod.CallOfTheVoidMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;

public class ModPacketHandler {

    public static int id;
    public static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CallOfTheVoidMod.MOD_ID, "essence"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        INSTANCE.registerMessage(id++, EssenceSyncPacket.class,
                (pkt, buffer) -> EssenceSyncPacket.encode(pkt, buffer),
                buffer -> EssenceSyncPacket.decode(buffer),
                (pkt, ctx) -> EssenceSyncPacket.handle(pkt, ctx));
    }
}
