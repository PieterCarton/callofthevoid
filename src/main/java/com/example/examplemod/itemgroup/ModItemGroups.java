package com.example.examplemod.itemgroup;

import com.example.examplemod.setup.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ModItemGroups {
    public static final ItemGroup CALL_OF_THE_VOID = new CallOfTheVoidItemGroup("Call of the Void",
            () -> new ItemStack(ModItems.CUSTOM_INGOT.get()));


    public static class CallOfTheVoidItemGroup extends ItemGroup{
        private Supplier<ItemStack> iconSupplier;
        public CallOfTheVoidItemGroup(final String name, final Supplier<ItemStack> supplier) {
            super(name);
            iconSupplier = supplier;
        }

        @Override
        public ItemStack createIcon() {
            return iconSupplier.get();
        }
    }
}
