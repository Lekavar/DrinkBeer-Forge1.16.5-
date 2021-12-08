package lekavar.lma.drinkbeer.essentials.spice;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;

import java.util.List;

public interface ISpice {
    /**
     * return a String as translation key;
     * @return a String, which will be automatically add "name.modid.spice" as prefix when being used.
     */
    String getTranslationKey();

    default String getModId() {
        return DrinkBeer.MODID;
    }

    List<IFlavor> getFlavor();
}
