package com.example.examplemod.block;


import com.example.examplemod.setup.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class RopeBlock extends Block {

    private static final Property<Boolean> attached = BooleanProperty.create("attached");
    private static final Property<Boolean> top = BooleanProperty.create("top");

    private static final VoxelShape SHAPE_LONG = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape SHAPE_SHORT = Block.makeCuboidShape(6.0D, 3.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public RopeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(attached, top);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos above = pos.up();
        BlockState aboveState = worldIn.getBlockState(above);
        return aboveState.getMaterial().blocksMovement() || aboveState.isIn(ModBlocks.ROPE_BLOCK.get());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        boolean isAttached = hasRopeBelow(world, pos);
        boolean isTop = !world.getBlockState(pos.up()).isIn(ModBlocks.ROPE_BLOCK.get());

        return this.getDefaultState().with(attached, isAttached).with(top, isTop);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            boolean ropeBelow = facingState.isIn(ModBlocks.ROPE_BLOCK.get());
            if (!stateIn.get(attached) && ropeBelow) {
                return stateIn.with(attached, true);
            }
            if (stateIn.get(attached) && !ropeBelow) {
                return stateIn.with(attached, false);
            }
        } else if (facing == Direction.UP) {
            if (!worldIn.isRemote()) {
                worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
            }
        }

        return stateIn;
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!isValidPosition(state, worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    private boolean hasRopeBelow(World world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockState stateBelow = world.getBlockState(below);

        return stateBelow.isIn(ModBlocks.ROPE_BLOCK.get());
    }



    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (!state.get(attached)) {
            return SHAPE_SHORT;
        }
        return SHAPE_LONG;
    }
}
