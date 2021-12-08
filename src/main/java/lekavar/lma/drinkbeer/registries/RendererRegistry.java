package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.client.renderer.BeerBlockRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RendererRegistry {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.BEER_BLOCK_TILEENTITY.get(), BeerBlockRenderer::new);
    }
}
