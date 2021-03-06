package com.example.examplemod.tileentity;

import com.example.examplemod.essence.MultiEssenceStorage;
import com.example.examplemod.essence.EssenceQuantity;
import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.essence.IEssenceStorage;
import com.example.examplemod.inventory.ReassemblerContainer;
import com.example.examplemod.network.EssenceSyncPacket;
import com.example.examplemod.network.ModPacketHandler;
import com.example.examplemod.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraftforge.fml.network.PacketDistributor;

public class ReassemblerTileEntity extends LockableLootTileEntity implements ITickableTileEntity, IEssenceStorage {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private MultiEssenceStorage storage;

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
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public ReassemblerTileEntity(){
        super(ModTileEntityTypes.REASSEMBLER.get());
        initializeStorage();
    }

    public ReassemblerTileEntity(TileEntityType type){
        super(type);
        initializeStorage();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);

        storage.read(nbt);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        this.repairTime = nbt.getInt("repairTime");
        ItemStackHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        storage.write(compound);
        ItemStackHelper.saveAllItems(compound, this.items);

        compound.putInt("repairTime", getRepairTime());
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

        PlayerEntity playerEntity = player.player;
        if (playerEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
        }
        return new ReassemblerContainer(id, player, this, reassemblerData, storage);
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
        return storage.receiveEssence(quantity);
    }

    @Override
    public EssenceQuantity extractEssence(EssenceQuantity quantity) {
        return storage.extractEssence(quantity);
    }

    @Override
    public int essenceStored(EssenceType type) {
        return storage.essenceStored(type);
    }

    @Override
    public int maxEssenceStored(EssenceType type) {
        return storage.maxEssenceStored(type);
    }

    @Override
    public boolean canReceiveEssence(EssenceType type) {
        return storage.canReceiveEssence(type);
    }

    @Override
    public boolean canExtractEssence(EssenceType type) {
        return storage.canExtractEssence(type);
    }

    private void initializeStorage() {
        storage = new MultiEssenceStorage(1000, 0);
        storage.addCompartment(EssenceType.ORDER, 1000);
    }
}
