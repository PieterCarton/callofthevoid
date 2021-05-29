package com.example.examplemod.item;

import com.example.examplemod.setup.ModBlocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryBlockItem extends BlockItem {
    public BatteryBlockItem(Properties properties) {
        super(ModBlocks.BATTERY_BLOCK.get() ,properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT compound = stack.getTag();
        int charge = 0;

        if(compound != null){
            CompoundNBT blockEntityTag = compound.getCompound("BlockEntityTag");
            if(blockEntityTag.contains("essence")) {
                charge = blockEntityTag.getInt("essence");
            }
        }
        TextComponent chargeText = new StringTextComponent("Charge: " + charge);
        tooltip.add(chargeText);
    }
}
