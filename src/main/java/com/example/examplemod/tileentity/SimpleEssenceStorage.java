package com.example.examplemod.tileentity;

import com.example.examplemod.essence.EssenceQuantity;
import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.essence.IEssenceStorage;
import com.example.examplemod.essence.IPushStorage;
import com.example.examplemod.setup.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SimpleEssenceStorage extends TileEntity implements IEssenceStorage {
    protected EssenceType essenceType;
    private int essence = 0;
    protected int maxEssence = 0;
    protected int maxOutput;
    protected int maxInput;

    protected final IIntArray storageData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return essence;
                case 1:
                    return maxEssence;
                case 2:
                    return maxInput;
                case 3:
                    return maxOutput;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    essence = value;
                    break;
                case 1:
                    maxEssence = value;
                    break;
                case 2:
                    maxInput = value;
                    break;
                case 3:
                    maxOutput = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        int nbtCharge = 0;
        if(nbt.contains("essence")){
            storageData.set(0, nbt.getInt("essence"));
            storageData.set(1, nbt.getInt("maxEssence"));
            storageData.set(2, nbt.getInt("maxInput"));
            storageData.set(3, nbt.getInt("maxOutput"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("essence", storageData.get(0));
        compound.putInt("maxEssence", storageData.get(1));
        compound.putInt("maxInput", storageData.get(2));
        compound.putInt("maxOutput", storageData.get(3));
        return compound;
    }

    public SimpleEssenceStorage(TileEntityType<?> tileEntityTypeIn) {
        this(tileEntityTypeIn, EssenceType.ORDER, 0, 0, 0, 0);
    }

    public SimpleEssenceStorage(TileEntityType<?> tileEntityTypeIn, EssenceType essenceType, int maxEssence) {
        this(tileEntityTypeIn, essenceType, 0, maxEssence, -1, -1);
    }

    public SimpleEssenceStorage(TileEntityType<?> tileEntityTypeIn, EssenceType essenceType, int essence, int maxEssence, int maxInput) {
        this(tileEntityTypeIn, essenceType, essence, maxEssence, maxInput, maxInput);
    }

    public SimpleEssenceStorage(TileEntityType<?> tileEntityTypeIn, EssenceType essenceType, int essence, int maxEssence, int maxOutput, int maxInput) {
        super(tileEntityTypeIn);
        this.essenceType = essenceType;
        this.essence = essence;
        this.maxEssence = maxEssence;
        this.maxOutput = maxOutput;
        this.maxInput = maxInput;
    }

    @Override
    public EssenceQuantity receiveEssence(EssenceQuantity quantity) {
        if (this.essenceType != quantity.getType()) {
            return new EssenceQuantity(quantity.getType(), 0);
        }

        int maxImportable = Math.min(maxEssence - essence, maxInput);
        int actualImport = Math.min(quantity.getQuantity(), maxImportable);

        essence += actualImport;
        return new EssenceQuantity(quantity.getType(), quantity.getQuantity() - actualImport);
    }

    @Override
    public EssenceQuantity extractEssence(EssenceQuantity quantity) {
        if (this.essenceType != quantity.getType()) {
            return new EssenceQuantity(quantity.getType(), 0);
        }

        int maxExportable = Math.min(essence, maxOutput);
        int actualExport = Math.min(quantity.getQuantity(), maxExportable);

        essence -= actualExport;
        return new EssenceQuantity(quantity.getType(), actualExport);
    }

    @Override
    public int essenceStored(EssenceType type) {
        if (this.essenceType == type) {
            return essence;
        }
        return 0;
    }

    @Override
    public int maxEssenceStored(EssenceType type) {
        if (this.essenceType == type) {
            return maxEssence;
        }
        return 0;
    }

    @Override
    public boolean canReceiveEssence(EssenceType type) {
        return this.essenceType == type;
    }

    @Override
    public boolean canExtractEssence(EssenceType type) {
        return this.essenceType == type;
    }

    public static IEssenceStorage getEssenceStorageAtPosition(World worldIn, double x, double y, double z) {
        IEssenceStorage essenceStorage = null;
        BlockPos pos = new BlockPos(x, y, z);
        BlockState blockstate = worldIn.getBlockState(pos);
        Block block = blockstate.getBlock();
        if (blockstate.hasTileEntity()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof IEssenceStorage) {
                essenceStorage = (IEssenceStorage) tileEntity;
            }
        }
        return essenceStorage;
    }

    public static List<IEssenceStorage> getAllAdjacentEssenceStorage(World worldIn, BlockPos pos) {
        List<BlockPos> adjacentPositions = Arrays.asList(pos.north(),
                pos.east(), pos.south(), pos.west(), pos.down(), pos.up());

        List<IEssenceStorage> adjacentEssenceStorage = new LinkedList<>();

        for (BlockPos p: adjacentPositions) {
            IEssenceStorage result = getEssenceStorageAtPosition(worldIn, p.getX(), p.getY(), p.getZ());
            if(result != null) {
                adjacentEssenceStorage.add(result);
            }
        }

        return adjacentEssenceStorage;
    }
}
