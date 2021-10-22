package lekavar.lma.drinkbeer.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public interface IBrewingInventory extends IInventory {
    /**
     * Must return copy of itemstack in Ingredient Slots
     */
    @Nonnull
    List<ItemStack> getIngredients();

    /**
     * Must return copy of itemstack in Cup Slots
     */
    @Nonnull
    ItemStack getCup();
}
