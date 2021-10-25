package lekavar.lma.drinkbeer.utils;

import lekavar.lma.drinkbeer.registries.BlockRegistry;
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
            // TODO: waiting for fabric port deciding
            itemStackNonNullList.sort(new Comparator<ItemStack>() {
                @Override
                public int compare(ItemStack o1, ItemStack o2) {
                    return 0;
                }
            });
            super.fillItemList(itemStackNonNullList);
        }
    }
}
