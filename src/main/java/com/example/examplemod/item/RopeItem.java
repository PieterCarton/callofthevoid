package com.example.examplemod.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RopeItem extends BlockItem {
    public RopeItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Nullable
    @Override
    public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context) {
        if (context.hasSecondaryUseForPlayer()) {
            return context;
        }

        Direction faceDirection = context.getFace().getOpposite();
        BlockPos.Mutable mutable = context.getPos().toMutable().move(faceDirection);
        World world = context.getWorld();
        Block ropeBlock = this.getBlock();
        BlockState blockState = world.getBlockState(mutable);

        if (blockState.isIn(ropeBlock)) {
            mutable.move(Direction.DOWN);
            while (world.getBlockState(mutable).isIn(ropeBlock)) {
                mutable.move(Direction.DOWN);
            }

            if (World.isValid(mutable) && world.getBlockState(mutable).isReplaceable(context)) {
                return BlockItemUseContext.func_221536_a(context, mutable, Direction.UP);
            }
            return null;
        }
        return context;
    }
}
