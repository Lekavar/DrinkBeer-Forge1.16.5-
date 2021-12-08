package lekavar.lma.drinkbeer.essentials.flavor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBaseFlavor implements IFlavor {
    public List<IFlavor> getOverridableFlavor(){
        return new ArrayList<>();
    }
}
