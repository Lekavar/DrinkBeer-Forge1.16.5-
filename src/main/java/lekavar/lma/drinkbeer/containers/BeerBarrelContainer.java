package lekavar.lma.drinkbeer.containers;

import lekavar.lma.drinkbeer.registries.ContainerTypeRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import lekavar.lma.drinkbeer.tileentity.BeerBarrelTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BeerBarrelContainer extends Container {
    private static final int STATUS_CODE = 1;
    private static final int BREWING_REMAINING_TIME = 0;
    private final IInventory brewingSpace;
    private final IIntArray syncData;

    public BeerBarrelContainer(int id, IInventory brewingSpace, IIntArray syncData, PlayerInventory playerInventory) {
        super(ContainerTypeRegistry.beerBarrelContainer.get(), id);
        this.brewingSpace = brewingSpace;
        this.syncData = syncData;

        // Layout Slot
        // Plauer Inventory
        layoutPlayerInventorySlots(8,84, new InvWrapper(playerInventory));
        // Input Ingredients
        addSlot(new InputSlot(brewingSpace,0,28,26,syncData));
        addSlot(new InputSlot(brewingSpace,1,46,26,syncData));
        addSlot(new InputSlot(brewingSpace,2,28,44,syncData));
        addSlot(new InputSlot(brewingSpace,3,46,44,syncData));
        // Empty Cup
        addSlot(new CupSlot(brewingSpace,4,73,50));
        // Output
        addSlot(new OutputSlot(brewingSpace,5,128,34,syncData));

        //Tracking Data
        addDataSlots(syncData);
    }

    public BeerBarrelContainer(int id, PlayerInventory playerInventory, PacketBuffer data) {
        this(id,playerInventory,data.readBlockPos());
    }

    public BeerBarrelContainer(int id, PlayerInventory playerInventory, BlockPos pos) {
        this(id,((BeerBarrelTileEntity) Minecraft.getInstance().level.getBlockEntity(pos)),((BeerBarrelTileEntity) Minecraft.getInstance().level.getBlockEntity(pos)).syncData,playerInventory);
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow, IItemHandler playerInventory) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_82846_2_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Try quick-pickup output
            if (p_82846_2_ == 2) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // Try quick-move item in player inv.
            else if (p_82846_2_ < 36) {
                // Try to fill cup slot first.
                if (this.isEmptyCup(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 40, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Try to fill ingredient slot.
                if (!this.moveItemStackTo(itemstack1, 36, 40, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Try quick-move item to player inv.
            else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            // Detect weather the quick-move is successful or not
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            // Detect weather the quick-move is successful or not
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(p_82846_1_, itemstack1);
        }

        return itemstack;
    }

    public boolean isEmptyCup(ItemStack itemStack){
        return itemStack.getItem()==ItemRegistry.EMPTY_BEER_MUG.get();
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return this.brewingSpace.stillValid(p_75145_1_);
    }

    public boolean getIsBrewing() {
        return syncData.get(STATUS_CODE)==1;
    }

    public int getStandardBrewingTime() {
        return syncData.get(BREWING_REMAINING_TIME);
    }

    public int getRemainingBrewingTime() {
        return syncData.get(BREWING_REMAINING_TIME);
    }

    @Override
    public void removed(PlayerEntity player) {
        if(!player.level.isClientSide()){
            // Return Item to Player;
            for(int i=0;i<5;i++){
                if(!brewingSpace.getItem(i).isEmpty()){
                    ItemHandlerHelper.giveItemToPlayer(player, brewingSpace.removeItem(i,brewingSpace.getItem(i).getCount()));
                }
            }
            // Play Closing Barrel Sound
            player.level.playSound(player, player.blockPosition(), SoundEvents.BARREL_CLOSE, SoundCategory.BLOCKS, 1f, 1f);
            super.removed(player);
        }
    }

    static class InputSlot extends Slot{
        private final IIntArray syncData;

        public InputSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, IIntArray syncData) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.syncData = syncData;
        }

        // When statusCode is 1 (brewing), place is prohibited.
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return syncData.get(STATUS_CODE) != 1;
        }

    }

    static class CupSlot extends Slot{
        public CupSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        // Only Empty Cup is Allowed.
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return p_75214_1_.getItem() == ItemRegistry.EMPTY_BEER_MUG.get();
        }
    }

    static class OutputSlot extends Slot{
        private final IIntArray syncData;

        public OutputSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, IIntArray syncData) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.syncData = syncData;
        }

        // After player picking up product, play pour sound effect
        // statusCode reset is handled by TileEntity#tick
        @Override
        public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
            p_190901_1_.level.playSound(p_190901_1_, p_190901_1_.blockPosition(), SoundEventRegistry.POURING.get(), SoundCategory.BLOCKS, 1f, 1f);
            return super.onTake(p_190901_1_, p_190901_2_);
        }

        // Placing item on output slot is prohibited.
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return false;
        }

        // Only statusCode is 2 (waiting for pickup), pickup is allowed.
        @Override
        public boolean mayPickup(PlayerEntity p_82869_1_) {
            return syncData.get(STATUS_CODE) == 2;
        }
    }

   /* private final Inventory input;
    private final Inventory output;
    private List<ItemStack> getBackResultList;
    private final Slot materialSlot1;
    private final Slot materialSlot2;
    private final Slot materialSlot3;
    private final Slot materialSlot4;
    private final Slot emptyMugSlot;
    private final Slot resultSlot;
    private PlayerInventory playerInv;
    private BeerBarrelTileEntity barrelTileEntity;
    public Boolean pouring;

    public BeerBarrelContainer(int id, PlayerInventory playerInventory, PacketBuffer extraData) {
        this(id, playerInventory, (BeerBarrelTileEntity) Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos()));
    }

    public BeerBarrelContainer(int id, PlayerInventory playerInventory, BeerBarrelTileEntity e) {
        super(ContainerTypeRegistry.beerBarrelContainer.get(), id);
        this.barrelTileEntity = e;
        this.input = new Inventory(5);
        this.output = new Inventory(1);
        this.playerInv = playerInventory;
        this.pouring = false;

        this.materialSlot1 = this.addSlot(new Slot(input, 0, 28, 26) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                updateResultSlot();
            }

            @Override
            public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                updateResultSlot();
                return super.onTake(player, stack);
            }
        });
        this.materialSlot2 = this.addSlot(new Slot(input, 1, 46, 26) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                updateResultSlot();
            }

            @Override
            public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                updateResultSlot();
                return super.onTake(player, stack);
            }
        });
        this.materialSlot3 = this.addSlot(new Slot(input, 2, 28, 44) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                updateResultSlot();
            }

            @Override
            public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                updateResultSlot();
                return super.onTake(player, stack);
            }
        });
        this.materialSlot4 = this.addSlot(new Slot(input, 3, 46, 44) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                updateResultSlot();
            }

            @Override
            public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                updateResultSlot();
                return super.onTake(player, stack);
            }
        });
        this.emptyMugSlot = this.addSlot(new Slot(input, 4, 73, 50) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (stack.getItem().asItem() == ItemRegistry.EMPTY_BEER_MUG.get() && isMaterialCompleted() && !isBrewing())
                    return true;
                else
                    return false;
            }

            @Override
            public void setChanged() {
                super.setChanged();
                updateResultSlot();
            }
        });
        this.resultSlot = this.addSlot(new Slot(output, 0, 128, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(PlayerEntity player) {
                if (isBrewing() && !isBrewingTimeRemain()) {
                    return true;
                } else
                    return false;
            }

            @Override
            public ItemStack onTake(PlayerEntity player, ItemStack stack) {
                pouring = true;
                resetBeerBarrel();
                return super.onTake(player, stack);
            }
        });
        //The player inventory
        int m;
        int l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInv, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInv, m, 8 + m * 18, 142));
        }
        if (this.barrelTileEntity.get(2) != 0) {
            BiMap<Integer, Item> beerTypeMap = createBeerTypeMap();
            ItemStack resultItemStack = new ItemStack(beerTypeMap.get(this.barrelTileEntity.get(2)), 4);
            this.resultSlot.set(resultItemStack);
        }

        trackRemainingBrewTime();
        trackIsMaterialCompleted();
        trackBeerType();
        trackIsBrewing();
    }

    private void trackRemainingBrewTime() {
        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return getRemainingBrewingTime();
            }

            @Override
            public void set(int value) {
                barrelTileEntity.set(0, value);
            }
        });
    }

    private void trackIsMaterialCompleted() {
        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return getRemainingBrewingTime();
            }

            @Override
            public void set(int value) {
                barrelTileEntity.set(1, value);
            }
        });
    }

    private void trackBeerType() {
        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return getBeerType();
            }

            @Override
            public void set(int value) {
                barrelTileEntity.set(2, value);
            }
        });
    }

    private void trackIsBrewing() {
        addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return getIsBrewing();
            }

            @Override
            public void set(int value) {
                barrelTileEntity.set(3, value);
            }
        });
    }

    public void resetBeerBarrel() {
        this.barrelTileEntity.set(1, 0);
        this.barrelTileEntity.set(2, 0);
        this.barrelTileEntity.set(3, 0);
        this.barrelTileEntity.setChanged();
    }

    public boolean isBrewing() {
        return this.getIsBrewing() == 1;
    }

    public boolean isBrewingTimeRemain() {
        return this.getRemainingBrewingTime() > 0;
    }

    public boolean isMaterialCompleted() {
        return getIsMaterialCompleted() != 0;
    }

    public int getRemainingBrewingTime() {
        return this.barrelTileEntity.get(0);
    }

    public int getIsMaterialCompleted() {
        return this.barrelTileEntity.get(1);
    }

    public int getBeerType() {
        return this.barrelTileEntity.get(2);
    }

    public int getIsBrewing() {
        return this.barrelTileEntity.get(3);
    }

    public void stopPouring() {
        this.pouring = false;
    }

    public int getBrewingTimeInResultSlot() {
        Item beerItem = this.resultSlot.getItem().getItem();
        if (beerItem != Items.AIR) {
            Map<Item, Integer> brewingTimeMap = createBrewingTimeMap();
            return brewingTimeMap.get(beerItem);
        }
        return 0;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return stillValid(IWorldPosCallable.create(barrelTileEntity.getLevel(), barrelTileEntity.getBlockPos()), player, BlockRegistry.BEER_BARREL.get());
    }

    public BeerBarrelTileEntity getBarrelTileEntity() {
        return barrelTileEntity;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (index < 5 && !this.moveItemStackTo(stack1, 5, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(stack1, 0, 5, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.mayPlace(ItemStack.EMPTY);
            }
            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    public void updateResultSlot() {
        if (isBrewing())
            return;

        this.barrelTileEntity.set(1, 0);
        this.resultSlot.set(ItemStack.EMPTY);

        ItemStack materialItemStack1 = this.materialSlot1.getItem();
        ItemStack materialItemStack2 = this.materialSlot2.getItem();
        ItemStack materialItemStack3 = this.materialSlot3.getItem();
        ItemStack materialItemStack4 = this.materialSlot4.getItem();
        Map<Item, Integer> map = new HashMap<>();
        Item materialItem1 = materialItemStack1.getItem();
        Item materialItem2 = materialItemStack2.getItem();
        Item materialItem3 = materialItemStack3.getItem();
        Item materialItem4 = materialItemStack4.getItem();
        map.put(materialItem1, 1);
        if (map.containsKey(materialItem2)) {
            map.put(materialItem2, map.get(materialItem2) + 1);
        } else {
            map.put(materialItem2, 1);
        }
        if (map.containsKey(materialItem3)) {
            map.put(materialItem3, map.get(materialItem3) + 1);
        } else {
            map.put(materialItem3, 1);
        }
        if (map.containsKey(materialItem4)) {
            map.put(materialItem4, map.get(materialItem4) + 1);
        } else {
            map.put(materialItem4, 1);
        }

        ItemStack resultItemStack = getResult(map);
        if (!resultItemStack.isEmpty()) {
            this.resultSlot.set(resultItemStack);
            this.barrelTileEntity.set(1, 1);
            this.barrelTileEntity.setChanged();
        } else {
            //doNothing
        }

        if (isMaterialCompleted()) {
            ItemStack emptyMugItemStack = this.emptyMugSlot.getItem();
            if (emptyMugItemStack.getCount() >= 4) {
                Item beerItem = this.resultSlot.getItem().getItem();
                BiMap<Item, Integer> beerTypeMap = createBeerTypeMap().inverse();
                this.barrelTileEntity.set(0, getBrewingTimeInResultSlot());
                this.barrelTileEntity.set(2, beerTypeMap.get(beerItem));
                this.barrelTileEntity.set(3, 1);
                this.barrelTileEntity.setChanged();

                this.getBackResultList = getBackResult();

                this.materialSlot1.remove(1);
                this.materialSlot2.remove(1);
                this.materialSlot3.remove(1);
                this.materialSlot4.remove(1);
                this.emptyMugSlot.remove(4);
            }
        }
    }

    public List<ItemStack> getBackResult() {
        Map<Item, Integer> map = new HashMap<>();
        ItemStack materialItemStack1 = this.materialSlot1.getItem();
        ItemStack materialItemStack2 = this.materialSlot2.getItem();
        ItemStack materialItemStack3 = this.materialSlot3.getItem();
        ItemStack materialItemStack4 = this.materialSlot4.getItem();
        Item materialItem1 = materialItemStack1.getItem();
        Item materialItem2 = materialItemStack2.getItem();
        Item materialItem3 = materialItemStack3.getItem();
        Item materialItem4 = materialItemStack4.getItem();
        map.put(materialItem1, 1);
        if (map.containsKey(materialItem2)) {
            map.put(materialItem2, map.get(materialItem2) + 1);
        } else {
            map.put(materialItem2, 1);
        }
        if (map.containsKey(materialItem3)) {
            map.put(materialItem3, map.get(materialItem3) + 1);
        } else {
            map.put(materialItem3, 1);
        }
        if (map.containsKey(materialItem4)) {
            map.put(materialItem4, map.get(materialItem4) + 1);
        } else {
            map.put(materialItem4, 1);
        }

        List<ItemStack> getBackResultList = new ArrayList<>();
        if (map.containsKey(Items.WATER_BUCKET)) {
            ItemStack itemStackWaterBucket = new ItemStack(Items.BUCKET, map.get(Items.WATER_BUCKET));
            getBackResultList.add(itemStackWaterBucket);
        }
        if (map.containsKey(Items.MILK_BUCKET)) {
            ItemStack itemStackMilkBucket = new ItemStack(Items.BUCKET, map.get(Items.MILK_BUCKET));
            getBackResultList.add(itemStackMilkBucket);
        }

        return getBackResultList;
    }

    public List<ItemStack> getBackResultBeforeClose() {
        ItemStack materialItemStack1 = this.materialSlot1.getItem();
        ItemStack materialItemStack2 = this.materialSlot2.getItem();
        ItemStack materialItemStack3 = this.materialSlot3.getItem();
        ItemStack materialItemStack4 = this.materialSlot4.getItem();
        ItemStack emptyMugItemStack = this.emptyMugSlot.getItem();
        List<ItemStack> getBackResultBeforeCloseList = new ArrayList<>();
        if (!materialItemStack1.isEmpty()) {
            getBackResultBeforeCloseList.add(materialItemStack1);
        }
        if (!materialItemStack2.isEmpty()) {
            getBackResultBeforeCloseList.add(materialItemStack2);
        }
        if (!materialItemStack3.isEmpty()) {
            getBackResultBeforeCloseList.add(materialItemStack3);
        }
        if (!materialItemStack4.isEmpty()) {
            getBackResultBeforeCloseList.add(materialItemStack4);
        }
        if (!emptyMugItemStack.isEmpty()) {
            getBackResultBeforeCloseList.add(emptyMugItemStack);
        }
        if (this.getBackResultList != null)
            getBackResultBeforeCloseList.addAll(this.getBackResultList);
        return getBackResultBeforeCloseList;
    }

    public static Map<Item, String> createMaterialMap() {
        Map<Item, String> map = Maps.newLinkedHashMap();
        map.put(Items.WATER_BUCKET, "water_bucket");
        map.put(Items.MILK_BUCKET, "milk_bucket");
        map.put(Items.BLAZE_POWDER, "blaze_powder");
        map.put(Items.WHEAT, "wheat");
        map.put(Items.SUGAR, "sugar");
        map.put(Items.APPLE, "apple");
        map.put(Items.SWEET_BERRIES, "sweet_berrires");
        map.put(Items.BLUE_ICE, "blue_ice");
        map.put(Items.BREAD, "bread");
        map.put(Items.PUMPKIN, "pumpkin");
        return map;
    }

    public static Map<Item, Integer> createBrewingTimeMap() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        map.put(ItemRegistry.BEER_MUG.get(), 24000);
        map.put(ItemRegistry.BEER_MUG_BLAZE_STOUT.get(), 12000);
        map.put(ItemRegistry.BEER_MUG_BLAZE_MILK_STOUT.get(), 18000);
        map.put(ItemRegistry.BEER_MUG_APPLE_LAMBIC.get(), 24000);
        map.put(ItemRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get(), 24000);
        map.put(ItemRegistry.BEER_MUG_HAARS_ICEY_PALE_LAGER.get(), 24000);
        map.put(ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get(), 12000);
        return map;
    }

    public static BiMap<Integer, Item> createBeerTypeMap() {
        BiMap<Integer, Item> map = HashBiMap.create();
        map.put(1, ItemRegistry.BEER_MUG.get());
        map.put(2, ItemRegistry.BEER_MUG_BLAZE_STOUT.get().asItem());
        map.put(3, ItemRegistry.BEER_MUG_BLAZE_MILK_STOUT.get().asItem());
        map.put(4, ItemRegistry.BEER_MUG_APPLE_LAMBIC.get().asItem());
        map.put(5, ItemRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get());
        map.put(6, ItemRegistry.BEER_MUG_HAARS_ICEY_PALE_LAGER.get());
        map.put(7, ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get());
        return map;
    }

    private ItemStack getResult(Map<Item, Integer> map) {
        //adding new recipes starts here
        if (map.containsKey(Items.WATER_BUCKET)) {
            if (map.get(Items.WATER_BUCKET) == 1) {
                if (map.containsKey(Items.WHEAT)) {
                    if (map.get(Items.WHEAT) == 3) {
                        return new ItemStack(ItemRegistry.BEER_MUG.get(), 4);
                    } else if (map.get(Items.WHEAT) == 2) {
                        if (map.containsKey(Items.BLAZE_POWDER))
                            return new ItemStack(ItemRegistry.BEER_MUG_BLAZE_STOUT.get(), 4);
                        if (map.containsKey(Items.APPLE))
                            return new ItemStack(ItemRegistry.BEER_MUG_APPLE_LAMBIC.get(), 4);
                        if (map.containsKey(Items.SWEET_BERRIES))
                            return new ItemStack(ItemRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get(), 4);
                    } else if (map.get(Items.WHEAT) == 1) {
                        if (map.containsKey(Items.BLAZE_POWDER) && map.containsKey(Items.SUGAR)) {
                            return new ItemStack(ItemRegistry.BEER_MUG_BLAZE_MILK_STOUT.get(), 4);
                        }
                    }
                } else if (map.containsKey(Items.BREAD)) {
                    if (map.get(Items.BREAD) == 2) {
                        if (map.containsKey(Items.PUMPKIN))
                            return new ItemStack(ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get(), 4);
                    }
                }
            }
        } else if (map.containsKey(Items.WHEAT)) {
            if (map.get(Items.WHEAT) == 3) {
                if (map.containsKey(Items.BLUE_ICE))
                    return new ItemStack(ItemRegistry.BEER_MUG_HAARS_ICEY_PALE_LAGER.get(), 4);
            }
        }
        //adding new recipes ends here
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(PlayerEntity player) {
        try {
            List<ItemStack> getBackResultBeforeCloseList = getBackResultBeforeClose();
            if (getBackResultBeforeCloseList != null) {
                for (ItemStack itemStack : getBackResultBeforeClose()) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemStack);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        player.level.playSound(player, player.blockPosition(), SoundEvents.BARREL_CLOSE, SoundCategory.BLOCKS, 1f, 1f);
        super.removed(player);
    }*/
}
