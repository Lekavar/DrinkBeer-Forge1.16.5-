package lekavar.lma.drinkbeer.utils;

import lekavar.lma.drinkbeer.blocks.*;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Comparator;

public class ModGroup {
    public static final BearItemGroup BEAR = new BearItemGroup();
    public static final GeneralItemGroup GENERAL = new GeneralItemGroup();

    static class BearItemGroup extends ItemGroup {
        public BearItemGroup() {
            super("drinkbeer.beer");
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockRegistry.BEER_MUG.get());
        }
    }

    static class GeneralItemGroup extends ItemGroup {
        GeneralItemGroup() {
            super("drinkbeer.general");
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockRegistry.BEER_BARREL.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> itemStackNonNullList) {
            super.fillItemList(itemStackNonNullList);
            // Order: Beer Barrel, Empty Mug, Call Bell, Recipe Board Package, Recipe Board
            itemStackNonNullList.sort(new Comparator<ItemStack>() {
                @Override
                public int compare(ItemStack o1, ItemStack o2) {
                    int i1 = getIndexNumber(o1);
                    int i2 = getIndexNumber(o2);
                    return i1 - i2;
                }
            });
        }

        private static int getIndexNumber(ItemStack itemStack){
            if(itemStack.getItem() instanceof BlockItem){
                Block block = ((BlockItem) itemStack.getItem()).getBlock();
                if(block instanceof BeerBarrelBlock) return 1;
                else if(block instanceof BeerMugBlock) return 2;
                else if(block instanceof CallBellBlock) return 3;
                else if(block instanceof RecipeBoardPackageBlock) return 4;
                else if(block instanceof RecipeBoardBlock) return 5;
            }
            return 9999;
        }
    }
}
