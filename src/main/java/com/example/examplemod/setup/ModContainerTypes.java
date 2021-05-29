package com.example.examplemod.setup;

import com.example.examplemod.inventory.ReassemblerContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainerTypes {

    public static final RegistryObject<ContainerType<ReassemblerContainer>> REASSEMBLER = Registration.CONTAINERS.register("reassembler",
            () -> new ContainerType<ReassemblerContainer>(ReassemblerContainer::new));

    public static void register(){}
}
