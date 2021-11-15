package com.example.examplemod.capability;

import com.example.examplemod.capability.climbing.CapabilityClimbing;
import com.example.examplemod.capability.climbing.ClimbingHandler;
import com.example.examplemod.network.ModPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDispatcher;
import net.minecraftforge.fml.network.PacketDistributor;

public class ModCapabilities {
    public static void register() {
        CapabilityClimbing.register();
    }

    @SubscribeEvent
    public static void syncCapabilities(EntityJoinWorldEvent evt) {
        // when on server, sync capabilities to player
        if (!evt.getWorld().isRemote && evt.getEntity() instanceof PlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)evt.getEntity();
            LazyOptional<ClimbingHandler> cap = player.getCapability(CapabilityClimbing.CLIMBING_HANDLER_CAPABILITY,null);
            cap.ifPresent(instance -> instance.sendPacketToPlayer(player));
        }
    }
}
