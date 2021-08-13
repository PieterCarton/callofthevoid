package com.example.examplemod.item;

import com.example.examplemod.setup.ModItems;
import com.example.examplemod.setup.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SheathedSwordCaneItem extends CaneItem {
    public SheathedSwordCaneItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        if (playerIn.isCrouching() && handIn.equals(Hand.MAIN_HAND) && playerIn.getHeldItem(Hand.OFF_HAND).equals(ItemStack.EMPTY)) {
            //TODO: Replace sound
            worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                    SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,
                    1.0f, 1.0f);

            CompoundNBT tag = itemStack.getTag();
            ItemStack swordCane = new ItemStack(ModItems.SWORD_CANE_ITEM.get());
            swordCane.setTag(tag);

            playerIn.setHeldItem(handIn, swordCane);
            playerIn.setHeldItem(Hand.OFF_HAND, new ItemStack(ModItems.SWORD_CANE_SHEATH_ITEM.get()));
        }

        //TODO: special attack on unsheathe

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
