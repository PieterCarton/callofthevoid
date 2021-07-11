package com.example.examplemod.inventory;

import com.example.examplemod.essence.*;
import com.example.examplemod.network.EssenceSyncPacket;
import com.example.examplemod.network.EssenceSyncPacketHandler;
import com.example.examplemod.network.ModPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class EssenceStorageContainer extends Container{

    private MultiEssenceStorage storage;
    private ServerPlayerEntity playerEntity;

    protected EssenceStorageContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, MultiEssenceStorage storage) {
        super(type, id);
        this.storage = storage;
        if (!playerInventory.player.world.isRemote()) {
            if (!(playerInventory.player instanceof ServerPlayerEntity)) {
                throw new IllegalArgumentException("Couldn't create container, ServerPlayerEntity needed");
            }
            this.playerEntity = (ServerPlayerEntity) playerInventory.player;
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        EssenceSyncPacket pkt = new EssenceSyncPacket(storage.write(new CompoundNBT()), this.windowId);
        ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> playerEntity),
                pkt);
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getEssenceFillScaled(EssenceType type){
        int i = essenceStored(type);
        int j = maxEssenceStored(type);
        return 31 * i / j;
    }

    @OnlyIn(Dist.CLIENT)
    public int essenceStored(EssenceType type) {
        return storage.essenceStored(type);
    }

    @OnlyIn(Dist.CLIENT)
    public int maxEssenceStored(EssenceType type) {
        return storage.maxEssenceStored(type);
    }

    public void updateEssenceStorage(CompoundNBT nbt) {
        System.out.println(nbt.toString());
        storage.read(nbt);
    }
}
