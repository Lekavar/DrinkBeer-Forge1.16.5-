package lekavar.lma.drinkbeer.essentials.spice;

import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;

import java.util.List;

public interface Spice {
    /**
     * return a String as translation key;
     * @return a String, which will be automatically add "name.drinkbeer.spice" as prefix when being used.
     */
    String getTranslationKey();

    List<IFlavor> getFlavor();
}
