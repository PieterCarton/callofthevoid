package com.example.examplemod.tileentity;

import com.example.examplemod.essence.*;
import com.example.examplemod.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.List;

public class BatteryBlockTileEntity extends TileEntity implements IPushStorage, ITickableTileEntity {

    private EssenceType essenceType;
    private MultiEssenceStorage storage;

    public BatteryBlockTileEntity(TileEntityType<?> type) {
        super(type);
        storage = new MultiEssenceStorage(0,0);
    }

    public BatteryBlockTileEntity() {
        this(EssenceType.ORDER, 0, 0);
    }

    public BatteryBlockTileEntity(EssenceType type, int maxEssence, int maxOutput) {
        super(ModTileEntityTypes.BATTERY.get());
        essenceType = type;
        storage = new MultiEssenceStorage(maxOutput, maxOutput);
        storage.addCompartment(type, maxEssence);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        storage.read(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        storage.write(compound);
        compound.putInt("charge", essenceStored(essenceType));
        return super.write(compound);
    }

    public void onCharge() {
        this.receiveEssence(new EssenceQuantity(essenceType, 1));
    }

    public void tick() {
        if (this.getWorld().isRemote) {
            return;
        }

        EssenceQuantity available = extractEssence(new EssenceQuantity(this.essenceType, Integer.MAX_VALUE));
        EssenceQuantity leftOver = push(available);
        receiveEssence(leftOver);
    }

    @Override
    public EssenceQuantity push(EssenceQuantity quantity) {
        List<IEssenceStorage> adjacent = EssenceUtils.getAllAdjacentEssenceStorage(this.getWorld(), this.pos);
        for (IEssenceStorage s: adjacent) {
            if (quantity.getQuantity() == 0) {
                break;
            }
            quantity = s.receiveEssence(quantity);
        }
        return quantity;
    }

    @Override
    public EssenceQuantity receiveEssence(EssenceQuantity quantity) {
        return storage.receiveEssence(quantity);
    }

    @Override
    public EssenceQuantity extractEssence(EssenceQuantity quantity) {
        return storage.extractEssence(quantity);
    }

    @Override
    public int essenceStored(EssenceType type) {
        return storage.essenceStored(type);
    }

    @Override
    public int maxEssenceStored(EssenceType type) {
        return storage.maxEssenceStored(type);
    }

    @Override
    public boolean canReceiveEssence(EssenceType type) {
        return storage.canReceiveEssence(type);
    }

    @Override
    public boolean canExtractEssence(EssenceType type) {
        return storage.canExtractEssence(type);
    }
}
