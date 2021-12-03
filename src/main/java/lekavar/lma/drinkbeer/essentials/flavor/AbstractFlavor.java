package lekavar.lma.drinkbeer.essentials.flavor;

public abstract class AbstractFlavor implements IFlavor {
    abstract boolean isOverrideableBy(IFlavor flavor);
}
