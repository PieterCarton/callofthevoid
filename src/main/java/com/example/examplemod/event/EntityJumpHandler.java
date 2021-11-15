package com.example.examplemod.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityJumpHandler {

    @SubscribeEvent
    public static void handleEntityJump(LivingEvent.LivingJumpEvent evt) {
        if (evt.getEntity() instanceof PlayerEntity) {
            System.out.println("evt triggered");
            System.out.println(evt.getEntity().world.isRemote);
        }
    }
}
