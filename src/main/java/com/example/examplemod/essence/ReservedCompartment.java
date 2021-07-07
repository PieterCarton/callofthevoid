package com.example.examplemod.essence;

import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;

public class ReservedCompartment extends EssenceCompartment{

    private int stored = 0;

    public ReservedCompartment(EssenceType type, int capacity) {
        this(type, capacity, 0);
    }

    private ReservedCompartment(EssenceType type, int capacity, int stored) {
        this.type = type;
        this.capacity = capacity;
        this.stored = stored;
    }

    @Override
    public EssenceQuantity receiveEssence(EssenceQuantity quantity) {
        EssenceType receivedType = quantity.getType();

        if (!canReceiveEssence(receivedType)) {
            return quantity;
        }

        int receivable = Math.min(capacity - stored, quantity.getQuantity());
        int nonReceivable = quantity.getQuantity() - receivable;
        stored += receivable;
        return new EssenceQuantity(receivedType, nonReceivable);
    }

    @Override
    public EssenceQuantity extractEssence(EssenceQuantity quantity) {
        EssenceType extractedType = quantity.getType();

        if (!canExtractEssence(extractedType)) {
            return new EssenceQuantity(EssenceType.ORDER, 0);
        }

        int extractable = Math.min(stored, quantity.getQuantity());
        stored -= extractable;
        return new EssenceQuantity(EssenceType.ORDER, extractable);
    }

    @Override
    public int essenceStored(EssenceType type) {
        if (this.type != type) {
            return 0;
        }
        return stored;
    }

    @Override
    public int maxEssenceStored(EssenceType type) {
        if (this.type != type) {
            return 0;
        }
        return capacity;
    }

    @Override
    public boolean canReceiveEssence(EssenceType type) {
        return this.type == type;
    }

    @Override
    public boolean canExtractEssence(EssenceType type) {
        return this.type == type;
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        compoundNBT.putInt("stored", stored);
        compoundNBT.putInt("compartmentType", 0);
        return super.write(compoundNBT);
    }

    public static ReservedCompartment read(CompoundNBT compoundNBT) {
        EssenceType essenceType = EssenceType.valueOf(compoundNBT.getString("essenceType"));
        int capacity = compoundNBT.getInt("capacity");
        int stored = compoundNBT.getInt("stored");
        return new ReservedCompartment(essenceType, capacity, stored);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ReservedCompartment)) return false;
        ReservedCompartment that = (ReservedCompartment) o;
        return stored == that.stored && super.equals(o);
    }
}
