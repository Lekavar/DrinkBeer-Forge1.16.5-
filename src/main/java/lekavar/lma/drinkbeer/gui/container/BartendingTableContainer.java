package lekavar.lma.drinkbeer.gui.container;

import com.google.common.collect.Lists;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerBarrelTileEntity;
import lekavar.lma.drinkbeer.capability.Capabilities;
import lekavar.lma.drinkbeer.capability.beerinfo.IBeerInfo;
import lekavar.lma.drinkbeer.essentials.flavor.ComboFlavors;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.essentials.spice.ISpiceProvider;
import lekavar.lma.drinkbeer.items.BeerItem;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.ContainerTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BartendingTableContainer extends Container {
    private final static int BASE_FLAVOR_LIMIT = 3;
    private final IInventory tablespace;
    private final IInventory resultSpace;
    private final IWorldPosCallable access;


    public BartendingTableContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(ContainerTypeRegistry.BARTENDING_TABLE_CONTAINER.get(), id);
        access = worldPosCallable;
        tablespace = new Inventory(4){
            @Override
            public void setChanged() {
                super.setChanged();
                BartendingTableContainer.this.slotsChanged(this);
            }
        };
        resultSpace = new Inventory(1);

        // Layout Slot
        // Player Inventory
        layoutPlayerInventorySlots(8, 84, new InvWrapper(playerInventory));
        // Beer Slot
        addSlot(new BeerSlot(tablespace, 0, 27, 34));
        // Spices Slot
        addSlot(new SpiceSlot(tablespace, 1, 67, 16));
        addSlot(new SpiceSlot(tablespace, 2, 67, 34));
        addSlot(new SpiceSlot(tablespace, 3, 67, 52));
        // Output Slot
        addSlot(new OutputSlot(resultSpace, 0, 128, 34, tablespace));
    }

    @Override
    public void slotsChanged(IInventory inventory) {
        super.slotsChanged(inventory);
        if(!inventory.getItem(0).isEmpty()){
            updateFlavoringResult(inventory);
        }
    }

    private void updateFlavoringResult(IInventory inventory){
        boolean qualified = false;
        ItemStack beer = inventory.getItem(0);
        ItemStack newBeer = beer.copy();
        newBeer.setCount(1);
        IBeerInfo beerInfo = beer.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + beer + " in bartending table"));
        IBeerInfo newBeerInfo = newBeer.getCapability(Capabilities.BEER_INFO_CAPABILITY, null)
                .orElseThrow(() -> new RuntimeException("Things goes wrong! Server side cannot get BeerInfo from ItemStack " + beer + " in bartending table"));

        if(beerInfo.isFlavoredBeer()){
            int totalFlavor = beerInfo.getBaseFlavor().size();
            if(totalFlavor < BASE_FLAVOR_LIMIT){
                ItemStack spice1 = inventory.getItem(1);
                ItemStack spice2 = inventory.getItem(2);
                ItemStack spice3 = inventory.getItem(3);
                List<IFlavor> baseFlavors = new ArrayList<>();
                if(totalFlavor == 1){
                    if(!spice1.isEmpty()){
                        baseFlavors.addAll(((ISpiceProvider) spice1.getItem()).getSpice().getFlavor());
                    }
                    baseFlavors.addAll(beerInfo.getBaseFlavor());
                    if(!spice2.isEmpty()){
                        baseFlavors.addAll(((ISpiceProvider) spice2.getItem()).getSpice().getFlavor());
                    }
                    if(!spice3.isEmpty()){
                        baseFlavors.addAll(((ISpiceProvider) spice3.getItem()).getSpice().getFlavor());

                    }
                } else if(totalFlavor == 2){
                    if(!spice1.isEmpty()){
                        baseFlavors.addAll(((ISpiceProvider) spice1.getItem()).getSpice().getFlavor());
                        baseFlavors.addAll(beerInfo.getBaseFlavor());
                    }
                    else if(!spice2.isEmpty()){
                        baseFlavors.add(beerInfo.getBaseFlavor().get(0));
                        baseFlavors.addAll(((ISpiceProvider) spice2.getItem()).getSpice().getFlavor());
                        baseFlavors.add(beerInfo.getBaseFlavor().get(1));
                    }
                    else if(!spice3.isEmpty()){
                        baseFlavors.addAll(beerInfo.getBaseFlavor());
                        baseFlavors.addAll(((ISpiceProvider) spice3.getItem()).getSpice().getFlavor());
                    }
                }
                if(baseFlavors.size()<=BASE_FLAVOR_LIMIT){
                    List<IFlavor> comboFlavors = ComboFlavors.getCorrespondFlavor(baseFlavors);
                    newBeerInfo.setBaseFlavor(baseFlavors);
                    newBeerInfo.setComboFlavor(comboFlavors);
                    qualified = true;
                }
            }

        } else {
            List<ItemStack> spices = Lists.newArrayList(inventory.getItem(1),inventory.getItem(2),inventory.getItem(3)).stream().filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toList());
            List<IFlavor> baseFlavors = new ArrayList<>();
            for(ItemStack spice:spices){
                baseFlavors.addAll(((ISpiceProvider) spice.getItem()).getSpice().getFlavor());
            }
            if(baseFlavors.size()<=BASE_FLAVOR_LIMIT && !baseFlavors.isEmpty()){
                List<IFlavor> comboFlavors = ComboFlavors.getCorrespondFlavor(baseFlavors);
                newBeerInfo.setFlavoredBeer(true);
                newBeerInfo.setBaseFlavor(baseFlavors);
                newBeerInfo.setComboFlavor(comboFlavors);
                qualified = true;
            }
        }

        if(qualified){
            resultSpace.setItem(0,newBeer);
        } else {
            resultSpace.removeItem(0,64);
        }
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
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
            if (p_82846_2_ == 40) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // Try quick-move item in player inv.
            else if (p_82846_2_ < 36) {
                // Try to beer slot first.
                if (itemstack.getItem() instanceof BeerItem) {
                    if (!this.moveItemStackTo(itemstack1, 36, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // Try to fill spice slot.
                if (!this.moveItemStackTo(itemstack1, 37, 40, false)) {
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

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.access.evaluate((world, blockPos) -> world.getBlockState(blockPos).is(BlockRegistry.BARTENDING_TABLE.get()) && player.distanceToSqr((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D) <= 32.0D, true);
    }

    @Override
    public void removed(PlayerEntity player) {
        if (!player.level.isClientSide()) {
            // Return Item to Player;
            for (int i = 0; i < 4; i++) {
                if (!tablespace.getItem(i).isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, tablespace.removeItem(i, tablespace.getItem(i).getCount()));
                }
                player.inventory.setChanged();
            }
        } else {
            // Play Closing Barrel Sound
            player.level.playSound(player, player.blockPosition(), SoundEvents.BARREL_CLOSE, SoundCategory.BLOCKS, 1f, 1f);
        }
        super.removed(player);
    }

    static class BeerSlot extends Slot {
        public BeerSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        // Only Beer is Allowed.
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return p_75214_1_.getItem() instanceof BeerItem;
        }
    }

    static class SpiceSlot extends Slot {
        public SpiceSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        // Only Spice is Allowed.
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return p_75214_1_.getItem() instanceof ISpiceProvider;
        }
    }

    static class OutputSlot extends Slot {
        IInventory tablespace;

        public OutputSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, IInventory tablespace) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.tablespace = tablespace;
        }

        @Override
        public ItemStack onTake(PlayerEntity playerEntity, ItemStack itemStack) {
            tablespace.removeItem(0,1);
            tablespace.removeItem(1,1);
            tablespace.removeItem(2,1);
            tablespace.removeItem(3,1);
            return super.onTake(playerEntity, itemStack);
        }

        // Placing item on output slot is prohibited.
        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return false;
        }
    }
}
