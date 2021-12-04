package lekavar.lma.drinkbeer.capability.beerinfo;

import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;

import java.util.List;

public interface IBeerInfo {
    boolean isFlavoredBeer();

    void setFlavoredBeer(boolean b);

    List<IFlavor> getBaseFlavor();

    List<IFlavor> getComboFlavor();

    void setBaseFlavor(List<IFlavor> flavors);

    void setComboFlavor(List<IFlavor> flavors);
}
