package com.example.examplemod.network;

import com.example.examplemod.essence.MultiEssenceStorage;
import com.example.examplemod.inventory.EssenceStorageContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * For client-side handling of packets
 */
public class EssenceSyncPacketHandler {
    public static void handle(EssenceSyncPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        Container container = Minecraft.getInstance().player.openContainer;
        if (container instanceof EssenceStorageContainer) {
            EssenceStorageContainer essenceContainer = (EssenceStorageContainer)container;
            //check if correct container is opened
            if (essenceContainer.windowId == pkt.getContainerId()) {
                essenceContainer.updateEssenceStorage(pkt.getNbt());
            }
        }
    }
}
