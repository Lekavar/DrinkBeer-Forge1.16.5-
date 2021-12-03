package lekavar.lma.drinkbeer.essentials.beer;

import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.List;

public interface IBeer {
    /**
     * return a String as translation key;
     * @return a String, which will be automatically add "name.drinkbeer.beer" as prefix when being used.
     */
    String getTranslationKey();

    int getNutrition(World world, ItemStack itemStack, LivingEntity drinker);

    float healthModify(World world, ItemStack itemStack, LivingEntity drinker);

    int drunkLevelModify(World world, ItemStack itemStack, LivingEntity drinker);

    List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors);

    void onDrink(World world, ItemStack stack, LivingEntity livingEntity);
}