package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.blocks.tileentity.BeerBarrelTileEntity;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "drinkbeer");
    public static final RegistryObject<TileEntityType<BeerBarrelTileEntity>> BEER_BARREL_TILEENTITY = TILE_ENTITY.register("beer_barrel_blockentity", () -> TileEntityType.Builder.of(BeerBarrelTileEntity::new, BlockRegistry.BEER_BARREL.get()).build(null));
    public static final RegistryObject<TileEntityType<BeerBlockTileEntity>> BEER_BLOCK_TILEENTITY = TILE_ENTITY.register("beer_block_blockentity", () -> TileEntityType.Builder.of(BeerBlockTileEntity::new, BlockRegistry.BEER.get()).build(null));
}
