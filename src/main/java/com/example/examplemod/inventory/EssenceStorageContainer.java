package com.example.examplemod.inventory;

import com.example.examplemod.essence.EssenceCompartment;
import com.example.examplemod.essence.IEssenceStorage;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import java.util.LinkedList;
import java.util.List;

public abstract class EssenceStorageContainer extends Container {

    protected EssenceStorageContainer(ContainerType<?> type, int id) {
        super(type, id);
    }
}
