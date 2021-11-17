package com.example.examplemod.event;

import com.example.examplemod.capability.climbing.ClimbingHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityJumpHandler {
    @CapabilityInject(ClimbingHandler.class)
    private static Capability<ClimbingHandler> CLIMBING_HANDLER_CAPABILITY = null;

    @SubscribeEvent
    public static void handleEntityJump(LivingEvent.LivingJumpEvent evt) {
        if (evt.getEntity() instanceof PlayerEntity) {
            // handle
        }
    }

    @SubscribeEvent
    public static void handleEntityFall(LivingFallEvent evt) {
        if (evt.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)evt.getEntity();
            // reset number of jumps made by player on climbing capability
            LazyOptional<ClimbingHandler> climbingCapability = player.getCapability(CLIMBING_HANDLER_CAPABILITY);
            climbingCapability.ifPresent(cap -> cap.resetJumps());
        }
    }
}
