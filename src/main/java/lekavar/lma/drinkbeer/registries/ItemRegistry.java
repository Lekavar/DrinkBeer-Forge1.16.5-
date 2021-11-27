package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.items.BeerMugItem;
import lekavar.lma.drinkbeer.utils.BearType;
import lekavar.lma.drinkbeer.utils.ModGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "drinkbeer");

    //general
    public static final RegistryObject<Item> BEER_BARREL = ITEMS.register("beer_barrel", () -> new BlockItem(BlockRegistry.BEER_BARREL.get(), new Item.Properties().tab(ModGroup.GENERAL)));
    public static final RegistryObject<Item> EMPTY_BEER_MUG = ITEMS.register("empty_beer_mug", () -> new BlockItem(BlockRegistry.EMPTY_BEER_MUG.get(), new Item.Properties().tab(ModGroup.GENERAL)));

    public static final RegistryObject<Item> IRON_CALL_BELL = ITEMS.register("iron_call_bell", () -> new BlockItem(BlockRegistry.IRON_CALL_BELL.get(), new Item.Properties().tab(ModGroup.GENERAL)));
    public static final RegistryObject<Item> GOLDEN_CALL_BELL = ITEMS.register("golden_call_bell", () -> new BlockItem(BlockRegistry.GOLDEN_CALL_BELL.get(), new Item.Properties().tab(ModGroup.GENERAL)));

    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG = ITEMS.register("recipe_board_beer_mug", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG_BLAZE_STOUT = ITEMS.register("recipe_board_beer_mug_blaze_stout", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG_BLAZE_STOUT.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG_BLAZE_MILK_STOUT = ITEMS.register("recipe_board_beer_mug_blaze_milk_stout", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG_BLAZE_MILK_STOUT.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG_APPLE_LAMBIC = ITEMS.register("recipe_board_beer_mug_apple_lambic", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG_APPLE_LAMBIC.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG_SWEET_BERRY_KRIEK = ITEMS.register("recipe_board_beer_mug_sweet_berry_kriek", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG_SWEET_BERRY_KRIEK.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG_HAARS_ICEY_PALE_LAGER = ITEMS.register("recipe_board_beer_mug_haars_icey_pale_lager", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG_HAARS_ICEY_PALE_LAGER.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG_PUMPKIN_KVASS = ITEMS.register("recipe_board_beer_mug_pumpkin_kvass", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG_PUMPKIN_KVASS.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BEER_MUG_NIGHT_HOWL_KVASS = ITEMS.register("recipe_board_beer_mug_night_howl_kvass", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BEER_MUG_NIGHT_HOWL_KVASS.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));

    public static final RegistryObject<Item> RECIPE_BOARD_PACKAGE = ITEMS.register("recipe_board_package", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_PACKAGE.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));

    //beer
    public static final RegistryObject<Item> BEER_MUG = ITEMS.register("beer_mug", () -> new BeerMugItem(BlockRegistry.BEER_MUG.get(), BearType.MINER_PALE_ALE));
    public static final RegistryObject<Item> BEER_MUG_BLAZE_STOUT = ITEMS.register("beer_mug_blaze_stout", () -> new BeerMugItem(BlockRegistry.BEER_MUG_BLAZE_STOUT.get(), BearType.BLAZE_STOUT));
    public static final RegistryObject<Item> BEER_MUG_BLAZE_MILK_STOUT = ITEMS.register("beer_mug_blaze_milk_stout", () -> new BeerMugItem(BlockRegistry.BEER_MUG_BLAZE_MILK_STOUT.get(), BearType.BLAZE_MILK_STOUT));
    public static final RegistryObject<Item> BEER_MUG_APPLE_LAMBIC = ITEMS.register("beer_mug_apple_lambic", () -> new BeerMugItem(BlockRegistry.BEER_MUG_APPLE_LAMBIC.get(), BearType.APPLE_LAMBIC));
    public static final RegistryObject<Item> BEER_MUG_SWEET_BERRY_KRIEK = ITEMS.register("beer_mug_sweet_berry_kriek", () -> new BeerMugItem(BlockRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get(), BearType.SWEET_BERRY_KRIEK));
    public static final RegistryObject<Item> BEER_MUG_HAARS_ICEY_PALE_LAGER = ITEMS.register("beer_mug_haars_icey_pale_lager", () -> new BeerMugItem(BlockRegistry.BEER_MUG_HAARS_ICEY_PALE_LAGER.get(), BearType.HAARS_ICEY_PALE_LAGER));
    public static final RegistryObject<Item> BEER_MUG_PUMPKIN_KVASS = ITEMS.register("beer_mug_pumpkin_kvass", () -> new BeerMugItem(BlockRegistry.BEER_MUG_PUMPKIN_KVASS.get(), BearType.PUMPKIN_KVASS));
    public static final RegistryObject<Item> BEER_MUG_NIGHT_HOWL_KVASS = ITEMS.register("beer_mug_night_howl_kvass", () -> new BeerMugItem(BlockRegistry.BEER_MUG_NIGHT_HOWL_KVASS.get(), BearType.NIGHT_HOWL_KVASS));
    public static final RegistryObject<Item> BEER_MUG_FROTHY_PINK_EGGNOG = ITEMS.register("beer_mug_frothy_pink_eggnog", () -> new BeerMugItem(BlockRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get(), BearType.FROTHY_PINK_EGGNOG));


}
