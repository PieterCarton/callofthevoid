package com.example.examplemod.inventory;

import com.example.examplemod.screen.ReassemblerScreen;
import com.example.examplemod.setup.ModContainerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

public class ReassemblerContainer extends EssenceStorageContainer {
    private final IInventory inventory;
    private final IIntArray reassemblerData;

    public ReassemblerContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new Inventory(1), new IntArray(4));
    }


    public ReassemblerContainer(int id, PlayerInventory playerInventory, IInventory blockInventory, IIntArray reassemblerData) {
        super(ModContainerTypes.REASSEMBLER.get(), id);
        //set default state later
        this.inventory = blockInventory;
        this.reassemblerData = reassemblerData;
        this.addSlot(new Slot(inventory, 0, 80, 18));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 50 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 108));
        }

        this.trackIntArray(reassemblerData);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        inventory.closeInventory(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index){
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()){
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if(index == 0 && !this.mergeItemStack(itemStack1, 1, this.inventorySlots.size(), true)){
                return ItemStack.EMPTY;
            }else if(!this.mergeItemStack(itemStack1, 0, 1, false)){
                return ItemStack.EMPTY;
            }

            if(itemStack1.isEmpty()){
                slot.putStack(ItemStack.EMPTY);
            }else{
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getRepairProgressScaled(){
        int i = reassemblerData.get(0);
        return 9 * i/ 100;
    }
}
