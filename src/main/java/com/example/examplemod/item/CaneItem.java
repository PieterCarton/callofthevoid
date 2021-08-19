package com.example.examplemod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CaneItem extends Item {
    private static final AttributeModifier WALK_SPEED_BOOST = new AttributeModifier("Cane speed boost", 0.018, AttributeModifier.Operation.ADDITION);

    public CaneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (worldIn.isRemote) {
            return;
        }

        if (entityIn instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) entityIn;
            ModifiableAttributeInstance attributeInstance = entity.getAttribute(Attributes.MOVEMENT_SPEED);

            //remove modifier if necessary
            if (attributeInstance.hasModifier(WALK_SPEED_BOOST) && (!isHoldingWalkingCane(entityIn) || entity.isSprinting() || entity.isCrouching())) {
                attributeInstance.removeModifier(WALK_SPEED_BOOST);
                return;
            }

            //otherwise, add modifier if not yet present
            if (isSelected && !attributeInstance.hasModifier(WALK_SPEED_BOOST)) {
                //server or client side?
                attributeInstance.applyNonPersistentModifier(WALK_SPEED_BOOST);
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
        if (player.world.isRemote) {
            return true;
        }

        ModifiableAttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance.hasModifier(WALK_SPEED_BOOST)) {
            attributeInstance.removeModifier(WALK_SPEED_BOOST);
        }
        return true;
    }



    private static boolean isHoldingWalkingCane(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            Item mainHandItem = livingEntity.getHeldItem(Hand.MAIN_HAND).getItem();
            Item offHandItem = livingEntity.getHeldItem(Hand.OFF_HAND).getItem();
            return offHandItem instanceof CaneItem || mainHandItem instanceof CaneItem;
        }
        return false;
    }

}
