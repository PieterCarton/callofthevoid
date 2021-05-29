package com.example.examplemod.item;

import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.essence.IEssenceStorage;
import com.example.examplemod.setup.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class EssenceAnalyserItem extends Item {
    public EssenceAnalyserItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote()) {
            BlockPos clickPosition = context.getPos();
            PlayerEntity player = context.getPlayer();

            TileEntity targetedTileEntity = world.getTileEntity(clickPosition);
            if (targetedTileEntity != null && targetedTileEntity instanceof IEssenceStorage) {
                IEssenceStorage storage = (IEssenceStorage) targetedTileEntity;

                displayEssenceStorageInfo(storage, player);
            }
        }

        return ActionResultType.func_233537_a_(world.isRemote);
    }

    private void displayEssenceStorageInfo(IEssenceStorage storage, PlayerEntity player) {
        String order = getEssenceText("order", storage);
        String entropy = getEssenceText("entropy", storage);

        StringTextComponent info = new StringTextComponent("Essence Stored: \n" + order + "\n" + entropy);
        player.sendMessage(info, Util.DUMMY_UUID);
    }

    private String getEssenceText(String typeName, IEssenceStorage storage) {
        EssenceType type = EssenceType.ORDER;

        if(typeName.equals("entropy")) {
            type = EssenceType.ENTROPY;
        }

        int stored = storage.essenceStored(type);
        int max = storage.maxEssenceStored(type);

        String result = typeName + ": " + stored + "/" + max;

        return result;
    }
}
