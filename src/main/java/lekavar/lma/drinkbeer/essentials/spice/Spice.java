package lekavar.lma.drinkbeer.essentials.spice;

import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;

public interface Spice {
    /**
     * return a String as translation key;
     * @return a String, which will be automatically add "name.drinkbeer.spice" as prefix when being used.
     */
    String getTranslationKey();

    IFlavor getFlavor();
}
