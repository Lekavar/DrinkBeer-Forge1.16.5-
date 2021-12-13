package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.recipes.TradeRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistry {
    public static class Type {
        public static final IRecipeType<BrewingRecipe> BREWING = IRecipeType.register("drinkbeer:brewing");
        public static final IRecipeType<TradeRecipe> TRADING = IRecipeType.register("drinkbeer:trading");
    }

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "drinkbeer");
    public static final RegistryObject<IRecipeSerializer<BrewingRecipe>> BREWING = RECIPE_SERIALIZERS.register("brewing", BrewingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<TradeRecipe>> TRADING = RECIPE_SERIALIZERS.register("trading", TradeRecipe.Serializer::new);
}
