package lekavar.lma.drinkbeer.essentials.flavor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.List;
import java.util.function.Supplier;

public class ComboFlavors {
    private static final BiMap<IFlavor, Byte> CARD_MAPPINGS = HashBiMap.create();

    public static IFlavor Sample = register(SampleComboFlavor::new,(byte)0);

    /**
     * ComboFlavor Need to be registered for serialization
     */
    public static <T extends IFlavor> T register(Supplier<T> flavor, byte id) {
        if (CARD_MAPPINGS.containsValue(id)) {
            throw new RuntimeException("ComboFlavor Registry ID " + id + " has been occupied!" + flavor.get().getTranslationKey() + " is trying to register with duplicated id!");
        } else {
            CARD_MAPPINGS.put(flavor.get(), id);
        }
        return flavor.get();
    }

    public static byte toByte(IFlavor flavor) {
        Byte b = CARD_MAPPINGS.get(flavor);
        if (b == null) {
            throw new RuntimeException("ComboFlavor " + flavor.getTranslationKey() + " hasn't been registered yet!");
        }
        return b;
    }

    public static IFlavor fromByte(byte b) {
        IFlavor flavor = CARD_MAPPINGS.inverse().get(b);
        if (flavor == null) {
            throw new RuntimeException("Retrieve ComboFlavor Info by id " + b + " wrongly! Beer Registry ID might been changed!");
        }
        return flavor;
    }

    static class SampleComboFlavor extends AbstractComboFlavor{
        @Override
        boolean isFlavorQualified(List<IFlavor> flavors) {
            return false;
        }

        @Override
        public String getTranslationKey() {
            return "sample_combo";
        }
    }
}
