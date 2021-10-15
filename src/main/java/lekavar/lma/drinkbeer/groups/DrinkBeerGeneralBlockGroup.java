package lekavar.lma.drinkbeer.groups;

import lekavar.lma.drinkbeer.registry.BlockRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class DrinkBeerGeneralBlockGroup extends ItemGroup {
    public DrinkBeerGeneralBlockGroup(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(BlockRegistry.beerBarrel.get());
    }
}

