package lekavar.lma.drinkbeer.essentials.flavor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class BaseFlavors {
    private static final BiMap<IFlavor, Byte> CARD_MAPPINGS = HashBiMap.create();

    public static IFlavor SPICY = register(Spicy::new,(byte) 0);
    public static IFlavor FIERY= register(Fiery::new,(byte) 1);
    public static IFlavor AROMATIC= register(Aromatic::new,(byte) 2);
    public static IFlavor MORE_AROMATIC= register(MoreAromatic::new,(byte) 3);
    public static IFlavor REFRESHING= register(Refreshing::new,(byte) 4);
    public static IFlavor MORE_REFRESHING= register(MoreRefreshing::new,(byte) 5);
    public static IFlavor STORMY= register(Stormy::new,(byte) 6);

    /**
     * Flavor Need to be registered for serialization
     */
    public static <T extends IFlavor> T register(Supplier<T> flavor, byte id) {
        if (CARD_MAPPINGS.containsValue(id)) {
            throw new RuntimeException("Flavor Registry ID " + id + " has been occupied!" + flavor.get().getTranslationKey() + " is trying to register with duplicated id!");
        } else {
            CARD_MAPPINGS.put(flavor.get(), id);
        }
        return flavor.get();
    }

    public static byte toByte(IFlavor flavor) {
        Byte b = CARD_MAPPINGS.get(flavor);
        if (b == null) {
            throw new RuntimeException("Flavor " + flavor.getTranslationKey() + " hasn't been registered yet!");
        }
        return b;
    }

    public static IFlavor fromByte(byte b) {
        IFlavor flavor = CARD_MAPPINGS.inverse().get(b);
        if (flavor == null) {
            throw new RuntimeException("Retrieve Flavor Info by id " + b + " wrongly! Beer Registry ID might been changed!");
        }
        return flavor;
    }

    static class Spicy extends AbstractBaseFlavor {
        @Override
        boolean isOverrideableBy(IFlavor flavor) {
            return flavor.equals(FIERY);
        }

        @Override
        public String getTranslationKey() {
            return "spicy";
        }

        @Override
        public EffectInstance modifyEffect(World world, ItemStack stack, LivingEntity livingEntity, EffectInstance effectInstance) {
            int duration = (int) (effectInstance.getDuration() * 1.8);
            return new EffectInstance(effectInstance.getEffect(),duration);
        }

        @Override
        public float modifyHealthModifier(World world, ItemStack stack, LivingEntity livingEntity, float healthModifier) {
            return healthModifier - 3;
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
            return new EffectInstance(effectInstance.getEffect(),duration);
        }

        @Override
        public float modifyHealthModifier(World world, ItemStack stack, LivingEntity livingEntity, float healthModifier) {
            return healthModifier - 4;
        }
    }

    static class Aromatic extends AbstractBaseFlavor {
        @Override
        boolean isOverrideableBy(IFlavor flavor) {
            return flavor.equals(MORE_AROMATIC);
        }

        @Override
        public String getTranslationKey() {
            return "aromatic";
        }

        @Override
        public EffectInstance modifyEffect(World world, ItemStack stack, LivingEntity livingEntity, EffectInstance effectInstance) {
            int duration = effectInstance.getDuration() + 800;
            return new EffectInstance(effectInstance.getEffect(),duration);
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
            return new EffectInstance(effectInstance.getEffect(),duration);
        }
    }

    static class Refreshing extends AbstractBaseFlavor {
        @Override
        boolean isOverrideableBy(IFlavor flavor) {
            return flavor.equals(MORE_REFRESHING);
        }

        @Override
        public String getTranslationKey() {
            return "refreshing";
        }

        @Override
        public int modifyDrunkLevelModifier(World world, ItemStack itemStack, LivingEntity drinker, int drunkLevelModifier) {
            return drunkLevelModifier - 1;
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
            int sideLength = level == 1? 13 : level == 2? 17 : 31;
            //TODO implement destroy trees and leaves function
        }
    }
}
