package com.example.examplemod.screen;

import com.example.examplemod.CallOfTheVoidMod;
import com.example.examplemod.inventory.ReassemblerContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ReassemblerScreen extends EssenceStorageScreen<ReassemblerContainer> {

    public static final ResourceLocation REASSEMBLER_GUI_TEXTURE = new ResourceLocation(CallOfTheVoidMod.MOD_ID, "textures/gui/container/reassembler.png");

    public ReassemblerScreen(ReassemblerContainer container, PlayerInventory inventory, ITextComponent title){
        super(container, inventory, title);
        this.xSize = 175;
        this.ySize = 131;
        this.playerInventoryTitleY = this.ySize - 94;
        System.out.println("Screen Created!");
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(REASSEMBLER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        int progress = ((ReassemblerContainer)getContainer()).getRepairProgressScaled();
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        this.blit(matrixStack, i + 102, j + 29 - progress, 177, 8 - progress, 8, progress);
        this.drawEssenceDisplay(matrixStack, i + 116, j + 8);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
    }
}
