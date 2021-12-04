package lekavar.lma.drinkbeer.essentials.flavor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.List;

public interface IFlavor {
    /**
     * return a String as translation key;
     * @return a String, which will be automatically add "name.drinkbeer.flavor" as prefix when being used.
     */
    String getTranslationKey();

    default EffectInstance modifyEffect(World world, ItemStack stack, LivingEntity livingEntity, EffectInstance effectInstance){
        return effectInstance;
    }

    default float modifyHealthModifier(World world, ItemStack stack, LivingEntity livingEntity, float healthModifier){
        return healthModifier;
    }

    default int modifyNutrition(World world, ItemStack stack, LivingEntity livingEntity, int nutrition){
        return nutrition;
    }

    default int modifyDrunkLevelModifier(World world, ItemStack itemStack, LivingEntity drinker, int drunkLevelModifier){
        return drunkLevelModifier;
    }

    default void onDrink(World world, ItemStack stack, LivingEntity drinker, List<IFlavor> baseFlavorsSet, List<IFlavor> comboFlavorsSet){}
}
