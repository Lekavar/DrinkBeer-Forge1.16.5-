package lekavar.lma.drinkbeer.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.inventory.IInventory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  Well, since this recipe is not actually a typical recipe, it is more like a "loot-table".
 *  So, all {@link #assemble(IInventory)}, {@link #matches(IInventory, World)}, {@link #getResultItem()} function should not be used.
 *  use {@link #generateNeed(int)} and {@link #generateOffer(int)}!
 */
public class TradeRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private final String residentNameKey;
    private final String locationNameKey;
    private final NonNullList<Good> needNormal;
    private final NonNullList<Good> needRare;
    private final NonNullList<Good> offerNormal;
    private final NonNullList<Good> offerRare;
    private final NonNullList<Good> offerSuperRare;
    private final NonNullList<Good> offerUnique;

    public TradeRecipe(ResourceLocation id, String residentNameKey, String locationNameKey, NonNullList<Good> needNormal, NonNullList<Good> needRare, NonNullList<Good> offerNormal, NonNullList<Good> offerRare, NonNullList<Good> offerSuperRare, NonNullList<Good> offerUnique) {
        this.id = id;
        this.residentNameKey = residentNameKey;
        this.locationNameKey = locationNameKey;
        this.needNormal = needNormal;
        this.needRare = needRare;
        this.offerNormal = offerNormal;
        this.offerRare = offerRare;
        this.offerSuperRare = offerSuperRare;
        this.offerUnique = offerUnique;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        return false;
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.TRADING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeRegistry.Type.TRADING;
    }

    public List<ItemStack> generateNeed(int count){
        if(count<1 || count>4)
            throw new RuntimeException("Invalid request number of need good to generate! It should be >0 and <5!");
        List<ItemStack> ret = new ArrayList<>();
        for(int i=0;i<count;i++){
            double x = Math.random();
            if(x<12D/19D)
                ret.add(randomPick(needNormal));
            else
                ret.add(randomPick(needRare));
        }
        return ret;
    }

    public List<ItemStack> generateOffer(int count){
        if(count<1 || count>4)
            throw new RuntimeException("Invalid request number of offer good to generate! It should be >0 and <5!");
        List<ItemStack> ret = new ArrayList<>();
        for(int i=0;i<count;i++){
            double x = Math.random();
            if(x<0.48D)
                ret.add(randomPick(offerNormal));
            else if(x<0.76D)
                ret.add(randomPick(offerRare));
            else if(x<0.91D)
                ret.add(randomPick(offerSuperRare));
            else
                ret.add(randomPick(offerUnique));
        }
        return ret;
    }

    private ItemStack randomPick(NonNullList<Good> goods){
        // Well, I'll get it done the stupid way:
        if(goods.isEmpty()){
            if(goods.equals(needRare))
                return randomPick(needNormal);
            else if(goods.equals(offerUnique))
                return randomPick(offerSuperRare);
            else if(goods.equals(offerSuperRare))
                return randomPick(offerRare);
            else if(goods.equals(offerRare))
                return randomPick(offerNormal);
        }
        return goods.get(new Random().nextInt(goods.size())).generateItemStack();
    }

    public String getResidentNameKey() {
        return residentNameKey;
    }

    public String getLocationNameKey() {
        return locationNameKey;
    }

    public NonNullList<Good> getNeedNormal() {
        return needNormal;
    }

    public NonNullList<Good> getNeedRare() {
        return needRare;
    }

    public NonNullList<Good> getOfferNormal() {
        return offerNormal;
    }

    public NonNullList<Good> getOfferRare() {
        return offerRare;
    }

    public NonNullList<Good> getOfferSuperRare() {
        return offerSuperRare;
    }

    public NonNullList<Good> getOfferUnique() {
        return offerUnique;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TradeRecipe> {
        private static Good DUMMY_GOOD = new Good(ItemStack.EMPTY,1,1);

        @Override
        public TradeRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String residentNameKey = JSONUtils.getAsString(jsonObject, "residentNameKey");
            if(residentNameKey.isEmpty())
                throw new JsonParseException("residentNameKey of trade recipe cannot be empty!");
            String locationNameKey = JSONUtils.getAsString(jsonObject, "locationNameKey");
            if(locationNameKey.isEmpty())
                throw new JsonParseException("locationNameKey of trade recipe cannot be empty!");

            JsonObject need = JSONUtils.getAsJsonObject(jsonObject, "need");
            NonNullList<Good> n0 = goodsFromJson(need.getAsJsonArray("normal"));
            if (n0.isEmpty()) {
                throw new JsonParseException("No normal need goods for trade recipe.");
            }
            NonNullList<Good> n1 = NonNullList.create();
            if(need.has("rare"))
                 n1 = goodsFromJson(JSONUtils.getAsJsonObject(jsonObject, "need").getAsJsonArray("rare"));

            JsonObject offer = JSONUtils.getAsJsonObject(jsonObject, "offer");
            NonNullList<Good> o0 = goodsFromJson(JSONUtils.getAsJsonObject(jsonObject, "offer").getAsJsonArray("normal"));
            if (o0.isEmpty()) {
                throw new JsonParseException("No normal offer goods for trade recipe.");
            }
            NonNullList<Good> o1 = NonNullList.create();
            NonNullList<Good> o2 = NonNullList.create();
            NonNullList<Good> o3 = NonNullList.create();

            if(offer.has("rare")){
                o1 = goodsFromJson(JSONUtils.getAsJsonObject(jsonObject, "offer").getAsJsonArray("rare"));
            }
            if(offer.has("super_rare")){
                o1 = goodsFromJson(JSONUtils.getAsJsonObject(jsonObject, "offer").getAsJsonArray("super_rare"));
            }
            if(offer.has("unique")){
                o1 = goodsFromJson(JSONUtils.getAsJsonObject(jsonObject, "offer").getAsJsonArray("unique"));
            }

            return new TradeRecipe(resourceLocation,residentNameKey,locationNameKey,n0,n1,o0,o1,o2,o3);
        }

        private static NonNullList<Good> goodsFromJson(JsonArray jsonArray) {
            NonNullList<Good> ingredients = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Good good = Good.fromJson(jsonArray.get(i).getAsJsonObject());
                ingredients.add(good);
            }
            return ingredients;
        }

        @Nullable
        @Override
        public TradeRecipe fromNetwork(ResourceLocation resourceLocation, PacketBuffer packetBuffer) {
            String residentNameKey = packetBuffer.readUtf();
            String locationNameKey = packetBuffer.readUtf();
            int i = packetBuffer.readVarInt();
            NonNullList<Good> n0 = NonNullList.withSize(i,DUMMY_GOOD);
            for(int j = 0; j < n0.size(); ++j) {
                n0.set(j, Good.fromNetwork(packetBuffer));
            }
            i = packetBuffer.readVarInt();
            NonNullList<Good> n1 = NonNullList.create();
            if(i!=0){
                n1 = NonNullList.withSize(i,DUMMY_GOOD);
                for(int j = 0; j < n1.size(); ++j) {
                    n1.set(j, Good.fromNetwork(packetBuffer));
                }
            }

            i = packetBuffer.readVarInt();
            NonNullList<Good> o0 = NonNullList.withSize(i,DUMMY_GOOD);
            for(int j = 0; j < o0.size(); ++j) {
                o0.set(j, Good.fromNetwork(packetBuffer));
            }
            i = packetBuffer.readVarInt();
            NonNullList<Good> o1 = NonNullList.create();
            if(i!=0){
                o1 = NonNullList.withSize(i,DUMMY_GOOD);
                for(int j = 0; j < o1.size(); ++j) {
                    o1.set(j, Good.fromNetwork(packetBuffer));
                }
            }
            i = packetBuffer.readVarInt();
            NonNullList<Good> o2 = NonNullList.create();
            if(i!=0){
                o2 = NonNullList.withSize(i,DUMMY_GOOD);
                for(int j = 0; j < o2.size(); ++j) {
                    o2.set(j, Good.fromNetwork(packetBuffer));
                }
            }
            i = packetBuffer.readVarInt();
            NonNullList<Good> o3 = NonNullList.create();
            if(i!=0){
                o3 = NonNullList.withSize(i,DUMMY_GOOD);
                for(int j = 0; j < o3.size(); ++j) {
                    o3.set(j, Good.fromNetwork(packetBuffer));
                }
            }
            return new TradeRecipe(resourceLocation,residentNameKey,locationNameKey,n0,n1,o0,o1,o2,o3);
        }

        @Override
        public void toNetwork(PacketBuffer packetBuffer, TradeRecipe tradeRecipe) {
            packetBuffer.writeUtf(tradeRecipe.residentNameKey);
            packetBuffer.writeUtf(tradeRecipe.locationNameKey);
            packetBuffer.writeInt(tradeRecipe.needNormal.size());
            for(Good good:tradeRecipe.needNormal){
                good.toNetwork(packetBuffer);
            }
            packetBuffer.writeInt(tradeRecipe.needRare.size());
            if(tradeRecipe.needRare.size()>0)
                for(Good good:tradeRecipe.needRare){
                    good.toNetwork(packetBuffer);
                }
            packetBuffer.writeInt(tradeRecipe.offerNormal.size());
            for(Good good:tradeRecipe.offerNormal){
                good.toNetwork(packetBuffer);
            }
            packetBuffer.writeInt(tradeRecipe.offerRare.size());
            if(tradeRecipe.offerRare.size()>0)
                for(Good good:tradeRecipe.offerRare){
                    good.toNetwork(packetBuffer);
                }
            packetBuffer.writeInt(tradeRecipe.offerSuperRare.size());
            if(tradeRecipe.offerSuperRare.size()>0)
                for(Good good:tradeRecipe.offerSuperRare){
                    good.toNetwork(packetBuffer);
                }
            packetBuffer.writeInt(tradeRecipe.offerUnique.size());
            if(tradeRecipe.offerUnique.size()>0)
                for(Good good:tradeRecipe.offerUnique){
                    good.toNetwork(packetBuffer);
                }
        }
    }

    public static class Good{
        private final ItemStack itemStack;
        private final int min;
        private final int max;

        public Good(ItemStack itemStack, int min, int max) {
            this.itemStack = itemStack;
            this.min = min;
            this.max = max;
        }

        /**
         * Display Only, the count is fixed.
         */
        public ItemStack getItemStack() {
            return itemStack;
        }

        public ItemStack generateItemStack(){
            ItemStack ret = itemStack.copy();
            ret.setCount(min+new Random().nextInt(max-min+1));
            return ret;
        }

        public static Good fromJson(JsonObject jsonObject){
            ItemStack itemStack = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(jsonObject, "good"), true);
            int max = JSONUtils.getAsInt(jsonObject, "max");
            if(max<=0)
                throw new JsonParseException("Good max count is not valid.");
            int min = JSONUtils.getAsInt(jsonObject, "min");
            if(min<=0)
                throw new JsonParseException("Good min count is not valid.");
            if(max<min)
                throw new JsonParseException("Good max count is smaller than min count.");
            return new Good(itemStack,min,max);
        }

        public void toNetwork(PacketBuffer packetBuffer) {
            packetBuffer.writeItem(itemStack);
            packetBuffer.writeInt(min);
            packetBuffer.writeInt(max);
        }

        public static Good fromNetwork(PacketBuffer packetBuffer){
            ItemStack itemStack = packetBuffer.readItem();
            int min = packetBuffer.readInt();
            int max = packetBuffer.readInt();
            return new Good(itemStack,min,max);
        }
    }
}
