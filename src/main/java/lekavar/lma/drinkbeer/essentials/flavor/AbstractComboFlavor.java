package lekavar.lma.drinkbeer.essentials.flavor;

import java.util.List;

public abstract class AbstractComboFlavor implements IFlavor {
    abstract boolean isFlavorQualified(List<IFlavor> flavors);
}

