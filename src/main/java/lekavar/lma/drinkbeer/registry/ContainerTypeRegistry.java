package lekavar.lma.drinkbeer.registry;

import lekavar.lma.drinkbeer.container.BeerBarrelContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeRegistry {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, "drinkbeer");
    public static final RegistryObject<ContainerType<BeerBarrelContainer>> beerBarrelContainer = CONTAINERS.register("beer_barrel_container", () -> IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) -> new BeerBarrelContainer(windowId, inv, data)));
}