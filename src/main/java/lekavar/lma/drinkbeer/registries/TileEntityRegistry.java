package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.blocks.TradeBoxBlock;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerBarrelTileEntity;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerTileEntity;
import lekavar.lma.drinkbeer.blocks.tileentity.TradeBoxTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, "drinkbeer");
    public static final RegistryObject<TileEntityType<BeerBarrelTileEntity>> BEER_BARREL_TILEENTITY = TILE_ENTITY.register("beer_barrel_blockentity", () -> TileEntityType.Builder.of(BeerBarrelTileEntity::new, BlockRegistry.BEER_BARREL.get()).build(null));
    public static final RegistryObject<TileEntityType<BeerTileEntity>> BEER_TILEENTITY = TILE_ENTITY.register("beer_blockentity", () -> TileEntityType.Builder.of(BeerTileEntity::new, BlockRegistry.BEER.get()).build(null));
    public static final RegistryObject<TileEntityType<TradeBoxTileEntity>> TRADE_BOX_TILEENTITY = TILE_ENTITY.register("trade_box_blockentity", () -> TileEntityType.Builder.of(TradeBoxTileEntity::new, BlockRegistry.TRADE_BOX.get()).build(null));

}
