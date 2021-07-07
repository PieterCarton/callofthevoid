package com.example.examplemod.essence;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class IEssenceStorageTest {
    @Test
    public void addEssenceValidType() {
        EssenceQuantity quantity = new EssenceQuantity(EssenceType.ORDER, 10);
        IEssenceStorage comp = createEssenceStorage(EssenceType.ORDER, 100);

        quantity = comp.receiveEssence(quantity);

        assertEquals(10, comp.essenceStored(EssenceType.ORDER));
        assertEquals(0, quantity.getQuantity());
    }

    @Test
    public void addEssenceInvalidType() {
        EssenceQuantity quantity = new EssenceQuantity(EssenceType.ORDER, 10);
        IEssenceStorage comp = createEssenceStorage(EssenceType.ENTROPY, 100);

        quantity = comp.receiveEssence(quantity);

        assertEquals(0, comp.essenceStored(EssenceType.ORDER));
        assertEquals(10, quantity.getQuantity());
    }

    @Test
    public void addEssenceNotEnoughCapacity() {
        EssenceQuantity quantity = new EssenceQuantity(EssenceType.ORDER, 110);
        IEssenceStorage comp = createEssenceStorage(EssenceType.ORDER, 100);

        quantity = comp.receiveEssence(quantity);

        assertEquals(100, comp.essenceStored(EssenceType.ORDER));
        assertEquals(10, quantity.getQuantity());
    }

    @Test
    public void removeEssenceValidType() {
        EssenceQuantity quantity = new EssenceQuantity(EssenceType.ORDER, 5);
        IEssenceStorage comp = createEssenceStorage(EssenceType.ORDER, 100);

        comp.receiveEssence(new EssenceQuantity(EssenceType.ORDER, 10));
        quantity = comp.extractEssence(quantity);

        assertEquals(5, comp.essenceStored(EssenceType.ORDER));
        assertEquals(5, quantity.getQuantity());
    }

    @Test
    public void removeEssenceInvalidType() {
        EssenceQuantity quantity = new EssenceQuantity(EssenceType.ORDER, 10);
        IEssenceStorage comp = createEssenceStorage(EssenceType.ENTROPY, 100);

        comp.receiveEssence(new EssenceQuantity(EssenceType.ENTROPY, 55));
        quantity = comp.extractEssence(quantity);

        assertEquals(55, comp.essenceStored(EssenceType.ENTROPY));
        assertEquals(0, quantity.getQuantity());
    }

    @Test
    public void removeEssenceNotEnoughStored() {
        EssenceQuantity quantity = new EssenceQuantity(EssenceType.ORDER, 110);
        IEssenceStorage comp = createEssenceStorage(EssenceType.ORDER, 100);

        comp.receiveEssence(new EssenceQuantity(EssenceType.ORDER, 100));
        quantity = comp.extractEssence(quantity);

        assertEquals(0, comp.essenceStored(EssenceType.ORDER));
        assertEquals(100, quantity.getQuantity());
    }

    public abstract IEssenceStorage createEssenceStorage(EssenceType type, int capacity);
}
