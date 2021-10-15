package lekavar.lma.drinkbeer.registry;

import lekavar.lma.drinkbeer.items.BeerMugItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static lekavar.lma.drinkbeer.registry.GroupRegistry.*;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "drinkbeer");

    //general
    public static final RegistryObject<Item> beerBarrel = ITEMS.register("beer_barrel", () -> new BlockItem(BlockRegistry.beerBarrel.get(), new Item.Properties().tab(GENERAL_BLOCK_GROUP)));
    public static final RegistryObject<Item> emptyBeerMug = ITEMS.register("empty_beer_mug", () -> new BlockItem(BlockRegistry.emptyBeerMug.get(), new Item.Properties().tab(GENERAL_BLOCK_GROUP)));

    //beer
    public static final RegistryObject<Item> beerMug = ITEMS.register("beer_mug", () -> new BeerMugItem(BlockRegistry.beerMugBlock.get(), new Item.Properties().tab(BEER_GROUP).food(new Food.Builder().nutrition(2).alwaysEat().effect(() -> new EffectInstance(Effects.DIG_SPEED, 1200), 1).build())));
    public static final RegistryObject<Item> beerMugBlazeStout = ITEMS.register("beer_mug_blaze_stout", () -> new BeerMugItem(BlockRegistry.beerMugBlazeStoutBlock.get(), new Item.Properties().tab(BEER_GROUP).food(new Food.Builder().nutrition(2).alwaysEat().effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 1800), 1).build())));
    public static final RegistryObject<Item> beerMugBlazeMilkStout = ITEMS.register("beer_mug_blaze_milk_stout", () -> new BeerMugItem(BlockRegistry.beerMugBlazeMilkStoutBlock.get(), new Item.Properties().tab(BEER_GROUP).food(new Food.Builder().nutrition(2).alwaysEat().effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 2400), 1).build())));
    public static final RegistryObject<Item> beerMugAppleLambic = ITEMS.register("beer_mug_apple_lambic", () -> new BeerMugItem(BlockRegistry.beerMugAppleLambicBlock.get(), new Item.Properties().tab(BEER_GROUP).food(new Food.Builder().nutrition(3).alwaysEat().effect(() -> new EffectInstance(Effects.REGENERATION, 300), 1).build())));
    public static final RegistryObject<Item> beerMugSweetBerryKriek = ITEMS.register("beer_mug_sweet_berry_kriek", () -> new BeerMugItem(BlockRegistry.beerMugSweetBerryKriekBlock.get(), new Item.Properties().tab(BEER_GROUP).food(new Food.Builder().nutrition(3).effect(() -> new EffectInstance(Effects.REGENERATION, 400), 1).alwaysEat().build())));
    public static final RegistryObject<Item> beerMugHaarsIceyPaleLager = ITEMS.register("beer_mug_haars_icey_pale_lager", () -> new BeerMugItem(BlockRegistry.beerMugHaarsIceyPaleLagerBlock.get(), new Item.Properties().tab(BEER_GROUP).food(new Food.Builder().nutrition(1).alwaysEat().effect(() -> new EffectInstance(StatusEffectRegistry.DRUNK_FROST_WALKER.get(), 1200), 1).build())));
    public static final RegistryObject<Item> beerMugPumpkinKvass = ITEMS.register("beer_mug_pumpkin_kvass", () -> new BeerMugItem(BlockRegistry.beerMugPumpkinKvassBlock.get(), new Item.Properties().tab(BEER_GROUP).food(new Food.Builder().nutrition(9).alwaysEat().build())));
}
