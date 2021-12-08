package lekavar.lma.drinkbeer.capability.beerinfo;

import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;

import java.util.ArrayList;
import java.util.List;

public class BeerInfo implements IBeerInfo {
    boolean f;
    List<IFlavor> baseFlavor;
    List<IFlavor> comboFlavor;

    public BeerInfo() {
        f = false;
        baseFlavor = new ArrayList<>();
        comboFlavor = new ArrayList<>();
    }

    @Override
    public boolean isFlavoredBeer() {
        return f;
    }

    @Override
    public void setFlavoredBeer(boolean b) {
        f = b;
    }

    @Override
    public List<IFlavor> getBaseFlavor() {
        return baseFlavor;
    }

    @Override
    public List<IFlavor> getComboFlavor() {
        return comboFlavor;
    }

    @Override
    public void setBaseFlavor(List<IFlavor> flavors) {
        baseFlavor = flavors;
    }

    @Override
    public void setComboFlavor(List<IFlavor> flavors) {
        comboFlavor = flavors;
    }
}
