package com.example.examplemod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;

public class CaneItem extends Item {
    public CaneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!isSelected || worldIn.isRemote) {
            return;
        }

        if (entityIn.isLiving() ) {
            LivingEntity entity = (LivingEntity) entityIn;
            if (entity.isInWater() && !entity.isActualySwimming()) {
                entity.addPotionEffect(new EffectInstance(Effects.SPEED, 1, 2));
            }
        }
    }
}
