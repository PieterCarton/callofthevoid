package com.example.examplemod.setup;

import com.example.examplemod.screen.ReassemblerScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModScreens {
    @OnlyIn(Dist.CLIENT)
    public static void init(){
        ScreenManager.registerFactory(ModContainerTypes.REASSEMBLER.get(), ReassemblerScreen::new);
    }
}
