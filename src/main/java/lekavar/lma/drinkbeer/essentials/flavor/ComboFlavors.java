package lekavar.lma.drinkbeer.essentials.flavor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class ComboFlavors {
    private static final BiMap<IFlavor, Byte> CARD_MAPPINGS = HashBiMap.create();

    public static IFlavor SO_SPICY = register(SoSpicy::new,(byte)0);
    public static IFlavor THE_FALL_OF_THE_GIANT = register(TheFallOfTheGiant::new,(byte)0);

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

    static class SoSpicy extends AbstractComboFlavor{
        @Override
        boolean isFlavorQualified(List<IFlavor> flavors) {
            return flavors.stream().filter(flavor -> flavor.equals(BaseFlavors.SPICY) || flavor.equals(BaseFlavors.FIERY)).count() == 3;
        }

        @Override
        public String getTranslationKey() {
            return "so_spicy";
        }

        @Override
        public void onDrink(World world, ItemStack stack, LivingEntity drinker, List<IFlavor> baseFlavorsSet, List<IFlavor> comboFlavorsSet) {
            if(!world.isClientSide())
                drinker.setRemainingFireTicks(drinker.getRemainingFireTicks()+100);
        }
    }

    static class TheFallOfTheGiant extends AbstractComboFlavor{
        @Override
        boolean isFlavorQualified(List<IFlavor> flavors) {
            return flavors.stream().filter(flavor -> flavor.equals(BaseFlavors.STORMY)).count() == 3;
        }

        @Override
        public String getTranslationKey() {
            return "the_fall_of_the_giant";
        }
    }
}
