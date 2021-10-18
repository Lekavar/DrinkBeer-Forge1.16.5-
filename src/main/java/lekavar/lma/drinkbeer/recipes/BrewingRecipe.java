package lekavar.lma.drinkbeer.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import lekavar.lma.drinkbeer.tileentity.BeerBarrelTileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;


//TODO 先重写完酒桶
public class BrewingRecipe implements IRecipe<BeerBarrelTileEntity> {
    private final ResourceLocation id;
    private final NonNullList<ItemStack> input;
    private final int requiredEmptyCup;
    private final int brewingTime;
    private final ItemStack result;

    public BrewingRecipe(ResourceLocation id, NonNullList<ItemStack> input, int requiredEmptyCup, int brewingTime, ItemStack result) {
        this.id = id;
        this.input = input;
        this.requiredEmptyCup = requiredEmptyCup;
        this.brewingTime = brewingTime;
        this.result = result;
    }

    @Override
    public boolean matches(BeerBarrelTileEntity p_77569_1_, World p_77569_2_) {
        List<Item> testTarget = input.stream().map(ItemStack::copy).map(ItemStack::getItem).collect(Collectors.toList());
        List<ItemStack> beCompared = p_77569_1_.getIngredientItems();
        for (ItemStack itemStack : beCompared) {
            if (testTarget.contains(itemStack.getItem())) {
                testTarget.remove(itemStack.getItem());
            } else {
                return false;
            }
        }
        return testTarget.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack assemble(BeerBarrelTileEntity p_77572_1_) {
        return result.copy();
    }

    // Can Craft at any dimension
    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }


    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book).
     * If your recipe has more than one possible result (e.g. it's dynamic and depends on its inputs),
     * then return an empty stack.
     */
    @Override
    public ItemStack getResultItem() {
        //For Safety, I use #copy
        return result.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.BREWING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeRegistry.Type.BREWING;
    }

    public int getRequiredEmptyCup() {
        return requiredEmptyCup;
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BrewingRecipe> {

        @Override
        public BrewingRecipe fromJson(ResourceLocation p_199425_1_, JsonObject p_199425_2_) {
            NonNullList<ItemStack> ingredients = itemsFromJson(JSONUtils.getAsJsonArray(p_199425_2_, "ingredients"));
            int requiredEmptyCut = JSONUtils.getAsInt(p_199425_2_, "required_empty_cup");
            int brewing_time = JSONUtils.getAsInt(p_199425_2_, "brewing_time");
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(p_199425_2_, "result"), true);
            return new BrewingRecipe(p_199425_1_, ingredients, requiredEmptyCut, brewing_time, result);
        }

        private static NonNullList<ItemStack> itemsFromJson(JsonArray p_199568_0_) {
            NonNullList<ItemStack> ingredients = NonNullList.create();
            for (int i = 0; i < p_199568_0_.size(); ++i) {
                ItemStack ingredient = CraftingHelper.getItemStack(p_199568_0_.get(i).getAsJsonObject(), false);
                ingredients.add(ingredient);
            }
            return ingredients;
        }

        @Nullable
        @Override
        public BrewingRecipe fromNetwork(ResourceLocation p_199426_1_, PacketBuffer p_199426_2_) {
            int i = p_199426_2_.readVarInt();
            NonNullList<ItemStack> ingredients = NonNullList.withSize(i, ItemStack.EMPTY);
            for (int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, p_199426_2_.readItem());
            }
            int requiredEmptyCup = p_199426_2_.readVarInt();
            int brewingTime = p_199426_2_.readVarInt();
            ItemStack result = p_199426_2_.readItem();
            return new BrewingRecipe(p_199426_1_, ingredients, requiredEmptyCup, brewingTime, result);
        }

        @Override
        public void toNetwork(PacketBuffer p_199427_1_, BrewingRecipe p_199427_2_) {
            p_199427_1_.writeVarInt(p_199427_2_.input.size());
            for (ItemStack ingredient : p_199427_2_.input) {
                p_199427_1_.writeItem(ingredient);
            }
            p_199427_1_.writeVarInt(p_199427_2_.requiredEmptyCup);
            p_199427_1_.writeVarInt(p_199427_2_.brewingTime);
            p_199427_1_.writeItem(p_199427_2_.result);

        }
    }
}
