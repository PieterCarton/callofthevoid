package com.example.examplemod.event;

import com.example.examplemod.capability.climbing.CapabilityClimbing;
import com.example.examplemod.capability.climbing.ClimbingHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityEventHandler {
    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt) {
        if (evt.getObject() instanceof PlayerEntity) {
            System.out.println("Instance of cap attached to player");
            ClimbingHandler instance = new ClimbingHandler();
            evt.addCapability(CapabilityClimbing.CLIMBING_HANDLER_CAPABILITY_NAME,
                    new ICapabilitySerializable<CompoundNBT>()  {
                        @Override
                        public CompoundNBT serializeNBT() {
                            CompoundNBT nbt = new CompoundNBT();
                            System.out.println("capability saved");
                            instance.writeToNBT(nbt);

                            return nbt;
                        }

                        @Override
                        public void deserializeNBT(CompoundNBT nbt) {
                            System.out.println("capability read");
                            System.out.println(nbt.getInt("Jumps"));
                            instance.readFromNBT(nbt);
                        }


                        @Nonnull
                        @Override
                        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                            if (cap == CapabilityClimbing.CLIMBING_HANDLER_CAPABILITY)
                                return LazyOptional.of(() -> instance).cast();
                            return LazyOptional.empty();
                        }
                    });
            // add listener for invalidation
        }
    }
}
