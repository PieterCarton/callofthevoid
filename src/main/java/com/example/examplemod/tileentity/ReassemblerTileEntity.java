package com.example.examplemod.tileentity;

import com.example.examplemod.essence.EssenceQuantity;
import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.essence.IEssenceStorage;
import com.example.examplemod.inventory.ReassemblerContainer;
import com.example.examplemod.setup.ModContainerTypes;
import com.example.examplemod.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ReassemblerTileEntity extends LockableLootTileEntity implements ITickableTileEntity, IEssenceStorage {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int essence = 0;
    private int maxEssence = 10000;
    private int repairTime;
    private int repairTimeTotal = 100;
    private final IIntArray reassemblerData = new IIntArray() {

        @Override
        public int get(int index) {
            switch(index){
                case 0:
                    return getRepairTime();
                case 1:
                    return getRepairTimeTotal();
                case 2:
                    return essence;
                case 3:
                    return maxEssence;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    setRepairTime(value);
                    break;
                case 1:
                    setRepairTimeTotal(value);
                    break;
                case 2:
                    essence = value;
                    break;
                case 3:
                    maxEssence = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    //TODO
    //NBTread and write to save tileEntity when chunk unloaded
    //NonNulList of items

    public ReassemblerTileEntity(){
        super(ModTileEntityTypes.REASSEMBLER.get());
    }

    public ReassemblerTileEntity(TileEntityType type){
        super(type);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        nbt.putInt("repairTime", getRepairTime());
        nbt.putInt("essence", getRepairTime());
        ItemStackHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        this.repairTime = compound.getInt("repairTime");
        this.essence = compound.getInt("essence");
        return compound;
    }

    public int getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(int repairTime) {
        this.repairTime = repairTime;
    }

    public int getRepairTimeTotal() {
        return repairTimeTotal;
    }

    public void setRepairTimeTotal(int repairTimeTotal) {
        this.repairTimeTotal = repairTimeTotal;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.callofthevoidmod.reassembler");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ReassemblerContainer(id, player, this, reassemblerData);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public void tick() {
        //check if there is an item in slot 0
        ItemStack repairItem = items.get(0);
        if(repairItem != null && !repairItem.isEmpty() && !world.isRemote){
            if(repairItem.isDamageable() && repairItem.isDamaged()){
                repairTime++;
                if(repairTime != repairTimeTotal){
                    return;
                }
                repairTime = 0;
                int i = repairItem.getDamage();
                int newDamage = i - 1;
                repairItem.setDamage(newDamage);
                return;
            }
        }
        repairTime = 0;
    }

    @Override
    public EssenceQuantity receiveEssence(EssenceQuantity quantity) {
        if (!canReceiveEssence(quantity.getType())) {
            return quantity;
        }
        int received = Math.min(maxEssence - essence, quantity.getQuantity());
        int leftOver = quantity.getQuantity() - received;
        essence += received;
        return new EssenceQuantity(EssenceType.ORDER, leftOver);
    }

    @Override
    public EssenceQuantity extractEssence(EssenceQuantity quantity) {
        return new EssenceQuantity(EssenceType.ORDER, 0);
    }

    @Override
    public int essenceStored(EssenceType type) {
        if (type != EssenceType.ORDER) {
            return 0;
        }
        return essence;
    }

    @Override
    public int maxEssenceStored(EssenceType type) {
        if (type != EssenceType.ORDER) {
            return 0;
        }
        return maxEssence;
    }

    @Override
    public boolean canReceiveEssence(EssenceType type) {
        return type == EssenceType.ORDER;
    }

    @Override
    public boolean canExtractEssence(EssenceType type) {
        return false;
    }
}
