package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.essentials.beer.Beers;
import lekavar.lma.drinkbeer.essentials.spice.Spices;
import lekavar.lma.drinkbeer.items.BeerItem;
import lekavar.lma.drinkbeer.items.SpiceItem;
import lekavar.lma.drinkbeer.misc.ModGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DrinkBeer.MODID);

    //general
    public static final RegistryObject<Item> BEER_BARREL = ITEMS.register("beer_barrel", () -> new BlockItem(BlockRegistry.BEER_BARREL.get(), new Item.Properties().tab(ModGroup.GENERAL)));
    public static final RegistryObject<Item> BARTENDING_TABLE = ITEMS.register("bartending_table", () -> new BlockItem(BlockRegistry.BARTENDING_TABLE.get(), new Item.Properties().tab(ModGroup.GENERAL)));
    public static final RegistryObject<Item> TRADE_BOX = ITEMS.register("trade_box", () -> new BlockItem(BlockRegistry.TRADE_BOX.get(), new Item.Properties().tab(ModGroup.GENERAL)));
    public static final RegistryObject<Item> EMPTY_BEER_MUG = ITEMS.register("empty_beer_mug", () -> new BlockItem(BlockRegistry.EMPTY_BEER_MUG.get(), new Item.Properties().tab(ModGroup.GENERAL)));

    public static final RegistryObject<Item> IRON_CALL_BELL = ITEMS.register("iron_call_bell", () -> new BlockItem(BlockRegistry.IRON_CALL_BELL.get(), new Item.Properties().tab(ModGroup.GENERAL)));
    public static final RegistryObject<Item> GOLDEN_CALL_BELL = ITEMS.register("golden_call_bell", () -> new BlockItem(BlockRegistry.GOLDEN_CALL_BELL.get(), new Item.Properties().tab(ModGroup.GENERAL)));
    public static final RegistryObject<Item> LEKAS_CALL_BELL = ITEMS.register("lekas_call_bell", () -> new BlockItem(BlockRegistry.LEKAS_CALL_BELL.get(), new Item.Properties().tab(ModGroup.GENERAL)));

    public static final RegistryObject<Item> RECIPE_BOARD_MINER_PALE_ALE = ITEMS.register("recipe_board_beer_mug", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_MINER_PALE_ALE.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BLAZE_STOUT = ITEMS.register("recipe_board_beer_mug_blaze_stout", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BLAZE_STOUT.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_BLAZE_MILK_STOUT = ITEMS.register("recipe_board_beer_mug_blaze_milk_stout", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_BLAZE_MILK_STOUT.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_APPLE_LAMBIC = ITEMS.register("recipe_board_beer_mug_apple_lambic", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_APPLE_LAMBIC.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_SWEET_BERRY_KRIEK = ITEMS.register("recipe_board_beer_mug_sweet_berry_kriek", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_SWEET_BERRY_KRIEK.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_HAARS_ICEY_PALE_LAGER = ITEMS.register("recipe_board_beer_mug_haars_icey_pale_lager", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_HAARS_ICEY_PALE_LAGER.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_PUMPKIN_KVASS = ITEMS.register("recipe_board_beer_mug_pumpkin_kvass", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_PUMPKIN_KVASS.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));
    public static final RegistryObject<Item> RECIPE_BOARD_NIGHT_HOWL_KVASS = ITEMS.register("recipe_board_beer_mug_night_howl_kvass", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_NIGHT_HOWL_KVASS.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));

    public static final RegistryObject<Item> RECIPE_BOARD_PACKAGE = ITEMS.register("recipe_board_package", () -> new BlockItem(BlockRegistry.RECIPE_BOARD_PACKAGE.get(), new Item.Properties().tab(ModGroup.GENERAL).stacksTo(1)));

    //beer
    public static final RegistryObject<Item> MINER_PALE_ALE = ITEMS.register("beer_mug", () -> new BeerItem(Beers.MINER_PALE_ALE));
    public static final RegistryObject<Item> BLAZE_STOUT = ITEMS.register("beer_mug_blaze_stout", () -> new BeerItem(Beers.BLAZE_STOUT));
    public static final RegistryObject<Item> BLAZE_MILK_STOUT = ITEMS.register("beer_mug_blaze_milk_stout", () -> new BeerItem(Beers.BLAZE_MILK_STOUT));
    public static final RegistryObject<Item> APPLE_LAMBIC = ITEMS.register("beer_mug_apple_lambic", () -> new BeerItem(Beers.APPLE_LAMBIC));
    public static final RegistryObject<Item> SWEET_BERRY_KRIEK = ITEMS.register("beer_mug_sweet_berry_kriek", () -> new BeerItem(Beers.SWEET_BERRY_KRIEK));
    public static final RegistryObject<Item> HAARS_ICEY_PALE_LAGER = ITEMS.register("beer_mug_haars_icey_pale_lager", () -> new BeerItem(Beers.HAARS_ICEY_PALE_LAGER));
    public static final RegistryObject<Item> PUMPKIN_KVASS = ITEMS.register("beer_mug_pumpkin_kvass", () -> new BeerItem(Beers.PUMPKIN_KVASS));
    public static final RegistryObject<Item> NIGHT_HOWL_KVASS = ITEMS.register("beer_mug_night_howl_kvass", () -> new BeerItem(Beers.NIGHT_HOWL_KVASS));
    public static final RegistryObject<Item> FROTHY_PINK_EGGNOG = ITEMS.register("beer_mug_frothy_pink_eggnog", () -> new BeerItem(Beers.FROTHY_PINK_EGGNOG));

    //Spice
    public static final RegistryObject<Item> BLAZE_PAPRIKA = ITEMS.register("spice_blaze_paprika",
            () -> new SpiceItem(BlockRegistry.BLAZE_PAPRIKA.get(), Spices.BLAZE_PAPRIKA));
    public static final RegistryObject<Item> AMETHYST_NIGELLA_SEEDS = ITEMS.register("spice_amethyst_nigella_seeds",
            () -> new SpiceItem(BlockRegistry.AMETHYST_NIGELLA_SEEDS.get(), Spices.AMETHYST_NIGELLA_SEEDS));
    public static final RegistryObject<Item> CITRINE_NIGELLA_SEEDS = ITEMS.register("spice_citrine_nigella_seeds",
            () -> new SpiceItem(BlockRegistry.CITRINE_NIGELLA_SEEDS.get(), Spices.CITRINE_NIGELLA_SEEDS));
    public static final RegistryObject<Item> DRIED_EGLIA_BUD = ITEMS.register("spice_dried_eglia_bud",
            () -> new SpiceItem(BlockRegistry.DRIED_EGLIA_BUD.get(), Spices.DRIED_EGLIA_BUD));
    public static final RegistryObject<Item> SMOKED_EGLIA_BUD = ITEMS.register("spice_smoked_eglia_bud",
            () -> new SpiceItem(BlockRegistry.SMOKED_EGLIA_BUD.get(), Spices.SMOKED_EGLIA_BUD));
    public static final RegistryObject<Item> ICE_MINT = ITEMS.register("spice_ice_mint",
            () -> new SpiceItem(BlockRegistry.ICE_MINT.get(), Spices.ICE_MINT));
    public static final RegistryObject<Item> ICE_PATCHOULI = ITEMS.register("spice_ice_patchouli",
            () -> new SpiceItem(BlockRegistry.ICE_PATCHOULI.get(), Spices.ICE_PATCHOULI));
    public static final RegistryObject<Item> STORM_SHARDS = ITEMS.register("spice_storm_shards",
            () -> new SpiceItem(BlockRegistry.STORM_SHARDS.get(), Spices.STORM_SHARDS));
}
