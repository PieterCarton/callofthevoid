package com.example.examplemod.essence;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.registries.ForgeRegistries;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiEssenceStorageTest extends IEssenceStorageTest{

    @Override
    public IEssenceStorage createEssenceStorage(EssenceType type, int capacity) {
        MultiEssenceStorage storage = new MultiEssenceStorage(Integer.MAX_VALUE, Integer.MAX_VALUE);
        storage.addCompartment(type, capacity);
        return storage;
    }

    @Test
    public void writeTest() {
        MultiEssenceStorage storage = new MultiEssenceStorage(-1, -1);
        storage.addCompartment(EssenceType.ENTROPY, 1000);
        storage.addCompartment(EssenceType.ORDER, 1000);
        CompoundNBT compound = storage.write(new CompoundNBT());
        assertTrue(1000 > compound.toString().length());
    }

    @Test
    public void readTest() {
        MultiEssenceStorage storage = new MultiEssenceStorage(-1, -1);
        storage.addCompartment(EssenceType.ENTROPY, 1000);
        storage.addCompartment(EssenceType.ORDER, 1000);
        CompoundNBT compound = storage.write(new CompoundNBT());

        MultiEssenceStorage readStorage = new MultiEssenceStorage(0, 0);
        readStorage.read(compound);
        assertEquals(readStorage, storage);
    }

}