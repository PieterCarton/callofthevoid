package com.example.examplemod.capability.climbing;

import com.example.examplemod.network.ClimbingCapabilitySyncPacket;
import com.example.examplemod.network.ModPacketHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class ClimbingHandler {
    private double stableHeight = Double.MAX_VALUE;
    private int jumps = 0;

    public ClimbingHandler() {}

    public double getStableHeight() {
        return stableHeight;
    }

    public void setStableHeight(double stableHeight) {
        this.stableHeight = stableHeight;
    }

    public int getJumps() {
        return jumps;
    }

    /**
     * Increase jumps made by player while climbing by one
     */
    public void incJumps() {
        this.jumps++;
    }

    /**
     * Set number of jumps to 0
     */
    public void resetJumps() {this.jumps = 0;};

    public void readFromNBT(CompoundNBT nbt) {
        jumps = nbt.getInt("Jumps");
        stableHeight = nbt.getDouble("Stable Height");
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        nbt.putInt("Jumps", jumps);
        nbt.putDouble("Stable Height", stableHeight);
        return nbt;
    }

    public void sendPacketToPlayer(ServerPlayerEntity player) {
        ClimbingCapabilitySyncPacket packet = new ClimbingCapabilitySyncPacket(writeToNBT(new CompoundNBT()));
        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
