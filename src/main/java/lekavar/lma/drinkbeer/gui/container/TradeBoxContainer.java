package lekavar.lma.drinkbeer.gui.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lekavar.lma.drinkbeer.registries.ContainerTypeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


public class TradeBoxContainer extends Container {
    private final IInventory showcaseSpace;
    private final IInventory inputSpace;
    private final String residentNameKey;
    private final String locationNameKey;
    private final IIntArray syncData;

    public TradeBoxContainer(int id, PlayerInventory playerInventory, IInventory showcaseSpace, IIntArray syncData, String residentNameKey, String locationNameKey) {
        super(ContainerTypeRegistry.TRADE_BOX_CONTAINER.get(), id);
        this.showcaseSpace = showcaseSpace;
        this.syncData = syncData;
        this.residentNameKey = residentNameKey;
        this.locationNameKey = locationNameKey;
        inputSpace =  new Inventory(4){
            @Override
            public void setChanged() {
                super.setChanged();
                TradeBoxContainer.this.slotsChanged(this);
            }
        };

        addSlot(new InputSlot(inputSpace, 0, 25, 26,syncData));
        addSlot(new InputSlot(inputSpace, 1, 43, 26,syncData));
        addSlot(new InputSlot(inputSpace, 2, 25, 44,syncData));
        addSlot(new InputSlot(inputSpace, 3, 43, 44,syncData));

        addSlot(new ShowcaseSlot(showcaseSpace, 0, 85, 26));
        addSlot(new ShowcaseSlot(showcaseSpace, 1, 103, 26));
        addSlot(new ShowcaseSlot(showcaseSpace, 2, 121, 26));
        addSlot(new ShowcaseSlot(showcaseSpace, 3, 139, 26));
        addSlot(new ShowcaseSlot(showcaseSpace, 4, 85, 44));
        addSlot(new ShowcaseSlot(showcaseSpace, 5, 103, 44));
        addSlot(new ShowcaseSlot(showcaseSpace, 6, 121, 44));
        addSlot(new ShowcaseSlot(showcaseSpace, 7, 139, 44));

        //Init player inventory
        IntStream.range(0, 3).forEach(m ->
                IntStream.range(0, 9).forEach(l -> this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18))));
        //Init player hot bar
        IntStream.range(0, 9).forEach(m -> this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142)));
    }

    @Override
    public void slotsChanged(IInventory inventory) {
        if(syncData.get(0)==0){
            super.slotsChanged(inventory);
            tryFinishTrade(inventory);
        }
    }

    private void tryFinishTrade(IInventory inventory){
        Map<Item,Integer> demandMap = getDemandMap();
        if(meetDemand(inventory,demandMap)){
            finishTrade(inventory,demandMap);
        }
    }


    private void finishTrade(IInventory inventory, Map<Item,Integer> demandMap){
        //TODO 需要完成的内容很多，休息会儿回头再来写吧
        // - 今天真是要被他妈的气晕了，恨他吗想甩手不干了
        // - 这里缺了处理完成交易后的一串动作
    }

    private Map<Item,Integer> getDemandMap(){
        Map<Item,Integer> ret = new HashMap<>();
        for(int i=0;i<4;i++){
            ItemStack temp = showcaseSpace.getItem(i);
            if(!temp.isEmpty()){
               if(ret.containsKey(temp.getItem()))
                   ret.put(temp.getItem(),ret.get(temp.getItem()) + temp.getCount());
               else
                   ret.put(temp.getItem(),temp.getCount());
            }
        }
        return ret;
    }

    private boolean meetDemand(IInventory inventory, Map<Item,Integer> demandMap){
        Map<Item,Integer> dupMap = Maps.newHashMap(demandMap);
        for(int i=0;i<4;i++){
            ItemStack temp = inventory.getItem(i);
            if(!temp.isEmpty()){
                if(dupMap.containsKey(temp.getItem())){
                    if(temp.getCount()>=dupMap.get(temp.getItem()))
                        dupMap.remove(temp.getItem());
                    else
                        dupMap.put(temp.getItem(),dupMap.get(temp.getItem())-temp.getCount());
                }
            }
        }
        return dupMap.isEmpty();
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return showcaseSpace.stillValid(playerEntity);
    }

    public String getResidentNameKey() {
        return residentNameKey;
    }

    public String getLocationNameKey() {
        return locationNameKey;
    }

    public IIntArray getSyncData() {
        return syncData;
    }

    static class InputSlot extends Slot {
        private final IIntArray syncData;

        public InputSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, IIntArray syncData) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.syncData = syncData;
        }

        // Placing item is allowed only when cooldown is over, which means trade is available
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return syncData.get(0)==0;
        }
    }

    static class ShowcaseSlot extends Slot {

        public ShowcaseSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return false;
        }

        @Override
        public boolean mayPickup(PlayerEntity p_82869_1_) {
            return false;
        }
    }

}
