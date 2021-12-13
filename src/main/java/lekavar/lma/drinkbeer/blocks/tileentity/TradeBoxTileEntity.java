package lekavar.lma.drinkbeer.blocks.tileentity;

import lekavar.lma.drinkbeer.gui.container.TradeBoxContainer;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.recipes.IBrewingInventory;
import lekavar.lma.drinkbeer.recipes.TradeRecipe;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import lekavar.lma.drinkbeer.registries.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TradeBoxTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider,IInventory {
    public static final int COOLING_TIME_ON_PLACE = 6000;
    public static final int COOLING_TIME_ON_REFRESH = 4800;
    private NonNullList<ItemStack> showcase = NonNullList.withSize(8, ItemStack.EMPTY);
    private String residentNameKey = "";
    private String locationNameKey = "";
    private int cooldown = COOLING_TIME_ON_PLACE;
    public final IIntArray syncData = new IIntArray() {
        @Override
        public int get(int i) {
            return cooldown;
        }

        @Override
        public void set(int i, int v) {
            cooldown = v;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public TradeBoxTileEntity() {
        super(TileEntityRegistry.TRADE_BOX_TILEENTITY.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.drinkbeer.trade_box");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TradeBoxContainer(id,playerInventory,this,syncData,residentNameKey,locationNameKey);
    }

    public void refreshTrade(){
        residentNameKey = "";
        locationNameKey = "";
        clearContent();
        cooldown = COOLING_TIME_ON_REFRESH;
        setChanged();
    }

    @Override
    public void tick() {
        if(!level.isClientSide()){
            if(cooldown>0)
                cooldown--;
            else{
                if(showcase.isEmpty()){
                    generateTradeContent();
                }
            }
        }
    }

    private void generateTradeContent(){
        int needCount = generateNeedGoodCount();
        int offerCount = generateOfferGoodCount(needCount);
        // Pick a random trade recipe
        List<TradeRecipe> trades = level.getRecipeManager().getAllRecipesFor(RecipeRegistry.Type.TRADING);
        TradeRecipe trade = trades.get(new Random().nextInt(trades.size()));
        List<ItemStack> needs = trade.generateNeed(needCount);
        List<ItemStack> offers = trade.generateNeed(offerCount);
        residentNameKey = trade.getResidentNameKey();
        locationNameKey = trade.getLocationNameKey();
        int i = 0;
        for(ItemStack itemStack:needs){
            showcase.set(i,itemStack);
            i++;
        }
        i = 4;
        for(ItemStack itemStack:offers){
            showcase.set(i,itemStack);
            i++;
        }
        setChanged();
    }

    private int generateNeedGoodCount(){
        double i = Math.random();
        if(i<1D/6D) return 1;
        else if (i<0.5D) return 2;
        else if (i<5D/6D) return 3;
        else return 4;
    }

    private int generateOfferGoodCount(int needGoodCount){
        if(needGoodCount==4) return needGoodCount;
        else{
            double i = Math.random();
            if(i<0.5D)
                return needGoodCount + 1;
        }
        return needGoodCount;
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        ItemStackHelper.saveAllItems(tag, showcase);
        tag.putString("resident",residentNameKey);
        tag.putString("location",locationNameKey);
        tag.putInt("cooldown", cooldown);
        super.save(tag);
        return tag;
    }

    @Override
    public void load(BlockState state, @Nonnull CompoundNBT tag) {
        super.load(state, tag);
        ItemStackHelper.loadAllItems(tag, showcase);
        residentNameKey = tag.getString("resident");
        locationNameKey = tag.getString("location");
        cooldown = tag.getInt("cooldown");
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        ItemStackHelper.saveAllItems(tag, showcase);
        tag.putString("resident",residentNameKey);
        tag.putString("location",locationNameKey);
        tag.putInt("cooldown", cooldown);
        super.save(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        ItemStackHelper.saveAllItems(tag, showcase);
        tag.putString("resident",residentNameKey);
        tag.putString("location",locationNameKey);
        tag.putInt("cooldown", cooldown);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public int getContainerSize() {
        return showcase.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : showcase) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return p_70301_1_ >= 0 && p_70301_1_ < showcase.size() ? showcase.get(p_70301_1_) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return ItemStackHelper.removeItem(showcase, p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return ItemStackHelper.takeItem(showcase, p_70304_1_);
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        if (p_70299_1_ >= 0 && p_70299_1_ < showcase.size()) {
            showcase.set(p_70299_1_, p_70299_2_);
        }
    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        } else {
            return !(p_70300_1_.distanceToSqr((double) worldPosition.getX() + 0.5D, (double) worldPosition.getY() + 0.5D, (double) worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        for(int i=0;i<showcase.size();i++){
            showcase.set(8,ItemStack.EMPTY);
        }
    }

    public String getResidentNameKey() {
        return residentNameKey;
    }

    public String getLocationNameKey() {
        return locationNameKey;
    }
}
