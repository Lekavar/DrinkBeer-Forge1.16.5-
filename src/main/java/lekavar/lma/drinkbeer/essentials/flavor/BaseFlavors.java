package lekavar.lma.drinkbeer.essentials.flavor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BaseFlavors {
    private static final BiMap<IFlavor, Byte> DATA_MAPPING = HashBiMap.create();

    public static IFlavor SPICY = new Spicy();
    public static IFlavor FIERY = new Fiery();
    public static IFlavor AROMATIC = new Aromatic();
    public static IFlavor MORE_AROMATIC = new MoreAromatic();
    public static IFlavor REFRESHING = new Refreshing();
    public static IFlavor MORE_REFRESHING = new MoreRefreshing();
    public static IFlavor STORMY = new Stormy();

    public static void registerAll() {
        register(SPICY, (byte) 0);
        register(FIERY, (byte) 1);
        register(AROMATIC, (byte) 2);
        register(MORE_AROMATIC, (byte) 3);
        register(REFRESHING, (byte) 4);
        register(MORE_REFRESHING, (byte) 5);
        register(STORMY, (byte) 6);
    }

    /**
     * Flavor Need to be registered for serialization
     */
    public static void register(IFlavor flavor, byte id) {
        if (DATA_MAPPING.containsValue(id)) {
            throw new RuntimeException("Flavor Registry ID " + id + " has been occupied!" + flavor.getTranslationKey() + " is trying to register with duplicated id!");
        } else {
            DATA_MAPPING.put(flavor, id);
        }
    }

    public static byte toByte(IFlavor flavor) {
        Byte b = DATA_MAPPING.get(flavor);
        if (b == null) {
            throw new RuntimeException("Flavor " + flavor.getTranslationKey() + " hasn't been registered yet!");
        }
        return b;
    }

    public static IFlavor fromByte(byte b) {
        IFlavor flavor = DATA_MAPPING.inverse().get(b);
        if (flavor == null) {
            throw new RuntimeException("Retrieve Flavor Info by id " + b + " wrongly! Beer Registry ID might been changed!");
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

    static class Spicy extends AbstractBaseFlavor {

        @Override
        public String getTranslationKey() {
            return "spicy";
        }

        @Override
        public EffectInstance modifyEffect(World world, ItemStack stack, LivingEntity livingEntity, EffectInstance effectInstance) {
            int duration = (int) (effectInstance.getDuration() * 1.8);
            return new EffectInstance(effectInstance.getEffect(), duration);
        }

        @Override
        public float modifyHealthModifier(World world, ItemStack stack, LivingEntity livingEntity, float healthModifier) {
            return healthModifier - 3;
        }

        @Override
        public List<IFlavor> getOverridableFlavor() {
            return Lists.newArrayList(FIERY);
        }
    }

    static class Fiery extends AbstractBaseFlavor {
        @Override
        public String getTranslationKey() {
            return "fiery";
        }

        @Override
        public EffectInstance modifyEffect(World world, ItemStack stack, LivingEntity livingEntity, EffectInstance effectInstance) {
            int duration = effectInstance.getDuration() * 2;
            return new EffectInstance(effectInstance.getEffect(), duration);
        }

        @Override
        public float modifyHealthModifier(World world, ItemStack stack, LivingEntity livingEntity, float healthModifier) {
            return healthModifier - 4;
        }
    }

    static class Aromatic extends AbstractBaseFlavor {

        @Override
        public String getTranslationKey() {
            return "aromatic";
        }

        @Override
        public EffectInstance modifyEffect(World world, ItemStack stack, LivingEntity livingEntity, EffectInstance effectInstance) {
            int duration = effectInstance.getDuration() + 800;
            return new EffectInstance(effectInstance.getEffect(), duration);
        }

        @Override
        public List<IFlavor> getOverridableFlavor() {
            return Lists.newArrayList(MORE_AROMATIC);
        }
    }

    static class MoreAromatic extends AbstractBaseFlavor {
        @Override
        public String getTranslationKey() {
            return "more_aromatic";
        }

        @Override
        public EffectInstance modifyEffect(World world, ItemStack stack, LivingEntity livingEntity, EffectInstance effectInstance) {
            int duration = effectInstance.getDuration() + 1200;
            return new EffectInstance(effectInstance.getEffect(), duration);
        }
    }

    static class Refreshing extends AbstractBaseFlavor {

        @Override
        public String getTranslationKey() {
            return "refreshing";
        }

        @Override
        public int modifyDrunkLevelModifier(World world, ItemStack itemStack, LivingEntity drinker, int drunkLevelModifier) {
            return drunkLevelModifier - 1;
        }

        @Override
        public List<IFlavor> getOverridableFlavor() {
            return Lists.newArrayList(MORE_REFRESHING);
        }
    }

    static class MoreRefreshing extends AbstractBaseFlavor {
        @Override
        public String getTranslationKey() {
            return "more_refreshing";
        }

        @Override
        public int modifyDrunkLevelModifier(World world, ItemStack itemStack, LivingEntity drinker, int drunkLevelModifier) {
            return drunkLevelModifier - 2;
        }
    }

    static class Stormy extends AbstractBaseFlavor {
        @Override
        public String getTranslationKey() {
            return "stormy";
        }

        @Override
        public void onDrink(World world, ItemStack stack, LivingEntity drinker, List<IFlavor> baseFlavorsSet, List<IFlavor> comboFlavorsSet) {
            int level = (int) comboFlavorsSet.stream().filter(flavor -> flavor.equals(STORMY)).count();
            int sideLength = level == 1 ? 13 : level == 2 ? 17 : 31;
            //TODO implement destroy trees and leaves function
        }
    }
}
