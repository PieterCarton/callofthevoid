package com.example.examplemod.tileentity;

import com.example.examplemod.essence.EssenceQuantity;
import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.essence.IEssenceStorage;
import com.example.examplemod.essence.IPushStorage;
import com.example.examplemod.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BatteryBlockTileEntity extends SimpleEssenceStorage implements IPushStorage, ITickableTileEntity {

    public BatteryBlockTileEntity() {
        super(ModTileEntityTypes.BATTERY.get());
    }

    public BatteryBlockTileEntity(EssenceType type, int maxEssence, int maxOutput) {
        super(ModTileEntityTypes.BATTERY.get(), type, 0, maxEssence, maxOutput);
    }

    public BatteryBlockTileEntity(TileEntityType<BannerTileEntity> tileEntityType, EssenceType essenceType, int essence, int maxEssence, int maxOutput) {
        super(tileEntityType, essenceType, essence, maxEssence, maxOutput);
    }

    public void onCharge() {
        storageData.set(0, storageData.get(0) + 1000);
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
        List<IEssenceStorage> adjacent = getAllAdjacentEssenceStorage(this.getWorld(), this.pos);
        for (IEssenceStorage s: adjacent) {
            if (quantity.getQuantity() == 0) {
                break;
            }
            quantity = s.receiveEssence(quantity);
        }
        return quantity;
    }
}
