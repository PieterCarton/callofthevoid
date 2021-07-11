package com.example.examplemod.screen;

import com.example.examplemod.CallOfTheVoidMod;
import com.example.examplemod.essence.EssenceType;
import com.example.examplemod.inventory.EssenceStorageContainer;
import com.example.examplemod.inventory.ReassemblerContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;

public class EssenceStorageScreen<T extends Container> extends ContainerScreen<T> {

    private static final ResourceLocation ESSENCE_DISPLAY = new ResourceLocation(CallOfTheVoidMod.MOD_ID, "textures/gui/container/essence_display.png");

    public EssenceStorageScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {}

    protected void drawEssenceDisplay(MatrixStack matrixStack, int x, int y, EssenceType type) {
        this.minecraft.getTextureManager().bindTexture(ESSENCE_DISPLAY);
        this.blit(matrixStack, x, y, 16, 0, 8, 31);
        int fill = ((EssenceStorageContainer)this.getContainer()).getEssenceFillScaled(type);
        this.blit(matrixStack, x, y, 0, 0, 8, 31 - fill);
    }

    protected void drawEssenceTooltip(MatrixStack matrixStack, EssenceType type, int x, int y) {
        EssenceStorageContainer container = (EssenceStorageContainer)this.getContainer();
        String tooltipText = type.name() + ": " + container.essenceStored(EssenceType.ORDER) + "/" + container.maxEssenceStored(EssenceType.ORDER);
        this.renderTooltip(matrixStack, new StringTextComponent(tooltipText), x, y);
    }
}
