package com.example.examplemod.capability.climbing;

import com.example.examplemod.CallOfTheVoidMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class CapabilityClimbing  {
    @CapabilityInject(ClimbingHandler.class)
    public static Capability<ClimbingHandler> CLIMBING_HANDLER_CAPABILITY = null;

    public static final ResourceLocation CLIMBING_HANDLER_CAPABILITY_NAME = new ResourceLocation(CallOfTheVoidMod.MOD_ID, "climbing_handler");

    public static void register() {
        System.out.println("Capability Registered!");
        CapabilityManager.INSTANCE.register(ClimbingHandler.class, new ClimbingStorageHandler(), () -> new ClimbingHandler());
    }

    private static class ClimbingStorageHandler implements Capability.IStorage<ClimbingHandler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ClimbingHandler> capability, ClimbingHandler instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            System.out.println("capability saved");
            instance.writeToNBT(nbt);

            return nbt;
        }

        @Override
        public void readNBT(Capability<ClimbingHandler> capability, ClimbingHandler instance, Direction side, INBT nbt) {
            if (!(nbt instanceof CompoundNBT))
                throw new RuntimeException("Cannot read NBT to serialize ClimingHandler - INBT must be of type CompoundNBT");
            System.out.println("capability read");
            instance.readFromNBT((CompoundNBT)nbt);
        }
    }
}
