package com.example.examplemod.capability.climbing;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    public void readFromNBT(CompoundNBT nbt) {
        jumps = nbt.getInt("Jumps");
        stableHeight = nbt.getDouble("Stable Height");
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        System.out.println("Writing Capability To nbt");
        System.out.println(jumps);
        System.out.println(stableHeight);
        nbt.putInt("Jumps", jumps);
        nbt.putDouble("Stable Height", stableHeight);
        System.out.println(nbt.getInt("Jumps"));
        System.out.println(nbt.getDouble("Stable Height"));
        return nbt;
    }
}
