package com.example.examplemod.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet that might be sent from server to client and vice versa.
 * Must have some was for both sides to handle receiving the packet.
 */
public abstract class BidirectionalPacket implements IModPacket{
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() ->
                    {
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.handleClientside(ctx.get()));
                    }
            );
        } else {
            ctx.get().enqueueWork(() -> this.handleServerside(ctx.get()));
        }
        ctx.get().setPacketHandled(true);
    }

    public abstract void handleClientside(NetworkEvent.Context ctx);

    public abstract void handleServerside(NetworkEvent.Context ctx);
}
