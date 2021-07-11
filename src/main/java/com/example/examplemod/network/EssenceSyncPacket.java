package com.example.examplemod.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import org.lwjgl.system.windows.MSG;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.function.Supplier;

public class EssenceSyncPacket {

    private CompoundNBT nbt;
    private int containerId;

    public EssenceSyncPacket(CompoundNBT nbt, int containerId) {
        System.out.println("send: " + nbt.toString());
        this.nbt = nbt;
        this.containerId = containerId;
    }

    public static void encode(EssenceSyncPacket pkt, PacketBuffer buffer) {
        buffer.writeCompoundTag(pkt.nbt);
        buffer.writeInt(pkt.containerId);
    }

    public static EssenceSyncPacket decode(PacketBuffer buffer) {
        CompoundNBT nbt = buffer.readCompoundTag();
        int containerId = buffer.readInt();
        return new EssenceSyncPacket(nbt, containerId);
    }

    public static void handle(EssenceSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            {
                System.out.println("Packet Received");
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EssenceSyncPacketHandler.handle(pkt, ctx));
            }
        );
        ctx.get().setPacketHandled(true);
    }

    public CompoundNBT getNbt() {
        return nbt;
    }

    public int getContainerId() {
        return containerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EssenceSyncPacket that = (EssenceSyncPacket) o;
        return containerId == that.containerId &&
                Objects.equals(nbt, that.nbt);
    }
}
