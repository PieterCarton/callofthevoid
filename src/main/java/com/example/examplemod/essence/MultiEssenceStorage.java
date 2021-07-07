package com.example.examplemod.essence;

import net.minecraft.nbt.CompoundNBT;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MultiEssenceStorage implements IEssenceStorage {

    private int maxInput;
    private int maxOutput;

    private Map<EssenceType, EssenceCompartment> compartments = new HashMap<>();

    public MultiEssenceStorage(int maxInput, int maxOutput) {
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
    }

    public void addCompartment(EssenceType type, int capacity) {
        if (compartments.containsKey(type)) {
            return;
        }

        compartments.put(type, new ReservedCompartment(type, capacity));
    }

    public void read(CompoundNBT nbt) {
        maxInput = nbt.getByte("maxInput");
        maxOutput = nbt.getByte("maxOutput");
        compartments = EssenceUtils.readEssenceCompartments(nbt);
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("maxInput", maxInput);
        compound.putInt("maxOutput", maxOutput);
        EssenceUtils.writeEssenceCompartments(compound, compartments.values());
        return compound;
    }

    @Override
    public EssenceQuantity receiveEssence(EssenceQuantity quantity) {
        EssenceType type = quantity.getType();
        if (!compartments.containsKey(type)) {
            return quantity;
        }

        return compartments.get(type).receiveEssence(quantity);
    }

    @Override
    public EssenceQuantity extractEssence(EssenceQuantity quantity) {
        EssenceType type = quantity.getType();
        if (!compartments.containsKey(type)) {
            return new EssenceQuantity(type, 0);
        }

        return compartments.get(type).extractEssence(quantity);
    }

    @Override
    public int essenceStored(EssenceType type) {
        if (!compartments.containsKey(type)) {
            return 0;
        }

        return compartments.get(type).essenceStored(type);
    }

    @Override
    public int maxEssenceStored(EssenceType type) {
        if (!compartments.containsKey(type)) {
            return 0;
        }

        return compartments.get(type).maxEssenceStored(type);
    }

    @Override
    public boolean canReceiveEssence(EssenceType type) {
        if (!compartments.containsKey(type)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canExtractEssence(EssenceType type) {
        if (!compartments.containsKey(type)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiEssenceStorage that = (MultiEssenceStorage) o;
        return maxInput == that.maxInput &&
                maxOutput == that.maxOutput &&
                Objects.equals(compartments, that.compartments);
    }
}
