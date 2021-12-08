package lekavar.lma.drinkbeer.essentials.flavor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ComboFlavors {
    private static final BiMap<IFlavor, Byte> DATA_MAPPING = HashBiMap.create();

    public static IFlavor SO_SPICY = new SoSpicy();
    public static IFlavor THE_FALL_OF_THE_GIANT = new TheFallOfTheGiant();

    public static void registerAll() {
        register(SO_SPICY, (byte) 0);
        register(THE_FALL_OF_THE_GIANT, (byte) 1);
    }

    /**
     * ComboFlavor Need to be registered for serialization
     */
    public static void register(IFlavor flavor, byte id) {
        if (DATA_MAPPING.containsValue(id)) {
            throw new RuntimeException("ComboFlavor Registry ID " + id + " has been occupied!" + flavor.getTranslationKey() + " is trying to register with duplicated id!");
        } else {
            DATA_MAPPING.put(flavor, id);
        }
    }

    public static byte toByte(IFlavor flavor) {
        Byte b = DATA_MAPPING.get(flavor);
        if (b == null) {
            throw new RuntimeException("ComboFlavor " + flavor.getTranslationKey() + " hasn't been registered yet!");
        }
        return b;
    }

    public static IFlavor fromByte(byte b) {
        IFlavor flavor = DATA_MAPPING.inverse().get(b);
        if (flavor == null) {
            throw new RuntimeException("Retrieve ComboFlavor Info by id " + b + " wrongly! Beer Registry ID might been changed!");
        }
        return flavor;
    }

    public static byte[] toByteArray(List<IFlavor> flavors, int size) {
        byte[] bytes = new byte[size];
        for (int i = 0; i < flavors.size(); i++) {
            bytes[i] = toByte(flavors.get(i));
        }
        return bytes;
    }

    public static List<IFlavor> fromByteArray(byte[] bytes, int size, int viable) {
        if (bytes.length != size)
            throw new RuntimeException("Base Flavors from byte array has encountered an error: array length did not match");
        List<IFlavor> flavors = new ArrayList<>();
        for (int i = 0; i < viable; i++) {
            flavors.add(fromByte(bytes[i]));
        }
        return flavors;
    }

    public static List<IFlavor> getCorrespondFlavor(List<IFlavor> flavors){
        List<IFlavor> ret = new ArrayList<>();
        for(IFlavor flavor:DATA_MAPPING.keySet()){
            if(((AbstractComboFlavor) flavor).isFlavorQualified(flavors))
                ret.add(flavor);
        }
        return ret;
    }


    static class SoSpicy extends AbstractComboFlavor {
        @Override
        boolean isFlavorQualified(List<IFlavor> flavors) {
            return flavors.stream().filter(flavor -> {
                if(flavor.equals(BaseFlavors.SPICY))
                    return true;
                for(IFlavor flavor1:((AbstractBaseFlavor) BaseFlavors.SPICY).getOverridableFlavor()){
                    if(flavor.equals(flavor1))
                        return true;
                }
                return false;
            }).count() == 3;
        }

        @Override
        public String getTranslationKey() {
            return "so_spicy";
        }

        @Override
        public void onDrink(World world, ItemStack stack, LivingEntity drinker, List<IFlavor> baseFlavorsSet, List<IFlavor> comboFlavorsSet) {
            if (!world.isClientSide())
                drinker.setRemainingFireTicks(drinker.getRemainingFireTicks() + 100);
        }
    }

    static class TheFallOfTheGiant extends AbstractComboFlavor {
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
