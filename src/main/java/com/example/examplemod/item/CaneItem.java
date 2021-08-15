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
import net.minecraft.world.World;

public class CaneItem extends Item {
    private static final AttributeModifier WALK_SPEED_BOOST = new AttributeModifier("Cane speed boost", 0.016, AttributeModifier.Operation.ADDITION);
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

            if (attributeInstance.hasModifier(WALK_SPEED_BOOST)) {
                attributeInstance.removeModifier(WALK_SPEED_BOOST);
            }

            if (!isSelected) {
                return;
            }

            if (!entity.isSprinting() && !entity.isCrouching()) {
                //server or client side?
                attributeInstance.applyNonPersistentModifier(WALK_SPEED_BOOST);
            }
        }

        /*
        if (entityIn.isLiving() ) {
            LivingEntity entity = (LivingEntity) entityIn;
            if (entity.isInWater() && !entity.isActualySwimming()) {
                entity.addPotionEffect(new EffectInstance(Effects.SPEED, 1, 2));
            }
        }
         */
    }
}
