package com.example.examplemod.network;

import com.example.examplemod.CallOfTheVoidMod;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.security.Provider;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

        register(ClimbingCapabilitySyncPacket.class, ClimbingCapabilitySyncPacket::new);
        register(CClimbingActionPacket.class, CClimbingActionPacket::new);
    }

    public static <MSG extends IModPacket> void register(Class<MSG> messageType, Function<PacketBuffer, MSG> decoder) {
        INSTANCE.registerMessage(id++, messageType, (pkt, buffer) -> pkt.serialize(buffer), decoder, (pkt, ctx) -> pkt.handle(ctx));
    }
}
