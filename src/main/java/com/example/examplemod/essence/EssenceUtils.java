package com.example.examplemod.essence;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class EssenceUtils {

    public static void writeEssenceCompartments(CompoundNBT compound, Collection<EssenceCompartment> compartments) {
        //save all compartments to NBT
        ListNBT listNBT = new ListNBT();

        for (EssenceCompartment compartment: compartments) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compartment.write(compoundNBT);
            listNBT.add(compoundNBT);
        }
        compound.put("compartments", listNBT);
    }

    public static Map<EssenceType, EssenceCompartment> readEssenceCompartments(CompoundNBT compound) {
        ListNBT listNBT = (ListNBT) compound.get("compartments");
        Map<EssenceType, EssenceCompartment> compartmentMap = new HashMap<>();

        if (listNBT == null) {
            return compartmentMap;
        }

        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT compoundNBT = listNBT.getCompound(i);
            String typeString = compoundNBT.getString("essenceType");
            EssenceType essenceType = EssenceType.valueOf(typeString);
            EssenceCompartment compartment = readEssenceCompartment(compoundNBT);
            compartmentMap.put(essenceType, compartment);
        }

        return compartmentMap;
    }

    private static EssenceCompartment readEssenceCompartment(CompoundNBT compoundNBT) {
        int compartmentType = compoundNBT.getInt("compartmentType");
        switch (compartmentType) {
            case 0:
                return ReservedCompartment.read(compoundNBT);
            default:
                return new ReservedCompartment(EssenceType.ORDER, 0);
        }

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
