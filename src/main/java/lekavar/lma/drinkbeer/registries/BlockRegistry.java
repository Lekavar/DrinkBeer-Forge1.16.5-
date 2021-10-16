package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.blocks.BeerBarrelBlock;
import lekavar.lma.drinkbeer.blocks.BeerMugBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "drinkbeer");

    //general
    public static final RegistryObject<Block> BEER_BARREL = BLOCKS.register("beer_barrel", BeerBarrelBlock::new);
    public static final RegistryObject<Block> EMPTY_BEER_MUG = BLOCKS.register("empty_beer_mug", BeerMugBlock::new);

    //beer
    public static final RegistryObject<Block> BEER_MUG = BLOCKS.register("beer_mug", BeerMugBlock::new);
    public static final RegistryObject<Block> BEER_MUG_BLAZE_STOUT = BLOCKS.register("beer_mug_blaze_stout", BeerMugBlock::new);
    public static final RegistryObject<Block> BEER_MUG_BLAZE_MILK_STOUT = BLOCKS.register("beer_mug_blaze_milk_stout", BeerMugBlock::new);
    public static final RegistryObject<Block> BEER_MUG_APPLE_LAMBIC = BLOCKS.register("beer_mug_apple_lambic", BeerMugBlock::new);
    public static final RegistryObject<Block> BEER_MUG_SWEET_BERRY_KRIEK = BLOCKS.register("beer_mug_sweet_berry_kriek", BeerMugBlock::new);
    public static final RegistryObject<Block> BEER_MUG_HAARS_ICEY_PALE_LAGER = BLOCKS.register("beer_mug_haars_icey_pale_lager", BeerMugBlock::new);
    public static final RegistryObject<Block> BEER_MUG_PUMPKIN_KVASS = BLOCKS.register("beer_mug_pumpkin_kvass", BeerMugBlock::new);
}