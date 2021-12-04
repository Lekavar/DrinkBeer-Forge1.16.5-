package lekavar.lma.drinkbeer.essentials.flavor;

public abstract class AbstractBaseFlavor implements IFlavor {
    boolean isOverrideableBy(IFlavor flavor){
        return false;
    }
}
