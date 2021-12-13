package lekavar.lma.drinkbeer.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import lekavar.lma.drinkbeer.gui.container.TradeBoxContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class TradeBoxContainerScreen  extends ContainerScreen<TradeBoxContainer> {

    private final ResourceLocation TEXTURE = new ResourceLocation("drinkbeer", "textures/gui/container/trade_box.png");

    public TradeBoxContainerScreen(TradeBoxContainer tradeBoxContainer, PlayerInventory playerInventory, ITextComponent iTextComponent) {
        super(tradeBoxContainer, playerInventory, iTextComponent);
    }

    @Override
    protected void renderBg(MatrixStack stack, float delta, int mouseX, int mouseY) {
        renderBackground(stack);
        this.minecraft.getTextureManager().bind(TEXTURE);
        int i = (this.width - this.getXSize()) / 2;
        int j = (this.height - this.getYSize()) / 2;
        blit(stack, i, j, 0, 0, imageWidth, imageHeight);
        if (menu.getSyncData().get(0)>0) {
            //Is on cooldown, render blank area on showcase space
            blit(stack, i + 84, j + 25, 178, 38, 72, 36);
        } else {
            if (isHovering(157, 6, 13, 13, (double) mouseX, (double) mouseY)) {
                blit(stack, i + 155, j + 4, 178, 19, 16, 16);
            } else {
                blit(stack, i + 155, j + 4, 178, 0, 16, 16);
            }
        }
    }

    @Override
    protected void renderLabels(MatrixStack stack, int x, int y) {
        int i = (this.width - this.getXSize()) / 2;
        int j = (this.height - this.getYSize()) / 2;
        if(menu.getSyncData().get(0)>0){
            //Is on cooldown, render remaining time
            String cooldown = convertTickToTime(menu.getSyncData().get(0));
            this.font.draw(stack,cooldown, i + 114, j + 39, new Color(64, 64, 64, 255).getRGB())
        } else {
            //render trade info
            if(!menu.getResidentNameKey().isEmpty() && !menu.getLocationNameKey().isEmpty()){
                ITextComponent you = new TranslationTextComponent("drinkbeer.you");
                ITextComponent trader = new TranslationTextComponent("name.drinkbeer.trade.location." + menu.getLocationNameKey())
                        .append(new StringTextComponent("-"))
                        .append(new TranslationTextComponent("name.drinkbeer.trade.resident." + menu.getResidentNameKey()));
                this.font.draw(stack,you, i + 85, j + 16, new Color(64, 64, 64, 255).getRGB());
                this.font.draw(stack,trader, i +  85, j + 63, new Color(64, 64, 64, 255).getRGB());
            }
        }
    }

    public String convertTickToTime(int tick) {
        String result;
        if (tick > 0) {
            double time = (double) tick / 20;
            int m = (int) (time / 60);
            int s = (int) (time % 60);
            result = m + ":" + s;
        } else result = "";
        return result;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    //TODO finish add bottom
    @Override
    protected <T extends AbstractButtonWidget> T addButton(T button) {
        return super.addButton(button);
    }

    @Override
    protected void init() {
        int i = (this.width - this.getXSize()) / 2;
        int j = (this.height - this.getYSize()) / 2;
        this.addButton(new TexturedButtonWidget(x + 156, y + 5, 15, 15, 197, 0, 0, TRADE_BOX_GUI, (buttonWidget) -> {
            if(screenHandler.isTrading()) {
                BlockPos pos = getHitTradeBoxBlockPos();
                if (pos != null)
                    NetWorking.sendRefreshTradebox(pos);
            }
        }));
        super.init();
    }

    private BlockPos getHitTradeBoxBlockPos() {
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hit = client.crosshairTarget;
        if (hit.getType().equals(HitResult.Type.BLOCK)) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            BlockPos blockPos = blockHit.getBlockPos();
            BlockState blockState = client.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block instanceof TradeboxBlock) {
                return blockPos;
            }
        }
        return null;
    }
}
