package com.example.examplemod.item;

import com.example.examplemod.setup.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Set;

public class SwordCaneItem extends ToolItem {

    public SwordCaneItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        Item sheathItem = ModItems.SWORD_CANE_SHEATH_ITEM.get();
        Item offhandItem = playerIn.getHeldItem(Hand.OFF_HAND).getItem();

        System.out.println(playerIn.isCrouching() );
        System.out.println(handIn.equals(Hand.MAIN_HAND));
        System.out.println(playerIn.getHeldItem(Hand.OFF_HAND).equals(sheathItem));

        if (playerIn.isCrouching() && handIn.equals(Hand.MAIN_HAND) && offhandItem.equals(sheathItem)) {
            //TODO: Replace sound
            worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                    SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,
                    1.0f, 1.5f);

            CompoundNBT tag = itemStack.getTag();
            ItemStack sheathedSwordCane = new ItemStack(ModItems.SHEATHED_SWORD_CANE_ITEM.get());
            sheathedSwordCane.setTag(tag);

            playerIn.setHeldItem(handIn, sheathedSwordCane);
            playerIn.setHeldItem(Hand.OFF_HAND, ItemStack.EMPTY);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
