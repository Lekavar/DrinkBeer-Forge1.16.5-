package lekavar.lma.drinkbeer.misc;

import lekavar.lma.drinkbeer.essentials.beer.IBeer;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.essentials.spice.ISpice;

public class I18nKey {
    public static String of(ISpice spice){
        return "name." + spice.getModId() +".spice." + spice.getTranslationKey();
    }

    public static String of(IFlavor flavor){
        return "name." + flavor.getModId() +".flavor." + flavor.getTranslationKey();
    }

    public static String of(IBeer beer){
        return "name." + beer.getModId() +".beer." + beer.getTranslationKey();
    }
}
