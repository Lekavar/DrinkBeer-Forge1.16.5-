package lekavar.lma.drinkbeer.registry;

import lekavar.lma.drinkbeer.blocks.entity.BeerBarrelEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEntityRegistry {
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "drinkbeer");
    public static final RegistryObject<TileEntityType<BeerBarrelEntity>> beerBarrelEntity = BLOCK_ENTITIES.register("beer_barrel_blockentity", () -> TileEntityType.Builder.of(BeerBarrelEntity::new,BlockRegistry.beerBarrel.get()).build(null));
    public static final RegistryObject<TileEntityType<BeerBarrelEntity>> obsidianCounterTileEntity = BLOCK_ENTITIES.register("obsidian_counter_tileentity", () -> TileEntityType.Builder.of(BeerBarrelEntity::new, BlockRegistry.beerBarrel.get()).build(null));

}
