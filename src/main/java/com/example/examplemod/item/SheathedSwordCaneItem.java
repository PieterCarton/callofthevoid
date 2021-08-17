package com.example.examplemod.item;

import com.example.examplemod.setup.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class SheathedSwordCaneItem extends CaneItem {
    public SheathedSwordCaneItem(Properties properties) {
        super(properties);
    }

    private static int CHARGE_TIME = 15;
    private static boolean interrupted = false;


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        if (playerIn.isCrouching() && handIn.equals(Hand.MAIN_HAND) && playerIn.getHeldItem(Hand.OFF_HAND).equals(ItemStack.EMPTY)) {
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemStack);
        } else {
            // Do not start using item if player not crouching
            return ActionResult.resultPass(itemStack);
        }
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        // play sound to indicate charge time has completed
        if (stack.getUseDuration() - count == CHARGE_TIME) {
            //TODO replace sound
            SoundEvent chargedSound = SoundEvents.ITEM_CROSSBOW_LOADING_END;
            worldIn.playSound((PlayerEntity) null, livingEntityIn.getPosX(), livingEntityIn.getPosY(), livingEntityIn.getPosZ(),
                    chargedSound, SoundCategory.PLAYERS,
                    1.0f, 1.0f);
        }

        if (!livingEntityIn.isCrouching()) {
            interrupted = true;
            onPlayerStoppedUsing(stack, worldIn, livingEntityIn, 0);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        //TODO: Replace sound
        worldIn.playSound((PlayerEntity) null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(),
                SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,
                1.0f, 1.0f);

        CompoundNBT tag = stack.getTag();
        ItemStack swordCane = new ItemStack(ModItems.SWORD_CANE_ITEM.get());
        swordCane.setTag(tag);

        entityLiving.setHeldItem(Hand.MAIN_HAND, swordCane);
        //TODO: fix so no item deletion
        entityLiving.setHeldItem(Hand.OFF_HAND, new ItemStack(ModItems.SWORD_CANE_SHEATH_ITEM.get()));

        System.out.println(timeLeft);
        if (interrupted || stack.getUseDuration() - timeLeft < CHARGE_TIME) {
            interrupted = false;
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        RayTraceResult mouseOver = mc.objectMouseOver;

        if (mouseOver != null && mouseOver.getType() == RayTraceResult.Type.ENTITY) {
            Entity targetEntity = ((EntityRayTraceResult)mouseOver).getEntity();
            if (targetEntity instanceof LivingEntity && !worldIn.isRemote) {
                LivingEntity le = ((LivingEntity)worldIn.getEntityByID(targetEntity.getEntityId()));

                DamageSource damageSource = entityLiving instanceof PlayerEntity ? DamageSource.causePlayerDamage((PlayerEntity) entityLiving) : DamageSource.causeMobDamage(entityLiving);
                le.attackEntityFrom(damageSource, swordCane.getDamage() * 1.5f);
            }
        }

        //forward dash
        Vector3d dashDirection = entityLiving.getLookVec();
        entityLiving.addVelocity(dashDirection.x, dashDirection.y * 0.2, dashDirection.z);
        entityLiving.swingArm(Hand.MAIN_HAND);

        if (worldIn.isRemote()) {
            Vector3d particlePosition = entityLiving.getLookVec()
                    .mul(2.0, 2.0, 2.0)
                    .add(entityLiving.getPositionVec())
                    .add(0.0, 1.0, 0.0);
            worldIn.addParticle(ParticleTypes.SWEEP_ATTACK, particlePosition.x, particlePosition.y, particlePosition.z, dashDirection.x * 2.0, dashDirection.y * 0.2, dashDirection.z * 2.0);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    //TODO overwrite getPose and implement special pose?
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }
}
