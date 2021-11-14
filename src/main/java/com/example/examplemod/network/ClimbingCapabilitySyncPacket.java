package com.example.examplemod.network;

import com.example.examplemod.capability.climbing.ClimbingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClimbingCapabilitySyncPacket extends BidirectionalPacket{
    @CapabilityInject(ClimbingHandler.class)
    private static Capability<ClimbingHandler> CLIMBING_HANDLER_CAPABILITY = null;

    private CompoundNBT nbt;

    public ClimbingCapabilitySyncPacket(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public ClimbingCapabilitySyncPacket(PacketBuffer buffer) {
        this.nbt = buffer.readCompoundTag();
    }

    @Override
    public void handleClientside(NetworkEvent.Context ctx) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        LazyOptional<ClimbingHandler> capability = player.getCapability(CLIMBING_HANDLER_CAPABILITY, null);
        capability.ifPresent(cap -> cap.readFromNBT(nbt));
    }

    @Override
    public void handleServerside(NetworkEvent.Context ctx) {
        ServerPlayerEntity player = ctx.getSender();
        LazyOptional<ClimbingHandler> capability = player.getCapability(CLIMBING_HANDLER_CAPABILITY, null);
        capability.ifPresent(cap -> cap.readFromNBT(nbt));
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeCompoundTag(nbt);
    }
}
