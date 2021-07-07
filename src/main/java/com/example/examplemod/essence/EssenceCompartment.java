package com.example.examplemod.essence;

import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;

public abstract class EssenceCompartment implements IEssenceStorage{
    protected EssenceType type;
    protected int capacity;

    public CompoundNBT write(CompoundNBT compoundNBT) {
        compoundNBT.putString("essenceType", this.type.name());
        compoundNBT.putInt("capacity", capacity);
        return  compoundNBT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof EssenceCompartment)) return false;
        EssenceCompartment that = (EssenceCompartment) o;
        return capacity == that.capacity &&
                type == that.type;
    }
}
