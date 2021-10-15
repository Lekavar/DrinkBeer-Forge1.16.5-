package lekavar.lma.drinkbeer.registry;

import lekavar.lma.drinkbeer.groups.BeerGroup;
import lekavar.lma.drinkbeer.groups.DrinkBeerGeneralBlockGroup;
import net.minecraft.item.ItemGroup;

public class GroupRegistry {
    public static final ItemGroup BEER_GROUP = new BeerGroup("drinkbeer.beer");
    public static final ItemGroup GENERAL_BLOCK_GROUP = new DrinkBeerGeneralBlockGroup("drinkbeer.general");
}
