package com.example.examplemod.block;

import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.tileentity.BatteryBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BatteryBlock extends Block {

    private EssenceType type;
    private int maxEssence;
    private int maxOutput;

    public BatteryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BatteryBlockTileEntity(EssenceType.ORDER, 1000, 10);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        BatteryBlockTileEntity te = (BatteryBlockTileEntity)worldIn.getTileEntity(pos);
        if (te != null) {
            te.onCharge();
        }
    }

    /*
    private int getChargeFromTag() {
        return 0;
    }
    *
     */
}
