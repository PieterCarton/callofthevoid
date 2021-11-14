package com.example.examplemod.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IModPacket {

    void handle(Supplier<NetworkEvent.Context> ctx);

    void serialize(PacketBuffer buffer);
}
