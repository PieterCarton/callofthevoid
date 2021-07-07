package com.example.examplemod.inventory;

import com.example.examplemod.essence.EssenceCompartment;
import com.example.examplemod.essence.IEssenceStorage;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.List;

public abstract class EssenceStorageContainer extends Container {

    protected EssenceStorageContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    @OnlyIn(Dist.CLIENT)
    public int getEssenceFillScaled(){
        int i = 5;
        int j = 10;
        return 31 * i / j;
    }
}
