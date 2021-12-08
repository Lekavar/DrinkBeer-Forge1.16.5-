package lekavar.lma.drinkbeer.essentials.beer;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.essentials.flavor.IFlavor;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public interface IBeer {
    /**
     * return a String as translation key;
     *
     * @return a String, which will be automatically add "name.modid.beer" as prefix when being used.
     */
    String getTranslationKey();

    /**
     * In common situation you only need to override  {@link IBeer#getTranslationKey()}. It will be used to fill the path to model and texture.
     * if you want to use the model and textures of existing beer, change it to the translation key of other existing beer.
     */
    default String getPath() {
        return getTranslationKey();
    }

    /**
     * When you want to place assets in your assets/modid/...，Override this.
     */
    default String getModId() {
        return DrinkBeer.MODID;
    }

    int getNutrition(World world, ItemStack itemStack, LivingEntity drinker);

    float healthModify(World world, ItemStack itemStack, LivingEntity drinker);

    int drunkLevelModify(World world, ItemStack itemStack, LivingEntity drinker);

    List<EffectInstance> getEffect(World world, ItemStack itemStack, LivingEntity drinker, List<IFlavor> flavors);

    void onDrink(World world, ItemStack stack, LivingEntity livingEntity);

    Supplier<Item> getItem();

    /**
     * The extra tooltip will be set to key:
     * name.modid.beer.translation_key.description
     */
    default boolean hasExtraTooltip(){
        return true;
    }

    default Supplier<SoundEvent> getPouringSound(){
        return SoundEventRegistry.POURING;
    }
}