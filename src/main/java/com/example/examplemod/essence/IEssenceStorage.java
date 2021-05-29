package com.example.examplemod.essence;

public interface IEssenceStorage {

    public EssenceQuantity receiveEssence(EssenceQuantity quantity);
    public EssenceQuantity extractEssence(EssenceQuantity quantity);

    public int essenceStored(EssenceType type);
    public int maxEssenceStored(EssenceType type);

    public boolean canReceiveEssence(EssenceType type);
    public boolean canExtractEssence(EssenceType type);
}
