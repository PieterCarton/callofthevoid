package com.example.examplemod.essence;

import net.minecraft.nbt.CompoundNBT;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReservedCompartmentTest extends IEssenceStorageTest{


    @Test
    public void equalsTestEmpty() {
        ReservedCompartment comp1 = new ReservedCompartment(EssenceType.ORDER, 100);
        ReservedCompartment comp2 = new ReservedCompartment(EssenceType.ORDER, 100);

        assertEquals(comp1, comp2);
    }

    @Test
    public void equalsTestFilled() {
        ReservedCompartment comp1 = new ReservedCompartment(EssenceType.ORDER, 100);
        comp1.receiveEssence(new EssenceQuantity(EssenceType.ORDER, 40));
        ReservedCompartment comp2 = new ReservedCompartment(EssenceType.ORDER, 100);
        comp2.receiveEssence(new EssenceQuantity(EssenceType.ORDER, 40));

        assertEquals(comp1, comp2);
    }

    @Test
    public void notEqualsTest1() {
        ReservedCompartment comp1 = new ReservedCompartment(EssenceType.ORDER, 100);
        comp1.receiveEssence(new EssenceQuantity(EssenceType.ORDER, 50));
        ReservedCompartment comp2 = new ReservedCompartment(EssenceType.ORDER, 100);
        comp2.receiveEssence(new EssenceQuantity(EssenceType.ORDER, 40));

        assertNotEquals(comp1, comp2);
    }

    @Test
    public void notEqualsTest2() {
        ReservedCompartment comp1 = new ReservedCompartment(EssenceType.ORDER, 100);
        ReservedCompartment comp2 = new ReservedCompartment(EssenceType.ENTROPY, 100);

        assertNotEquals(comp1, comp2);
    }

    @Test
    public void writeAndReadEmpty() {
        ReservedCompartment comp = new ReservedCompartment(EssenceType.ORDER, 100);
        CompoundNBT nbt = comp.write(new CompoundNBT());
        ReservedCompartment readCompartment = ReservedCompartment.read(nbt);

        assertEquals(comp, readCompartment);
    }

    @Test
    public void writeAndReadFilled() {
        ReservedCompartment comp = new ReservedCompartment(EssenceType.ORDER, 100);
        comp.receiveEssence(new EssenceQuantity(EssenceType.ORDER, 50));
        CompoundNBT nbt = comp.write(new CompoundNBT());
        ReservedCompartment readCompartment = ReservedCompartment.read(nbt);

        assertEquals(comp, readCompartment);
    }

    @Override
    public IEssenceStorage createEssenceStorage(EssenceType type, int capacity) {
        return new ReservedCompartment(type, capacity);
    }
}