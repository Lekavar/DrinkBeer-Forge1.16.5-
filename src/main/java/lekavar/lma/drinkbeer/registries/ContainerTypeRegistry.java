package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blocks.tileentity.BeerBarrelTileEntity;
import lekavar.lma.drinkbeer.gui.container.BartendingTableContainer;
import lekavar.lma.drinkbeer.gui.container.BeerBarrelContainer;
import lekavar.lma.drinkbeer.gui.screen.BartendingTableContainerScreen;
import lekavar.lma.drinkbeer.gui.screen.BeerBarrelContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// Register Container & ContainerScreen in one class.
// Automatically Registering Static Event Handlers, see https://mcforge.readthedocs.io/en/1.16.x/events/intro/#automatically-registering-static-event-handlers
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ContainerTypeRegistry {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DrinkBeer.MODID);
    public static final RegistryObject<ContainerType<BeerBarrelContainer>> BEER_BARREL_CONTAINER = CONTAINERS.register("beer_barrel_container", () -> IForgeContainerType.create((id,inv,data)->{
        BlockPos pos = data.readBlockPos();
        BeerBarrelTileEntity tileEntity = ((BeerBarrelTileEntity) inv.player.level.getBlockEntity(pos));
        return new BeerBarrelContainer(id,tileEntity,tileEntity.syncData, inv, tileEntity);
    }));
    public static final RegistryObject<ContainerType<BartendingTableContainer>> BARTENDING_TABLE_CONTAINER = CONTAINERS.register("bartending_table_container", () -> IForgeContainerType.create((id,inv,data)->{
        BlockPos pos = data.readBlockPos();
        return new BartendingTableContainer(id,inv, IWorldPosCallable.create(inv.player.level,pos));
    }));


    @SubscribeEvent
    public static void registerContainerScreen(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ScreenManager.register(ContainerTypeRegistry.BEER_BARREL_CONTAINER.get(), BeerBarrelContainerScreen::new);
            ScreenManager.register(ContainerTypeRegistry.BARTENDING_TABLE_CONTAINER.get(), BartendingTableContainerScreen::new);
        });
    }
}