package lekavar.lma.drinkbeer.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import lekavar.lma.drinkbeer.gui.container.BartendingTableContainer;
import lekavar.lma.drinkbeer.gui.container.BeerBarrelContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class BartendingTableContainerScreen extends ContainerScreen<BartendingTableContainer> {

    private final ResourceLocation TEXTURE = new ResourceLocation("drinkbeer", "textures/gui/container/bartending_table.png");

    public BartendingTableContainerScreen(BartendingTableContainer screenContainer, PlayerInventory inv, ITextComponent title) {
        super(screenContainer, inv, title);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        renderBackground(stack);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int i = (this.width - this.getXSize()) / 2;
        int j = (this.height - this.getYSize()) / 2;
        blit(stack, i, j, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int x, int y) {
        drawCenteredString(stack, this.font, this.title, this.imageWidth / 2, (int) this.titleLabelY, 4210752);
        this.font.draw(stack, this.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
    }
}
