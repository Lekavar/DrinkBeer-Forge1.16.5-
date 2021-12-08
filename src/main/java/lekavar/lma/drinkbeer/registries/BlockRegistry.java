package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blocks.*;
import lekavar.lma.drinkbeer.blocks.legacy.BeerMugBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DrinkBeer.MODID);

    //general
    public static final RegistryObject<Block> BEER_BARREL = BLOCKS.register("beer_barrel", BeerBarrelBlock::new);
    public static final RegistryObject<Block> BARTENDING_TABLE = BLOCKS.register("bartending_table", BartendingTableBlock::new);
    public static final RegistryObject<Block> EMPTY_BEER_MUG = BLOCKS.register("empty_beer_mug", EmptyBeerMug::new);
    public static final RegistryObject<Block> IRON_CALL_BELL = BLOCKS.register("iron_call_bell", CallBellBlock::new);
    public static final RegistryObject<Block> GOLDEN_CALL_BELL = BLOCKS.register("golden_call_bell", CallBellBlock::new);
    public static final RegistryObject<Block> LEKAS_CALL_BELL = BLOCKS.register("lekas_call_bell", CallBellBlock::new);

    public static final RegistryObject<Block> BEER = BLOCKS.register("beer", BeerBlock::new);

    public static final RegistryObject<Block> RECIPE_BOARD_MINER_PALE_ALE = BLOCKS.register("recipe_board_beer_mug", () -> new RecipeBoardBlock(true));
    public static final RegistryObject<Block> RECIPE_BOARD_BLAZE_STOUT = BLOCKS.register("recipe_board_beer_mug_blaze_stout", () -> new RecipeBoardBlock(true));
    public static final RegistryObject<Block> RECIPE_BOARD_BLAZE_MILK_STOUT = BLOCKS.register("recipe_board_beer_mug_blaze_milk_stout", () -> new RecipeBoardBlock(true));
    public static final RegistryObject<Block> RECIPE_BOARD_APPLE_LAMBIC = BLOCKS.register("recipe_board_beer_mug_apple_lambic", () -> new RecipeBoardBlock(true));
    public static final RegistryObject<Block> RECIPE_BOARD_SWEET_BERRY_KRIEK = BLOCKS.register("recipe_board_beer_mug_sweet_berry_kriek", () -> new RecipeBoardBlock(true));
    public static final RegistryObject<Block> RECIPE_BOARD_HAARS_ICEY_PALE_LAGER = BLOCKS.register("recipe_board_beer_mug_haars_icey_pale_lager", () -> new RecipeBoardBlock(true));
    public static final RegistryObject<Block> RECIPE_BOARD_PUMPKIN_KVASS = BLOCKS.register("recipe_board_beer_mug_pumpkin_kvass", () -> new RecipeBoardBlock(true));
    public static final RegistryObject<Block> RECIPE_BOARD_NIGHT_HOWL_KVASS = BLOCKS.register("recipe_board_beer_mug_night_howl_kvass", () -> new RecipeBoardBlock(true));

    public static final RegistryObject<Block> RECIPE_BOARD_PACKAGE = BLOCKS.register("recipe_board_package", RecipeBoardPackageBlock::new);

    public static final RegistryObject<Block> BLAZE_PAPRIKA = BLOCKS.register("spice_blaze_paprika", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).noOcclusion()));
    public static final RegistryObject<Block> AMETHYST_NIGELLA_SEEDS = BLOCKS.register("spice_amethyst_nigella_seeds", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).noOcclusion()));
    public static final RegistryObject<Block> CITRINE_NIGELLA_SEEDS = BLOCKS.register("spice_citrine_nigella_seeds", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).noOcclusion()));
    public static final RegistryObject<Block> DRIED_EGLIA_BUD = BLOCKS.register("spice_dried_eglia_bud", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).noOcclusion()));
    public static final RegistryObject<Block> SMOKED_EGLIA_BUD = BLOCKS.register("spice_smoked_eglia_bud", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).noOcclusion()));
    public static final RegistryObject<Block> ICE_MINT = BLOCKS.register("spice_ice_mint", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).noOcclusion()));
    public static final RegistryObject<Block> ICE_PATCHOULI = BLOCKS.register("spice_ice_patchouli", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.WOOD).strength(1.0f).noOcclusion()));
    public static final RegistryObject<Block> STORM_SHARDS = BLOCKS.register("spice_storm_shards", () -> new SpiceBlock(AbstractBlock.Properties.of(Material.GLASS).strength(0.5f).noOcclusion()));

    // Legacy Design. Will be abandoned in 1,18
    public static final RegistryObject<Block> BEER_MUG = BLOCKS.register("beer_mug", () -> new BeerMugBlock(ItemRegistry.MINER_PALE_ALE));
    public static final RegistryObject<Block> BEER_MUG_BLAZE_STOUT = BLOCKS.register("beer_mug_blaze_stout", () -> new BeerMugBlock(ItemRegistry.BLAZE_STOUT));
    public static final RegistryObject<Block> BEER_MUG_BLAZE_MILK_STOUT = BLOCKS.register("beer_mug_blaze_milk_stout", () -> new BeerMugBlock(ItemRegistry.BLAZE_MILK_STOUT));
    public static final RegistryObject<Block> BEER_MUG_APPLE_LAMBIC = BLOCKS.register("beer_mug_apple_lambic", () -> new BeerMugBlock(ItemRegistry.APPLE_LAMBIC));
    public static final RegistryObject<Block> BEER_MUG_SWEET_BERRY_KRIEK = BLOCKS.register("beer_mug_sweet_berry_kriek", () -> new BeerMugBlock(ItemRegistry.SWEET_BERRY_KRIEK));
    public static final RegistryObject<Block> BEER_MUG_HAARS_ICEY_PALE_LAGER = BLOCKS.register("beer_mug_haars_icey_pale_lager", () -> new BeerMugBlock(ItemRegistry.HAARS_ICEY_PALE_LAGER));
    public static final RegistryObject<Block> BEER_MUG_PUMPKIN_KVASS = BLOCKS.register("beer_mug_pumpkin_kvass", () -> new BeerMugBlock(ItemRegistry.PUMPKIN_KVASS));
    public static final RegistryObject<Block> BEER_MUG_NIGHT_HOWL_KVASS = BLOCKS.register("beer_mug_night_howl_kvass", () -> new BeerMugBlock(ItemRegistry.NIGHT_HOWL_KVASS));
    public static final RegistryObject<Block> BEER_MUG_FROTHY_PINK_EGGNOG = BLOCKS.register("beer_mug_frothy_pink_eggnog", () -> new BeerMugBlock(ItemRegistry.FROTHY_PINK_EGGNOG));

}
