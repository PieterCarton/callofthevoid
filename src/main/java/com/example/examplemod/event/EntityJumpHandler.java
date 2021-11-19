package com.example.examplemod.event;

import com.example.examplemod.capability.climbing.ClimbingHandler;
import com.example.examplemod.item.ClimbingPickItem;
import com.example.examplemod.setup.ModItems;
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
            ClimbingPickItem climbingPick = (ClimbingPickItem) ModItems.CLIMBING_PICK_ITEM.get();
            climbingPick.onLand(player);
        }
    }
}
