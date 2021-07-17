package com.example.examplemod.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Set;

public class PipeWrenchItem extends ToolItem {
    public PipeWrenchItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        //check for critical hit
        if (!attacker.world.isRemote() && isFalling(attacker)) {
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 40, 1));
        }
        return super.hitEntity(stack, target, attacker);
    }

    private boolean isFalling(LivingEntity entity) {
        double y = entity.getPositionVec().y;
        double offset = y - Math.floor(y);

        BlockPos entityPos = entity.getPosition().add(0, -1, 0);
        BlockState blockState = entity.world.getBlockState(entityPos);

        boolean blockUnderEntity = blockState.equals(Blocks.AIR.getDefaultState());

        return (offset > 0.01D || blockUnderEntity) && entity.getMotion().y < 0;
    }
}
